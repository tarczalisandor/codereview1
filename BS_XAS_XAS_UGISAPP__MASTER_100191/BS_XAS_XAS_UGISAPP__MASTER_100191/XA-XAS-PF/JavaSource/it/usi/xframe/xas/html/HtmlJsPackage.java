package it.usi.xframe.xas.html;

public class HtmlJsPackage
{
	// JS core
	public static HtmlJsPackage JS_JQUERY						= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_JQUERY);
	public static HtmlJsPackage JS_POPPER						= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_POPPER);
	public static HtmlJsPackage JS_TOOLTIP						= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_TOOLTIP);
	public static HtmlJsPackage JS_FONTAWESOME					= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_FONTAWESOME);
	public static HtmlJsPackage JS_BOOSTRAP						= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_BOOSTRAP);
	// JS Third parties
	public static HtmlJsPackage JS_MOMENT_LOCALE				= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_MOMENT_LOCALE);
	public static HtmlJsPackage JS_BOOSTRAP_MDB					= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_BOOSTRAP_MDB);
	public static HtmlJsPackage JS_TEMPUSDOMINUS				= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_TEMPUSDOMINUS);
	public static HtmlJsPackage JS_BOOSTRAP_COMBOBOX			= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_BOOSTRAP_COMBOBOX);
	public static HtmlJsPackage JS_BOOSTRAP_FORMHELPERS			= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_BOOSTRAP_FORMHELPERS);
	public static HtmlJsPackage JS_BOOSTRAP_FORMHELPERS_PHONE	= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_BOOSTRAP_FORMHELPERS_PHONE);
	public static HtmlJsPackage JS_CHARTJS						= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_CHARTJS);
	public static HtmlJsPackage JS_DATATABLES_JQYERY			= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_DATATABLES_JQUERY);
	public static HtmlJsPackage JS_DATATABLES_BOOSTRAP			= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_DATATABLES_BOOSTRAP);
	public static HtmlJsPackage JS_DATATABLES					= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_DATATABLES);
	public static HtmlJsPackage JS_TOOGLE						= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_TOOGLE);
	public static HtmlJsPackage JS_PRETTYPRINT					= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_PRETTYPRINT);
	public static HtmlJsPackage JS_SPINNER						= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_SPINNER);
	// app core
	public static HtmlJsPackage JS_APP_CORE						= new HtmlJsPackage(HtmlJsPackage.VALUE_JS_APP_CORE);
	
	// values are used to determinate the weight, the right position in the html packages list
	// 0 = core components, will be skipped by page
	// 1 = third parties components
	// 3 = application components
	// JS core
	public static final String VALUE_JS_JQUERY						= "01.jquery";
	public static final String VALUE_JS_POPPER						= "02.popper";
	public static final String VALUE_JS_TOOLTIP						= "03.tooltip";
	public static final String VALUE_JS_FONTAWESOME					= "04.fontawesome";
	public static final String VALUE_JS_BOOSTRAP					= "05.boostrap";
	// JS Third parties
	public static final String VALUE_JS_MOMENT_LOCALE				= "10.moment-locale";
	public static final String VALUE_JS_BOOSTRAP_MDB				= "11.bootsrap-mdb";
	public static final String VALUE_JS_TEMPUSDOMINUS				= "12.tempusdominus";
	public static final String VALUE_JS_BOOSTRAP_COMBOBOX			= "13.boostrap-combobox";
	public static final String VALUE_JS_BOOSTRAP_FORMHELPERS		= "14.boostrap-formhelpers";
	public static final String VALUE_JS_BOOSTRAP_FORMHELPERS_PHONE	= "15.boostrap-formhelpers-phone";
	public static final String VALUE_JS_CHARTJS						= "16.chartjs";
	public static final String VALUE_JS_DATATABLES_JQUERY			= "17.datatables-jquery";
	public static final String VALUE_JS_DATATABLES_BOOSTRAP			= "18.datatables.boostrap";
	public static final String VALUE_JS_DATATABLES					= "19.datatables";
	public static final String VALUE_JS_TOOGLE						= "20.toogle";
	public static final String VALUE_JS_PRETTYPRINT					= "21.prettyprint";
	public static final String VALUE_JS_SPINNER						= "22.spinner";
	// Application core JS
	public static final String VALUE_JS_APP_CORE					= "30.app-core";

	private String value = null;
	
	private HtmlJsPackage (String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

	public boolean isEqual(HtmlJsPackage css)
	{
		if(this.value.compareTo(css.value)==0)
			return true;
		return false;
	}

	/**
	 * @param o
	 * @return return the html package include script
	 */
	public static String getPackage(HtmlJsPackage o)
	{
		if(o.isEqual(JS_JQUERY)) {
			StringBuffer ret = new StringBuffer();
			ret.append("<script src=\"./jquery/3.3.1/jquery-3.3.1.min.js\" crossorigin=\"anonymous\"></script>\n");
			ret.append("<script>window.jQuery || document.write('<script src=\"./jquery/3.3.1/jquery-3.3.1.min.js\"><\\/script>')</script>");
			return ret.toString();
		}
		else if(o.isEqual(JS_POPPER))
			return "<script src=\"./popper/1.14.1/dist/js/popper.min.js\"></script>";
		else if(o.isEqual(JS_TOOLTIP))
			return "<script src=\"./popper/1.14.1/dist/js/tooltip.min.js\"></script>";
		else if(o.isEqual(JS_BOOSTRAP))
			return "<script src=\"./Bootstrap/dist/js/bootstrap.min.js\"></script>";
		else if(o.isEqual(JS_MOMENT_LOCALE))
			return "<script src=\"./moment/dist/js/moment-with-locales.min.js\"></script>";
		else if(o.isEqual(JS_FONTAWESOME))
			return "<script src=\"./fontawesome/dist/js/fontawesome-all.min.js\"></script>";
		else if(o.isEqual(JS_APP_CORE))
			return "<script src=\"./js/app-core.js\"></script>";
		else if(o.isEqual(JS_TEMPUSDOMINUS))
			return "<script src=\"./tempusdominus/dist/js/tempusdominus-bootstrap-4.js\"></script>";
		else if(o.isEqual(JS_BOOSTRAP_FORMHELPERS))
			return "<script src=\"./js/bootstrap-formhelpers-phone.format.js\"></script>";
		else if(o.isEqual(JS_BOOSTRAP_FORMHELPERS_PHONE))
			return "<script src=\"./BootstrapFormHelpers/js/bootstrap-formhelpers-phone.js\"></script>";
		else if(o.isEqual(JS_CHARTJS))
			return "<script src=\"./chartjs/dist/js/Chart.bundle.min.js\"></script>";
		else if(o.isEqual(JS_TOOGLE))
			return "<script src=\"./toogle/dist/js/bootstrap-toggle.min.js\"></script>";
		else if(o.isEqual(JS_PRETTYPRINT))
			return "<script src=\"./js/prettyprint.js\"></script>";
		else if(o.isEqual(JS_BOOSTRAP_MDB))
			return "<script src=\"./mdb/dist/js/mdb.min.js\"></script>";
		else if(o.isEqual(JS_BOOSTRAP_COMBOBOX))
			return "<script src=\"./combobox/dist/js/bootstrap-combobox.js\"></script>";
		else if(o.isEqual(JS_DATATABLES_BOOSTRAP))
			return "<script src=\"./datatables/DataTables-1.10.16/js/dataTables.bootstrap4.min.js\"></script>";
		else if(o.isEqual(JS_DATATABLES_JQYERY))
			return "<script src=\"./datatables/DataTables-1.10.16/js/jquery.dataTables.min.js\"></script>";
		else if(o.isEqual(JS_DATATABLES))
			return "<script src=\"./datatables/datatables.js\"></script>";
		else if(o.isEqual(JS_SPINNER))
			return "<script src=\"./spinner/dist/js/spinner.js\"></script>";

		return "";
	}
}
