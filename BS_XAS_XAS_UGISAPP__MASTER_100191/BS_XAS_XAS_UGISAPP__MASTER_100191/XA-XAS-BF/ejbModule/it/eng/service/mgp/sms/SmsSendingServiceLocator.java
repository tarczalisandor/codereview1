/**
 * SmsSendingServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.eng.service.mgp.sms;

public class SmsSendingServiceLocator extends com.ibm.ws.webservices.multiprotocol.AgnosticService implements com.ibm.ws.webservices.multiprotocol.GeneratedService, it.eng.service.mgp.sms.SmsSendingService {

    public SmsSendingServiceLocator() {
        super(com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
           "http://sms.mgp.service.eng.it/",
           "SmsSendingService"));

        context.setLocatorName("it.eng.service.mgp.sms.SmsSendingServiceLocator");
    }

    public SmsSendingServiceLocator(com.ibm.ws.webservices.multiprotocol.ServiceContext ctx) {
        super(ctx);
        context.setLocatorName("it.eng.service.mgp.sms.SmsSendingServiceLocator");
    }

    // Utilizzo per richiamare una classe proxy per smsSendingServicePort
    private final java.lang.String smsSendingServicePort_address = "http://localhost:9090/SmsSendingServicePort";

    public java.lang.String getSmsSendingServicePortAddress() {
        return smsSendingServicePort_address;
    }

    private java.lang.String smsSendingServicePortPortName = "SmsSendingServicePort";

    // The WSDD port name defaults to the port name.
    private java.lang.String smsSendingServicePortWSDDPortName = "SmsSendingServicePort";

    public java.lang.String getSmsSendingServicePortWSDDPortName() {
        return smsSendingServicePortWSDDPortName;
    }

    public void setSmsSendingServicePortWSDDPortName(java.lang.String name) {
        smsSendingServicePortWSDDPortName = name;
    }

    public it.eng.service.mgp.sms.SmsSendingServicePortType getSmsSendingServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(smsSendingServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // diversamente da come è stato convalidato lURL in WSDL2Java
        }
        return getSmsSendingServicePort(endpoint);
    }

    public it.eng.service.mgp.sms.SmsSendingServicePortType getSmsSendingServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        it.eng.service.mgp.sms.SmsSendingServicePortType _stub =
            (it.eng.service.mgp.sms.SmsSendingServicePortType) getStub(
                smsSendingServicePortPortName,
                (String) getPort2NamespaceMap().get(smsSendingServicePortPortName),
                it.eng.service.mgp.sms.SmsSendingServicePortType.class,
                "it.eng.service.mgp.sms.SmsSendingServiceSoapBindingStub",
                portAddress.toString());
        if (_stub instanceof com.ibm.ws.webservices.engine.client.Stub) {
            ((com.ibm.ws.webservices.engine.client.Stub) _stub).setPortName(smsSendingServicePortWSDDPortName);
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
            if (it.eng.service.mgp.sms.SmsSendingServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                return getSmsSendingServicePort();
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
        if ("SmsSendingServicePort".equals(inputPortName)) {
            return getSmsSendingServicePort();
        }
        else  {
            throw new javax.xml.rpc.ServiceException();
        }
    }

    public void setPortNamePrefix(java.lang.String prefix) {
        smsSendingServicePortWSDDPortName = prefix + "/" + smsSendingServicePortPortName;
    }

    public javax.xml.namespace.QName getServiceName() {
        return super.getServiceName();
    }

    private java.util.Map port2NamespaceMap = null;

    protected java.util.Map getPort2NamespaceMap() {
        if (port2NamespaceMap == null) {
            port2NamespaceMap = new java.util.HashMap();
            port2NamespaceMap.put(
               "SmsSendingServicePort",
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
        if  (portName.getLocalPart().equals("SmsSendingServicePort")) {
            return new javax.xml.rpc.Call[] {
                createCall(portName, "sendMessage", "sendMessage"),
            };
        }
        else {
            throw new javax.xml.rpc.ServiceException("WSWS3062E: Errore: Il nome porta non può essere nullo.");
        }
    }
}
