package it.usi.xframe.xas.bfutil.data;

import it.usi.xframe.xas.wsutil.MobileOriginated;

public class InternalMobileOriginated {
    MobileOriginated source;

    private String userId = null;
	private Boolean userInRole = null;
	private Boolean accepted = null;
    
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
    
    public MobileOriginated getMobileOriginated() {
    	return source;
    }
    public InternalMobileOriginated(MobileOriginated source) {
		this.source = source;
	}
	
	public String getMsg() {
    	return source.getMsg();
    }

	public int getMsgByteLength() {
	    return this.getMsg() != null ? this.getMsg().getBytes().length : 0;
    }	

	public String getPhoneNumber() {
    	return source.getPhoneNumber();
    }

	public String getMoDestinator() { //#TIM
    	return source.getMoDestinator();
    }


	public String[] getSmsIds() {
    	return source.getSmsIds();
    }

	public final static int INTERFACE_VERSION_1 = 1;
	private int interfaceVersion;
	public int getInterfaceVersion() {
    	return interfaceVersion;
    }

	public void setInterfaceV1() {
    	this.interfaceVersion = INTERFACE_VERSION_1;
    }
	public boolean isInferfaceV1() {
		return this.interfaceVersion == INTERFACE_VERSION_1;
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

	private String uuid;

	private String providerType;

	public String getProviderType() {
    	return providerType;
    }

	public String getUuid() {
    	return uuid;
    }
	public void setUuid(String uuid) {
    	this.uuid = uuid;
    }

	public void setProviderType(String providerType) {
	    this.providerType = providerType;
	    
    }

	public Boolean getAccepted() {
    	return accepted;
    }

	public void setAccepted(Boolean accepted) {
    	this.accepted = accepted;
    }
}
