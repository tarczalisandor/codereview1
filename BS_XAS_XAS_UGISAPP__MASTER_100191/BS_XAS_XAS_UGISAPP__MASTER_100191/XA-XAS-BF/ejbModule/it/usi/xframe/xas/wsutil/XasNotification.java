/**
 * XasNotification.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public interface XasNotification extends java.rmi.Remote {
    public it.usi.xframe.xas.wsutil.NotificationResponse notifyMobileOriginated(it.usi.xframe.xas.wsutil.MobileOriginated mobileOriginatedRequest) throws java.rmi.RemoteException;
    public it.usi.xframe.xas.wsutil.NotificationResponse notifyDeliveryReport(it.usi.xframe.xas.wsutil.DeliveryReport deliveryReportRequest) throws java.rmi.RemoteException;
}
