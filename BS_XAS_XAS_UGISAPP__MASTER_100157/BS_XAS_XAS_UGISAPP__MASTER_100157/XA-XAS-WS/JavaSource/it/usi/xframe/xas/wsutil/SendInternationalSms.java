/**
 * SendInternationalSms.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * 4
 */

package it.usi.xframe.xas.wsutil;

public class SendInternationalSms  implements java.io.Serializable {
    private it.usi.xframe.xas.bfutil.data.InternationalSmsMessage sms;
    private it.usi.xframe.xas.bfutil.data.SmsDelivery[] delivery;
    private it.usi.xframe.xas.bfutil.data.SmsBillingInfo billing;

    public SendInternationalSms() {
    }

    public it.usi.xframe.xas.bfutil.data.InternationalSmsMessage getSms() {
        return sms;
    }

    public void setSms(it.usi.xframe.xas.bfutil.data.InternationalSmsMessage sms) {
        this.sms = sms;
    }

    public it.usi.xframe.xas.bfutil.data.SmsDelivery[] getDelivery() {
        return delivery;
    }

    public void setDelivery(it.usi.xframe.xas.bfutil.data.SmsDelivery[] delivery) {
        this.delivery = delivery;
    }

    public it.usi.xframe.xas.bfutil.data.SmsDelivery getDelivery(int i) {
        return delivery[i];
    }

    public void setDelivery(int i, it.usi.xframe.xas.bfutil.data.SmsDelivery value) {
        this.delivery[i] = value;
    }

    public it.usi.xframe.xas.bfutil.data.SmsBillingInfo getBilling() {
        return billing;
    }

    public void setBilling(it.usi.xframe.xas.bfutil.data.SmsBillingInfo billing) {
        this.billing = billing;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        SendInternationalSms history = (SendInternationalSms) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        SendInternationalSms other = (SendInternationalSms) obj;
        boolean _equals;
        _equals = true
            && ((this.sms==null && other.getSms()==null) || 
             (this.sms!=null &&
              this.sms.equals(other.getSms())))
            && ((this.delivery==null && other.getDelivery()==null) || 
             (this.delivery!=null &&
              java.util.Arrays.equals(this.delivery, other.getDelivery())))
            && ((this.billing==null && other.getBilling()==null) || 
             (this.billing!=null &&
              this.billing.equals(other.getBilling())));
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
        SendInternationalSms history = (SendInternationalSms) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getSms() != null) {
            _hashCode += getSms().hashCode();
        }
        if (getDelivery() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDelivery());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDelivery(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getBilling() != null) {
            _hashCode += getBilling().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
