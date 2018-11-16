package it.usi.xframe.xas.servlet;

import it.usi.xframe.system.eservice.ServiceFactoryException;
import it.usi.xframe.xas.bfintf.IXasSendsmsServiceFacade;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XasServiceFactory;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;

import eu.unicredit.xframe.slf.SmartLog;
import eu.unicredit.xframe.slf.UUID;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Servlet implementation class Initialization
 * 
 * Used to dump configuration file in the log
 */
public class ServletInitialization extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(ServletInitialization.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletInitialization() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {
	    super.init();
		MDC.put(ConstantsSms.MY_UUID_KEY, UUID.randomUUID().toString());
		SmartLog sl = new SmartLog().logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER,
				"it.usi.xframe.xas.servlet.Sms", (String) MDC.get(ConstantsSms.MY_UUID_KEY),
				SmartLog.V_SCOPE_DEBUG);
		IXasSendsmsServiceFacade service;
        try {
	        service = XasServiceFactory.getInstance().getXasSendsmsServiceFacade();
			String configuration = service.getConfiguration("cache,json");
			// LOG THE CONFIGURATION 
			sl.logIt("a_configuration", configuration); // LOG THE CONFIGURATION 
			logger.info(sl.getLogRow());				// LOG THE CONFIGURATION
			// LOG THE CONFIGURATION 
        } catch (ServiceFactoryException e) {
	        e.printStackTrace();
        } catch (RemoteException e) {
	        e.printStackTrace();
        }
    }
}
