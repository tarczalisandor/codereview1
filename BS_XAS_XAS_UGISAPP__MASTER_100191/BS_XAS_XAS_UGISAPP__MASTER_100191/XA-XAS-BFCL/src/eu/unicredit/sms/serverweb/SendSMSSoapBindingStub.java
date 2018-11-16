/**
 * SendSMSSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package eu.unicredit.sms.serverweb;

public class SendSMSSoapBindingStub extends com.ibm.ws.webservices.engine.client.Stub implements eu.unicredit.sms.serverweb.SendSMS_Port {
    public SendSMSSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws com.ibm.ws.webservices.engine.WebServicesFault {
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
    }

    private com.ibm.ws.webservices.engine.description.OperationDesc _sendOperation0 = null;
    private com.ibm.ws.webservices.engine.description.OperationDesc _getsendOperation0() {
        if (_sendOperation0 == null) {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "phonenumber"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, true, true),
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "msg"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, true, true),
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "alias"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, true, true),
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "abicode"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, true, true),
          };
        if (_params0[0] instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_params0[0]).setOption("inputPosition","0");
        }
        if (_params0[1] instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_params0[1]).setOption("inputPosition","1");
        }
        if (_params0[2] instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_params0[2]).setOption("inputPosition","2");
        }
        if (_params0[3] instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_params0[3]).setOption("inputPosition","3");
        }
        _sendOperation0 = new com.ibm.ws.webservices.engine.description.OperationDesc("send", _params0, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "status"));
        _sendOperation0.setReturnType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        _sendOperation0.setElementQName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://serverweb.sms.unicredit.eu/", "send"));
        _sendOperation0.setSoapAction("");
        if (_sendOperation0 instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendOperation0).setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://serverweb.sms.unicredit.eu/", "SendSMS"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendOperation0).setOption("inputName","send");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendOperation0).setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://serverweb.sms.unicredit.eu/", "sendResponse"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendOperation0).setOption("ResponseNamespace","http://serverweb.sms.unicredit.eu/");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendOperation0).setOption("targetNamespace","http://serverweb.sms.unicredit.eu/");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendOperation0).setOption("outputName","sendResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendOperation0).setOption("ResponseLocalPart","sendResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendOperation0).setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://serverweb.sms.unicredit.eu/", "send"));
        }
        if (_sendOperation0.getReturnParamDesc() instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_sendOperation0.getReturnParamDesc()).setOption("outputPosition","0");
        }
        com.ibm.ws.webservices.engine.description.FaultDesc _fault0 = null;
        }
        return _sendOperation0;
    }

    private int _sendIndex0 = 0;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getsendInvoke0(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_sendIndex0];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(_getsendOperation0());
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("");
            mc.setOperationStyle("wrapped");
            mc.setOperationUse("literal");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.ws.webservices.engine.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
            mc.setProperty(com.ibm.ws.webservices.engine.WebServicesEngine.PROP_DOMULTIREFS, Boolean.FALSE);
            java.util.HashSet _set = new java.util.HashSet();
            _set.add(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "phonenumber"));
            _set.add(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "msg"));
            _set.add(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "alias"));
            _set.add(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "abicode"));
            mc.setProperty(com.ibm.ws.webservices.engine.MessageContext.PARAM_MINOCCURS_0, _set);
            super.primeMessageContext(mc);
            super.messageContexts[_sendIndex0] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public int send(java.lang.String phonenumber, java.lang.String msg, java.lang.String alias, java.lang.String abicode) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getsendInvoke0(new java.lang.Object[] {phonenumber, msg, alias, abicode}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            throw wsf;
        } 
        try {
            return ((java.lang.Integer) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue()).intValue();
        } catch (java.lang.Exception _exception) {
            return ((java.lang.Integer) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), int.class)).intValue();
        }
    }

}
