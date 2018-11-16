/*
 * Created on June 17, 2015
 *
 */
package it.usi.xframe.xas.bfimpl.a2psms.dataobject;

import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.XASWrongRequestException;
import it.usi.xframe.xas.bfutil.data.SmsRequest;
import it.usi.xframe.xas.bfutil.data.SmsRequest3;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.ReadablePeriod;

/**
 * @author us01391
 */
public class InternalSmsRequest {
	private static final long serialVersionUID = 1L;
	/**
	 * Source object for SmsRequest/SmsRequest3.
	 */
	private Object source;
	
	//TODO definire i limiti
	private static final ReadablePeriod DELIVERY_LIMIT = Days.TWO;
	private static final ReadablePeriod VALIDITY_LIMIT = Days.TWO;
	private String srcPhoneNumber = null;
	private String srcXasUserName = null;

	private Date srcDelivery = null;
	private Date srcValidity = null;
	private Integer srcValidityPeriod = null;
	private String userId = null;

	private Boolean userInRole = null;
	private int protocolId = 0;
	private String uuid = null;
	private int firstNotGsm = -1;
	
	public int getFirstNotGsm() {
    	return firstNotGsm;
    }

	public void setFirstNotGsm(int firstNotGsm) {
    	this.firstNotGsm = firstNotGsm;
    }

	public int getProtocolId() {
    	return protocolId;
    }

	public void setProtocolId(int protocolId) {
    	this.protocolId = protocolId;
    }

	/**
	 * Create an internal sms request from an exposed sms request version 2.
	 * Saving in the src* fields the source values.
	 * @param source the exposed sms request
	 */
	public InternalSmsRequest(SmsRequest source) {
		this.source = source;
		initSrcField();
	}

	/**
	 * Create an internal sms request from an exposed sms request version 3.
	 * Saving in the src* fields the source values.
	 * @param source the exposed sms request
	 */
	public InternalSmsRequest(SmsRequest3 source) {
		this.source = source;
		initSrcField();
	}

	private void initSrcField() {
		srcPhoneNumber = ((SmsRequest)source).getPhoneNumber();
		srcXasUserName = ((SmsRequest)source).getXasUser();
		srcDelivery = ((SmsRequest)source).getDeliveryTime();
		srcValidity = ((SmsRequest)source).getValidity();
		srcValidityPeriod = ((SmsRequest)source).getValidityPeriod();
	}
	
	/*
	 * Source delegation
	 */
	public String getCorrelationID() {return ((SmsRequest)source).getCorrelationID();}
	public Date getDeliveryTime() {return ((SmsRequest)source).getDeliveryTime();}
	public Boolean getForceAsciiEncoding() {return ((SmsRequest)source).getForceAsciiEncoding();}
	public String getMsg() {return ((SmsRequest)source).getMsg();}
	public String getPhoneNumber() {return ((SmsRequest)source).getPhoneNumber();}
	public Boolean getSmsResponse() {return ((SmsRequest)source).getSmsResponse();}
	public Date getValidity() {return ((SmsRequest)source).getValidity();}
	public Integer getValidityPeriod() {return ((SmsRequest)source).getValidityPeriod();}
	// Available online for SmsRequest3.
	public String getReplaceClass() {return SmsRequest3.class.isInstance(source) ? ((SmsRequest3)source).getReplaceClass() : null;}
	public String getUuid() {return SmsRequest3.class.isInstance(source) ? ((SmsRequest3)source).getUuid() : this.uuid;}
	
	/**
	 * Get xasUserName.
	 * @deprecated use getXasUserName(). 
	 * @return xasUserName.
	 */
	public String getXasUser() {return ((SmsRequest)source).getXasUser();}
	public String getXasUserName() {return ((SmsRequest)source).getXasUser();}
	public void setCorrelationID(String p) {((SmsRequest)source).setCorrelationID(p);}
	public void setDeliveryTime(Date p) {((SmsRequest)source).setDeliveryTime(p);}
	public void setForceAsciiEncoding(Boolean p) {((SmsRequest)source).setForceAsciiEncoding(p);}
	public void setMsg(String p) {((SmsRequest)source).setMsg(p);}
	public void setPhoneNumber(String p) {((SmsRequest)source).setPhoneNumber(p);}
	public void setSmsResponse(Boolean p) {((SmsRequest)source).setSmsResponse(p);}
	public void setValidity(Date p) {((SmsRequest)source).setValidity(p);}
	public void setValidityPeriod(Integer p) {((SmsRequest)source).setValidityPeriod(p);}
	public void setXasUser(String p) {((SmsRequest)source).setXasUser(p);}
	public void setUuid(String p) { if (SmsRequest3.class.isInstance(source)) ((SmsRequest3)source).setUuid(p); else this.uuid = p;}
	/*
	 * End of source delegation
	 */
	
