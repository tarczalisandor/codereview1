/*
 * Created on Jan 26, 2012
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl;

import it.usi.xframe.system.eservice.AbstractSupportServiceFacade;
import it.usi.xframe.xas.bfintf.IXasTestServiceFacade;
import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.ConstantsMonitor;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.security.MultiSecurityProfile;
import it.usi.xframe.xas.bfutil.security.SecurityUtility;
import it.usi.xframe.xas.bfutil.security.SingleSecurityProfile;

import java.io.IOException;
import java.rmi.RemoteException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.SmartLog;

/**
 * @author ee00681
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XasTestServiceFacade
	extends AbstractSupportServiceFacade
	implements IXasTestServiceFacade {

	private static Logger logger = LoggerFactory.getLogger(XasTestServiceFacade.class);
	
	private static final String HTTP_EXCEPTION_CODE     = "XAS.Monitor.HttpError";
	private static final String IO_EXCEPTION_CODE       = "XAS.Monitor.IOError";

	private static String crlf				= System.getProperty("line.separator");
	
	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfintf.IXasTestServiceFacade#testlog()
	 */
	public void testlog() throws RemoteException, XASException
	{
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfintf.IXasTestServiceFacade#testnolog()
	 */
	public void testnolog() throws RemoteException, XASException {
	}
	
	
	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfintf.IXasTestServiceFacade#testcommonlog()
	 */
	public void testcommonlog() throws RemoteException, XASException
	{
	}

	public String ping(String server) throws RemoteException, XASException
	{
		// Security Validation
		boolean isUserInRole = SecurityUtility.isUserInRole(getSupport(), new MultiSecurityProfile(SingleSecurityProfile.ADMIN));
		if(!isUserInRole)
			throw new XASException(ConstantsSms.XAS05015E_MESSAGE, null, ConstantsSms.XAS05015E_CODE,
					new Object[] {
						getSupport().getPrincipal().getName(),
						SingleSecurityProfile.ADMIN.getJavaProfileDescription(),
						"ping"
					});

		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param event
	 * @return
	 * @throws RemoteException
	 * @throws XASException
	 */
	public String eventMonitor(int event) throws RemoteException, XASException
	{
		// Security Validation
		boolean isUserInRole = SecurityUtility.isUserInRole(getSupport(), new MultiSecurityProfile(SingleSecurityProfile.ADMIN));
		if(!isUserInRole)
			throw new XASException(ConstantsSms.XAS05015E_MESSAGE, null, ConstantsSms.XAS05015E_CODE,
					new Object[] {
						getSupport().getPrincipal().getName(),
						SingleSecurityProfile.ADMIN.getJavaProfileDescription(),
						"execute eventMonitor"
					});

		// 		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
//    	.logItCompact(ConstantsMonitor.MY_APPL_ID, ConstantsMonitor.MY_LOG_VER, XasTestServiceFacade.class.getName(), smsRequest.getUuid(), SmartLog.V_SCOPE_DEBUG)
//		.logIt(SmartLog.K_METHOD, "eventMonitor").preset("default"); 
//
// 		logger.debug(sl.reload("default").logIt(SmartLog.K_STEP, "decrypting", SmartLog.K_PARAMS, smsRequest.getEncryptionType().getValue()).getLogRow());

		String result = null;
		String currentUrl= null;
		// URL di pagina statica per rilevare se il webserver è up&running (se recuperate 200 Ok significa che il webserver è attivo)
		String urlWeb = "https://timsms.unicreditgroup.eu/UnicreditFE/echo.jsp";
		// URL di pagina dinamica (response in formato json) Un esempio di risposta è :
		String urlAppJson = "https://timsms.unicreditgroup.eu/UnicreditFE/monitor/info";
		// URL di pagina dinamica con gli stessi indicatori della URL precedente dove però la risposta è fornita in una pagina HTML
		String urlAppHtml = "https://timsms.unicreditgroup.eu/UnicreditFE/webmonitor/info";

		switch(event)
		{
			case ConstantsMonitor.MONITOR_APPSERVERJSON:
				currentUrl= urlAppJson;
				break;
			case ConstantsMonitor.MONITOR_APPSERVERHTML:
				currentUrl= urlAppHtml;
				break;
			case ConstantsMonitor.MONITOR_WEBSERVER:
				currentUrl= urlWeb;
				break;
			default:
				throw new XASException("Option not available");
		}

		
		PostMethod method = new PostMethod(currentUrl);
		//method.addParameter("MSG", sms.getMsg());
		HttpClient client = new HttpClient();
		try
		{
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				StringBuffer err = new StringBuffer();
				err.append(crlf);
				err.append("Http Error calling: " + currentUrl + crlf);
				err.append("Http Status Code: " + statusCode + crlf);
				err.append("Http Error Description: " + HttpStatus.getStatusText(statusCode));
				throw new XASException(HTTP_EXCEPTION_CODE, err.toString());
			}
			result = new String(method.getResponseBody());
		}
		catch (HttpException e)
		{
			throw new XASException(HTTP_EXCEPTION_CODE, e);
		}
		catch (IOException e)
		{
			throw new XASException(IO_EXCEPTION_CODE, e);
		}
		catch (XASException e)
		{
			throw e;
		}
		finally {
			method.releaseConnection();
		}
		
		return result;
	}
}
