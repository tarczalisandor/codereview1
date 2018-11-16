/**
 * SendSMS_ServiceInformation.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package eu.unicredit.sms.serverweb;

public class SendSMS_ServiceInformation implements com.ibm.ws.webservices.multiprotocol.ServiceInformation {

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
        inner0.put("send", list0);

        com.ibm.ws.webservices.engine.description.OperationDesc send0Op = _send0Op();
        list0.add(send0Op);

        operationDescriptions.put("SendSMSImplPort",inner0);
        operationDescriptions = java.util.Collections.unmodifiableMap(operationDescriptions);
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _send0Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc send0Op = null;
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
        send0Op = new com.ibm.ws.webservices.engine.description.OperationDesc("send", _params0, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "status"));
        send0Op.setReturnType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        send0Op.setElementQName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://serverweb.sms.unicredit.eu/", "send"));
        if (send0Op instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)send0Op).setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://serverweb.sms.unicredit.eu/", "SendSMS"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)send0Op).setOption("inputName","send");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)send0Op).setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://serverweb.sms.unicredit.eu/", "sendResponse"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)send0Op).setOption("ResponseNamespace","http://serverweb.sms.unicredit.eu/");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)send0Op).setOption("targetNamespace","http://serverweb.sms.unicredit.eu/");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)send0Op).setOption("outputName","sendResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)send0Op).setOption("ResponseLocalPart","sendResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)send0Op).setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://serverweb.sms.unicredit.eu/", "send"));
        }
        if (send0Op.getReturnParamDesc() instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)send0Op.getReturnParamDesc()).setOption("outputPosition","0");
        }
        com.ibm.ws.webservices.engine.description.FaultDesc _fault0 = null;
        return send0Op;

    }


    private static void initTypeMappings() {
        typeMappings = new java.util.HashMap();
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
