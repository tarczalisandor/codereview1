package it.usi.xframe.xas.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

public abstract class HtmlObject
{
	public static final String DEFAULT_CHARSET		= "UTF-8";
	
	private static final String FRAGMENT_HEADER		= "header";
	private static final String FRAGMENT_BODY		= "body";
	private static final String FRAGMENT_FOOTER		= "footer";
	
	private static final String FRAGMENT_PATH_BOOSTRAP	= "it/usi/xframe/xas/html/fragments/boostrap";
	private static final String FRAGMENT_PATH_HTML	= "it/usi/xframe/xas/html/fragments/html";
	private static final String FRAGMENT_EXTENSION	= ".html";

	// object name
	private String name = null;
	// parent tag
	private HtmlObject parentTag = null;
	// child html objects
	private List objects = new ArrayList();

	// set browser type
	private BrowserType browser	= BrowserType.FIREFOX;

	// list of css and js packages for this object
	protected ArrayList listCssPackages	= new ArrayList();
	protected ArrayList listJsPackages	= new ArrayList();

	protected HttpServletRequest req	= null;
	protected HttpServletResponse resp	= null;
	
	protected String cacheHeaderTag		= null;
	protected String cacheBodyTag		= null;
	protected String cacheFooterTag		= null;

	protected String headerTag			= null;
	protected String bodyTag			= null;
	protected String footerTag			= null;
	
	protected String temporaryTag = null;

	protected boolean serializeOnlyBody = false;

	/**
	 * 
	 */
	public HtmlObject()
	{
	}

	/**
	 * @param req
	 * @param resp
	 * 
	 * set servlet HttpServletRequest and HttpServletResponse
	 */
	public void setServlet (HttpServletRequest req, HttpServletResponse resp)
	{
		this.req = req;
		this.resp = resp;
	}
	
	/**
	 * @return
	 * 
	 * return initialization status
	 */
	protected boolean isServletSet()
	{
		if(req==null || resp==null)
			return false;
		
		return true;
	}

	/**
	 * @throws IOException
	 * 
	 * collect all the packages names with css and js
	 * 
	 */
	public void preparePackages() throws IOException
	{
		populateCssPackages();
		populateJsPackages();

		Iterator itera = objects.iterator();
		while (itera.hasNext())
		{
			HtmlObject base = (HtmlObject) itera.next();
			base.preparePackages();
		}
	}

	/**
	 * @throws IOException
	 * 
	 * write all html content in servlet output stream
	 */
	protected void serialize() throws IOException
	{
		// get the browser type from parent tag
		if(parentTag!=null)
			this.setBrowser(parentTag.getBrowser());
		
		// load the html code
		loadTags();
		
		//serialize header
		if (!this.serializeOnlyBody) this.header();
		//serialize body
		this.body();
		// call child objects
		Iterator itera = objects.iterator();
		while (itera.hasNext())
		{
			HtmlObject base = (HtmlObject) itera.next();
			if(base.isServletSet()==false)
				base.setServlet(req, resp);
			base.serialize();
		}
		//serialize footer
		if (!this.serializeOnlyBody) this.footer();
	}

	/**
	 * @return
	 * 
	 * get the complete list of css used by child objects
	 */
	public ArrayList getCss()
	{
		ArrayList list = new ArrayList();
		list.addAll(listCssPackages);

		// transfer child css packages into list
		Iterator itera = objects.iterator();
		while (itera.hasNext())
		{
			HtmlObject base = (HtmlObject) itera.next();
			list.addAll(base.getCss());
		}
		return list;
	}

	/**
	 * @return
	 * 
	 * get the complete list of js used by child objects
	 */
	public ArrayList getJs()
	{
		ArrayList list = new ArrayList();
		list.addAll(listJsPackages);

		// transfer child js packages into list
		Iterator itera = objects.iterator();
		while (itera.hasNext())
		{
			HtmlObject base = (HtmlObject) itera.next();
			list.addAll(base.getJs());
		}
		return list;
	}

	/**
	 * @throws IOException
	 * 
	 * load html fragments
	 */
	private void loadTags () throws IOException
	{
		String name = this.getClass().getName();
		
		if(cacheHeaderTag==null)
			cacheHeaderTag = loadResource(name, FRAGMENT_HEADER);
		headerTag = cacheHeaderTag;

		if(cacheBodyTag==null)
			cacheBodyTag = loadResource(name, FRAGMENT_BODY);
		bodyTag = cacheBodyTag;

		if(cacheFooterTag==null)
			cacheFooterTag = loadResource(name, FRAGMENT_FOOTER);
		footerTag = cacheFooterTag;
	}

	/**
	 * @param name
	 * @param fragment
	 * @return
	 * @throws IOException
	 * 
	 * load html fragments resources
	 */
	private String loadResource(String name, String fragment) throws IOException
	{
		String ret = null;

		String[] result = name.split("\\.");
		String newName = "/" 
			+ (browser.equals(BrowserType.FIREFOX) ? FRAGMENT_PATH_BOOSTRAP : FRAGMENT_PATH_HTML)
			+ "/"+ result[result.length-1] + FRAGMENT_EXTENSION;

		InputStream is = this.getClass().getResourceAsStream(newName);
		if(is!=null)
		{
			//ret = IOUtils.toString(is, "UTF-8");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String line;
			boolean state = false;
		    while ((line = br.readLine()) != null) 
		    {
		    	if(state==true && line.trim().compareTo("<!--" + fragment + ".end-->")==0)
		    		break;
		    	if(state)
		    		sb.append(line+"\n");
		    	if(state==false && line.trim().compareTo("<!--" + fragment + ".start-->")==0)
		    		state=true;
		    }
		    ret = sb.toString();
		    br.close();
		    isr.close();
			is.close();
		}
		else
			ret = "&lt;missing tag " + newName + "&gt;";
		return ret;
	}
	
