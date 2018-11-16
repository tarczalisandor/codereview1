package it.usi.xframe.xas.bfimpl.sms;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.Configuration;
import it.usi.xframe.xas.bfimpl.a2psms.configuration.XASConfigurationException;
import it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XASRuntimeException;
import it.usi.xframe.xas.bfutil.XASWrongRequestException;
import it.usi.xframe.xas.bfutil.XasServiceFactory;
import it.usi.xframe.xas.bfutil.data.SmsRequest;
import it.usi.xframe.xas.bfutil.data.SmsRequest3;
import it.usi.xframe.xas.bfutil.data.SmsResponse3;
import it.usi.xframe.xas.util.PreSmsRoleCache;
import it.usi.xframe.xas.util.json.XConstants;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.unicredit.xframe.slf.SmartLog;

public class JMSMessageConsumer {
	
	private Logger logger = LoggerFactory.getLogger(JMSMessageConsumer.class);
	//private static final String USER_ID = "MDB";
	private static final String USER_ID_PROPERTY = "JMSXUserID";
	private static final String MSG_DATE_PROPERTY = "JMS_IBM_PutDate";
	private static final String MSG_TIME_PROPERTY = "JMS_IBM_PutTime";
	private static final String JMS_DATE_FORMAT = "yyyyMMdd";
	private static final String JMS_TIME_FORMAT = "HHmmssSS";
	private static final String MSG_TIME_ZONE_FORMAT = "Z";
	
	private static DateFormat df = new SimpleDateFormat(JMS_DATE_FORMAT + JMS_TIME_FORMAT + MSG_TIME_ZONE_FORMAT);
	
	/**
	 * onMessage
	 */
	public void onMessage(javax.jms.Message msg) {
		
			/*Enumeration properties;
			try {
				properties = msg.getPropertyNames();
			} catch (JMSException e1) {
				logger.debug("Aiuto!!! fallita getPropertyNames()!!");
				e1.printStackTrace();
				return;
			}
			while(properties.hasMoreElements()) {
				String prp = (String)properties.nextElement();
				logger.debug("Proprietà presente: " + prp + " = ");
			}*/
			if (logger.isDebugEnabled()) {
			String prpValue, prpName = null;
			try {
				prpName = "JMSXAppID"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMSXUserID"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMS_IBM_Encoding"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMS_IBM_MsgType"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMSXDeliveryCount"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMS_IBM_PutTime"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMS_IBM_Format"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMS_IBM_PutApplType"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMS_IBM_PutDate"; prpValue = msg.getStringProperty(prpName); logger.debug(prpName + " = " + prpValue);
				prpName = "JMS_IBM_Character_Set"; prpValue = msg.getStringProperty(prpName);	 logger.debug(prpName + " = " + prpValue);
				prpName = "JMS CorrelationID"; prpValue = msg.getJMSCorrelationID();	 logger.debug(prpName + " = " + prpValue);
			} catch(JMSException e) {
				logger.debug("Errore sulla proprietà " + prpName);
				return;
			}
			}
			
			//PreServiceFactory factory = PreServiceFactory.getInstance();
			//IPreServiceFacade facade = null;
			try {
				String userId = msg.getStringProperty(USER_ID_PROPERTY);
				//userId = StringUtils.trim(userId);
				//userId = StringUtils.rightPad(userId, 8);
				userId = (userId == null ? null : userId.trim());
				boolean isUserInRole = PreSmsRoleCache.getInstance().isInRole(userId);
				/*boolean isUserInRole = false;
				try {
					facade = factory.getPreServiceFacade();
					PreEnvironmentVar env = facade.getEnvVarExtended(false);
					PGEabdb2 user = facade.SoapRequestPGEabdb2(env.getCodBanca(), userId, Constants.MY_APPL_ID);
					isUserInRole = Constants.SMS_AUTHORITY.equals(user.getPG_COD_AUTHOR());
					/*if (! Constants.SMS_AUTHORITY.equals(user.getPG_COD_AUTHOR())) {
						SmsResponse response = new SmsResponse(Constants.XAS00004E_CODE, Constants.XAS00004E_MESSAGE, userId);
						LogData logData = new LogData();
						SlfLogger slfLogger = new SlfLogger();
						logData.chrono.start();
						logData.userid = userId;
						logData.isUserInRole = new Boolean(false);
						logData.gatewayContacted = false;
						logData.chrono.stop();
						logData.errorMessage = response.getMessage();
						slfLogger.logError(log, logData);
						return;
					}*/
				/*} catch (Exception e) {
					throw new XASRuntimeException("Exception calling PRE for PreUserData",e);
				} finally {
					//factory.dispose(facade);    CANNOT DISPOSE FACADE AS MDB (UNAUTHENTICATED USER) GETS ERROR
				}*/
				SmsRequest3 smsRequest = getRequest(msg);

				IXasSendsmsServiceFacade facade;
			        facade = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
		        SmsResponse3 smsResponse = null;
				try{ 
					// UUID retrieved from SmsRequest3 or from MDC, Header, Generated
					String myUUID = smsRequest.getUuid();
					
					SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, JMSMessageConsumer.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
					.logIt(SmartLog.K_METHOD, "sendSms3", SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER, ConstantsSms.K_UUID_TYPE, myUUID).preset("default"); 

//		 			// Write to log the associated UUID and CRID if present
//					String myCRID = (String) MDC.get(Constants.MY_CRID_KEY);
//		 			if (myCRID != null) {
//		 				logger.info(sl.logIt(SmartLog.K_STEP, "correlating", SmartLog.K_CRID, myCRID).getLogRow(true));
//		 			}

		 			// If debug dump parameters
		 			if (logger.isDebugEnabled()) { 
						logger.debug(sl.reload("default").logIt(SmartLog.K_STEP, "displayParams", SmartLog.K_PARAMS, XConstants.XSTREAMER.toXML(smsRequest)).getLogRow());
					}
		 			
					smsResponse = facade.sendSms3(smsRequest);	
                } finally {
					XasServiceFactory.getInstance().dispose(facade);
				}
				return;
			} catch (XASWrongRequestException e) {
				//error while trying to retrieve SmsRequest: print the whole stack trace (possibly error in JMS)
				e.printStackTrace(System.out);
			} catch (it.usi.xframe.xas.bfimpl.a2psms.configuration.XASConfigurationException e) {
				throw new XASRuntimeException(e);
			} catch (JMSException e) {
				throw new XASRuntimeException(e);
			} catch (ParseException e) {
				throw new XASRuntimeException(e);
			} catch (RemoteException e) {
				throw new XASRuntimeException(e);
			} catch (ServiceFactoryException e) {
				throw new XASRuntimeException(e);
            }
			
	}
	