	/**
	 * Stores the phone number before it gets normalized
	 * Its getter returns phoneNumber when this is null
	 */
	private String preNormalizedPhoneNumber;
	byte[] msgBytes;
	byte encoding;
	public byte getEncoding() {
    	return encoding;
    }
	
	public static final byte ENCODING_GSM338 = 0;			// encoding default GSM 03.38	
	public static final byte ENCODING_UCS2   = 8;			// encoding UCS2 to support Unicode
	public boolean isEncodingUCS2() {
    	return encoding == ENCODING_UCS2;
    }
	public boolean isEncodingGSM338() {
    	return encoding == ENCODING_GSM338;
    }

	public void setEncodingUCS2() {
    	this.encoding = ENCODING_UCS2;
    }
	public void setEncodingGSM338() {
    	this.encoding = ENCODING_GSM338;
    }

	public String getEncodingDescription() {
		return this.encoding == ENCODING_GSM338 ? "GSM338" :
			   this.encoding == ENCODING_UCS2 ? "UCS2" :
				   "UNKNOWN";
	}

	public byte[] getMsgBytes() {
    	return msgBytes;
    }

	public void setMsgBytes(byte[] msgBytes) {
    	this.msgBytes = msgBytes;
    }

	/**
	 * Request Time.
	 * <p/>
	 * Used to set a time to send the sms.
	 */
	private Date requestTime;

	public final static int INTERFACE_VERSION_1 = 1;
	public final static int INTERFACE_VERSION_2 = 2;
	public final static int INTERFACE_VERSION_3 = 3;
	private int interfaceVersion;
	public int getInterfaceVersion() {
    	return interfaceVersion;
    }

	public void setInterfaceV1() {
    	this.interfaceVersion = INTERFACE_VERSION_1;
    }
	public void setInterfaceV2() {
    	this.interfaceVersion = INTERFACE_VERSION_2;
    }
	public void setInterfaceV3() {
    	this.interfaceVersion = INTERFACE_VERSION_3;
    }
	public boolean isInterfaceV1() {
		return this.interfaceVersion == INTERFACE_VERSION_1;
	}
	public boolean isInterfaceV2() {
		return this.interfaceVersion == INTERFACE_VERSION_2;
	}
	public boolean isInterfaceV3() {
		return this.interfaceVersion == INTERFACE_VERSION_3;
	}
 
	/**
	 * Channel requesting the sms sending.
	 * <p/>
	 */
	private String channel;
	
	public String getChannel() {
    	return channel;
    }

	public void setChannel(String channel) {
    	this.channel = channel;
    }

	/**
	 * Get xasUser name.
	 * @deprecated please use getNormalizedXasUserName().
	 * @return xasUserName
	 */
	public String getNormalizedXasUser() {
		return getNormalizedXasUserName();
	}
	
	public String getNormalizedXasUserName() {
		String ret = null;
		if (getXasUserName() != null) {
			ret = getXasUserName().trim();
			if (ret.length() > 8)
				ret = ret.substring(0, 8);
		}
		return ret;
	}

	public String getPreNormalizedPhoneNumber() {
		return (preNormalizedPhoneNumber == null ? getPhoneNumber() : preNormalizedPhoneNumber);
	}

	public void setPreNormalizedPhoneNumber(String preNormalizedPhoneNumber) {
		this.preNormalizedPhoneNumber = preNormalizedPhoneNumber;
	}

