/**
 * SendSMS_Port.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package eu.unicredit.sms.serverweb;

public interface SendSMS_Port extends java.rmi.Remote {
    public int send(java.lang.String phonenumber, java.lang.String msg, java.lang.String alias, java.lang.String abicode) throws java.rmi.RemoteException;
}