	private SmsRequest3 getRequest(javax.jms.Message msg) throws XASWrongRequestException, JMSException, ParseException, XASConfigurationException {
		SmsRequest3 req = null;
		if (msg instanceof MapMessage) {
			req = getRequest((MapMessage)msg);
		} else if (msg instanceof TextMessage) {
			req = getRequest((TextMessage)msg);
		} else if (msg instanceof ObjectMessage) {
			req = getRequest((ObjectMessage)msg);
		} else throw new XASWrongRequestException("Message of unexpected class: " + msg.getClass().getName());
//		InternalSmsRequest intreq = new InternalSmsRequest(req);
//		intreq.setRequestTime(getRequestTime(msg));
//		intreq.setChannel(ConstantsSms.CHANNEL_MQ);
		//logger.debug("Letto messaggio: timezone = " + MSG_TIME_ZONE);
//		return intreq;
		return req;
	}
	
	private Date getRequestTime(javax.jms.Message msg) throws JMSException, XASConfigurationException, XASException {
		String writeDate = msg.getStringProperty(MSG_DATE_PROPERTY);
		String writeTime = msg.getStringProperty(MSG_TIME_PROPERTY);
		int year = Integer.parseInt(writeDate.substring(0, 4));
		int month = Integer.parseInt(writeDate.substring(4,6));
		int day = Integer.parseInt(writeDate.substring(6,8));
		int hours = Integer.parseInt(writeTime.substring(0,2));
		int minutes = Integer.parseInt(writeTime.substring(2,4));
		int seconds = Integer.parseInt(writeTime.substring(4,6));
		Calendar cal = Calendar.getInstance(Configuration.getInstance().getJmsTimeZone());
		cal.set(year, month-1, day, hours, minutes, seconds);
		return cal.getTime();
	}
	
	private SmsRequest3 getRequest(MapMessage msg) throws XASWrongRequestException {
		try {
			if (logger.isDebugEnabled()) logger.debug("Request is a MapMessage");
			SmsRequest3 request = new SmsRequest3();
			request.setCorrelationID(msg.getString("correlationID"));
			request.setForceAsciiEncoding(new Boolean(msg.getBoolean("forceAsciiEncoding")));
			request.setMsg(msg.getString("msg"));
			request.setPhoneNumber(msg.getString("phoneNumber"));
			request.setXasUser(msg.getString("xasUser"));
			request.setSmsResponse(new Boolean(msg.getBoolean("smsResponse")));
			SimpleDateFormat sdf = new SimpleDateFormat(SmsRequest.DATE_FORMAT); 
			try {
				if (msg.getString("validity") != null) request.setValidity(sdf.parse(msg.getString("validity")));
				if (msg.getString("validityPeriod") != null) request.setValidityPeriod(new Integer(Integer.parseInt(msg.getString("validityPeriod"))));
				if (msg.getString("deliveryTime") != null) request.setDeliveryTime(sdf.parse(msg.getString("deliveryTime")));
			} catch (ParseException e) {
				throw new XASWrongRequestException("Error parsing DateTime value in MapMessage",e);
			}
			return request;
		} catch (JMSException e) {
			throw new XASWrongRequestException(e);
		}
	}
	
	private SmsRequest3 getRequest(TextMessage msg) throws XASWrongRequestException {
		try {
			if (logger.isDebugEnabled()) logger.debug("Request is a TextMessage");
			String text = msg.getText();
			XStream xstream = null;
			if (text.length() == 0) throw new XASWrongRequestException("Empty request");
			boolean bool = text.startsWith("<");
			xstream = (bool ? new XStream(new DomDriver()) : new XStream(new JettisonMappedXmlDriver()));
			xstream.alias("smsRequest", SmsRequest.class);
			//logger.debug("Testo ricevuto: " + text + "E' un xml? " + bool + "--");
			//logger.debug("Convertitore usato: " + (bool ? "xml" : "json"));
			SmsRequest3 smsRequest = (SmsRequest3) xstream.fromXML(text);
			return smsRequest;
		} catch(JMSException e) {
			throw new XASWrongRequestException("Method getText() failed",e);
		}
	}
	
	private SmsRequest3 getRequest(ObjectMessage msg) throws XASWrongRequestException {
		if (logger.isDebugEnabled()) logger.debug("Request is a ObjectMessage");
		Serializable obj = null;
		try {
			obj = msg.getObject( );
		} catch(JMSException e) {
			throw new XASWrongRequestException("Method getObject() failed",e);
		}
		if (obj instanceof SmsRequest3) return (SmsRequest3)obj;
		throw new XASWrongRequestException("ObjectMessage contains an Object of type " + obj.getClass().getName());
	}
	
}