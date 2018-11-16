package it.usi.xframe.xas.wsutil;


public interface SendSms2_SEI extends java.rmi.Remote
{
  public it.usi.xframe.xas.bfutil.data.SmsResponse3 sendSms3(it.usi.xframe.xas.bfutil.data.SmsRequest3 smsRequest) throws java.rmi.RemoteException;
  public it.usi.xframe.xas.bfutil.data.SmsResponse sendSms2(it.usi.xframe.xas.bfutil.data.SmsRequest smsRequest) throws it.usi.xframe.system.eservice.ServiceFactoryException,java.rmi.RemoteException,it.usi.xframe.xas.bfutil.XASException;
}