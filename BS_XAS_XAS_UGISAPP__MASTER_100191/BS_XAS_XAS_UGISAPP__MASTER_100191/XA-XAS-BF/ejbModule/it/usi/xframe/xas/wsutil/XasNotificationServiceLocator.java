/**
 * XasNotificationServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public class XasNotificationServiceLocator extends com.ibm.ws.webservices.multiprotocol.AgnosticService implements com.ibm.ws.webservices.multiprotocol.GeneratedService, it.usi.xframe.xas.wsutil.XasNotificationService {

    public XasNotificationServiceLocator() {
        super(com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
           "http://wsutil.xas.xframe.usi.it",
           "XasNotificationService"));

        context.setLocatorName("it.usi.xframe.xas.wsutil.XasNotificationServiceLocator");
    }

    public XasNotificationServiceLocator(com.ibm.ws.webservices.multiprotocol.ServiceContext ctx) {
        super(ctx);
        context.setLocatorName("it.usi.xframe.xas.wsutil.XasNotificationServiceLocator");
    }

    // Utilizzo per richiamare una classe proxy per xasEncryptedNotification
    private final java.lang.String xasEncryptedNotification_address = "https://localhost:9080/XA-XAS-WS/services/XasEncryptedNotification";

    public java.lang.String getXasEncryptedNotificationAddress() {
        return xasEncryptedNotification_address;
    }

    private java.lang.String xasEncryptedNotificationPortName = "XasEncryptedNotification";

    // The WSDD port name defaults to the port name.
    private java.lang.String xasEncryptedNotificationWSDDPortName = "XasEncryptedNotification";

    public java.lang.String getXasEncryptedNotificationWSDDPortName() {
        return xasEncryptedNotificationWSDDPortName;
    }

    public void setXasEncryptedNotificationWSDDPortName(java.lang.String name) {
        xasEncryptedNotificationWSDDPortName = name;
    }

    // Utilizzo per richiamare una classe proxy per xasNotification
    private final java.lang.String xasNotification_address = "http://localhost:9080/XA-XAS-WS/services/XasNotification";

    public java.lang.String getXasNotificationAddress() {
        return xasNotification_address;
    }

    private java.lang.String xasNotificationPortName = "XasNotification";

    // The WSDD port name defaults to the port name.
    private java.lang.String xasNotificationWSDDPortName = "XasNotification";

    public java.lang.String getXasNotificationWSDDPortName() {
        return xasNotificationWSDDPortName;
    }

    public void setXasNotificationWSDDPortName(java.lang.String name) {
        xasNotificationWSDDPortName = name;
    }

    public it.usi.xframe.xas.wsutil.XasNotification getXasNotification() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(xasNotification_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // diversamente da come è stato convalidato lURL in WSDL2Java
        }
        return getXasNotification(endpoint);
    }

    public it.usi.xframe.xas.wsutil.XasNotification getXasNotification(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        it.usi.xframe.xas.wsutil.XasNotification _stub =
            (it.usi.xframe.xas.wsutil.XasNotification) getStub(
                xasNotificationPortName,
                (String) getPort2NamespaceMap().get(xasNotificationPortName),
                it.usi.xframe.xas.wsutil.XasNotification.class,
                "it.usi.xframe.xas.wsutil.XasNotificationServiceSOAPStub",
                portAddress.toString());
        if (_stub instanceof com.ibm.ws.webservices.engine.client.Stub) {
            ((com.ibm.ws.webservices.engine.client.Stub) _stub).setPortName(xasNotificationWSDDPortName);
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
            if (it.usi.xframe.xas.wsutil.XasNotification.class.isAssignableFrom(serviceEndpointInterface)) {
                return getXasNotification();
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
        if ("XasNotification".equals(inputPortName)) {
            return getXasNotification();
        }
        else  {
            throw new javax.xml.rpc.ServiceException();
        }
    }

    public void setPortNamePrefix(java.lang.String prefix) {
        xasEncryptedNotificationWSDDPortName = prefix + "/" + xasEncryptedNotificationPortName;
        xasNotificationWSDDPortName = prefix + "/" + xasNotificationPortName;
    }

    public javax.xml.namespace.QName getServiceName() {
        return super.getServiceName();
    }

    private java.util.Map port2NamespaceMap = null;

    protected java.util.Map getPort2NamespaceMap() {
        if (port2NamespaceMap == null) {
            port2NamespaceMap = new java.util.HashMap();
            port2NamespaceMap.put(
               "XasEncryptedNotification",
               "http://schemas.xmlsoap.org/wsdl/soap/");
            port2NamespaceMap.put(
               "XasNotification",
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
        if  (portName.getLocalPart().equals("XasEncryptedNotification")) {
            return new javax.xml.rpc.Call[] {
                createCall(portName, "notifyMobileOriginated", "notifyMobileOriginatedRequest"),
                createCall(portName, "notifyDeliveryReport", "notifyDeliveryReportRequest"),
            };
        }
        else if  (portName.getLocalPart().equals("XasNotification")) {
            return new javax.xml.rpc.Call[] {
                createCall(portName, "notifyMobileOriginated", "notifyMobileOriginatedRequest"),
                createCall(portName, "notifyDeliveryReport", "notifyDeliveryReportRequest"),
            };
        }
        else {
            throw new javax.xml.rpc.ServiceException("WSWS3062E: Errore: Il nome porta non può essere nullo.");
        }
    }
}
