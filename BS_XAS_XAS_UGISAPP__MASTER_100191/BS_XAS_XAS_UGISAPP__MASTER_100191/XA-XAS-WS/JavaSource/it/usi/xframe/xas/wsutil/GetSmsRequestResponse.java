/**
 * GetSmsRequestResponse.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public class GetSmsRequestResponse  implements java.io.Serializable {
    private it.usi.xframe.xas.bfutil.data.SmsRequest getSmsRequestReturn;

    public GetSmsRequestResponse() {
    }

    public it.usi.xframe.xas.bfutil.data.SmsRequest getGetSmsRequestReturn() {
        return getSmsRequestReturn;
    }

    public void setGetSmsRequestReturn(it.usi.xframe.xas.bfutil.data.SmsRequest getSmsRequestReturn) {
        this.getSmsRequestReturn = getSmsRequestReturn;
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
        GetSmsRequestResponse history = (GetSmsRequestResponse) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        GetSmsRequestResponse other = (GetSmsRequestResponse) obj;
        boolean _equals;
        _equals = true
            && ((this.getSmsRequestReturn==null && other.getGetSmsRequestReturn()==null) || 
             (this.getSmsRequestReturn!=null &&
              this.getSmsRequestReturn.equals(other.getGetSmsRequestReturn())));
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
        GetSmsRequestResponse history = (GetSmsRequestResponse) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getGetSmsRequestReturn() != null) {
            _hashCode += getGetSmsRequestReturn().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
