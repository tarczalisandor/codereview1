/**
 * SmsSendingServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.eng.service.mgp.sms;

public class SmsSendingServiceSoapBindingStub extends com.ibm.ws.webservices.engine.client.Stub implements it.eng.service.mgp.sms.SmsSendingServicePortType {
    public SmsSendingServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws com.ibm.ws.webservices.engine.WebServicesFault {
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
        super.messageContexts = new com.ibm.ws.webservices.engine.MessageContext[1];
    }

    private void initTypeMapping() {
        javax.xml.rpc.encoding.TypeMapping tm = super.getTypeMapping(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
        java.lang.Class javaType = null;
        javax.xml.namespace.QName xmlType = null;
        com.ibm.ws.webservices.engine.encoding.SerializerFactory sf = null;
        com.ibm.ws.webservices.engine.encoding.DeserializerFactory df = null;
        javaType = it.eng.service.mgp.sms.Result.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "result");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.EnumSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.EnumDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = it.eng.service.mgp.sms.GsmPriority.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/commonTypes", "gsmPriority");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.EnumSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.EnumDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = it.eng.service.mgp.sms.Message.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/commonTypes", "message");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = it.eng.service.mgp.sms.GsmEncoding.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/commonTypes", "gsmEncoding");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.EnumSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.EnumDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = it.eng.service.mgp.sms.Reason.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "reason");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.EnumSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.EnumDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = it.eng.service.mgp.sms.MtSendingResult.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "mtSendingResult");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = it.eng.service.mgp.sms.MtMessage.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "mtMessage");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

    }

    private com.ibm.ws.webservices.engine.description.OperationDesc _sendMessageOperation0 = null;
    private com.ibm.ws.webservices.engine.description.OperationDesc _getsendMessageOperation0() {
        if (_sendMessageOperation0 == null) {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "arg1"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "mtMessage"), it.eng.service.mgp.sms.MtMessage.class, false, false, false, true),
          };
        if (_params0[0] instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_params0[0]).setOption("inputPosition","0");
        }
        _sendMessageOperation0 = new com.ibm.ws.webservices.engine.description.OperationDesc("sendMessage", _params0, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "return"));
        _sendMessageOperation0.setReturnType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "mtSendingResult"));
        _sendMessageOperation0.setElementQName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "sendMessage"));
        _sendMessageOperation0.setSoapAction("");
        if (_sendMessageOperation0 instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendMessageOperation0).setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "SmsSendingServicePortType"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendMessageOperation0).setOption("inputName","sendMessage");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendMessageOperation0).setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "sendMessageResponse"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendMessageOperation0).setOption("ResponseNamespace","http://sms.mgp.service.eng.it/");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendMessageOperation0).setOption("targetNamespace","http://sms.mgp.service.eng.it/");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendMessageOperation0).setOption("outputName","sendMessageResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendMessageOperation0).setOption("ResponseLocalPart","sendMessageResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendMessageOperation0).setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "sendMessage"));
        }
        if (_sendMessageOperation0.getReturnParamDesc() instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendMessageOperation0.getReturnParamDesc()).setOption("outputPosition","0");
        }
        com.ibm.ws.webservices.engine.description.FaultDesc _fault0 = null;
        }
        return _sendMessageOperation0;
    }

    private int _sendMessageIndex0 = 0;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getsendMessageInvoke0(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_sendMessageIndex0];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(_getsendMessageOperation0());
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("");
            mc.setOperationStyle("wrapped");
            mc.setOperationUse("literal");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.ws.webservices.engine.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
            mc.setProperty(com.ibm.ws.webservices.engine.WebServicesEngine.PROP_DOMULTIREFS, Boolean.FALSE);
            java.util.HashSet _set = new java.util.HashSet();
            _set.add(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "return"));
            mc.setProperty(com.ibm.ws.webservices.engine.MessageContext.PARAM_MINOCCURS_0, _set);
            super.primeMessageContext(mc);
            super.messageContexts[_sendMessageIndex0] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public it.eng.service.mgp.sms.MtSendingResult sendMessage(it.eng.service.mgp.sms.MtMessage arg1) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getsendMessageInvoke0(new java.lang.Object[] {arg1}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            throw wsf;
        } 
        try {
            return (it.eng.service.mgp.sms.MtSendingResult) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (it.eng.service.mgp.sms.MtSendingResult) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), it.eng.service.mgp.sms.MtSendingResult.class);
        }
    }

}
