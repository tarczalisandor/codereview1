/**
 * Message.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.eng.service.mgp.sms;

public abstract class Message  implements java.io.Serializable {
    private java.lang.String txId;
    private java.lang.String destAddr;
    private int destAddrNpi;
    private int destAddrTon;
    private int numOfParts;
    private int partNum;
    private java.math.BigInteger partsRefNum;
    private byte[] payloadUd;
    private it.eng.service.mgp.sms.GsmEncoding payloadUddc;
    private byte[] payloadUdh;
    private it.eng.service.mgp.sms.GsmPriority priority;
    private java.lang.String srcAddr;
    private int srcAddrNpi;
    private int srcAddrTon;
    private java.util.Calendar validityPeriod;

    public Message() {
    }

    public java.lang.String getTxId() {
        return txId;
    }

    public void setTxId(java.lang.String txId) {
        this.txId = txId;
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

    public int getNumOfParts() {
        return numOfParts;
    }

    public void setNumOfParts(int numOfParts) {
        this.numOfParts = numOfParts;
    }

    public int getPartNum() {
        return partNum;
    }

    public void setPartNum(int partNum) {
        this.partNum = partNum;
    }

    public java.math.BigInteger getPartsRefNum() {
        return partsRefNum;
    }

    public void setPartsRefNum(java.math.BigInteger partsRefNum) {
        this.partsRefNum = partsRefNum;
    }

    public byte[] getPayloadUd() {
        return payloadUd;
    }

    public void setPayloadUd(byte[] payloadUd) {
        this.payloadUd = payloadUd;
    }

    public it.eng.service.mgp.sms.GsmEncoding getPayloadUddc() {
        return payloadUddc;
    }

    public void setPayloadUddc(it.eng.service.mgp.sms.GsmEncoding payloadUddc) {
        this.payloadUddc = payloadUddc;
    }

    public byte[] getPayloadUdh() {
        return payloadUdh;
    }

    public void setPayloadUdh(byte[] payloadUdh) {
        this.payloadUdh = payloadUdh;
    }

    public it.eng.service.mgp.sms.GsmPriority getPriority() {
        return priority;
    }

    public void setPriority(it.eng.service.mgp.sms.GsmPriority priority) {
        this.priority = priority;
    }

    public java.lang.String getSrcAddr() {
        return srcAddr;
    }

    public void setSrcAddr(java.lang.String srcAddr) {
        this.srcAddr = srcAddr;
    }

    public int getSrcAddrNpi() {
        return srcAddrNpi;
    }

    public void setSrcAddrNpi(int srcAddrNpi) {
        this.srcAddrNpi = srcAddrNpi;
    }

    public int getSrcAddrTon() {
        return srcAddrTon;
    }

    public void setSrcAddrTon(int srcAddrTon) {
        this.srcAddrTon = srcAddrTon;
    }

    public java.util.Calendar getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(java.util.Calendar validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        Message other = (Message) obj;
        boolean _equals;
        _equals = true
            && ((this.txId==null && other.getTxId()==null) || 
             (this.txId!=null &&
              this.txId.equals(other.getTxId())))
            && ((this.destAddr==null && other.getDestAddr()==null) || 
             (this.destAddr!=null &&
              this.destAddr.equals(other.getDestAddr())))
            && this.destAddrNpi == other.getDestAddrNpi()
            && this.destAddrTon == other.getDestAddrTon()
            && this.numOfParts == other.getNumOfParts()
            && this.partNum == other.getPartNum()
            && ((this.srcAddr==null && other.getSrcAddr()==null) || 
             (this.srcAddr!=null &&
              this.srcAddr.equals(other.getSrcAddr())))
            && this.srcAddrNpi == other.getSrcAddrNpi()
            && this.srcAddrTon == other.getSrcAddrTon();
        if (!_equals) { return false; }
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        Message history = (Message) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        _equals = true
            && ((this.partsRefNum==null && other.getPartsRefNum()==null) || 
             (this.partsRefNum!=null &&
              this.partsRefNum.equals(other.getPartsRefNum())))
            && ((this.payloadUd==null && other.getPayloadUd()==null) || 
             (this.payloadUd!=null &&
              java.util.Arrays.equals(this.payloadUd, other.getPayloadUd())))
            && ((this.payloadUddc==null && other.getPayloadUddc()==null) || 
             (this.payloadUddc!=null &&
              this.payloadUddc.equals(other.getPayloadUddc())))
            && ((this.payloadUdh==null && other.getPayloadUdh()==null) || 
             (this.payloadUdh!=null &&
              java.util.Arrays.equals(this.payloadUdh, other.getPayloadUdh())))
            && ((this.priority==null && other.getPriority()==null) || 
             (this.priority!=null &&
              this.priority.equals(other.getPriority())))
            && ((this.validityPeriod==null && other.getValidityPeriod()==null) || 
             (this.validityPeriod!=null &&
              this.validityPeriod.equals(other.getValidityPeriod())));
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
        Message history = (Message) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getTxId() != null) {
            _hashCode += getTxId().hashCode();
        }
        if (getDestAddr() != null) {
            _hashCode += getDestAddr().hashCode();
        }
        _hashCode += getDestAddrNpi();
        _hashCode += getDestAddrTon();
        _hashCode += getNumOfParts();
        _hashCode += getPartNum();
        if (getPartsRefNum() != null) {
            _hashCode += getPartsRefNum().hashCode();
        }
        if (getPayloadUd() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPayloadUd());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPayloadUd(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPayloadUddc() != null) {
            _hashCode += getPayloadUddc().hashCode();
        }
        if (getPayloadUdh() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPayloadUdh());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPayloadUdh(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPriority() != null) {
            _hashCode += getPriority().hashCode();
        }
        if (getSrcAddr() != null) {
            _hashCode += getSrcAddr().hashCode();
        }
        _hashCode += getSrcAddrNpi();
        _hashCode += getSrcAddrTon();
        if (getValidityPeriod() != null) {
            _hashCode += getValidityPeriod().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
