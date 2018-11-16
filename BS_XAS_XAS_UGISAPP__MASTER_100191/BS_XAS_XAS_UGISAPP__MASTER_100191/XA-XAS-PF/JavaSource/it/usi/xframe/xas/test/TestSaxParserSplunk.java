package it.usi.xframe.xas.test;

import it.usi.xframe.xas.html.HtmlObject;
import it.usi.xframe.xas.util.SaxParserSplunk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSaxParserSplunk
{

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(TestSaxParserSplunk.class);

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			logger.debug("App started");
			TestSaxParserSplunk t = new TestSaxParserSplunk();
			String xml = t.loadfile("xmlSplunk.xml");
			//System.out.println(xml);
			
			SaxParserSplunk sps = new SaxParserSplunk (false);
			sps.read(xml);
			
			ArrayList columns = sps.getColumns();
			ArrayList rows = sps.getRows();

			logger.debug("---------------------");
			logger.debug(columns.toString());
			logger.debug("---------------------");

			int i=0;
			Iterator itera = rows.iterator();
			while (itera.hasNext())
			{
				ArrayList row = (ArrayList) itera.next();
				System.out.println(i++ + ")" + row.toString());
			}

			logger.debug("App ended");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	public String loadfile(String name) throws IOException
	{
		String ret = null;
		InputStream is = this.getClass().getResourceAsStream(name);
		if(is!=null)
		{
			//ret = IOUtils.toString(is, "UTF-8");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String line;
		    while ((line = br.readLine()) != null) 
		    {
	    		sb.append(line+"\n");
		    }
		    ret = sb.toString();
		    br.close();
		    isr.close();
			is.close();
		}

		return ret; 
	}
}