	/**
	 * @param placeholder
	 * @param replacement string
	 * replace html fragment placeholder ${key} into temporaryTag
	 * during header and footer renderinmg
	 */
	protected void replace (String placeholder, String replacement)
	{
		if(temporaryTag!=null)
		{
			placeholder = "${"+placeholder+"}";
			int pos = -1;
			do
			{
				pos = temporaryTag.indexOf(placeholder);
				if(pos!=-1)
					temporaryTag =temporaryTag.substring(0, pos) + replacement + temporaryTag.substring(pos+placeholder.length());
			} while(pos!=-1);
		}
	}
	
	/**
	 * @return
	 * 
	 * return the object identificator
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @param name
	 * 
	 * set the object identificator
	 */
	public void setName(String name)
	{
		this.name = (name==null) ? "" : name;
	}

	/**
	 * @param o
	 * 
	 * add a html child object
	 */
	public void add (HtmlObject o)
	{
		this.add(o, false);
	}

	/**
	 * @param o
	 * @param serializeOnlyBody
	 *  
	 * add a html child object
	 */
	public void add (HtmlObject o, boolean serializeOnlyBody)
	{
		objects.add(o);
		o.serializeOnlyBody = serializeOnlyBody;
		o.setParentTag(this);
		o.setBrowser(this.browser);
	}

	/**
	 * Search an HtmlObject in the object tree
	 * 
	 * @param name
	 * @return
	 */
	public HtmlObject search (String name)
	{
		// search the html object in the child objects
		for(int i=0; i<objects.size(); i++)
		{
			HtmlObject o = (HtmlObject)objects.get(i);
			if(o.getName().equals(name))
				return o;
			
			// search the html object in the descendants of child objects
			HtmlObject child = o.search(name);
			if(child!=null)
				return child;
		}
		return null;	
	}
	
	/**
	 * Detach an object from the page
	 * 
	 * @param name
	 * @return
	 */
	public HtmlObject detach (String name)
	{
		// detach the html object in the child objects
		for(int i=0; i<objects.size(); i++)
		{
			HtmlObject o = (HtmlObject)objects.get(i);
			if(o.getName().equals(name))
				return (HtmlObject)objects.remove(i);

			// detach the html object in the descendants of child objects
			HtmlObject child = o.detach(name);
			if(child!=null)
				return child;
		}
		return null;	
	}

	/**
	 * @param o
	 * 
	 * add js package to the list
	 */
	protected void add(HtmlJsPackage o)
	{
		this.listJsPackages.add(o);
	}
	
	/**
	 * @param o
	 * 
	 * encode String s for html page
	 */
	protected String encodeHtml(String s)
	{
		return s == null ? "" : StringEscapeUtils.escapeHtml(s);
	}

	
	/**
	 * @param o
	 * 
	 * add css package to the list
	 */
	protected void add(HtmlCssPackage o)
	{
		this.listCssPackages.add(o);
	}

	/**
	 * @param s
	 * 
	 * write on servlet response stream
	 */
	protected void println(String s)
	{
		PrintWriter out;
		try
		{
			out = resp.getWriter();
			out.println(s);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @param s
	 * 
	 * write on servlet response stream
	 */
	protected void print(String s)
	{
		PrintWriter out;
		try
		{
			out = resp.getWriter();
			out.print(s);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void header ()
	{
		this.temporaryTag = this.headerTag;
		replaceAllPlaceholder();
		replaceHeaderPlaceholder();
		this.headerTag = this.temporaryTag;

		println(headerTag);
	}

	/**
	 * 
	 */
	private void body ()
	{
		this.temporaryTag = this.bodyTag;
		replaceAllPlaceholder();
		replaceBodyPlaceholder();
		this.bodyTag = this.temporaryTag;

		println(bodyTag);
	}

	/**
	 * 
	 */
	private void footer ()
	{
		this.temporaryTag = this.footerTag;
		replaceAllPlaceholder();
		replaceFooterPlaceholder();
		this.footerTag = this.temporaryTag;

		println(footerTag);
	}
	
	/**
	 * @return
	 */
	protected HtmlObject getParentTag()
	{
		return parentTag;
	}

	/**
	 * @param parentTag
	 */
	protected void setParentTag(HtmlObject parentTag)
	{
		this.parentTag = parentTag;
	}

	/**
	 * @return
	 */
	public BrowserType getBrowser()
	{
		return this.browser;
	}

	/**
	 * @param browser
	 */
	public void setBrowser(BrowserType browser)
	{
		this.browser = browser;
	}

	/**
	 * 
	 */
	protected void replaceAllPlaceholder()
	{}

	/**
	 * 
	 */
	protected void replaceHeaderPlaceholder()
	{}

	/**
	 * 
	 */
	protected void replaceBodyPlaceholder()
	{}

	/**
	 * 
	 */
	protected void replaceFooterPlaceholder()
	{}

	/**
	 * 
	 */
	protected void populateCssPackages()
	{}
	
	/**
	 * 
	 */
	protected void populateJsPackages()
	{}
}
