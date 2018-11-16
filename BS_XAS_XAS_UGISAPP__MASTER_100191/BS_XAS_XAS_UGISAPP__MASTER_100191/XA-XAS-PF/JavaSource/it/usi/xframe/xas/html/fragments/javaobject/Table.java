package it.usi.xframe.xas.html.fragments.javaobject;

import java.util.ArrayList;

import it.usi.xframe.xas.html.HtmlCssPackage;
import it.usi.xframe.xas.html.HtmlJsPackage;
import it.usi.xframe.xas.html.HtmlObject;
import it.usi.xframe.xas.util.SplunkDataConstant;


/**
 * @author C305373
 *
 * datatable web: https://datatables.net
 */
public class Table extends HtmlObject
{
	private static final String COMPONENT_NAME = "table_";
	
	private String label = null;
	private String[] columns = null;
	private ArrayList rows = null;

	private boolean ajaxCall = false;
	private String ajaxUrlMaster = null;
	private String ajaxUrlDetails = null;
	private String ajaxSearchKey = null;

	private boolean masterdetails = false;

	/**
	 * @param name
	 * @param label
	 * @param columns
	 */
	public Table(String name, String label, String[] columns)
	{
		super();
		this.setName(name);
		this.label = label;
		this.columns = columns;
	}
	
	/**
	 * @return the ajax
	 */
	public boolean isAjaxCall()
	{
		return ajaxCall;
	}

	/**
	 * @param ajax the ajax to set
	 */
	public void setAjaxCall(boolean enable, String urlMaster, String urlDetail)
	{
		this.ajaxCall = enable;
		this.ajaxUrlMaster = urlMaster;
		if(urlDetail!=null) {
			this.masterdetails = true;
			this.ajaxUrlDetails = urlDetail;
		}
	}

	/**
	 * @return
	 */
	public String SearchKey() {
		return ajaxSearchKey;
	}

	/**
	 * @param searchKey
	 */
	public void setSearchKey(String searchKey) {
		this.ajaxSearchKey = searchKey;
	}

	/**
	 * @return
	 */
	public String getUrlMaster() {
		return ajaxUrlMaster;
	}

	/**
	 * @param url
	 */
	public void setUrlMaster(String url) {
		this.ajaxUrlMaster = url;
	}

	/**
	 * @return the urlDetails
	 */
	public String getUrlDetails() {
		return ajaxUrlDetails;
	}

	/**
	 * @param urlDetails the urlDetails to set
	 */
	public void setUrlDetails(String urlDetails) {
		this.ajaxUrlDetails = urlDetails;
	}

	/**
	 * @return
	 */
	public ArrayList getRows() {
		return rows;
	}

	/**
	 * @param rows
	 */
	public void setRows(ArrayList rows) {
		this.rows = rows;
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#replaceHeaderPlaceholder()
	 */
	public void replaceHeaderPlaceholder() 
	{
		String field_urlDetails = null;
		String field_column_length = null;
		String field_ready = null;

		replace(COMPONENT_NAME + "name", this.getName());
		replace(COMPONENT_NAME + "label", this.label);
		replace(COMPONENT_NAME + "searchKey", this.ajaxSearchKey);
		StringBuffer sb = new StringBuffer();
		if(masterdetails) 
			sb.append("<th></th>");
		if(columns!=null)
			for(int i=0; i<columns.length; i++)
				sb.append("<th>" + columns[i] + "</th>");
		replace(COMPONENT_NAME + "columns", sb.toString());
		replace(COMPONENT_NAME + "url", ajaxUrlMaster);
		
		StringBuffer column_list = new StringBuffer();
		for(int i=0;i<columns.length;i++){
			if(i == (columns.length-1)){
				column_list.append("	{ \"className\": \"" + columns[i] + "\",");
				column_list.append("\"data\": \"" + columns[i] + "\" }\n");
			}else{
				column_list.append("	{ \"className\": \"" + columns[i] + "\",");
				column_list.append("\"data\": \"" + columns[i] + "\" },\n");
			}
		}
		replace(COMPONENT_NAME + "column_list", column_list.toString());
		
		if(this.ajaxCall)
		{
			if(this.masterdetails)
			{
				field_urlDetails = this.ajaxUrlDetails;
				field_column_length = Integer.toString(this.columns.length+1);
				field_ready = "table_ajax_masterdetail_ready";
			}
			else
			{
				field_urlDetails = "";
				field_column_length = Integer.toString(this.columns.length);
				field_ready = "table_ajax_ready";
			}
		}
		else
		{
			field_urlDetails = "";
			field_column_length = Integer.toString(this.columns.length);
			field_ready = "table_ready";
		}

		replace(COMPONENT_NAME + "urlDetails", field_urlDetails);
		replace(COMPONENT_NAME + "column_length", field_column_length);
		replace(COMPONENT_NAME + "ready", field_ready);
	}
	
	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateCssPackages()
	 */
	protected void populateCssPackages()
	{
		super.add(HtmlCssPackage.CSS_DATATABLES_JQUERY);
		super.add(HtmlCssPackage.CSS_DATATABLES_BOOSTRAP);
		super.add(HtmlCssPackage.CSS_DATATABLES_SELECT);
		super.add(HtmlCssPackage.CSS_SPINNER);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.html.HtmlObject#populateJsPackages()
	 */
	protected void populateJsPackages()
	{
		super.add(HtmlJsPackage.JS_DATATABLES_JQYERY);
		super.add(HtmlJsPackage.JS_DATATABLES_BOOSTRAP);
		super.add(HtmlJsPackage.JS_SPINNER);
	}
}
