package it.usi.xframe.xas.bfimpl.a2psms.providers.virtual;

import it.usi.xframe.xas.bfimpl.a2psms.configuration.CustomizationAbstract;

public class Customization extends CustomizationAbstract {
	private String user = null;
	private String password = null;
	
	public String getUser() {
    	return user;
    }
	public void setUser(String user) {
    	this.user = user;
    }
	public String getPassword() {
    	return password;
    }
	public void setPassword(String password) {
    	this.password = password;
    }
	public boolean isSupportDeliveryReport() {
	    return false;
    }
	public boolean isSupportMobileOriginated() {
	    return false;
    }
	public boolean isSupportReplaceMap() {
	    return false;
    }
	
	
}