package it.usi.xframe.xas.servlet;

import it.usi.xframe.xas.app.MethodData;
import it.usi.xframe.xas.html.AlertType;
import it.usi.xframe.xas.html.BrowserType;
import it.usi.xframe.xas.html.FieldType;
import it.usi.xframe.xas.html.fragments.javaobject.Alert;
import it.usi.xframe.xas.html.fragments.javaobject.Chart;
import it.usi.xframe.xas.html.fragments.javaobject.Check;
import it.usi.xframe.xas.html.fragments.javaobject.Combobox;
import it.usi.xframe.xas.html.fragments.javaobject.ComboboxRow;
import it.usi.xframe.xas.html.fragments.javaobject.Dropdown;
import it.usi.xframe.xas.html.fragments.javaobject.DropdownRow;
import it.usi.xframe.xas.html.fragments.javaobject.Field;
import it.usi.xframe.xas.html.fragments.javaobject.FieldDate;
import it.usi.xframe.xas.html.fragments.javaobject.Form;
import it.usi.xframe.xas.html.fragments.javaobject.Page;
import it.usi.xframe.xas.html.fragments.javaobject.Radio;
import it.usi.xframe.xas.html.fragments.javaobject.RadioGroup;
import it.usi.xframe.xas.html.fragments.javaobject.Spinner;
import it.usi.xframe.xas.html.fragments.javaobject.Switch;
import it.usi.xframe.xas.html.fragments.javaobject.Table;
import it.usi.xframe.xas.html.fragments.javaobject.TableRow;
import it.usi.xframe.xas.html.fragments.javaobject.TestTag;
import it.usi.xframe.xas.html.fragments.javaobject.TextArea;
import it.usi.xframe.xas.html.fragments.javaobject.TitleBar;
import it.usi.xframe.xas.html.fragments.javaobject.TitleMenu;
import it.usi.xframe.xas.html.fragments.javaobject.TitleVoice;
import it.usi.xframe.xas.html.fragments.javaobject.Toogle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class TestHtmlObjects
 */
