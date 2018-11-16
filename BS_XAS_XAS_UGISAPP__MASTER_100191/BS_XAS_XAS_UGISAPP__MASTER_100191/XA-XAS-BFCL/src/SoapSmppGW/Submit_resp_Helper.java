/**
 * Submit_resp_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf190823.02 v62608112801
 */

package SoapSmppGW;

public class Submit_resp_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(Submit_resp.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("message_id");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "message_id"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("error_code");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "error_code"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("command_status");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "command_status"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("sequence_number");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "sequence_number"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
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
          new Submit_resp_Ser(
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
          new Submit_resp_Deser(
            javaType, xmlType, typeDesc);
    };

}
