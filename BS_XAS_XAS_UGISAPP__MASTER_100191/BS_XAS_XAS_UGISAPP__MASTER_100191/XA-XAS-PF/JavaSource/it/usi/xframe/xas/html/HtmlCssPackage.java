package it.usi.xframe.xas.html;

public class HtmlCssPackage
{
	// CSS Core
	public static HtmlCssPackage CSS_BOOSTRAP				= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_BOOSTRAP);
	public static HtmlCssPackage CSS_BOOSTRAP_RESPONSIVE	= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_BOOSTRAP_RESPONSIVE);
	public static HtmlCssPackage CSS_FONTAWESOME			= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_FONTAWESOME);
	public static HtmlCssPackage CSS_NORMALIZE				= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_NORMALIZE);
	// CSS Third parties
	public static HtmlCssPackage CSS_BOOSTRAP_FORMHELPERS	= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_BOOSTRAP_FORMHELPERS);
	public static HtmlCssPackage CSS_BOOSTRAP_MDB			= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_BOOSTRAP_MDB);
	public static HtmlCssPackage CSS_TEMPUSDOMINUS			= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_TEMPUSDOMINUS);
	public static HtmlCssPackage CSS_BOOSTRAP_COMBOBOX		= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_BOOSTRAP_COMBOBOX);
	public static HtmlCssPackage CSS_DATATABLES_BOOSTRAP	= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_DATATABLES_BOOSTRAP);
	public static HtmlCssPackage CSS_DATATABLES_JQUERY		= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_DATATABLES_JQUERY);
	public static HtmlCssPackage CSS_DATATABLES				= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_DATATABLES);
	public static HtmlCssPackage CSS_DATATABLES_SELECT		= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_DATATABLES_SELECT);
	public static HtmlCssPackage CSS_TESTCHARTJS			= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_TESTCHARTJS);
	public static HtmlCssPackage CSS_SPINNER				= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_SPINNER);
	public static HtmlCssPackage CSS_TOOGLE					= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_TOOGLE);
	// CSS Application Core
	public static HtmlCssPackage CSS_APP_CORE				= new HtmlCssPackage(HtmlCssPackage.VALUE_CSS_APP_CORE);
	
	// values are used to determinate the weight, the right position in the html packages list
	// 0 = core components, will be skipped by page
	// 1 = third parties components
	// 3 = application components
	// CSS Core
	public static final String VALUE_CSS_BOOSTRAP				= "01.boostrap";				// For bootstrap
	public static final String VALUE_CSS_BOOSTRAP_RESPONSIVE	= "02.bootstrap-responsive";	// For bootstrap
	public static final String VALUE_CSS_FONTAWESOME			= "03.fontawesome";				// For icons and fonts
	public static final String VALUE_CSS_NORMALIZE				= "04.normalize";				// For improved cross-browser rendering
	// CSS Third parties
	public static final String VALUE_CSS_BOOSTRAP_FORMHELPERS	= "10.bootstrap-formhelpers";
	public static final String VALUE_CSS_BOOSTRAP_MDB			= "11.bootstrap-mdb";
	public static final String VALUE_CSS_TEMPUSDOMINUS			= "12.tempusdominus";
	public static final String VALUE_CSS_BOOSTRAP_COMBOBOX		= "13.boostrap_combobox";
	public static final String VALUE_CSS_DATATABLES_BOOSTRAP	= "14.datatables.boostrap";
	public static final String VALUE_CSS_DATATABLES_JQUERY		= "15.datatables.jquery";
	public static final String VALUE_CSS_DATATABLES				= "16.datatables";
	public static final String VALUE_CSS_DATATABLES_SELECT		= "17.datatables-select";
	public static final String VALUE_CSS_TESTCHARTJS			= "19.testchartjs";
	public static final String VALUE_CSS_SPINNER				= "20.spinner";
	public static final String VALUE_CSS_TOOGLE					= "21.toogle";
	// CSS Application Core
	public static final String VALUE_CSS_APP_CORE				= "30.app-core";

	private String value = null;
	
	private HtmlCssPackage (String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

	public boolean isEqual(HtmlCssPackage css)
	{
		if(this.value.compareTo(css.value)==0)
			return true;
		return false;
	}

	/**
	 * @param css
	 * @return return the html package include script
	 */
	public static String getPackage(HtmlCssPackage o)
	{
		if(o.isEqual(CSS_BOOSTRAP))
			return "<link href=\"./Bootstrap/dist/css/bootstrap.min.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_BOOSTRAP_RESPONSIVE))
			return "<link href=\"./css/bootstrap-responsive.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_FONTAWESOME))
			return "<link href=\"./fontawesome/dist/css/fontawesome-all.min.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_NORMALIZE))
			return "<link href=\"./css/normalize.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_TEMPUSDOMINUS))
			return "<link href=\"./tempusdominus/dist/css/tempusdominus-bootstrap-4.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_BOOSTRAP_FORMHELPERS))
			return "<link href=\"./BootstrapFormHelpers/dist/css/bootstrap-formhelpers.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_TESTCHARTJS))
			return "<link href=\"./chartjs/dist/js/Chart-navbar.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_APP_CORE))
			return "<link href=\"./css/app-core.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_TOOGLE))
			return "<link href=\"./toogle/dist/css/bootstrap-toggle.min.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_BOOSTRAP_MDB))
			return "<link href=\"./mdb/dist/css/mdb.min.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_BOOSTRAP_COMBOBOX))
			return "<link href=\"./combobox/dist/css/bootstrap-combobox.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_DATATABLES_JQUERY))
			return "<link href=\"./datatables/DataTables-1.10.16/css/jquery.bootstrap4.min.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_DATATABLES_BOOSTRAP))
			return "<link href=\"./datatables/DataTables-1.10.16/css/dataTables.bootstrap4.min.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_DATATABLES))
			return "<link href=\"./datatables/datatables.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_DATATABLES_SELECT))
			return "<link href=\"./datatables/select.bootstrap.min.css\" rel=\"stylesheet\">";
		else if(o.isEqual(CSS_SPINNER))
			return "<link href=\"./spinner/dist/css/spinner.css\" rel=\"stylesheet\">";
		
		return "";
	}
}
