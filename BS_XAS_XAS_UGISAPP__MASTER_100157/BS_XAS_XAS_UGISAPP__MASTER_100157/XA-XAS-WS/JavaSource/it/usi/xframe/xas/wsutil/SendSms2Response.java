/**
 * SendSms2Response.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public class SendSms2Response  implements java.io.Serializable {
    private it.usi.xframe.xas.bfutil.data.SmsResponse sendSms2Return;

    public SendSms2Response() {
    }

    public it.usi.xframe.xas.bfutil.data.SmsResponse getSendSms2Return() {
        return sendSms2Return;
    }

    public void setSendSms2Return(it.usi.xframe.xas.bfutil.data.SmsResponse sendSms2Return) {
        this.sendSms2Return = sendSms2Return;
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
        SendSms2Response history = (SendSms2Response) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        SendSms2Response other = (SendSms2Response) obj;
        boolean _equals;
        _equals = true
            && ((this.sendSms2Return==null && other.getSendSms2Return()==null) || 
             (this.sendSms2Return!=null &&
              this.sendSms2Return.equals(other.getSendSms2Return())));
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
        SendSms2Response history = (SendSms2Response) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getSendSms2Return() != null) {
            _hashCode += getSendSms2Return().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
