/**
 * MessageStatus.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.eng.service.mgp.sms;

public abstract class MessageStatus  implements java.io.Serializable {
    private java.lang.String protocolId;
    private java.util.Calendar date;
    private java.util.Calendar deliveryDate;
    private java.lang.String responseCode;
    private java.lang.String responseMessage;

    public MessageStatus() {
    }

    public java.lang.String getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(java.lang.String protocolId) {
        this.protocolId = protocolId;
    }

    public java.util.Calendar getDate() {
        return date;
    }

    public void setDate(java.util.Calendar date) {
        this.date = date;
    }

    public java.util.Calendar getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(java.util.Calendar deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public java.lang.String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(java.lang.String responseCode) {
        this.responseCode = responseCode;
    }

    public java.lang.String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(java.lang.String responseMessage) {
        this.responseMessage = responseMessage;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        MessageStatus other = (MessageStatus) obj;
        boolean _equals;
        _equals = true
            && ((this.protocolId==null && other.getProtocolId()==null) || 
             (this.protocolId!=null &&
              this.protocolId.equals(other.getProtocolId())))
            && ((this.responseCode==null && other.getResponseCode()==null) || 
             (this.responseCode!=null &&
              this.responseCode.equals(other.getResponseCode())))
            && ((this.responseMessage==null && other.getResponseMessage()==null) || 
             (this.responseMessage!=null &&
              this.responseMessage.equals(other.getResponseMessage())));
        if (!_equals) { return false; }
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        MessageStatus history = (MessageStatus) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        _equals = true
            && ((this.date==null && other.getDate()==null) || 
             (this.date!=null &&
              this.date.equals(other.getDate())))
            && ((this.deliveryDate==null && other.getDeliveryDate()==null) || 
             (this.deliveryDate!=null &&
              this.deliveryDate.equals(other.getDeliveryDate())));
        if (!_equals) {
            __history.set(null);
            return false;
        };
        __history.set(null);
        return true;
    }

    private transient java.lang.ThreadLocal __hashHistory;
    public int hashCode() {
        if (__hashHistory == null) {
            synchronized (this) {
                if (__hashHistory == null) {
                    __hashHistory = new java.lang.ThreadLocal();
                }
            }
        }
        MessageStatus history = (MessageStatus) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getProtocolId() != null) {
            _hashCode += getProtocolId().hashCode();
        }
        if (getDate() != null) {
            _hashCode += getDate().hashCode();
        }
        if (getDeliveryDate() != null) {
            _hashCode += getDeliveryDate().hashCode();
        }
        if (getResponseCode() != null) {
            _hashCode += getResponseCode().hashCode();
        }
        if (getResponseMessage() != null) {
            _hashCode += getResponseMessage().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
