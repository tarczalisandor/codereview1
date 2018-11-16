/**
 * Deliver_sm_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package SoapSmppGW;

public class Deliver_sm_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public Deliver_sm_Ser(
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType, 
           com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {
        super(_javaType, _xmlType, _typeDesc);
    }
    public void serialize(
        javax.xml.namespace.QName name,
        org.xml.sax.Attributes attributes,
        java.lang.Object value,
        com.ibm.ws.webservices.engine.encoding.SerializationContext context)
        throws java.io.IOException
    {
        context.startElement(name, addAttributes(attributes,value,context));
        addElements(value,context);
        context.endElement();
    }
    protected org.xml.sax.Attributes addAttributes(
        org.xml.sax.Attributes attributes,
        java.lang.Object value,
        com.ibm.ws.webservices.engine.encoding.SerializationContext context)
        throws java.io.IOException
    {
        return attributes;
    }
    protected void addElements(
        java.lang.Object value,
        com.ibm.ws.webservices.engine.encoding.SerializationContext context)
        throws java.io.IOException
    {
        Deliver_sm bean = (Deliver_sm) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_0_0;
          propValue = bean.getDestination_addr();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_19,
              true,null);
          }
          propQName = QName_0_1;
          propValue = bean.getService_type();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_19,
              true,null);
          }
          propQName = QName_0_2;
          propValue = bean.getSource_addr();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_19,
              true,null);
          }
          propQName = QName_0_3;
          propValue = bean.getSource_addr_ton();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_20,
              true,null);
          propQName = QName_0_4;
          propValue = bean.getSource_addr_npi();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_20,
              true,null);
          propQName = QName_0_5;
          propValue = bean.getDest_addr_ton();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_20,
              true,null);
          propQName = QName_0_6;
          propValue = bean.getDest_addr_npi();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_20,
              true,null);
          propQName = QName_0_7;
          propValue = bean.getEsm_class();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_20,
              true,null);
          propQName = QName_0_8;
          propValue = bean.getProtocol_id();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_20,
              true,null);
          propQName = QName_0_9;
          propValue = bean.getPriority_flag();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_20,
              true,null);
          propQName = QName_0_12;
          propValue = bean.getRegistered_delivery();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_20,
              true,null);
          propQName = QName_0_13;
          propValue = bean.getShort_message();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_19,
              true,null);
          }
          propQName = QName_0_15;
          propValue = bean.getData_coding();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_20,
              true,null);
          propQName = QName_0_17;
          propValue = bean.getSm_length();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_20,
              true,null);
          propQName = QName_0_28;
          propValue = bean.getMessage_state();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_27,
              true,null);
          propQName = QName_0_29;
          propValue = bean.getReceipted_message_id();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_19,
              true,null);
          }
        }
    }
    private final static javax.xml.namespace.QName QName_0_13 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "short_message");
    private final static javax.xml.namespace.QName QName_1_19 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://schemas.xmlsoap.org/soap/encoding/",
                  "string");
    private final static javax.xml.namespace.QName QName_0_0 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "destination_addr");
    private final static javax.xml.namespace.QName QName_0_7 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "esm_class");
    private final static javax.xml.namespace.QName QName_0_17 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "sm_length");
    private final static javax.xml.namespace.QName QName_0_6 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "dest_addr_npi");
    private final static javax.xml.namespace.QName QName_0_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "dest_addr_ton");
    private final static javax.xml.namespace.QName QName_0_29 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "receipted_message_id");
    private final static javax.xml.namespace.QName QName_0_8 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "protocol_id");
    private final static javax.xml.namespace.QName QName_0_3 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "source_addr_ton");
    private final static javax.xml.namespace.QName QName_0_28 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "message_state");
    private final static javax.xml.namespace.QName QName_1_20 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://schemas.xmlsoap.org/soap/encoding/",
                  "byte");
    private final static javax.xml.namespace.QName QName_0_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "source_addr_npi");
    private final static javax.xml.namespace.QName QName_0_1 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "service_type");
    private final static javax.xml.namespace.QName QName_1_27 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://schemas.xmlsoap.org/soap/encoding/",
                  "int");
    private final static javax.xml.namespace.QName QName_0_15 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "data_coding");
    private final static javax.xml.namespace.QName QName_0_12 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "registered_delivery");
    private final static javax.xml.namespace.QName QName_0_9 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "priority_flag");
    private final static javax.xml.namespace.QName QName_0_2 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "source_addr");
}
