/**
 * MtDeliveryStatus.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.eng.service.mgp.sms;

public class MtDeliveryStatus  extends it.eng.service.mgp.sms.MessageStatus  implements java.io.Serializable {
    private java.lang.String destAddr;
    private int destAddrNpi;
    private int destAddrTon;
    private it.eng.service.mgp.sms.Status status;
    private java.lang.String txId;
    private java.lang.String msgId;
    private java.lang.String smsId;

    public MtDeliveryStatus() {
    }

    public java.lang.String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(java.lang.String destAddr) {
        this.destAddr = destAddr;
    }

    public int getDestAddrNpi() {
        return destAddrNpi;
    }

    public void setDestAddrNpi(int destAddrNpi) {
        this.destAddrNpi = destAddrNpi;
    }

    public int getDestAddrTon() {
        return destAddrTon;
    }

    public void setDestAddrTon(int destAddrTon) {
        this.destAddrTon = destAddrTon;
    }

    public it.eng.service.mgp.sms.Status getStatus() {
        return status;
    }

    public void setStatus(it.eng.service.mgp.sms.Status status) {
        this.status = status;
    }

    public java.lang.String getTxId() {
        return txId;
    }

    public void setTxId(java.lang.String txId) {
        this.txId = txId;
    }

    public java.lang.String getMsgId() {
        return msgId;
    }

    public void setMsgId(java.lang.String msgId) {
        this.msgId = msgId;
    }

    public java.lang.String getSmsId() {
        return smsId;
    }

    public void setSmsId(java.lang.String smsId) {
        this.smsId = smsId;
    }

    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        MtDeliveryStatus other = (MtDeliveryStatus) obj;
        boolean _equals;
        _equals = true
            && ((this.destAddr==null && other.getDestAddr()==null) || 
             (this.destAddr!=null &&
              this.destAddr.equals(other.getDestAddr())))
            && this.destAddrNpi == other.getDestAddrNpi()
            && this.destAddrTon == other.getDestAddrTon()
            && ((this.txId==null && other.getTxId()==null) || 
             (this.txId!=null &&
              this.txId.equals(other.getTxId())))
            && ((this.msgId==null && other.getMsgId()==null) || 
             (this.msgId!=null &&
              this.msgId.equals(other.getMsgId())))
            && ((this.smsId==null && other.getSmsId()==null) || 
             (this.smsId!=null &&
              this.smsId.equals(other.getSmsId())));
        if (!_equals) { return false; }
        if (!super.equals(obj)) { return false; }
        _equals = true
            && ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus())));
        if (!_equals) {
            return false;
        };
        return true;
    }

    public int hashCode() {
        int _hashCode = super.hashCode();
        if (getDestAddr() != null) {
            _hashCode += getDestAddr().hashCode();
        }
        _hashCode += getDestAddrNpi();
        _hashCode += getDestAddrTon();
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getTxId() != null) {
            _hashCode += getTxId().hashCode();
        }
        if (getMsgId() != null) {
            _hashCode += getMsgId().hashCode();
        }
        if (getSmsId() != null) {
            _hashCode += getSmsId().hashCode();
        }
        return _hashCode;
    }

}
