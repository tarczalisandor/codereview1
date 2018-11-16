/**
 * NotificationResponse.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public class NotificationResponse  implements java.io.Serializable {
    private it.usi.xframe.xas.wsutil.StatusCodeType statusCode;
    private java.lang.String statusMsg;

    public NotificationResponse() {
    }

    public it.usi.xframe.xas.wsutil.StatusCodeType getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(it.usi.xframe.xas.wsutil.StatusCodeType statusCode) {
        this.statusCode = statusCode;
    }

    public java.lang.String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(java.lang.String statusMsg) {
        this.statusMsg = statusMsg;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        NotificationResponse other = (NotificationResponse) obj;
        boolean _equals;
        _equals = true
            && ((this.statusMsg==null && other.getStatusMsg()==null) || 
             (this.statusMsg!=null &&
              this.statusMsg.equals(other.getStatusMsg())));
        if (!_equals) { return false; }
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        NotificationResponse history = (NotificationResponse) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        _equals = true
            && ((this.statusCode==null && other.getStatusCode()==null) || 
             (this.statusCode!=null &&
              this.statusCode.equals(other.getStatusCode())));
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
        NotificationResponse history = (NotificationResponse) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getStatusCode() != null) {
            _hashCode += getStatusCode().hashCode();
        }
        if (getStatusMsg() != null) {
            _hashCode += getStatusMsg().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
