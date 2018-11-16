/**
 * SmsSendingServiceInformation.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.eng.service.mgp.sms;

public class SmsSendingServiceInformation implements com.ibm.ws.webservices.multiprotocol.ServiceInformation {

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
        inner0.put("sendMessage", list0);

        com.ibm.ws.webservices.engine.description.OperationDesc sendMessage0Op = _sendMessage0Op();
        list0.add(sendMessage0Op);

        operationDescriptions.put("SmsSendingServicePort",inner0);
        operationDescriptions = java.util.Collections.unmodifiableMap(operationDescriptions);
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _sendMessage0Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc sendMessage0Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "arg1"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "mtMessage"), it.eng.service.mgp.sms.MtMessage.class, false, false, false, true),
          };
        if (_params0[0] instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_params0[0]).setOption("inputPosition","0");
        }
        sendMessage0Op = new com.ibm.ws.webservices.engine.description.OperationDesc("sendMessage", _params0, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "return"));
        sendMessage0Op.setReturnType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "mtSendingResult"));
        sendMessage0Op.setElementQName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "sendMessage"));
        if (sendMessage0Op instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)sendMessage0Op).setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "SmsSendingServicePortType"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)sendMessage0Op).setOption("inputName","sendMessage");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)sendMessage0Op).setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "sendMessageResponse"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)sendMessage0Op).setOption("ResponseNamespace","http://sms.mgp.service.eng.it/");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)sendMessage0Op).setOption("targetNamespace","http://sms.mgp.service.eng.it/");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)sendMessage0Op).setOption("outputName","sendMessageResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)sendMessage0Op).setOption("ResponseLocalPart","sendMessageResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)sendMessage0Op).setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "sendMessage"));
        }
        if (sendMessage0Op.getReturnParamDesc() instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)sendMessage0Op.getReturnParamDesc()).setOption("outputPosition","0");
        }
        com.ibm.ws.webservices.engine.description.FaultDesc _fault0 = null;
        return sendMessage0Op;

    }


    private static void initTypeMappings() {
        typeMappings = new java.util.HashMap();
        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "result"),
                         it.eng.service.mgp.sms.Result.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/commonTypes", "gsmPriority"),
                         it.eng.service.mgp.sms.GsmPriority.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/commonTypes", "message"),
                         it.eng.service.mgp.sms.Message.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/commonTypes", "gsmEncoding"),
                         it.eng.service.mgp.sms.GsmEncoding.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "reason"),
                         it.eng.service.mgp.sms.Reason.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "mtSendingResult"),
                         it.eng.service.mgp.sms.MtSendingResult.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://sms.mgp.service.eng.it/", "mtMessage"),
                         it.eng.service.mgp.sms.MtMessage.class);

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
