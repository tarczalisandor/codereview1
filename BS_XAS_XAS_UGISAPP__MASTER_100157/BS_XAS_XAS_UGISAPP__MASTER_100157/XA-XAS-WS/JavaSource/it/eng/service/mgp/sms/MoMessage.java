/**
 * MoMessage.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.eng.service.mgp.sms;

public class MoMessage  extends it.eng.service.mgp.sms.Message  implements java.io.Serializable {
    private java.lang.String smsId;
    private java.util.Calendar date;
    private java.util.Calendar smscDate;

    public MoMessage() {
    }

    public java.lang.String getSmsId() {
        return smsId;
    }

    public void setSmsId(java.lang.String smsId) {
        this.smsId = smsId;
    }

    public java.util.Calendar getDate() {
        return date;
    }

    public void setDate(java.util.Calendar date) {
        this.date = date;
    }

    public java.util.Calendar getSmscDate() {
        return smscDate;
    }

    public void setSmscDate(java.util.Calendar smscDate) {
        this.smscDate = smscDate;
    }

    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        MoMessage other = (MoMessage) obj;
        boolean _equals;
        _equals = true
            && ((this.smsId==null && other.getSmsId()==null) || 
             (this.smsId!=null &&
              this.smsId.equals(other.getSmsId())));
        if (!_equals) { return false; }
        if (!super.equals(obj)) { return false; }
        _equals = true
            && ((this.date==null && other.getDate()==null) || 
             (this.date!=null &&
              this.date.equals(other.getDate())))
            && ((this.smscDate==null && other.getSmscDate()==null) || 
             (this.smscDate!=null &&
              this.smscDate.equals(other.getSmscDate())));
        if (!_equals) {
            return false;
        };
        return true;
    }

    public int hashCode() {
        int _hashCode = super.hashCode();
        if (getSmsId() != null) {
            _hashCode += getSmsId().hashCode();
        }
        if (getDate() != null) {
            _hashCode += getDate().hashCode();
        }
        if (getSmscDate() != null) {
            _hashCode += getSmscDate().hashCode();
        }
        return _hashCode;
    }

}
