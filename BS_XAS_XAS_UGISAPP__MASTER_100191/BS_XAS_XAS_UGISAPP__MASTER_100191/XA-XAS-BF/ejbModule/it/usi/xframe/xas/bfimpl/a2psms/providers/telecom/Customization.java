package it.usi.xframe.xas.bfimpl.a2psms.providers.telecom;

import it.usi.xframe.xas.bfimpl.a2psms.configuration.CustomizationBasic;
import it.usi.xframe.xas.bfutil.ConstantsSplunk;
import it.usi.xframe.xas.wsutil.ENUM_STATUS;

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
	static public String[] xasDRstatus(String status) {
		String[] xasStatus = {"","",""};
		if (status.equals(ENUM_STATUS._DELIVERED_TO_DEV)) {
			xasStatus = new String[] {"2", ConstantsSplunk.V_SMSSTATUS_OK, "DELIVERED: Message is delivered to destination"};
		} else if (status.equals(ENUM_STATUS._EXPIRED_IN_QUEUE) || status.equals(ENUM_STATUS._EXPIRED_ON_SMSC)) {
			xasStatus = new String[] {"3", ConstantsSplunk.V_SMSSTATUS_ERROR, "EXPIRED: Message validity period has expired"};
		} else if (status.equals(ENUM_STATUS._DROPPED_BY_SMSC)) {
			xasStatus = new String[] {"4", ConstantsSplunk.V_SMSSTATUS_ERROR, "DELETED: Message has been deleted"};
		} else if (status.equals(ENUM_STATUS._NO_ROUTE)) {
			xasStatus = new String[] {"5", ConstantsSplunk.V_SMSSTATUS_ERROR, "UNDELIVERABLE: Message is undeliverable"};
		} else if (status.equals(ENUM_STATUS._UNKNOWN_SMSC_RESPONSE)) {
			xasStatus = new String[] {"7", ConstantsSplunk.V_SMSSTATUS_ERROR, "UNKNOWN: Message is in invalid state"};
		} else if (status.equals(ENUM_STATUS._REJECTED_BY_DEV) || status.equals(ENUM_STATUS._REJECTED_BY_FILTER) || status.equals(ENUM_STATUS._REJECTED_BY_SMSC)) {
			xasStatus = new String[] {"8", ConstantsSplunk.V_SMSSTATUS_ERROR, "REJECTED: Message is in a rejected state"};
		} else {
			xasStatus = new String[] {"9", ConstantsSplunk.V_SMSSTATUS_ERROR, "UNDEFINED STATE: Message is in an undefined status:" + status};
		}
		return xasStatus;
 	}
	
}