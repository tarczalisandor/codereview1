/**
 * MobileOriginated.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public class MobileOriginated  implements java.io.Serializable {
    private java.lang.String uuid;
    private java.util.Calendar moDate;
    private java.lang.String[] smsIds;
    private java.util.Calendar providerDate;
    private java.lang.String phoneNumber;
    private java.lang.String moDestinator;
    private java.lang.String msg;

    public MobileOriginated() {
    }

    public java.lang.String getUuid() {
        return uuid;
    }

    public void setUuid(java.lang.String uuid) {
        this.uuid = uuid;
    }

    public java.util.Calendar getMoDate() {
        return moDate;
    }

    public void setMoDate(java.util.Calendar moDate) {
        this.moDate = moDate;
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

    public java.util.Calendar getProviderDate() {
        return providerDate;
    }

    public void setProviderDate(java.util.Calendar providerDate) {
        this.providerDate = providerDate;
    }

    public java.lang.String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(java.lang.String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public java.lang.String getMoDestinator() {
        return moDestinator;
    }

    public void setMoDestinator(java.lang.String moDestinator) {
        this.moDestinator = moDestinator;
    }

    public java.lang.String getMsg() {
        return msg;
    }

    public void setMsg(java.lang.String msg) {
        this.msg = msg;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        MobileOriginated other = (MobileOriginated) obj;
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
              this.phoneNumber.equals(other.getPhoneNumber())))
            && ((this.moDestinator==null && other.getMoDestinator()==null) || 
             (this.moDestinator!=null &&
              this.moDestinator.equals(other.getMoDestinator())))
            && ((this.msg==null && other.getMsg()==null) || 
             (this.msg!=null &&
              this.msg.equals(other.getMsg())));
        if (!_equals) { return false; }
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        MobileOriginated history = (MobileOriginated) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        _equals = true
            && ((this.moDate==null && other.getMoDate()==null) || 
             (this.moDate!=null &&
              this.moDate.equals(other.getMoDate())))
            && ((this.providerDate==null && other.getProviderDate()==null) || 
             (this.providerDate!=null &&
              this.providerDate.equals(other.getProviderDate())));
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
        MobileOriginated history = (MobileOriginated) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getUuid() != null) {
            _hashCode += getUuid().hashCode();
        }
        if (getMoDate() != null) {
            _hashCode += getMoDate().hashCode();
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
        if (getProviderDate() != null) {
            _hashCode += getProviderDate().hashCode();
        }
        if (getPhoneNumber() != null) {
            _hashCode += getPhoneNumber().hashCode();
        }
        if (getMoDestinator() != null) {
            _hashCode += getMoDestinator().hashCode();
        }
        if (getMsg() != null) {
            _hashCode += getMsg().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