public class ServletTestHtmlObjects extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(ServletTestHtmlObjects.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletTestHtmlObjects() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		this.execute(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		this.execute(request, response);
	}

	private class TestParams extends MethodData
	{
		// servlet params
		public final String CHART1		= "chart1";
		public final String CHECK1		= "check1";
		public final String COMBOBOX1	= "combobox1";
		public final String DATEBEGIN	= "dateBegin";
		public final String DATEEND		= "dateEnd";
		public final String DROPDOWN1	= "dropdown1";
		public final String EMAIL1		= "email1";
		public final String FILE1		= "file1";
		public final String NUMBER1		= "number1";
		public final String PASSWORD1	= "password1";
		public final String PHONE1		= "phone1";
		public final String RADIOGROUP1	= "radiogroup1";
		public final String SWITCH1		= "switch1";
		public final String TABLE1		= "table1";
		public final String TABLEAJAX1	= "tableajax1";
		public final String TABLEAJAX2	= "tableajax2";
		public final String TESTTAG1	= "testtag1";
		public final String TEXT1		= "text1";
		public final String TEXT2		= "text2";
		public final String TEXTAREA1	= "textarea1";
		public final String TOOGLE1		= "toogle1";
		public final String USER		= "user";
	}

	protected void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		TestParams  data = new TestParams();
		data.set(data.CHECK1, getRequestParameter(req,data.CHECK1), "");
		boolean bool_check1 = Boolean.valueOf(data.getString(data.CHECK1).toLowerCase()).booleanValue();
		data.set(data.COMBOBOX1, getRequestParameter(req,data.COMBOBOX1), "");
		data.set(data.DATEBEGIN, getRequestParameter(req,data.DATEBEGIN), "");
		Calendar calendarBegin = FieldDate.toCalendar(data.getString(data.DATEBEGIN));
		data.set(data.DATEEND, getRequestParameter(req,data.DATEEND), "");
		Calendar calendarEnd = FieldDate.toCalendar(data.getString(data.DATEEND));
		data.set(data.DROPDOWN1, getRequestParameter(req,data.DROPDOWN1), "");
		data.set(data.EMAIL1, getRequestParameter(req,data.EMAIL1), "");
		data.set(data.FILE1, getRequestParameter(req,data.FILE1), "");
		data.set(data.NUMBER1, getRequestParameter(req,data.NUMBER1), "");
		data.set(data.PASSWORD1, getRequestParameter(req,data.PASSWORD1), "");
		data.set(data.PHONE1, getRequestParameter(req,data.PHONE1), "");
		data.set(data.RADIOGROUP1, getRequestParameter(req,data.RADIOGROUP1), "");
		data.set(data.SWITCH1, getRequestParameter(req,data.SWITCH1), "");
		data.set(data.TABLE1, getRequestParameter(req,data.TABLE1), "");
		data.set(data.TABLEAJAX1, getRequestParameter(req,data.TABLEAJAX1), "");
		data.set(data.TEXT1, getRequestParameter(req,data.TEXT1), "");
		data.set(data.TEXT2, getRequestParameter(req,data.TEXT2), "");
		data.set(data.TESTTAG1, getRequestParameter(req,data.TESTTAG1), "");
		data.set(data.TEXTAREA1, getRequestParameter(req,data.TEXTAREA1), "");
		data.set(data.TOOGLE1, getRequestParameter(req,data.TOOGLE1), Toogle.STATUS_OFF);
		boolean bool_toogle1 = Toogle.STATUS_ON.equals(data.getString(data.TOOGLE1));
		data.set(data.USER, getRequestParameter(req,data.USER), "");

		data.dump();
		
		String useragent = req.getHeader("User-Agent").toLowerCase();
		logger.debug("User-Agent:" + useragent);
		BrowserType browser = null;
		if(useragent.indexOf("firefox")==-1)
			browser = BrowserType.IE;
		else
			browser = BrowserType.FIREFOX;

		Page page = new Page("XAS Console");
		page.setBrowser(browser);
		page.add(new Spinner("spinner1"));
		
		TitleBar titleBar = new TitleBar("titleBar1", req.getContextPath() + "/" + ServletConsole.SERVLETNAME, "HOME");
		page.add(titleBar);

//		titleBar.add(getTitleMenu(req, new Sms(), "Sms", reqString));
//		titleBar.add(getTitleMenu(req, new SmsInternational(), "Sms international", reqString));
//		titleBar.add(getTitleMenu(req, new EMail(), "EMail", reqString));
//		titleBar.add(getTitleMenu(req, new Search(), "Search", reqString));
//		titleBar.add(getTitleMenu(req, new Monitor(), "Monitor", reqString));
//		titleBar.add(getTitleMenu(req, new Account(), "User", reqString));
		
		Form form = new Form("form1", "luca", "Input data:");
		page.add(form);

		int counter=0;
		int TEST_ALL			= counter++;
		int TEST_CHART			= counter++;
		int TEST_COMBO			= counter++;
		int TEST_DROPDOWN		= counter++;
		int TEST_NOTHING		= counter++;
		int TEST_RADIO1			= counter++;
		int TEST_RADIO2			= counter++;
		int TEST_TESTTAG		= counter++;
		int TEST_TEXT			= counter++;
		int TEST_TOOGLE			= counter++;
		int TEST_TABLE1			= counter++;
		int TEST_TABLEAJAXMASTER = counter++;
		int TEST_TABLEAJAXMASTERDETAIL = counter++;
		
		int testType = TEST_TOOGLE;
		
		if(testType==TEST_NOTHING) {
		}
		if(testType==TEST_TESTTAG) {
			// texttag
			form.add(new TestTag(data.TESTTAG1, "value"));
		}
		if(testType==TEST_TEXT) {
			// text
			Field field_text1 = new Field(data.TEXT1, data.getString(data.TEXT1), "Text1", FieldType.TYPE_TEXT);
			field_text1.setSuggestion("Un suggerimento fa sempre bene");
			form.add(field_text1);
		}
		if(testType==TEST_COMBO) {
			// test combobox
			Combobox field_combobox1 = new Combobox(data.COMBOBOX1, "Test Combobox");
			field_combobox1.add(new ComboboxRow("", "", false));
			field_combobox1.add(new ComboboxRow("1", "desc (1)", "1".equals(data.getString(data.COMBOBOX1))? true:false));
			field_combobox1.add(new ComboboxRow("2", "desc (2)", "2".equals(data.getString(data.COMBOBOX1))? true:false));
			form.add(field_combobox1);
		}
		if(testType==TEST_DROPDOWN) {
			// test dropdown
			Dropdown field_dropdown1 = new Dropdown(data.DROPDOWN1, "Test dropdown");
			//field_dropdown1.add(new ComboboxRow("", "", false));
			field_dropdown1.add(new ComboboxRow("1", "Emma (1)", "1".equals(data.getString(data.DROPDOWN1))? true:false));
			field_dropdown1.add(new ComboboxRow("2", "Juny (2)", "2".equals(data.getString(data.DROPDOWN1))? true:false));
			form.add(field_dropdown1);

			form.add(new FieldDate(data.DATEBEGIN, calendarBegin, "Date begin"));
			form.add(new Field(data.TEXT1, data.getString(data.TEXT1), "Testo 1", FieldType.TYPE_TEXT));
		}
		if(testType==TEST_RADIO2)
		{
			// radio
			RadioGroup radiogroup = new RadioGroup(data.RADIOGROUP1, "Scelta"); 
			radiogroup.add(new Radio("radio1", data.RADIOGROUP1, "1", "si", "1".equals(data.getString(data.RADIOGROUP1)) ? true : false), true);
			radiogroup.add(new Radio("radio2", data.RADIOGROUP1, "2", "no", "2".equals(data.getString(data.RADIOGROUP1)) ? true : false), true);
			radiogroup.add(new Radio("radio3", data.RADIOGROUP1, "3", "forse", "3".equals(data.getString(data.RADIOGROUP1)) ? true : false), true);
			form.add(radiogroup);
		}
		if(testType==TEST_RADIO1)
		{
			// radio
//			RadioGroup radiogroup = new RadioGroup("radiogroup1", "Scelta");
//			form.add(radiogroup, false);

			Radio radio1 = new Radio("radio1", data.RADIOGROUP1, "1", "si", "1".equals(data.getString(data.RADIOGROUP1)) ? true : false);
			radio1.setEnabledFields(new String[] {"text1"});
			radio1.setDisabledFields(new String[] {"text2"});
			Radio radio2 = new Radio("radio2", data.RADIOGROUP1, "2", "no", "2".equals(data.getString(data.RADIOGROUP1)) ? true : false);
			radio2.setEnabledFields(new String[] {data.TEXT2});
			radio2.setDisabledFields(new String[] {data.TEXT1});

			Field field_text1 = new Field(data.TEXT1, data.getString(data.TEXT1), "Testo 1", FieldType.TYPE_TEXT);
			Field field_text2 = new Field(data.TEXT2, data.getString(data.TEXT2), "Testo 2", FieldType.TYPE_TEXT);

			field_text1.add(radio1, true);
			form.add(field_text1);
			
			field_text2.add(radio2, true);
			form.add(field_text2);
		}
		if(testType==TEST_TOOGLE)
		{
			// fielddate begin
			form.add(new FieldDate(data.DATEBEGIN, calendarBegin, "Date begin"));
			// toogle1
			Toogle field_toogle1 = new Toogle(data.TOOGLE1, bool_toogle1, "");
			field_toogle1.setDisabledFields(new String[] {data.TEXT1, data.NUMBER1, data.DATEBEGIN});
			// field text
			Field field_text1 = new Field(data.TEXT1, data.getString(data.TEXT1), "Sms message", FieldType.TYPE_TEXT);
			field_text1.setSuggestion("Insert a valid text not too short not too long.");
			field_text1.setEnabled(false);
			form.add(field_text1);
			field_text1.add(field_toogle1, true);
			// field number
			Field field_numer1 = new Field("number1", data.getString(data.NUMBER1), "Number", FieldType.TYPE_NUMBER);
			form.add(field_numer1);
		}

		if(testType==TEST_TABLE1)
		{
			// simple table
			Table table1 = new Table(data.TABLE1, "SIMPLE table grid", new String[] {"#", "value"});
			for(int rowcounter=0;rowcounter<20;rowcounter++)
				table1.add(new TableRow(Integer.toString(rowcounter), new String[] {Integer.toString(rowcounter), "value " + Integer.toString(rowcounter)}));
			form.add(table1);
		}
		if(testType==TEST_TABLEAJAXMASTER)
		{
			String urlMaster = "http://guupzm000102.hd00.unicreditgroup.eu:9089/XA-XAS-PF/table_master.json";
			Table tableAjax1 = new Table(data.TABLEAJAX1, "AJAX table grid", (String[]) new String[] {"id", "name", "total"});
			tableAjax1.setAjaxCall(true, urlMaster, null);
			form.add(tableAjax1);
		}	
		if(testType==TEST_TABLEAJAXMASTERDETAIL)
		{
			String urlMaster2 = "http://guupzm000102.hd00.unicreditgroup.eu:9089/XA-XAS-PF/table_master.json";
			String urlDetail2 = "http://guupzm000102.hd00.unicreditgroup.eu:9089/XA-XAS-PF/table_masterdetail.json";
			Table tableAjax2 = new Table(data.TABLEAJAX2, "AJAX table grid", (String[]) new String[] {"id", "name", "total"});
			tableAjax2.setAjaxCall(true, urlMaster2, urlDetail2);
			tableAjax2.setSearchKey("id");
			form.add(tableAjax2);
		}	
		if(testType==TEST_CHART)
		{
			// chart
			form.add(new Chart(data.CHART1, "Line chart"));
		}
		if(testType==TEST_ALL)
		{
			// switch
			form.add(new Switch(data.SWITCH1, true, "Switch"));
			// radio
			RadioGroup radiogroup = new RadioGroup("radiogroup1", "Scelta"); 
			radiogroup.add(new Radio("radio1", data.RADIOGROUP1, "1", "si", "1".equals(data.getString(data.RADIOGROUP1)) ? true : false), true);
			radiogroup.add(new Radio("radio2", data.RADIOGROUP1, "2", "no", "2".equals(data.getString(data.RADIOGROUP1)) ? true : false), true);
			radiogroup.add(new Radio("radio3", data.RADIOGROUP1, "3", "forse", "3".equals(data.getString(data.RADIOGROUP1)) ? true : false), true);
			form.add(radiogroup);
			// fielddate begin
			form.add(new FieldDate(data.DATEBEGIN, calendarBegin, "Date begin"));
			// fielddate end
			form.add(new FieldDate(data.DATEEND, calendarEnd, "Date end"));
			// field phone
			form.add(new Field(data.PHONE1, data.getString(data.PHONE1), "Phone number", FieldType.TYPE_PHONE));
			// field text
			Field field_text1 = new Field(data.TEXT1, data.getString(data.TEXT1), "Sms message", FieldType.TYPE_TEXT);
			field_text1.setSuggestion("Insert a valid text not too short not too long.");
			form.add(field_text1);
			// field number
			Field field_numer1 = new Field(data.NUMBER1, data.getString(data.NUMBER1), "Number", FieldType.TYPE_NUMBER);
			form.add(field_numer1);
			// field email
			Field field_email1 = new Field(data.EMAIL1, data.getString(data.EMAIL1), "Email address", FieldType.TYPE_EMAIL);;
			form.add(field_email1);
			// field password
			form.add(new Field(data.PASSWORD1, data.getString(data.PASSWORD1), "Password", FieldType.TYPE_PASSWORD));
			// field file
			form.add(new Field(data.FILE1, data.getString(data.FILE1), "File", FieldType.TYPE_FILE));
			// textarea
			form.add(new TextArea(data.TEXTAREA1, data.getString(data.TEXTAREA1), "Free text", 5));
			// check
			form.add(new Check(data.CHECK1, "Checkbox confirmation", bool_check1));
			// dropdown
			Dropdown dropdown = new Dropdown(data.DROPDOWN1, "Dropdown list");
			dropdown.add(new DropdownRow("", "", false));
			dropdown.add(new DropdownRow("1", "desc (1)", "1".equals(data.getString(data.DROPDOWN1))? true:false));
			dropdown.add(new DropdownRow("2", "desc (2)", "2".equals(data.getString(data.DROPDOWN1))? true:false));
			dropdown.add(new DropdownRow("3", "desc (3)", "3".equals(data.getString(data.DROPDOWN1))? true:false));
			form.add(dropdown);
			// simple table
			Table table1 = new Table(data.TABLE1, "SIMPLE table grid", new String[] {"#", "value"});
			for(int rowcounter=0;rowcounter<20;rowcounter++)
				table1.add(new TableRow(Integer.toString(rowcounter), new String[] {Integer.toString(rowcounter), "value " + Integer.toString(rowcounter)}));
			form.add(table1);
			// alert
			form.add(new Alert(null, "info text", AlertType.INFO));
		}
		page.serialize(req, resp);
	}
	
//	private TitleMenu getTitleMenu(HttpServletRequest req, BaseServletOld bservlet, String menuTitle, String reqString)
//	{
//		TitleMenu titleMenu = new TitleMenu(bservlet.getServletNameEx(), bservlet.getServletNameEx(), menuTitle);
//		String[] methods = bservlet.getMethods();
//		for(int i=0; i<methods.length; i++)
//		{
//			String href = req.getContextPath() + "/" + bservlet.getServletNameEx() + "?" + BaseServletOld.PAGE_KEY + "=" + methods[i] + "&" + reqString;
//			titleMenu.add(new TitleVoice("voice" + i, href, methods[i]));
//		}
//		return titleMenu ;
//	}

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
			boolean state = false;
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
	
	/**
	 * @param req
	 * @param param
	 * @return
	 */
	private String getRequestParameter(HttpServletRequest req,String param){
// To uncomment if you want to use the StringEscapeUtils class		
//		String safeValue = StringEscapeUtils.escapeHtml(req.getParameter(param));
		String safeValue = req.getParameter(param);
		if(null == safeValue)
			safeValue = "";
		return safeValue;
	}		

}
