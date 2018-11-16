/**
 * SendSMS_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package eu.unicredit.sms.serverweb;

public class SendSMS_ServiceLocator extends com.ibm.ws.webservices.multiprotocol.AgnosticService implements com.ibm.ws.webservices.multiprotocol.GeneratedService, eu.unicredit.sms.serverweb.SendSMS_Service {

    public SendSMS_ServiceLocator() {
        super(com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
           "http://serverweb.sms.unicredit.eu/",
           "SendSMS"));

        context.setLocatorName("eu.unicredit.sms.serverweb.SendSMS_ServiceLocator");
    }

    public SendSMS_ServiceLocator(com.ibm.ws.webservices.multiprotocol.ServiceContext ctx) {
        super(ctx);
        context.setLocatorName("eu.unicredit.sms.serverweb.SendSMS_ServiceLocator");
    }

    // Utilizzo per richiamare una classe proxy per sendSMSImplPort
    private final java.lang.String sendSMSImplPort_address = "http://guupzm000102:9000/sendSMS";

    public java.lang.String getSendSMSImplPortAddress() {
        return sendSMSImplPort_address;
    }

    private java.lang.String sendSMSImplPortPortName = "SendSMSImplPort";

    // The WSDD port name defaults to the port name.
    private java.lang.String sendSMSImplPortWSDDPortName = "SendSMSImplPort";

    public java.lang.String getSendSMSImplPortWSDDPortName() {
        return sendSMSImplPortWSDDPortName;
    }

    public void setSendSMSImplPortWSDDPortName(java.lang.String name) {
        sendSMSImplPortWSDDPortName = name;
    }

    public eu.unicredit.sms.serverweb.SendSMS_Port getSendSMSImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(sendSMSImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // diversamente da come è stato convalidato lURL in WSDL2Java
        }
        return getSendSMSImplPort(endpoint);
    }

    public eu.unicredit.sms.serverweb.SendSMS_Port getSendSMSImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        eu.unicredit.sms.serverweb.SendSMS_Port _stub =
            (eu.unicredit.sms.serverweb.SendSMS_Port) getStub(
                sendSMSImplPortPortName,
                (String) getPort2NamespaceMap().get(sendSMSImplPortPortName),
                eu.unicredit.sms.serverweb.SendSMS_Port.class,
                "eu.unicredit.sms.serverweb.SendSMSSoapBindingStub",
                portAddress.toString());
        if (_stub instanceof com.ibm.ws.webservices.engine.client.Stub) {
            ((com.ibm.ws.webservices.engine.client.Stub) _stub).setPortName(sendSMSImplPortWSDDPortName);
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
            if (eu.unicredit.sms.serverweb.SendSMS_Port.class.isAssignableFrom(serviceEndpointInterface)) {
                return getSendSMSImplPort();
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
        if ("SendSMSImplPort".equals(inputPortName)) {
            return getSendSMSImplPort();
        }
        else  {
            throw new javax.xml.rpc.ServiceException();
        }
    }

    public void setPortNamePrefix(java.lang.String prefix) {
        sendSMSImplPortWSDDPortName = prefix + "/" + sendSMSImplPortPortName;
    }

    public javax.xml.namespace.QName getServiceName() {
        return super.getServiceName();
    }

    private java.util.Map port2NamespaceMap = null;

    protected java.util.Map getPort2NamespaceMap() {
        if (port2NamespaceMap == null) {
            port2NamespaceMap = new java.util.HashMap();
            port2NamespaceMap.put(
               "SendSMSImplPort",
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
        if  (portName.getLocalPart().equals("SendSMSImplPort")) {
            return new javax.xml.rpc.Call[] {
                createCall(portName, "send", "send"),
            };
        }
        else {
            throw new javax.xml.rpc.ServiceException("WSWS3062E: Errore: Il nome porta non può essere nullo.");
        }
    }
}
