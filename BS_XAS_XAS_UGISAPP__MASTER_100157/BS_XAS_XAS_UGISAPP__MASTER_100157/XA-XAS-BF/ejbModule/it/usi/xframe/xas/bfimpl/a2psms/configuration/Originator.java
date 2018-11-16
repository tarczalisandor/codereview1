package it.usi.xframe.xas.bfimpl.a2psms.configuration;

import java.util.regex.Pattern;

public class Originator {
	private String name         	= null;
	private Pattern routerRegex	    = null;
	private Integer typeOfNumber   	= null;
	private Integer numPlanId	   	= null;
	private String address			= null;
	private String alias	        = null;
	public String getName() {
    	return name;
    }
	public void setName(String name) {
    	this.name = name;
    }
	public Pattern getRouterRegex() {
    	return routerRegex;
    }
	public void setRouterRegex(Pattern routerRegex) {
    	this.routerRegex = routerRegex;
    }
	public Integer getTypeOfNumber() {
    	return typeOfNumber;
    }
	public void setTypeOfNumber(Integer typeOfNumber) {
    	this.typeOfNumber = typeOfNumber;
    }
	public Integer getNumPlanId() {
    	return numPlanId;
    }
	public void setNumPlanId(Integer numPlanId) {
    	this.numPlanId = numPlanId;
    }
	public String getAddress() {
    	return address;
    }
	public void setAddress(String address) {
    	this.address = address;
    }
	public String getAlias() {
    	return alias;
    }
	public void setAlias(String alias) {
    	this.alias = alias;
    }
	public String getOriginator() {
		return this.alias != null && this.alias.length() != 0 ? this.alias : this.address;
	}

}
