package it.usi.xframe.xas.bfimpl.sms.providers.vodafone;

import ie.omk.smpp.util.PacketStatus;

import java.util.HashMap;

public class SMPPUtil {
	/*	DEPRECATED 2016.07.18	
	
	// SMPP error code mapping
	private static final HashMap STATUS_MESSAGE_MAP;
	static {
        STATUS_MESSAGE_MAP = new HashMap();
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.OK), "OK");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_MESSAGE_LEN), "Message length invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_COMMAND_LEN), "Command length invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_COMMAND_ID), "Command ID invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_BIND_STATUS), "Incorrect bind status for given command");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.ALREADY_BOUND), "ESME already in bound state");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_PRIORITY_FLAG), "Priority flag invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_REGISTERED_DELIVERY_FLAG), "Registered delivery flag invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.SYSTEM_ERROR), "System error");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_SOURCE_ADDRESS), "Source address invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_DEST_ADDRESS), "Dest address invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_MESSAGE_ID), "Message ID invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.BIND_FAILED), "Bind failed");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_PASSWORD), "Password invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_SYSTEM_ID), "System ID invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.CANCEL_SM_FAILED), "Cancel SM failed");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.REPLACE_SM_FAILED), "Replace SM failed");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.MESSAGE_QUEUE_FULL), "Message queue full");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_SERVICE_TYPE), "Service type invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_NUMBER_OF_DESTINATIONS), "Number of destinations invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_DISTRIBUTION_LIST), "Distribution list name invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_DESTINATION_FLAG), "Destination flag is invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_SUBMIT_WITH_REPLACE), "Submit with replace request invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_ESM_CLASS), "Field esm_class invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.SUBMIT_TO_DISTRIBUTION_LIST_FAILED), "Cannot submit to distribution list");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.SUBMIT_FAILED), "Submit SM failed");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_SOURCE_TON), "Source address TON invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_SOURCE_NPI), "Source address NPI invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_DESTINATION_TON), "Dest address TON invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_DESTINATION_NPI), "Dest address NPI invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_SYSTEM_TYPE), "System type invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_REPLACE_IF_PRESENT_FLAG), "Field replace_if_present invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_NUMBER_OF_MESSAGES), "Number of messages invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.THROTTLING_ERROR), "Throttling error");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_SCHEDULED_DELIVERY_TIME), "Scheduled delivery time invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_EXPIRY_TIME), "Message validity period invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_PREDEFINED_MESSAGE), "Predefined message invalid or not found");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.RECEIVER_TEMPORARY_ERROR), "ESME receiver temporary app error");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.RECEIVER_PERMANENT_ERROR), "ESME receiver permanent app error");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.RECEIVER_REJECT_MESSAGE), "ESME receiver reject app error");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.QUERY_SM_FAILED), "Query SM failed");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_OPTIONAL_PARAMETERS), "Error in the optional part of the PDU Body");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.OPTIONAL_PARAMETER_NOT_ALLOWED), "Optional Parameter not allowed");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_PARAMETER_LENGTH), "Parameter length invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.MISSING_EXPECTED_PARAMETER), "Expected optional parameter missing");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.INVALID_PARAMETER_VALUE), "Optional parameter value invalid");
        STATUS_MESSAGE_MAP.put(new Integer(PacketStatus.DELIVERY_FAILED), "Deliver SM failed");
	}
	
 	public static String getSMPPErrorDescription(int smppErrorCode) {
		String descr = (String)STATUS_MESSAGE_MAP.get(new Integer(smppErrorCode));
		if ( descr!=null )
			return descr;
		return "Unknown error (code "+smppErrorCode+")";
	}
	*/
}
