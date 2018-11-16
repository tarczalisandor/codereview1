package it.usi.xframe.xas.wsutil;


public interface SendEmail_SEI extends java.rmi.Remote
{
  public byte[] exportEmailMessage(it.usi.xframe.xas.bfutil.data.EmailMessage emailMsg,it.usi.xframe.xas.bfutil.data.BinaryEmailAttachment[] attachments) throws java.rmi.RemoteException,it.usi.xframe.system.eservice.ServiceFactoryException,it.usi.xframe.xas.bfutil.XASException;
  public void sendEmailMessageWithBinary(it.usi.xframe.xas.bfutil.data.EmailMessage emailMessage,it.usi.xframe.xas.bfutil.data.BinaryEmailAttachment[] attachments) throws java.rmi.RemoteException,it.usi.xframe.system.eservice.ServiceFactoryException,it.usi.xframe.xas.bfutil.XASException;
}