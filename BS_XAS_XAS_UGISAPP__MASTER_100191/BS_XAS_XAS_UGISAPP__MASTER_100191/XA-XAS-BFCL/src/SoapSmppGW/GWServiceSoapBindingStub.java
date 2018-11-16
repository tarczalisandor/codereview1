/**
 * GWServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package SoapSmppGW;

public class GWServiceSoapBindingStub extends com.ibm.ws.webservices.engine.client.Stub implements SoapSmppGW.GWService {
    public GWServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws com.ibm.ws.webservices.engine.WebServicesFault {
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
        javax.xml.rpc.encoding.TypeMapping tm = super.getTypeMapping(com.ibm.ws.webservices.engine.Constants.URI_SOAP11_ENC);
        java.lang.Class javaType = null;
        javax.xml.namespace.QName xmlType = null;
        com.ibm.ws.webservices.engine.encoding.SerializerFactory sf = null;
        com.ibm.ws.webservices.engine.encoding.DeserializerFactory df = null;
        javaType = SoapSmppGW.Submit_sm.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Submit_sm");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = SoapSmppGW.Deliver_sm.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Deliver_sm");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = SoapSmppGW.Deliver_resp.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Deliver_resp");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = SoapSmppGW.Submit_resp.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Submit_resp");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = SoapSmppGW.GWSession.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "GWSession");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

    }

    private com.ibm.ws.webservices.engine.description.OperationDesc _submitOperation0 = null;
    private com.ibm.ws.webservices.engine.description.OperationDesc _getsubmitOperation0() {
        if (_submitOperation0 == null) {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "sm"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Submit_sm"), SoapSmppGW.Submit_sm.class, false, false, false, true),
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "gws"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "GWSession"), SoapSmppGW.GWSession.class, false, false, false, true),
          };
        _submitOperation0 = new com.ibm.ws.webservices.engine.description.OperationDesc("submit", _params0, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "SubmitReturn"));
        _submitOperation0.setReturnType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Submit_resp"));
        _submitOperation0.setElementQName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Submit"));
        _submitOperation0.setSoapAction("");
        if (_submitOperation0 instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_submitOperation0).setOption("inoutOrderingReq","false");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_submitOperation0).setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "GWService"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_submitOperation0).setOption("inputName","SubmitRequest");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_submitOperation0).setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "SubmitResponse"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_submitOperation0).setOption("targetNamespace","urn:SoapSmppGW");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_submitOperation0).setOption("outputName","SubmitResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_submitOperation0).setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "SubmitRequest"));
        }
        com.ibm.ws.webservices.engine.description.FaultDesc _fault0 = null;
        }
        return _submitOperation0;
    }

    private int _submitIndex0 = 0;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getsubmitInvoke0(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_submitIndex0];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(_getsubmitOperation0());
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("");
            mc.setOperationStyle("rpc");
            mc.setOperationUse("encoded");
            super.primeMessageContext(mc);
            super.messageContexts[_submitIndex0] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public SoapSmppGW.Submit_resp submit(SoapSmppGW.Submit_sm sm, SoapSmppGW.GWSession gws) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getsubmitInvoke0(new java.lang.Object[] {sm, gws}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            throw wsf;
        } 
        try {
            return (SoapSmppGW.Submit_resp) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (SoapSmppGW.Submit_resp) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), SoapSmppGW.Submit_resp.class);
        }
    }

    private com.ibm.ws.webservices.engine.description.OperationDesc _deliverOperation1 = null;
    private com.ibm.ws.webservices.engine.description.OperationDesc _getdeliverOperation1() {
        if (_deliverOperation1 == null) {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params1 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "sm"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Deliver_sm"), SoapSmppGW.Deliver_sm.class, false, false, false, true),
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "gws"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "GWSession"), SoapSmppGW.GWSession.class, false, false, false, true),
          };
        _deliverOperation1 = new com.ibm.ws.webservices.engine.description.OperationDesc("deliver", _params1, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "DeliverReturn"));
        _deliverOperation1.setReturnType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Deliver_resp"));
        _deliverOperation1.setElementQName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Deliver"));
        _deliverOperation1.setSoapAction("");
        if (_deliverOperation1 instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_deliverOperation1).setOption("inoutOrderingReq","false");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_deliverOperation1).setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "GWService"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_deliverOperation1).setOption("inputName","DeliverRequest");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_deliverOperation1).setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "DeliverResponse"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_deliverOperation1).setOption("targetNamespace","urn:SoapSmppGW");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_deliverOperation1).setOption("outputName","DeliverResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_deliverOperation1).setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "DeliverRequest"));
        }
        com.ibm.ws.webservices.engine.description.FaultDesc _fault1 = null;
        }
        return _deliverOperation1;
    }

    private int _deliverIndex1 = 1;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getdeliverInvoke1(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_deliverIndex1];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(_getdeliverOperation1());
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("");
            mc.setOperationStyle("rpc");
            mc.setOperationUse("encoded");
            super.primeMessageContext(mc);
            super.messageContexts[_deliverIndex1] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public SoapSmppGW.Deliver_resp deliver(SoapSmppGW.Deliver_sm sm, SoapSmppGW.GWSession gws) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getdeliverInvoke1(new java.lang.Object[] {sm, gws}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            throw wsf;
        } 
        try {
            return (SoapSmppGW.Deliver_resp) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (SoapSmppGW.Deliver_resp) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), SoapSmppGW.Deliver_resp.class);
        }
    }

}
