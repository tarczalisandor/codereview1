/**
 * SendInternationalSmsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * 4
 */

package it.usi.xframe.xas.wsutil;

public class SendInternationalSmsResponse  implements java.io.Serializable {
    private it.usi.xframe.xas.bfutil.data.InternationalSmsResponse sendInternationalSmsReturn;

    public SendInternationalSmsResponse() {
    }

    public it.usi.xframe.xas.bfutil.data.InternationalSmsResponse getSendInternationalSmsReturn() {
        return sendInternationalSmsReturn;
    }

    public void setSendInternationalSmsReturn(it.usi.xframe.xas.bfutil.data.InternationalSmsResponse sendInternationalSmsReturn) {
        this.sendInternationalSmsReturn = sendInternationalSmsReturn;
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
        SendInternationalSmsResponse history = (SendInternationalSmsResponse) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        SendInternationalSmsResponse other = (SendInternationalSmsResponse) obj;
        boolean _equals;
        _equals = true
            && ((this.sendInternationalSmsReturn==null && other.getSendInternationalSmsReturn()==null) || 
             (this.sendInternationalSmsReturn!=null &&
              this.sendInternationalSmsReturn.equals(other.getSendInternationalSmsReturn())));
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
        SendInternationalSmsResponse history = (SendInternationalSmsResponse) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getSendInternationalSmsReturn() != null) {
            _hashCode += getSendInternationalSmsReturn().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
