/**
 * MtSendingResult.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.eng.service.mgp.sms;

public class MtSendingResult  implements java.io.Serializable {
    private java.lang.String smsId;
    private it.eng.service.mgp.sms.Reason reason;
    private it.eng.service.mgp.sms.Result result;

    public MtSendingResult() {
    }

    public java.lang.String getSmsId() {
        return smsId;
    }

    public void setSmsId(java.lang.String smsId) {
        this.smsId = smsId;
    }

    public it.eng.service.mgp.sms.Reason getReason() {
        return reason;
    }

    public void setReason(it.eng.service.mgp.sms.Reason reason) {
        this.reason = reason;
    }

    public it.eng.service.mgp.sms.Result getResult() {
        return result;
    }

    public void setResult(it.eng.service.mgp.sms.Result result) {
        this.result = result;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        MtSendingResult other = (MtSendingResult) obj;
        boolean _equals;
        _equals = true
            && ((this.smsId==null && other.getSmsId()==null) || 
             (this.smsId!=null &&
              this.smsId.equals(other.getSmsId())));
        if (!_equals) { return false; }
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        MtSendingResult history = (MtSendingResult) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        _equals = true
            && ((this.reason==null && other.getReason()==null) || 
             (this.reason!=null &&
              this.reason.equals(other.getReason())))
            && ((this.result==null && other.getResult()==null) || 
             (this.result!=null &&
              this.result.equals(other.getResult())));
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
        MtSendingResult history = (MtSendingResult) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getSmsId() != null) {
            _hashCode += getSmsId().hashCode();
        }
        if (getReason() != null) {
            _hashCode += getReason().hashCode();
        }
        if (getResult() != null) {
            _hashCode += getResult().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
