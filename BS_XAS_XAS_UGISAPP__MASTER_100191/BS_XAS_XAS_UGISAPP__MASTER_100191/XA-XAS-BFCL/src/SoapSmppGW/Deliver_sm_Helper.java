/**
 * Deliver_sm_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package SoapSmppGW;

public class Deliver_sm_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(Deliver_sm.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("destination_addr");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "destination_addr"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("service_type");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "service_type"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("source_addr");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "source_addr"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("source_addr_ton");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "source_addr_ton"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "byte"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("source_addr_npi");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "source_addr_npi"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "byte"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("dest_addr_ton");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "dest_addr_ton"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "byte"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("dest_addr_npi");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "dest_addr_npi"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "byte"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("esm_class");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "esm_class"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "byte"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("protocol_id");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "protocol_id"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "byte"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("priority_flag");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "priority_flag"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "byte"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("registered_delivery");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "registered_delivery"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "byte"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("short_message");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "short_message"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("data_coding");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "data_coding"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "byte"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("sm_length");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "sm_length"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "byte"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("message_state");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "message_state"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("receipted_message_id");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "receipted_message_id"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(field);
    };

    /**
     * Return type metadata object
     */
    public static com.ibm.ws.webservices.engine.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static com.ibm.ws.webservices.engine.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class javaType,  
           javax.xml.namespace.QName xmlType) {
        return 
          new Deliver_sm_Ser(
            javaType, xmlType, typeDesc);
    };

    /**
     * Get Custom Deserializer
     */
    public static com.ibm.ws.webservices.engine.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class javaType,  
           javax.xml.namespace.QName xmlType) {
        return 
          new Deliver_sm_Deser(
            javaType, xmlType, typeDesc);
    };

}
