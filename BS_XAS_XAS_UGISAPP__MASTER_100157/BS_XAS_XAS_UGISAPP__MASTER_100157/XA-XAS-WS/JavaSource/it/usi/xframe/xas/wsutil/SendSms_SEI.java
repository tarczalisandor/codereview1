package it.usi.xframe.xas.wsutil;


public interface SendSms_SEI extends java.rmi.Remote
{
  public void sendSms(it.usi.xframe.xas.bfutil.data.SmsMessage sms,it.usi.xframe.xas.bfutil.data.SmsSenderInfo sender) throws it.usi.xframe.system.eservice.ServiceFactoryException,java.rmi.RemoteException,it.usi.xframe.xas.bfutil.XASException;
  public it.usi.xframe.xas.bfutil.data.InternationalSmsResponse sendInternationalSms(it.usi.xframe.xas.bfutil.data.InternationalSmsMessage sms,it.usi.xframe.xas.bfutil.data.SmsDelivery delivery,it.usi.xframe.xas.bfutil.data.SmsBillingInfo billing) throws it.usi.xframe.system.eservice.ServiceFactoryException,java.rmi.RemoteException,it.usi.xframe.xas.bfutil.XASException;
  public void receiveDeliveryReport(it.usi.xframe.xas.bfutil.data.DeliveryRequest deliveryRequest) throws it.usi.xframe.system.eservice.ServiceFactoryException,java.rmi.RemoteException,it.usi.xframe.xas.bfutil.XASException;
}