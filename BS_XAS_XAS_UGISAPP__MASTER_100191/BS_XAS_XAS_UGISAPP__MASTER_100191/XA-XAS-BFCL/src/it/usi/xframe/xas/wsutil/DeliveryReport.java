/**
 * DeliveryReport.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public class DeliveryReport  implements java.io.Serializable {
    private java.lang.String uuid;
    private java.util.Calendar providerDate;
    private java.lang.String[] smsIds;
    private java.lang.String phoneNumber;
    private java.util.Calendar deliveryDate;
    private it.usi.xframe.xas.wsutil.ENUM_STATUS status;

    public DeliveryReport() {
    }

    public java.lang.String getUuid() {
        return uuid;
    }

    public void setUuid(java.lang.String uuid) {
        this.uuid = uuid;
    }

    public java.util.Calendar getProviderDate() {
        return providerDate;
    }

    public void setProviderDate(java.util.Calendar providerDate) {
        this.providerDate = providerDate;
    }

    public java.lang.String[] getSmsIds() {
        return smsIds;
    }

    public void setSmsIds(java.lang.String[] smsIds) {
        this.smsIds = smsIds;
    }

    public java.lang.String getSmsIds(int i) {
        return smsIds[i];
    }

    public void setSmsIds(int i, java.lang.String value) {
        this.smsIds[i] = value;
    }

    public java.lang.String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(java.lang.String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public java.util.Calendar getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(java.util.Calendar deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public it.usi.xframe.xas.wsutil.ENUM_STATUS getStatus() {
        return status;
    }

    public void setStatus(it.usi.xframe.xas.wsutil.ENUM_STATUS status) {
        this.status = status;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        DeliveryReport other = (DeliveryReport) obj;
        boolean _equals;
        _equals = true
            && ((this.uuid==null && other.getUuid()==null) || 
             (this.uuid!=null &&
              this.uuid.equals(other.getUuid())))
            && ((this.smsIds==null && other.getSmsIds()==null) || 
             (this.smsIds!=null &&
              java.util.Arrays.equals(this.smsIds, other.getSmsIds())))
            && ((this.phoneNumber==null && other.getPhoneNumber()==null) || 
             (this.phoneNumber!=null &&
              this.phoneNumber.equals(other.getPhoneNumber())));
        if (!_equals) { return false; }
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        DeliveryReport history = (DeliveryReport) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        _equals = true
            && ((this.providerDate==null && other.getProviderDate()==null) || 
             (this.providerDate!=null &&
              this.providerDate.equals(other.getProviderDate())))
            && ((this.deliveryDate==null && other.getDeliveryDate()==null) || 
             (this.deliveryDate!=null &&
              this.deliveryDate.equals(other.getDeliveryDate())))
            && ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus())));
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
        DeliveryReport history = (DeliveryReport) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getUuid() != null) {
            _hashCode += getUuid().hashCode();
        }
        if (getProviderDate() != null) {
            _hashCode += getProviderDate().hashCode();
        }
        if (getSmsIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSmsIds());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSmsIds(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPhoneNumber() != null) {
            _hashCode += getPhoneNumber().hashCode();
        }
        if (getDeliveryDate() != null) {
            _hashCode += getDeliveryDate().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
