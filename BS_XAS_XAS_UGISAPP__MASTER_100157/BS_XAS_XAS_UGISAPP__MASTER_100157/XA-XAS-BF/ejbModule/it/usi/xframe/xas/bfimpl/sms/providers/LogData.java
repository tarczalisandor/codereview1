package it.usi.xframe.xas.bfimpl.sms.providers;

import it.usi.xframe.xas.bfimpl.sms.Chronometer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class LogData
{
	public static final DateFormat LOG_DATE_FORMATTER = new SimpleDateFormat("yyyyMMddHHmmssZ"); 

	public Chronometer chrono = new Chronometer(false);

	public String userid = ""; 
	public Boolean isUserInRole;
	public String originalMsg = "";
	public int    originalMsgLength = 0; 
	public String originalDestinationNumber = ""; 
	public String originalAlias = "";
	public String originalAbiCode = "";

	public String normalizedDestinationNumber = "";
	public int    messageLength = 0; 
	public String messagePartLength = "";
	public String messagePart = "";
	public String usedAlias = "";
	public String usedAbiCode = "";
	public String smsEncoding = "";
	public String smsId = "";
	public String validity = "";
	public String delivery = "";
	public String srcValidity = "";
	public String srcValidityPeriod = "";
	public String defaultValidityPeriod = "";
	public String srcDelivery = "";

	public int typeOfNumber = 0;
	public int numPlanId = 0;
	public String originator = "";

	public String logPrefix = "";
	public String logLevel = "";
	public String errorMessage = "";
	public String errorAt = "";

	public boolean gatewayContacted = false;
	
	public boolean showResponseData = false;
	public String provider;
	public String responseMessageId;
	public String responseErrorCode;
	public String responseCommandStatus;
	public String responseSequenceNumber;
	public String channel = "";
	public String version = "";
}
