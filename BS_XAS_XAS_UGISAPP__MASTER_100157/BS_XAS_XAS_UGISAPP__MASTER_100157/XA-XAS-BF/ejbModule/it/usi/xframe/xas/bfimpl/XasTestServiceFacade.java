/*
 * Created on Jan 26, 2012
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl;

import it.usi.xframe.system.eservice.AbstractSupportServiceFacade;
import it.usi.xframe.xas.bfintf.IXasTestServiceFacade;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.XAS_Message;

import java.rmi.RemoteException;

/**
 * @author ee00681
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XasTestServiceFacade
	extends AbstractSupportServiceFacade
	implements IXasTestServiceFacade {

	private it.usi.xframe.system.logging.elf.Logger logElf = it.usi.xframe.system.logging.elf.Logger.getInstance();
	private static org.apache.commons.logging.Log logCom = org.apache.commons.logging.LogFactory.getLog(XasTestServiceFacade.class);

	private static final int MAXCOUNT   = 100; 
	private static final int LOG_ELF    = 1; 
	private static final int LOG_COMMON = 2; 
	private static final int LOG_NONE   = 3; 

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfintf.IXasTestServiceFacade#testlog()
	 */
	public void testlog() throws RemoteException, XASException
	{
		this.compute(LOG_ELF);
	}

	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfintf.IXasTestServiceFacade#testnolog()
	 */
	public void testnolog() throws RemoteException, XASException {
		this.compute(LOG_NONE);
	}
	
	
	/* (non-Javadoc)
	 * @see it.usi.xframe.xas.bfintf.IXasTestServiceFacade#testcommonlog()
	 */
	public void testcommonlog() throws RemoteException, XASException
	{
		this.compute(LOG_COMMON);
	}

	public void compute(int logSystem) throws RemoteException, XASException
	{
		switch(logSystem)
		{
			case LOG_ELF:
				logElf.debug("Entering myMethod: s={0}, i={1}", "testlog", "and now some words");
				logElf.debug("some words");
			break; 
			case LOG_COMMON: 
				logCom.debug("Entering myMethod: s={" + "testlog" + "}, i={" + "and now some words" + "}");
				logCom.debug("some words");
			break; 
			case LOG_NONE: 
			break; 
		}

		Integer dividendo = new Integer(100);
		Integer divisore = new Integer(0);
		Integer quoziente = null;

		try
		{
			switch(logSystem)
			{
				case LOG_ELF:
					logElf.emit(XAS_Message.MSG_XAS00001I, "testlog", "and now some words");
					logElf.emit(XAS_Message.MSG_XAS00002I, quoziente, dividendo.toString(), divisore.toString());
				break; 
				case LOG_COMMON: 
					logCom.info(XAS_Message.MSG_XAS00001I + "testlog " + "and now some words");
					logCom.info(XAS_Message.MSG_XAS00002I + " " + quoziente +" "+ dividendo.toString() +" "+ divisore.toString());
				break; 
				case LOG_NONE: 
				break; 
			}
			quoziente = new Integer(dividendo.intValue() / divisore.intValue());
		}
		catch (Exception e)
		{
			switch(logSystem)
			{
				case LOG_ELF:
					logElf.emit(XAS_Message.MSG_XAS00003E, new Object[]{quoziente, dividendo.toString(), divisore.toString(), e});
				break; 
				case LOG_COMMON: 
					logCom.error(XAS_Message.MSG_XAS00003E +" "+ quoziente +" "+ dividendo.toString() +" "+ divisore.toString(), e);
				break; 
				case LOG_NONE: 
				break; 
			}
		}
		finally
		{
			for (int i = 0; i<MAXCOUNT; i++)
			{
				switch(logSystem)
				{
					case LOG_ELF:
						logElf.debug("DEBUG: Sequential message i=" + i + " type=Type One");
						logElf.emit(XAS_Message.MSG_XAS00005I, new Integer(i), "Type Two");
					break; 
					case LOG_COMMON: 
						logCom.debug("DEBUG: Sequential message i=" + i + " type=Type One");
						logCom.info("INFO: Sequential message i=" + i + " type=" + "Type Two");
					break; 
					case LOG_NONE: 
					break; 
				}
			}
		}
	}

}
