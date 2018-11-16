package it.usi.xframe.xas.bfimpl.a2psms.providers.virtual;

import java.text.MessageFormat;

import it.usi.xframe.xas.bfimpl.a2psms.configuration.Originator;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Provider;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.XasUser;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsRequest;
import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsResponse;
import it.usi.xframe.xas.bfimpl.a2psms.providers.GatewayA2Psms;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.util.json.XConstants;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.Chronometer;
import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

public class GatewaySmsProvider extends GatewayA2Psms {
	private static Logger logger = LoggerFactory.getLogger(GatewaySmsProvider.class);

	public InternalSmsResponse sendSingleMessage(InternalSmsRequest internalSmsRequest, XasUser xasUser, Provider provider, Originator originator, boolean multipart) {
		String myUUID = (String) MDC.get(ConstantsSms.MY_UUID_KEY);
		if (myUUID == null) myUUID = UUID.randomUUID().toString();
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I).logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, GatewaySmsProvider.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
              .logIt(SmartLog.K_METHOD, "sendSms2");
		
 		sl.preset("myPreset");
		logger.debug(sl.logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER 
				, "a_internalSmsRequest", XConstants.XSTREAMER.toXML(internalSmsRequest)
				, "a_xasUser", XConstants.XSTREAMER.toXML(xasUser)
				, "a_provider", XConstants.XSTREAMER.toXML(provider)
				, "a_originator", XConstants.XSTREAMER.toXML(originator)
				).getLogRow(true)); // Debug and keep row
		sl.reload("myPreset");

		InternalSmsResponse internalSmsResponse = new InternalSmsResponse();
		
		boolean sendOk = true;
		if (sendOk) {
			String[] noSmsIdS = {"NoSmsIds"};
			internalSmsResponse.setSmsIds(noSmsIdS);
			internalSmsResponse.setCode(ConstantsSms.XAS00000I_CODE);
			internalSmsResponse.setMessage(MessageFormat.format(ConstantsSms.XAS00000I_MESSAGE2, new String[]{internalSmsRequest.getUuid(), Integer.toString(internalSmsRequest.getDstByteLength()), "?", internalSmsRequest.getEncodingDescription()}));
		} else {
			internalSmsResponse = new InternalSmsResponse(ConstantsSms.XAS05004E_CODE, ConstantsSms.XAS05004E_MESSAGE, null, null);
		}

		logger.debug(sl.reload("myPreset").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_RETURN, "a_internalSmsResponse", XConstants.XSTREAMER.toXML(internalSmsResponse)).getLogRow());
		return internalSmsResponse;
	}

	public InternalSmsResponse sendMessage(
			InternalSmsResponse internalSmsResponse,
            InternalSmsRequest internalSmsRequest, XasUser xasUser,
            Provider provider, Originator originator, boolean multiPart,
            Chronometer chronometer) throws XASException {
	    // TODO Auto-generated method stub
	    return null;
    }
}
