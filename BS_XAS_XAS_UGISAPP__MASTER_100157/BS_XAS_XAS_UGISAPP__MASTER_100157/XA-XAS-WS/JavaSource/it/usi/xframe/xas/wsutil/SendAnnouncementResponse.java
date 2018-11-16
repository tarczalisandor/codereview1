/**
 * SendAnnouncementResponse.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * 4
 */

package it.usi.xframe.xas.wsutil;

public class SendAnnouncementResponse  implements java.io.Serializable {
    private int sendAnnouncementReturn;

    public SendAnnouncementResponse() {
    }

    public int getSendAnnouncementReturn() {
        return sendAnnouncementReturn;
    }

    public void setSendAnnouncementReturn(int sendAnnouncementReturn) {
        this.sendAnnouncementReturn = sendAnnouncementReturn;
    }

    private transient java.lang.ThreadLocal __history;
    public boolean equals(java.lang.Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false;}
        SendAnnouncementResponse other = (SendAnnouncementResponse) obj;
        boolean _equals;
        _equals = true
            && this.sendAnnouncementReturn == other.getSendAnnouncementReturn();
        if (!_equals) { return false; }
        if (__history == null) {
            synchronized (this) {
                if (__history == null) {
                    __history = new java.lang.ThreadLocal();
                }
            }
        }
        SendAnnouncementResponse history = (SendAnnouncementResponse) __history.get();
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
        SendAnnouncementResponse history = (SendAnnouncementResponse) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        _hashCode += getSendAnnouncementReturn();
        __hashHistory.set(null);
        return _hashCode;
    }

}
