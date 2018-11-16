/**
 * GWServiceServiceInformation.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package SoapSmppGW;

public class GWServiceServiceInformation implements com.ibm.ws.webservices.multiprotocol.ServiceInformation {

    private static java.util.Map operationDescriptions;
    private static java.util.Map typeMappings;

    static {
         initOperationDescriptions();
         initTypeMappings();
    }

    private static void initOperationDescriptions() { 
        operationDescriptions = new java.util.HashMap();

        java.util.Map inner0 = new java.util.HashMap();

        java.util.List list0 = new java.util.ArrayList();
        inner0.put("deliver", list0);

        com.ibm.ws.webservices.engine.description.OperationDesc deliver0Op = _deliver0Op();
        list0.add(deliver0Op);

        java.util.List list1 = new java.util.ArrayList();
        inner0.put("submit", list1);

        com.ibm.ws.webservices.engine.description.OperationDesc submit1Op = _submit1Op();
        list1.add(submit1Op);

        operationDescriptions.put("GWService",inner0);
        operationDescriptions = java.util.Collections.unmodifiableMap(operationDescriptions);
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _deliver0Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc deliver0Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "sm"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Deliver_sm"), SoapSmppGW.Deliver_sm.class, false, false, false, true),
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "gws"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "GWSession"), SoapSmppGW.GWSession.class, false, false, false, true),
          };
        deliver0Op = new com.ibm.ws.webservices.engine.description.OperationDesc("deliver", _params0, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "DeliverReturn"));
        deliver0Op.setReturnType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Deliver_resp"));
        deliver0Op.setElementQName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Deliver"));
        if (deliver0Op instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)deliver0Op).setOption("inoutOrderingReq","false");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)deliver0Op).setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "GWService"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)deliver0Op).setOption("inputName","DeliverRequest");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)deliver0Op).setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "DeliverResponse"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)deliver0Op).setOption("targetNamespace","urn:SoapSmppGW");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)deliver0Op).setOption("outputName","DeliverResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)deliver0Op).setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "DeliverRequest"));
        }
        com.ibm.ws.webservices.engine.description.FaultDesc _fault0 = null;
        return deliver0Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _submit1Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc submit1Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "sm"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Submit_sm"), SoapSmppGW.Submit_sm.class, false, false, false, true),
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "gws"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "GWSession"), SoapSmppGW.GWSession.class, false, false, false, true),
          };
        submit1Op = new com.ibm.ws.webservices.engine.description.OperationDesc("submit", _params0, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "SubmitReturn"));
        submit1Op.setReturnType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Submit_resp"));
        submit1Op.setElementQName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Submit"));
        if (submit1Op instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)submit1Op).setOption("inoutOrderingReq","false");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)submit1Op).setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "GWService"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)submit1Op).setOption("inputName","SubmitRequest");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)submit1Op).setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "SubmitResponse"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)submit1Op).setOption("targetNamespace","urn:SoapSmppGW");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)submit1Op).setOption("outputName","SubmitResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)submit1Op).setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "SubmitRequest"));
        }
        com.ibm.ws.webservices.engine.description.FaultDesc _fault0 = null;
        return submit1Op;

    }


    private static void initTypeMappings() {
        typeMappings = new java.util.HashMap();
        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Submit_sm"),
                         SoapSmppGW.Submit_sm.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Deliver_sm"),
                         SoapSmppGW.Deliver_sm.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Deliver_resp"),
                         SoapSmppGW.Deliver_resp.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "Submit_resp"),
                         SoapSmppGW.Submit_resp.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:SoapSmppGW", "GWSession"),
                         SoapSmppGW.GWSession.class);

        typeMappings = java.util.Collections.unmodifiableMap(typeMappings);
    }

    public java.util.Map getTypeMappings() {
        return typeMappings;
    }

    public Class getJavaType(javax.xml.namespace.QName xmlName) {
        return (Class) typeMappings.get(xmlName);
    }

    public java.util.Map getOperationDescriptions(String portName) {
        return (java.util.Map) operationDescriptions.get(portName);
    }

    public java.util.List getOperationDescriptions(String portName, String operationName) {
        java.util.Map map = (java.util.Map) operationDescriptions.get(portName);
        if (map != null) {
            return (java.util.List) map.get(operationName);
        }
        return null;
    }

}
