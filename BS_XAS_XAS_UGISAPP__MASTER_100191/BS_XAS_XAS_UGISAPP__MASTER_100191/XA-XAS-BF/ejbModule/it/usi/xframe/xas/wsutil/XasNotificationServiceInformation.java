/**
 * XasNotificationServiceInformation.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package it.usi.xframe.xas.wsutil;

public class XasNotificationServiceInformation implements com.ibm.ws.webservices.multiprotocol.ServiceInformation {

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
        inner0.put("notifyDeliveryReport", list0);

        com.ibm.ws.webservices.engine.description.OperationDesc notifyDeliveryReport0Op = _notifyDeliveryReport0Op();
        list0.add(notifyDeliveryReport0Op);

        java.util.List list1 = new java.util.ArrayList();
        inner0.put("notifyMobileOriginated", list1);

        com.ibm.ws.webservices.engine.description.OperationDesc notifyMobileOriginated1Op = _notifyMobileOriginated1Op();
        list1.add(notifyMobileOriginated1Op);

        operationDescriptions.put("XasEncryptedNotification",inner0);
        java.util.Map inner1 = new java.util.HashMap();

        java.util.List list2 = new java.util.ArrayList();
        inner1.put("notifyDeliveryReport", list2);

        com.ibm.ws.webservices.engine.description.OperationDesc notifyDeliveryReport2Op = _notifyDeliveryReport2Op();
        list2.add(notifyDeliveryReport2Op);

        java.util.List list3 = new java.util.ArrayList();
        inner1.put("notifyMobileOriginated", list3);

        com.ibm.ws.webservices.engine.description.OperationDesc notifyMobileOriginated3Op = _notifyMobileOriginated3Op();
        list3.add(notifyMobileOriginated3Op);

        operationDescriptions.put("XasNotification",inner1);
        operationDescriptions = java.util.Collections.unmodifiableMap(operationDescriptions);
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _notifyDeliveryReport0Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc notifyDeliveryReport0Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "deliveryReportRequest"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "deliveryReport"), it.usi.xframe.xas.wsutil.DeliveryReport.class, false, false, false, true),
          };
        if (_params0[0] instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_params0[0]).setOption("inputPosition","0");
        }
        notifyDeliveryReport0Op = new com.ibm.ws.webservices.engine.description.OperationDesc("notifyDeliveryReport", _params0, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "deliveryReportResponse"));
        notifyDeliveryReport0Op.setReturnType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notificationResponse"));
        notifyDeliveryReport0Op.setElementQName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyDeliveryReport"));
        if (notifyDeliveryReport0Op instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport0Op).setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "XasEncryptedNotification"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport0Op).setOption("inputName","notifyDeliveryReportRequest");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport0Op).setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyDeliveryReportResponse"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport0Op).setOption("ResponseNamespace","http://wsutil.xas.xframe.usi.it");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport0Op).setOption("targetNamespace","http://wsutil.xas.xframe.usi.it");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport0Op).setOption("outputName","notifyDeliveryReportResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport0Op).setOption("ResponseLocalPart","notifyDeliveryReportResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport0Op).setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyDeliveryReportRequest"));
        }
        if (notifyDeliveryReport0Op.getReturnParamDesc() instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport0Op.getReturnParamDesc()).setOption("outputPosition","0");
        }
        com.ibm.ws.webservices.engine.description.FaultDesc _fault0 = null;
        return notifyDeliveryReport0Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _notifyMobileOriginated1Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc notifyMobileOriginated1Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "mobileOriginatedRequest"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "mobileOriginated"), it.usi.xframe.xas.wsutil.MobileOriginated.class, false, false, false, true),
          };
        if (_params0[0] instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_params0[0]).setOption("inputPosition","0");
        }
        notifyMobileOriginated1Op = new com.ibm.ws.webservices.engine.description.OperationDesc("notifyMobileOriginated", _params0, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "mobileOriginatedResponse"));
        notifyMobileOriginated1Op.setReturnType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notificationResponse"));
        notifyMobileOriginated1Op.setElementQName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyMobileOriginated"));
        if (notifyMobileOriginated1Op instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated1Op).setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "XasEncryptedNotification"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated1Op).setOption("inputName","notifyMobileOriginatedRequest");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated1Op).setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyMobileOriginatedResponse"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated1Op).setOption("ResponseNamespace","http://wsutil.xas.xframe.usi.it");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated1Op).setOption("targetNamespace","http://wsutil.xas.xframe.usi.it");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated1Op).setOption("outputName","notifyMobileOriginatedResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated1Op).setOption("ResponseLocalPart","notifyMobileOriginatedResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated1Op).setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyMobileOriginatedRequest"));
        }
        if (notifyMobileOriginated1Op.getReturnParamDesc() instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated1Op.getReturnParamDesc()).setOption("outputPosition","0");
        }
        com.ibm.ws.webservices.engine.description.FaultDesc _fault0 = null;
        return notifyMobileOriginated1Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _notifyDeliveryReport2Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc notifyDeliveryReport2Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "deliveryReportRequest"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "deliveryReport"), it.usi.xframe.xas.wsutil.DeliveryReport.class, false, false, false, true),
          };
        if (_params0[0] instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_params0[0]).setOption("inputPosition","0");
        }
        notifyDeliveryReport2Op = new com.ibm.ws.webservices.engine.description.OperationDesc("notifyDeliveryReport", _params0, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "deliveryReportResponse"));
        notifyDeliveryReport2Op.setReturnType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notificationResponse"));
        notifyDeliveryReport2Op.setElementQName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyDeliveryReport"));
        if (notifyDeliveryReport2Op instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport2Op).setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "XasNotification"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport2Op).setOption("inputName","notifyDeliveryReportRequest");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport2Op).setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyDeliveryReportResponse"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport2Op).setOption("ResponseNamespace","http://wsutil.xas.xframe.usi.it");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport2Op).setOption("targetNamespace","http://wsutil.xas.xframe.usi.it");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport2Op).setOption("outputName","notifyDeliveryReportResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport2Op).setOption("ResponseLocalPart","notifyDeliveryReportResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport2Op).setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyDeliveryReportRequest"));
        }
        if (notifyDeliveryReport2Op.getReturnParamDesc() instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyDeliveryReport2Op.getReturnParamDesc()).setOption("outputPosition","0");
        }
        com.ibm.ws.webservices.engine.description.FaultDesc _fault0 = null;
        return notifyDeliveryReport2Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _notifyMobileOriginated3Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc notifyMobileOriginated3Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "mobileOriginatedRequest"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "mobileOriginated"), it.usi.xframe.xas.wsutil.MobileOriginated.class, false, false, false, true),
          };
        if (_params0[0] instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)_params0[0]).setOption("inputPosition","0");
        }
        notifyMobileOriginated3Op = new com.ibm.ws.webservices.engine.description.OperationDesc("notifyMobileOriginated", _params0, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "mobileOriginatedResponse"));
        notifyMobileOriginated3Op.setReturnType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notificationResponse"));
        notifyMobileOriginated3Op.setElementQName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyMobileOriginated"));
        if (notifyMobileOriginated3Op instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated3Op).setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "XasNotification"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated3Op).setOption("inputName","notifyMobileOriginatedRequest");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated3Op).setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyMobileOriginatedResponse"));
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated3Op).setOption("ResponseNamespace","http://wsutil.xas.xframe.usi.it");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated3Op).setOption("targetNamespace","http://wsutil.xas.xframe.usi.it");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated3Op).setOption("outputName","notifyMobileOriginatedResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated3Op).setOption("ResponseLocalPart","notifyMobileOriginatedResponse");
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated3Op).setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notifyMobileOriginatedRequest"));
        }
        if (notifyMobileOriginated3Op.getReturnParamDesc() instanceof com.ibm.ws.webservices.engine.configurable.Configurable) {
         ((com.ibm.ws.webservices.engine.configurable.Configurable)notifyMobileOriginated3Op.getReturnParamDesc()).setOption("outputPosition","0");
        }
        com.ibm.ws.webservices.engine.description.FaultDesc _fault0 = null;
        return notifyMobileOriginated3Op;

    }


    private static void initTypeMappings() {
        typeMappings = new java.util.HashMap();
        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "mobileOriginated"),
                         it.usi.xframe.xas.wsutil.MobileOriginated.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "ENUM_STATUS"),
                         it.usi.xframe.xas.wsutil.ENUM_STATUS.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "deliveryReport"),
                         it.usi.xframe.xas.wsutil.DeliveryReport.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "statusCodeType"),
                         it.usi.xframe.xas.wsutil.StatusCodeType.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://wsutil.xas.xframe.usi.it", "notificationResponse"),
                         it.usi.xframe.xas.wsutil.NotificationResponse.class);

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
