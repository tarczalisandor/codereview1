/**
 * Submit_resp.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package SoapSmppGW;

public class Submit_resp  implements java.io.Serializable {
    private java.lang.String message_id;
    private java.lang.String error_code;
    private java.lang.Integer command_status;
    private java.lang.Integer sequence_number;

    public Submit_resp() {
    }

    public java.lang.String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(java.lang.String message_id) {
        this.message_id = message_id;
    }

    public java.lang.String getError_code() {
        return error_code;
    }

    public void setError_code(java.lang.String error_code) {
        this.error_code = error_code;
    }

    public java.lang.Integer getCommand_status() {
        return command_status;
    }

    public void setCommand_status(java.lang.Integer command_status) {
        this.command_status = command_status;
    }

    public java.lang.Integer getSequence_number() {
        return sequence_number;
    }

    public void setSequence_number(java.lang.Integer sequence_number) {
        this.sequence_number = sequence_number;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        Submit_resp other = (Submit_resp) obj;
        boolean _equals;
        _equals = true
            && ((this.message_id==null && other.getMessage_id()==null) || 
             (this.message_id!=null &&
              this.message_id.equals(other.getMessage_id())))
            && ((this.error_code==null && other.getError_code()==null) || 
             (this.error_code!=null &&
              this.error_code.equals(other.getError_code())))
            && ((this.command_status==null && other.getCommand_status()==null) || 
             (this.command_status!=null &&
              this.command_status.equals(other.getCommand_status())))
            && ((this.sequence_number==null && other.getSequence_number()==null) || 
             (this.sequence_number!=null &&
              this.sequence_number.equals(other.getSequence_number())));
        if (!_equals) { return false; }
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        Submit_resp history = (Submit_resp) __history.get();
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
        Submit_resp history = (Submit_resp) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getMessage_id() != null) {
            _hashCode += getMessage_id().hashCode();
        }
        if (getError_code() != null) {
            _hashCode += getError_code().hashCode();
        }
        if (getCommand_status() != null) {
            _hashCode += getCommand_status().hashCode();
        }
        if (getSequence_number() != null) {
            _hashCode += getSequence_number().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
