/**
 * SetSmsRequest.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public class SetSmsRequest  implements java.io.Serializable {
    private it.usi.xframe.xas.bfutil.data.SmsRequest smsRequest;

    public SetSmsRequest() {
    }

    public it.usi.xframe.xas.bfutil.data.SmsRequest getSmsRequest() {
        return smsRequest;
    }

    public void setSmsRequest(it.usi.xframe.xas.bfutil.data.SmsRequest smsRequest) {
        this.smsRequest = smsRequest;
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
        SetSmsRequest history = (SetSmsRequest) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        SetSmsRequest other = (SetSmsRequest) obj;
        boolean _equals;
        _equals = true
            && ((this.smsRequest==null && other.getSmsRequest()==null) || 
             (this.smsRequest!=null &&
              this.smsRequest.equals(other.getSmsRequest())));
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
        SetSmsRequest history = (SetSmsRequest) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getSmsRequest() != null) {
            _hashCode += getSmsRequest().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
