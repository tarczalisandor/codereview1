package it.usi.xframe.xas.wsutil;


public interface XasNotification_SEI extends java.rmi.Remote
{
  public it.usi.xframe.xas.bfutil.data.NotificationResponse notifyMobileOriginated(it.usi.xframe.xas.wsutil.MobileOriginated mobileOriginated) throws java.rmi.RemoteException;
  public it.usi.xframe.xas.bfutil.data.NotificationResponse notifyDeliveryReport(it.usi.xframe.xas.wsutil.DeliveryReport deliveryReport) throws java.rmi.RemoteException;
}