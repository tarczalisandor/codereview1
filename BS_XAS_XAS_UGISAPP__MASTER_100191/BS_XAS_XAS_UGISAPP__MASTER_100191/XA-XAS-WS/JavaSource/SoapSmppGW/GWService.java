/**
 * GWService.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package SoapSmppGW;

public interface GWService extends java.rmi.Remote {
    public SoapSmppGW.Submit_resp submit(SoapSmppGW.Submit_sm sm, SoapSmppGW.GWSession gws) throws java.rmi.RemoteException;
    public SoapSmppGW.Deliver_resp deliver(SoapSmppGW.Deliver_sm sm, SoapSmppGW.GWSession gws) throws java.rmi.RemoteException;
}
