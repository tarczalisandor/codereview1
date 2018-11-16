package it.usi.xframe.xas.wsutil;

public interface Announcement_SEI extends java.rmi.Remote
{
 public int getUserStatus(java.lang.String userid) throws it.usi.xframe.system.eservice.ServiceFactoryException,java.rmi.RemoteException;
 public int sendAnnouncement(java.lang.String user,java.lang.String sender,java.lang.String message,java.util.Date expirationDate) throws it.usi.xframe.system.eservice.ServiceFactoryException,java.rmi.RemoteException;
}