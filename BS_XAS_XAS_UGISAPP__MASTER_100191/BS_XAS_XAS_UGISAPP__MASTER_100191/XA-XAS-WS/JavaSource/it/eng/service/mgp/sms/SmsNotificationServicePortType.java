/**
 * SmsNotificationServicePortType.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.eng.service.mgp.sms;

public interface SmsNotificationServicePortType extends java.rmi.Remote {
    public void newMessage(it.eng.service.mgp.sms.MoMessage arg1) throws java.rmi.RemoteException;
    public void newDeliveryStatus(it.eng.service.mgp.sms.MtDeliveryStatus arg1) throws java.rmi.RemoteException;
}
