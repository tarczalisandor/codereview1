package it.usi;

public class VodafoneTest {

	private static VodafoneTest instance = new VodafoneTest();
	private String requestString;
	
	private VodafoneTest() {
		requestString = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
		requestString += "<soapenv:Header soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"/>";
		requestString += "<soapenv:Body soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">";
		requestString +=	"<p518:Submit xmlns:p518=\"urn:SoapSmppGW\">";
		requestString +=		"<sm href=\"#id0\"/>";
		requestString +=		"<gws href=\"#id1\"/>";
		requestString +=	"</p518:Submit>";
		requestString +=	"<multiRef id=\"id1\" soapenc:root=\"0\" soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xsi:type=\"p518:GWSession\" xmlns:p518=\"urn:SoapSmppGW\">";
		requestString +=		"<accountName xsi:type=\"soapenc:string\">NEWGWService</accountName>";
		requestString +=		"<accountPassword xsi:type=\"soapenc:string\">NEWGWService</accountPassword>";
		requestString +=	"</multiRef>";
		requestString +=	"<multiRef id=\"id0\" soapenc:root=\"0\" soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xsi:type=\"p518:Submit_sm\" xmlns:p518=\"urn:SoapSmppGW\">";
		requestString +=		"<destination_addr xsi:type=\"soapenc:string\">393424190086</destination_addr>";
		//requestString +=		"<destination_addr xsi:type=\"soapenc:string\"></destination_addr>";
		requestString +=		"<service_type xsi:type=\"soapenc:string\" xsi:nil=\"true\"/>";
		requestString +=		"<source_addr xsi:type=\"soapenc:string\">393424190086</source_addr>";
		requestString +=		"<source_addr_ton xsi:type=\"soapenc:byte\">1</source_addr_ton>";
		requestString +=		"<source_addr_npi xsi:type=\"soapenc:byte\">1</source_addr_npi>";
		requestString +=		"<dest_addr_ton xsi:type=\"soapenc:byte\">1</dest_addr_ton>";
		requestString +=		"<dest_addr_npi xsi:type=\"soapenc:byte\">1</dest_addr_npi>";
		requestString +=		"<esm_class xsi:type=\"soapenc:byte\" xsi:nil=\"true\"/>";
		requestString +=		"<protocol_id xsi:type=\"soapenc:byte\" xsi:nil=\"true\"/>";
		requestString +=		"<priority_flag xsi:type=\"soapenc:byte\" xsi:nil=\"true\"/>";
		requestString +=		"<schedule_delivery_time xsi:type=\"soapenc:string\" xsi:nil=\"true\"/>";
		requestString +=		"<validity_period xsi:type=\"soapenc:string\" xsi:nil=\"true\"/>";
		requestString +=		"<registered_delivery xsi:type=\"soapenc:byte\">1</registered_delivery>";
		requestString +=		"<short_message xsi:type=\"soapenc:string\">Test message</short_message>";
		requestString +=		"<replace_if_present_flag xsi:type=\"soapenc:byte\" xsi:nil=\"true\"/>";
		requestString +=		"<data_coding xsi:type=\"soapenc:byte\">0</data_coding>";
		requestString +=		"<sm_default_msg_id xsi:type=\"soapenc:byte\" xsi:nil=\"true\"/>";
		requestString +=		"<sm_length xsi:type=\"soapenc:byte\" xsi:nil=\"true\"/>";
		requestString +=		"<message_payload xsi:type=\"soapenc:string\" xsi:nil=\"true\"/>";
		requestString +=	"</multiRef>";
		requestString += "</soapenv:Body>";
		requestString += "</soapenv:Envelope>";
}
	
	public static VodafoneTest getInstance() {
		return instance;
	}
	
	public String getRequestString() {
		return requestString;
	}
}
