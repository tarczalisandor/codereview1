package it.usi.xframe.xas.bfutil;

public class ConstantsEmail extends Constants
{
	/** Utility class. */
	private ConstantsEmail() {
		super();
	}

	public final static String MAILSRV_JNDI = "java:comp/env/mail/default";

	// profiles transfered into singleSecurityProfile
//	public static final String SENDEMAIL_ROLE = "SendEmail";
//	public static final String EXPORTEMAIL_ROLE = "ExportEmail";

	public static final String ADDRESS_EXCEPTION_MESSAGE = "XAS.SendEmail.invalidMailAddress";
	public static final String NAMING_EXCEPTION_MESSAGE = "XAS.SendEmail.jndiNameNotFound";
	public static final String MESSAGE_EXCEPTION_MESSAGE = "XAS.SendEmail.messageException";

	public static final String MESSAGE_OK_CODE = XAS00000I_CODE;
	public static final String MESSAGE_OK = "Message successfully sent";
	
	public static final String MESSAGE_ERROR_CODE = XAS00001E_CODE;

}
