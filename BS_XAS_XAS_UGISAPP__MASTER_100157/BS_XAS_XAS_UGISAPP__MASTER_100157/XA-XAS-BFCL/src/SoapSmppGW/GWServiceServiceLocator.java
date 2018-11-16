/**
 * GWServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package SoapSmppGW;

public class GWServiceServiceLocator extends com.ibm.ws.webservices.multiprotocol.AgnosticService implements com.ibm.ws.webservices.multiprotocol.GeneratedService, SoapSmppGW.GWServiceService {

    public GWServiceServiceLocator() {
        super(com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
           "urn:SoapSmppGW",
           "GWServiceService"));

        context.setLocatorName("SoapSmppGW.GWServiceServiceLocator");
    }

    public GWServiceServiceLocator(com.ibm.ws.webservices.multiprotocol.ServiceContext ctx) {
        super(ctx);
        context.setLocatorName("SoapSmppGW.GWServiceServiceLocator");
    }

    // Utilizzo per richiamare una classe proxy per GWService
    private final java.lang.String GWService_address = "http://VodafonePop.intranet.unicreditgroup.eu:80/axis/services/GWService";

    public java.lang.String getGWServiceAddress() {
        return GWService_address;
    }

    private java.lang.String GWServicePortName = "GWService";

    // The WSDD port name defaults to the port name.
    private java.lang.String GWServiceWSDDPortName = "GWService";

    public java.lang.String getGWServiceWSDDPortName() {
        return GWServiceWSDDPortName;
    }

    public void setGWServiceWSDDPortName(java.lang.String name) {
        GWServiceWSDDPortName = name;
    }

    public SoapSmppGW.GWService getGWService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GWService_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // diversamente da come è stato convalidato lURL in WSDL2Java
        }
        return getGWService(endpoint);
    }

    public SoapSmppGW.GWService getGWService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        SoapSmppGW.GWService _stub =
            (SoapSmppGW.GWService) getStub(
                GWServicePortName,
                (String) getPort2NamespaceMap().get(GWServicePortName),
                SoapSmppGW.GWService.class,
                "SoapSmppGW.GWServiceSoapBindingStub",
                portAddress.toString());
        if (_stub instanceof com.ibm.ws.webservices.engine.client.Stub) {
            ((com.ibm.ws.webservices.engine.client.Stub) _stub).setPortName(GWServiceWSDDPortName);
        }
        return _stub;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (SoapSmppGW.GWService.class.isAssignableFrom(serviceEndpointInterface)) {
                return getGWService();
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("WSWS3273E: Errore: Nessuna implementazione stub per linterfaccia:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        String inputPortName = portName.getLocalPart();
        if ("GWService".equals(inputPortName)) {
            return getGWService();
        }
        else  {
            throw new javax.xml.rpc.ServiceException();
        }
    }

    public void setPortNamePrefix(java.lang.String prefix) {
        GWServiceWSDDPortName = prefix + "/" + GWServicePortName;
    }

    public javax.xml.namespace.QName getServiceName() {
        return super.getServiceName();
    }

    private java.util.Map port2NamespaceMap = null;

    protected java.util.Map getPort2NamespaceMap() {
        if (port2NamespaceMap == null) {
            port2NamespaceMap = new java.util.HashMap();
            port2NamespaceMap.put(
               "GWService",
               "http://schemas.xmlsoap.org/wsdl/soap/");
        }
        return port2NamespaceMap;
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            String serviceNamespace = getServiceName().getNamespaceURI();
            for (java.util.Iterator i = getPort2NamespaceMap().keySet().iterator(); i.hasNext(); ) {
                ports.add(
                    com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                        serviceNamespace,
                        (String) i.next()));
            }
        }
        return ports.iterator();
    }

    public javax.xml.rpc.Call[] getCalls(javax.xml.namespace.QName portName) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            throw new javax.xml.rpc.ServiceException("WSWS3062E: Errore: Il nome porta non può essere nullo.");
        }
        if  (portName.getLocalPart().equals("GWService")) {
            return new javax.xml.rpc.Call[] {
                createCall(portName, "Submit", "SubmitRequest"),
                createCall(portName, "Deliver", "DeliverRequest"),
            };
        }
        else {
            throw new javax.xml.rpc.ServiceException("WSWS3062E: Errore: Il nome porta non può essere nullo.");
        }
    }
}
