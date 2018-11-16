/**
 * ReceiveDeliveryReport.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public class ReceiveDeliveryReport  implements java.io.Serializable {
    private it.usi.xframe.xas.bfutil.data.DeliveryRequest deliveryRequest;

    public ReceiveDeliveryReport() {
    }

    public it.usi.xframe.xas.bfutil.data.DeliveryRequest getDeliveryRequest() {
        return deliveryRequest;
    }

    public void setDeliveryRequest(it.usi.xframe.xas.bfutil.data.DeliveryRequest deliveryRequest) {
        this.deliveryRequest = deliveryRequest;
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
        ReceiveDeliveryReport history = (ReceiveDeliveryReport) __history.get();
        if (history != null) { return (history == obj); }
        if (this == obj) return true;
        __history.set(obj);
        ReceiveDeliveryReport other = (ReceiveDeliveryReport) obj;
        boolean _equals;
        _equals = true
            && ((this.deliveryRequest==null && other.getDeliveryRequest()==null) || 
             (this.deliveryRequest!=null &&
              this.deliveryRequest.equals(other.getDeliveryRequest())));
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
        ReceiveDeliveryReport history = (ReceiveDeliveryReport) __hashHistory.get();
        if (history != null) { return 0; }
        __hashHistory.set(this);
        int _hashCode = 1;
        if (getDeliveryRequest() != null) {
            _hashCode += getDeliveryRequest().hashCode();
        }
        __hashHistory.set(null);
        return _hashCode;
    }

}
