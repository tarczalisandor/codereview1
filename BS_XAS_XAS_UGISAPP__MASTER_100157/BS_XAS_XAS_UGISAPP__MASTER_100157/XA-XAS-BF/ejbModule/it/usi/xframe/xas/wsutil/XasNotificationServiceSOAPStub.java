/**
 * XasNotificationServiceSOAPStub.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public class XasNotificationServiceSOAPStub extends com.ibm.ws.webservices.engine.client.Stub implements it.usi.xframe.xas.wsutil.XasNotification {
    public XasNotificationServiceSOAPStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws com.ibm.ws.webservices.engine.WebServicesFault {
        if (service == null) {
            super.service = new com.ibm.ws.webservices.engine.client.Service();
        }
        else {
            super.service = service;
        }
        super.engine = ((com.ibm.ws.webservices.engine.client.Service) super.service).getEngine();
        initTypeMapping();
        super.cachedEndpoint = endpointURL;
        super.connection = ((com.ibm.ws.webservices.engine.client.Service) super.service).getConnection(endpointURL);
        super.messageContexts = new com.ibm.ws.webservices.engine.MessageContext[2];
    }

    private void initTypeMapping() {
        javax.xml.rpc.encoding.TypeMapping tm = super.getTypeMapping(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
        java.lang.Class javaType = null;
        javax.xml.namespace.QName xmlType = null;
        com.ibm.ws.webservices.engine.encoding.SerializerFactory sf = null;
        com.ibm.ws.webservices.engine.encoding.DeserializerFactory df = null;
        javaType = it.usi.xframe.xas.wsutil.MobileOriginated.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "mobileOriginated");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = it.usi.xframe.xas.wsutil.ENUM_STATUS.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "ENUM_STATUS");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.EnumSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.EnumDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = it.usi.xframe.xas.wsutil.DeliveryReport.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "deliveryReport");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = it.usi.xframe.xas.wsutil.StatusCodeType.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "statusCodeType");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.EnumSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.EnumDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = it.usi.xframe.xas.wsutil.NotificationResponse.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notificationResponse");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

    }

    private com.ibm.ws.webservices.engine.description.OperationDesc _notifyMobileOriginatedOperation0 = null;
    private com.ibm.ws.webservices.engine.description.OperationDesc _getnotifyMobileOriginatedOperation0() {
        if (_notifyMobileOriginatedOperation0 == null) {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "mobileOriginatedRequest"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "mobileOriginated"), it.usi.xframe.xas.wsutil.MobileOriginated.class, false, false, false, true),
          };
        if (_params0[0] instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_params0[0]).setOption("inputPosition","0");
        }
        _notifyMobileOriginatedOperation0 = new com.ibm.ws.webservices.engine.description.OperationDesc("notifyMobileOriginated", _params0, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "mobileOriginatedResponse"));
        _notifyMobileOriginatedOperation0.setReturnType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notificationResponse"));
        _notifyMobileOriginatedOperation0.setElementQName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyMobileOriginated"));
        _notifyMobileOriginatedOperation0.setSoapAction("http://wsutil.xas.xframe.usi.it/notifyMobileOriginated");
        if (_notifyMobileOriginatedOperation0 instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyMobileOriginatedOperation0).setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "XasNotification"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyMobileOriginatedOperation0).setOption("inputName","notifyMobileOriginatedRequest");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyMobileOriginatedOperation0).setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyMobileOriginatedResponse"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyMobileOriginatedOperation0).setOption("ResponseNamespace","http://wsutil.xas.xframe.usi.it");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyMobileOriginatedOperation0).setOption("targetNamespace","http://wsutil.xas.xframe.usi.it");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyMobileOriginatedOperation0).setOption("outputName","notifyMobileOriginatedResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyMobileOriginatedOperation0).setOption("ResponseLocalPart","notifyMobileOriginatedResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyMobileOriginatedOperation0).setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyMobileOriginatedRequest"));
        }
        if (_notifyMobileOriginatedOperation0.getReturnParamDesc() instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyMobileOriginatedOperation0.getReturnParamDesc()).setOption("outputPosition","0");
        }
        com.ibm.ws.webservices.engine.description.FaultDesc _fault0 = null;
        }
        return _notifyMobileOriginatedOperation0;
    }

    private int _notifyMobileOriginatedIndex0 = 0;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getnotifyMobileOriginatedInvoke0(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_notifyMobileOriginatedIndex0];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(_getnotifyMobileOriginatedOperation0());
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("http://wsutil.xas.xframe.usi.it/notifyMobileOriginated");
            mc.setOperationStyle("wrapped");
            mc.setOperationUse("literal");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.ws.webservices.engine.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
            mc.setProperty(com.ibm.ws.webservices.engine.WebServicesEngine.PROP_DOMULTIREFS, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_notifyMobileOriginatedIndex0] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public it.usi.xframe.xas.wsutil.NotificationResponse notifyMobileOriginated(it.usi.xframe.xas.wsutil.MobileOriginated mobileOriginatedRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getnotifyMobileOriginatedInvoke0(new java.lang.Object[] {mobileOriginatedRequest}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            throw wsf;
        } 
        try {
            return (it.usi.xframe.xas.wsutil.NotificationResponse) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (it.usi.xframe.xas.wsutil.NotificationResponse) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), it.usi.xframe.xas.wsutil.NotificationResponse.class);
        }
    }

    private com.ibm.ws.webservices.engine.description.OperationDesc _notifyDeliveryReportOperation1 = null;
    private com.ibm.ws.webservices.engine.description.OperationDesc _getnotifyDeliveryReportOperation1() {
        if (_notifyDeliveryReportOperation1 == null) {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params1 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "deliveryReportRequest"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "deliveryReport"), it.usi.xframe.xas.wsutil.DeliveryReport.class, false, false, false, true),
          };
        if (_params1[0] instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_params1[0]).setOption("inputPosition","0");
        }
        _notifyDeliveryReportOperation1 = new com.ibm.ws.webservices.engine.description.OperationDesc("notifyDeliveryReport", _params1, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "deliveryReportResponse"));
        _notifyDeliveryReportOperation1.setReturnType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notificationResponse"));
        _notifyDeliveryReportOperation1.setElementQName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyDeliveryReport"));
        _notifyDeliveryReportOperation1.setSoapAction("http://wsutil.xas.xframe.usi.it/notifyDeliveryReport");
        if (_notifyDeliveryReportOperation1 instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyDeliveryReportOperation1).setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "XasNotification"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyDeliveryReportOperation1).setOption("inputName","notifyDeliveryReportRequest");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyDeliveryReportOperation1).setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyDeliveryReportResponse"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyDeliveryReportOperation1).setOption("ResponseNamespace","http://wsutil.xas.xframe.usi.it");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyDeliveryReportOperation1).setOption("targetNamespace","http://wsutil.xas.xframe.usi.it");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyDeliveryReportOperation1).setOption("outputName","notifyDeliveryReportResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyDeliveryReportOperation1).setOption("ResponseLocalPart","notifyDeliveryReportResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyDeliveryReportOperation1).setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyDeliveryReportRequest"));
        }
        if (_notifyDeliveryReportOperation1.getReturnParamDesc() instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_notifyDeliveryReportOperation1.getReturnParamDesc()).setOption("outputPosition","0");
        }
        com.ibm.ws.webservices.engine.description.FaultDesc _fault1 = null;
        }
        return _notifyDeliveryReportOperation1;
    }

    private int _notifyDeliveryReportIndex1 = 1;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getnotifyDeliveryReportInvoke1(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_notifyDeliveryReportIndex1];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(_getnotifyDeliveryReportOperation1());
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("http://wsutil.xas.xframe.usi.it/notifyDeliveryReport");
            mc.setOperationStyle("wrapped");
            mc.setOperationUse("literal");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.ws.webservices.engine.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
            mc.setProperty(com.ibm.ws.webservices.engine.WebServicesEngine.PROP_DOMULTIREFS, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_notifyDeliveryReportIndex1] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public it.usi.xframe.xas.wsutil.NotificationResponse notifyDeliveryReport(it.usi.xframe.xas.wsutil.DeliveryReport deliveryReportRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getnotifyDeliveryReportInvoke1(new java.lang.Object[] {deliveryReportRequest}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            throw wsf;
        } 
        try {
            return (it.usi.xframe.xas.wsutil.NotificationResponse) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (it.usi.xframe.xas.wsutil.NotificationResponse) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), it.usi.xframe.xas.wsutil.NotificationResponse.class);
        }
    }

}
