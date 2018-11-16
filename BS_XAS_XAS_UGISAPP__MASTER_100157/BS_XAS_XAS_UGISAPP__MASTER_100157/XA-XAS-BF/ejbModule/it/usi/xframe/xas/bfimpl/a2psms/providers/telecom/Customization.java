package it.usi.xframe.xas.bfimpl.a2psms.providers.telecom;

import it.usi.xframe.xas.bfimpl.a2psms.configuration.CustomizationBasic;

public class Customization extends CustomizationBasic {
	public boolean isSupportDeliveryReport() {
	    return true;
    }
	public boolean isSupportMobileOriginated() {
	    return true;
    }
	public boolean isSupportReplaceMap() {
	    return true;
    }
	
}