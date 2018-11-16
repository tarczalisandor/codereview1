package it.usi.xframe.xas.bfimpl.sms;

import it.usi.xframe.xas.bfimpl.a2psms.dataobject.InternalSmsRequest;
import it.usi.xframe.xas.bfimpl.sms.configuration.Client;
import it.usi.xframe.xas.bfimpl.sms.configuration.ClientsContainer;
import it.usi.xframe.xas.bfimpl.sms.configuration.XASConfigurationException;
import it.usi.xframe.xas.bfutil.XASWrongRequestException;

import java.util.regex.PatternSyntaxException;

public class SmsRequestPreprocessor {

	private InternalSmsRequest request;
	
	public SmsRequestPreprocessor(InternalSmsRequest request) {
		this.request = request;
	}
	
	public Client preprocess(ClientsContainer cc) throws XASConfigurationException, XASWrongRequestException {
		String xasUser = request.getNormalizedXasUser();
    	Client client = (Client) cc.getFinalClient(xasUser);

    	if (client.isValidatePhoneNumber()) {
    		if (! validatePhoneNumber(client.getValidatePhoneNumberRegEx())) {
    			throw new XASWrongRequestException("Phone number [" + request.getPhoneNumber() + "] must be formatted as [" + client.getValidatePhoneNumberRegEx() + "]");
    		}
    	} else {
    		normalizePhoneNumber();
    	}
    	if (client.isUseBlackList()) {
    		if (! checkBlackList()) {
    			throw new XASWrongRequestException("Phone number [" + request.getPhoneNumber() + "] is blacklisted! ");
    		}
    	}
    	return client;
	}
	
	public void normalizePhoneNumber()
	{
		String phoneNumber = request.getPhoneNumber();
		if (phoneNumber == null) return;
		StringBuffer normal = new StringBuffer();
		for (int i=0; i<phoneNumber.length(); ++i) {
			String a = phoneNumber.substring(i,i+1);
			if(a.equals(" ") || a.equals("-") || a.equals("(") || a.equals(")")) continue;
			else
				normal.append(a);
		}
		request.setPreNormalizedPhoneNumber(phoneNumber);
		request.setPhoneNumber(normal.toString());
	}
		
	/**
	 * Check for phone number not in black list.
	 * 
	 * @param phoneNumber to check for black list.
	 * @return boolean: true if number is blacklisted!!
	 */
	public boolean checkBlackList() throws XASConfigurationException {
		String phoneNumber = request.getPhoneNumber();
		String regex = "^(555.*)$"; // Black list mobile starting with 555
    	boolean foundMatch = false; 
		if (phoneNumber == null || regex == null || regex.length() == 0) {
			return foundMatch;
		}

    	try	{
    		foundMatch = phoneNumber.matches(regex);
    	} catch (PatternSyntaxException ex)	{
			throw new XASConfigurationException("NumberParseException: Syntax error in the regular expression");
    	}

    	return foundMatch;	
    }
	

	/**
	 * Validate the phone number.
	 * @param phoneNumber
	 */
	public boolean validatePhoneNumber (String regex) throws XASConfigurationException {
		String phoneNumber = request.getPhoneNumber();
		if (phoneNumber == null || regex == null || regex.length() == 0)
			return true;

    	boolean foundMatch = false; 
    	try {
    		foundMatch = phoneNumber.matches(regex);
    	} catch (PatternSyntaxException ex)	{
			throw new XASConfigurationException("NumberParseException: Syntax error in the regular expression");
    	}

    	return foundMatch;
	}


	
}
