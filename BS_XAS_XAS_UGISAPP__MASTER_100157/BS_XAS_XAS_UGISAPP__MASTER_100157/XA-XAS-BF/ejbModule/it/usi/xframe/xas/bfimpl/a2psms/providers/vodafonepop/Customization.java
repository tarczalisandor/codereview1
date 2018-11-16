package it.usi.xframe.xas.bfimpl.a2psms.providers.vodafonepop;

import it.usi.xframe.xas.bfimpl.a2psms.configuration.CustomizationBasic;


public class Customization extends CustomizationBasic {

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