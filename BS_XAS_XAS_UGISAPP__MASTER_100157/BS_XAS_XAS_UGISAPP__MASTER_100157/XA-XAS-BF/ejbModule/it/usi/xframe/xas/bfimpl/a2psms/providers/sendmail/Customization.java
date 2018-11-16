package it.usi.xframe.xas.bfimpl.a2psms.providers.sendmail;

import it.usi.xframe.xas.bfimpl.a2psms.configuration.CustomizationAbstract;

import java.util.HashMap;

public class Customization extends CustomizationAbstract {
	private HashMap mailMap = new HashMap();
	public void add(String user, String address) {
		mailMap.put(user, address);
	}
	public String get(String user) {
		return (String) mailMap.get(user);
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