	public Date getRequestTime() {
		return requestTime;
    }
	public void setRequestTime(Date requestTime) {
    	this.requestTime = requestTime;
    }
	public String getSrcPhoneNumber() {
    	return srcPhoneNumber;
    }

	public void setSrcPhoneNumber(String srcPhoneNumber) {
    	this.srcPhoneNumber = srcPhoneNumber;
    }
	public String getSrcXasUserName() {
    	return srcXasUserName;
    }

	public void setSrcXasUserName(String srcXasUserName) {
    	this.srcXasUserName = srcXasUserName;
    }
	public Date getSrcDelivery() {
    	return srcDelivery;
    }

	public void setSrcDelivery(Date srcDelivery) {
    	this.srcDelivery = srcDelivery;
    }

	public Date getSrcValidity() {
    	return srcValidity;
    }

	public void setSrcValidity(Date srcValidity) {
    	this.srcValidity = srcValidity;
    }
	public Integer getSrcValidityPeriod() {
    	return srcValidityPeriod;
    }

	public void setSrcValidityPeriod(Integer srcValidityPeriod) {
    	this.srcValidityPeriod = srcValidityPeriod;
    }
	public String getUserId() {
    	return userId;
    }

	public void setUserId(String userId) {
    	this.userId = userId;
    }
	public Boolean getUserInRole() {
    	return userInRole;
    }

	public void setUserInRole(Boolean userInRole) {
    	this.userInRole = userInRole;
    }



	private DateTime evaluateValidity(Integer defaultValidityPeriod) {
		if (getValidity() != null) return new DateTime(getValidity().getTime());
		return evaluateValidityByPeriod(defaultValidityPeriod);
	}
	
	private DateTime evaluateValidityByPeriod(Integer defaultValidityPeriod) {
		if (getValidityPeriod() == null && defaultValidityPeriod == null) return null;
		int myPeriod = (getValidityPeriod() == null) ? defaultValidityPeriod.intValue() : getValidityPeriod().intValue(); 
		DateTime myRequestTime = (requestTime == null ? new DateTime() : new DateTime(requestTime.getTime()));
		return myRequestTime.plusMinutes(myPeriod);
	}
	
	public void validateDelivery(DateTime validity) throws XASWrongRequestException {
		if (getDeliveryTime() == null) return;
		DateTime myDelivery = new DateTime(getDeliveryTime().getTime());
		DateTime limit = new DateTime().plus(DELIVERY_LIMIT);
		if (myDelivery.isAfter(limit)) 
			throw new XASWrongRequestException(Constants.XAS00005E_MESSAGE, null, Constants.XAS00005E_CODE, new Object[] {Constants.ISO_DATE_FORMAT.format(getDeliveryTime())});
		if (validity != null && myDelivery.isAfter(validity)) 
			throw new XASWrongRequestException(Constants.XAS00006E_MESSAGE, null, Constants.XAS00006E_CODE, new Object[] {Constants.ISO_DATE_FORMAT.format(getDeliveryTime()), Constants.ISO_DATE_FORMAT.format(validity.toDate())});
		return;
	}
	
	public DateTime validateValidity(Integer defaultValidityPeriod) throws XASWrongRequestException {
 		DateTime validity = evaluateValidity(defaultValidityPeriod);
		if (validity == null) return null;
		if (validity.isBeforeNow()) 
			throw new XASWrongRequestException(Constants.XAS00007E_MESSAGE, null, Constants.XAS00007E_CODE, new Object[] {Constants.ISO_DATE_FORMAT.format(validity.toDate())});
 
		DateTime limit = new DateTime().plus(VALIDITY_LIMIT);
		if (validity.isAfter(limit)) 
			throw new XASWrongRequestException(Constants.XAS00008E_MESSAGE, null, Constants.XAS00008E_CODE, new Object[] {Constants.ISO_DATE_FORMAT.format(validity.toDate()), VALIDITY_LIMIT});
		else return validity;
	}

	public int getSrcMsgLength() {
	    return ((SmsRequest)source).getMsg() != null ? ((SmsRequest)source).getMsg().length() : 0;
    }
	
	public int getDstByteLength() {
	    return this.getMsgBytes() != null ? this.getMsgBytes().length : 0;
    }	
}
