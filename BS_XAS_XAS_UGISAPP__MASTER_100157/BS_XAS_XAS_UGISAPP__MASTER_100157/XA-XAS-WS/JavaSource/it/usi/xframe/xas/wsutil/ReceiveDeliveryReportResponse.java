/**
 * ReceiveDeliveryReportResponse.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public class ReceiveDeliveryReportResponse  implements java.io.Serializable {

    public ReceiveDeliveryReportResponse() {
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
        ReceiveDeliveryReportResponse history = (ReceiveDeliveryReportResponse) __history.get();
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
        ReceiveDeliveryReportResponse history = (ReceiveDeliveryReportResponse) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        __hashHistory.set(null);
        return _hashCode;
    }

}
