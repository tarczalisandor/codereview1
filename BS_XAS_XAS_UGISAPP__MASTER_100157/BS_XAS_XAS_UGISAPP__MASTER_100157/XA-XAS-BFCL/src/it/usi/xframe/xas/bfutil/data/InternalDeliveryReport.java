package it.usi.xframe.xas.bfutil.data;

import it.usi.xframe.xas.wsutil.DeliveryReport;

public class InternalDeliveryReport {
    DeliveryReport source;
    private String providerType = null;
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

	public InternalDeliveryReport(DeliveryReport source) {
		this.source = source;
	}
	
	public String[] getSmsIds() {
    	return source.getSmsIds();
    }

	public String getStatus() {
    	return source.getStatus() != null ? source.getStatus().toString() : null;
    }

	public String getPhoneNumber() {
    	return source.getPhoneNumber();
    }

	public String getUuid() { //#TIM
		return source.getUuid() != null ? source.getUuid().toString() : null;
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

	public DeliveryReport getDeliveryReport() {
	    return source;
    }

	public String getProviderType() {
    	return providerType;
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
