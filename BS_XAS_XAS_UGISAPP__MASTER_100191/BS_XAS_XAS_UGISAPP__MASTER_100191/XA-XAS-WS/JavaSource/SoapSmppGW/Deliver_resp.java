/**
 * Deliver_resp.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package SoapSmppGW;

public class Deliver_resp  implements java.io.Serializable {
    private int command_status;
    private java.lang.String error_code;
    private java.lang.String message_id;

    public Deliver_resp() {
    }

    public int getCommand_status() {
        return command_status;
    }

    public void setCommand_status(int command_status) {
        this.command_status = command_status;
    }

    public java.lang.String getError_code() {
        return error_code;
    }

    public void setError_code(java.lang.String error_code) {
        this.error_code = error_code;
    }

    public java.lang.String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(java.lang.String message_id) {
        this.message_id = message_id;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        Deliver_resp other = (Deliver_resp) obj;
        boolean _equals;
        _equals = true
            && this.command_status == other.getCommand_status()
            && ((this.error_code==null && other.getError_code()==null) || 
             (this.error_code!=null &&
              this.error_code.equals(other.getError_code())))
            && ((this.message_id==null && other.getMessage_id()==null) || 
             (this.message_id!=null &&
              this.message_id.equals(other.getMessage_id())));
        if (!_equals) { return false; }
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        Deliver_resp history = (Deliver_resp) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
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
        Deliver_resp history = (Deliver_resp) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        _hashCode += getCommand_status();
        if (getError_code() != null) {
            _hashCode += getError_code().hashCode();
        }
        if (getMessage_id() != null) {
            _hashCode += getMessage_id().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
