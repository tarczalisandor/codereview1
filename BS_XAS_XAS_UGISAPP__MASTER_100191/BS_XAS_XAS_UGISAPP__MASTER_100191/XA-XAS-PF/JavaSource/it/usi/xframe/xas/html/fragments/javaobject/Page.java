package it.usi.xframe.xas.html.fragments.javaobject;

import it.usi.xframe.xas.html.HtmlCssPackage;
import it.usi.xframe.xas.html.HtmlJsPackage;
import it.usi.xframe.xas.html.HtmlObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Page extends HtmlObject
{
	private static final String COMPONENT_NAME = "page_";

	private String title				= null;
	private boolean skipSerialization	= false;
	private TreeMap cssPackages			= new TreeMap();
	private TreeMap jsPackages			= new TreeMap();
	
	/**
	 * @param title
	 */
	public Page(String title)
	{
		super();
		this.setName("");
		this.title = title;
	}

	/**
	 * Skip the final serialization of html code
	 * 
	 * @param b
	 */
	public void skipSerialization(boolean b)
	{
		this.skipSerialization = b;
	}
	
	/**
	 * Return the skipSerialization status
	 * 
	 * @return
	 */
	public boolean getSkipSerialization()
	{
		return this.skipSerialization;
	}

	/**
	 * @param req
	 * @param resp
	 * @throws IOException
	 * 
	 * render html java object in html 
	 */
	public void serialize(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		if(this.getSkipSerialization()==true)
			return;

		// set HttpServletRequest and HttpServletResponse before serialize 
		this.setServlet(req, resp);
		
		// collect all packages names
		super.preparePackages();

		// get the complete list of css
		ArrayList listCss = this.getCss();
		for (int i = 0; i < listCss.size(); i++) {
			HtmlCssPackage css = (HtmlCssPackage) listCss.get(i);
			cssPackages.put(css.getValue(), css);
		    //System.out.println("CSS: Key = " + css.getValue());
		}
		
		// get the complete list of js 
		ArrayList listJs = this.getJs();
		for (int i = 0; i < listJs.size(); i++) {
			HtmlJsPackage js = (HtmlJsPackage) listJs.get(i);
			jsPackages.put(js.getValue(), js);
		    //System.out.println("JS: Key = " + js.getValue());
		}

		// start to serialize
		super.serialize();
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceHeaderPlaceholder()
	 */
	public void replaceHeaderPlaceholder()
	{
		resp.setContentType("text/html; charset=" + DEFAULT_CHARSET);

		replace(COMPONENT_NAME + "title", this.title);
		
		// build js packages list
		StringBuffer jsList = new StringBuffer();
		if(jsPackages!=null)
		{
			Iterator entries = jsPackages.entrySet().iterator();
			while (entries.hasNext()) {
			    Map.Entry entry = (Map.Entry) entries.next();
			    String key = (String)entry.getKey();
			    HtmlJsPackage value = (HtmlJsPackage)entry.getValue();
				jsList.append("\t\t" + HtmlJsPackage.getPackage(value) + "\n");
			}
		}
		replace(COMPONENT_NAME + "js", jsList.toString());

		// build css packages list
		StringBuffer cssList = new StringBuffer();
		if(cssPackages!=null)
		{
			// get the list of css and ad to an ordered list
			Iterator i = cssPackages.entrySet().iterator();
			while (i.hasNext()) {
			    Map.Entry entry = (Map.Entry) i.next();
			    String key = (String)entry.getKey();
			    HtmlCssPackage p = (HtmlCssPackage)entry.getValue();
			    cssList.append("\t\t" + HtmlCssPackage.getPackage(p) + "\n");
			}
		}
		replace(COMPONENT_NAME + "css", cssList.toString());
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateCssPackages()
	 */
	protected void populateCssPackages()
	{
		super.add(HtmlCssPackage.CSS_BOOSTRAP);
		super.add(HtmlCssPackage.CSS_FONTAWESOME);
		super.add(HtmlCssPackage.CSS_NORMALIZE);
		super.add(HtmlCssPackage.CSS_APP_CORE);
	}
	
	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateJsPackages()
	 */
	protected void populateJsPackages()
	{
		super.add(HtmlJsPackage.JS_JQUERY);
		super.add(HtmlJsPackage.JS_POPPER);
		super.add(HtmlJsPackage.JS_TOOLTIP);
		super.add(HtmlJsPackage.JS_FONTAWESOME);
		super.add(HtmlJsPackage.JS_BOOSTRAP);
		super.add(HtmlJsPackage.JS_APP_CORE);
	}
}
