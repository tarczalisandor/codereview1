/**
 * MtMessage.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.eng.service.mgp.sms;

public class MtMessage  extends it.eng.service.mgp.sms.Message  implements java.io.Serializable {
    private java.util.Calendar deliveryDate;
    private java.lang.String serviceType;
    private java.lang.String esmClass;
    private java.lang.String replaceIfPresentFlag;
    private boolean registeredDelivery;
    private java.lang.String protocolId;
    private java.lang.String msgId;

    public MtMessage() {
    }

    public java.util.Calendar getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(java.util.Calendar deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public java.lang.String getServiceType() {
        return serviceType;
    }

    public void setServiceType(java.lang.String serviceType) {
        this.serviceType = serviceType;
    }

    public java.lang.String getEsmClass() {
        return esmClass;
    }

    public void setEsmClass(java.lang.String esmClass) {
        this.esmClass = esmClass;
    }

    public java.lang.String getReplaceIfPresentFlag() {
        return replaceIfPresentFlag;
    }

    public void setReplaceIfPresentFlag(java.lang.String replaceIfPresentFlag) {
        this.replaceIfPresentFlag = replaceIfPresentFlag;
    }

    public boolean isRegisteredDelivery() {
        return registeredDelivery;
    }

    public void setRegisteredDelivery(boolean registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
    }

    public java.lang.String getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(java.lang.String protocolId) {
        this.protocolId = protocolId;
    }

    public java.lang.String getMsgId() {
        return msgId;
    }

    public void setMsgId(java.lang.String msgId) {
        this.msgId = msgId;
    }

    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        MtMessage other = (MtMessage) obj;
        boolean _equals;
        _equals = true
            && ((this.serviceType==null && other.getServiceType()==null) || 
             (this.serviceType!=null &&
              this.serviceType.equals(other.getServiceType())))
            && ((this.esmClass==null && other.getEsmClass()==null) || 
             (this.esmClass!=null &&
              this.esmClass.equals(other.getEsmClass())))
            && ((this.replaceIfPresentFlag==null && other.getReplaceIfPresentFlag()==null) || 
             (this.replaceIfPresentFlag!=null &&
              this.replaceIfPresentFlag.equals(other.getReplaceIfPresentFlag())))
            && this.registeredDelivery == other.isRegisteredDelivery()
            && ((this.protocolId==null && other.getProtocolId()==null) || 
             (this.protocolId!=null &&
              this.protocolId.equals(other.getProtocolId())))
            && ((this.msgId==null && other.getMsgId()==null) || 
             (this.msgId!=null &&
              this.msgId.equals(other.getMsgId())));
        if (!_equals) { return false; }
        if (!super.equals(obj)) { return false; }
        _equals = true
            && ((this.deliveryDate==null && other.getDeliveryDate()==null) || 
             (this.deliveryDate!=null &&
              this.deliveryDate.equals(other.getDeliveryDate())));
        if (!_equals) {
            return false;
        };
        return true;
    }

    public int hashCode() {
        int _hashCode = super.hashCode();
        if (getDeliveryDate() != null) {
            _hashCode += getDeliveryDate().hashCode();
        }
        if (getServiceType() != null) {
            _hashCode += getServiceType().hashCode();
        }
        if (getEsmClass() != null) {
            _hashCode += getEsmClass().hashCode();
        }
        if (getReplaceIfPresentFlag() != null) {
            _hashCode += getReplaceIfPresentFlag().hashCode();
        }
        _hashCode += new Boolean(isRegisteredDelivery()).hashCode();
        if (getProtocolId() != null) {
            _hashCode += getProtocolId().hashCode();
        }
        if (getMsgId() != null) {
            _hashCode += getMsgId().hashCode();
        }
        return _hashCode;
    }

}
