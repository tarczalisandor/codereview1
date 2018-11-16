package it.usi.xframe.xas.bfimpl.a2psms.configuration;

import it.usi.xframe.xas.bfimpl.a2psms.configuration.CustomizationAbstract;
import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XASException;

import java.io.UnsupportedEncodingException;

import com.ibm.ws.webservices.engine.encoding.Base64;

import eu.unicredit.xframe.slf.Aes;
import eu.unicredit.xframe.slf.SLF_Exception;


public class CustomizationBasic extends CustomizationAbstract {
	private String user = null;
	private String password = null;
	final static private String PROVIDER_ENCONDING = "ISO-8859-1";
	final static private String PROVIDER_ENCRYPTING_KEY = "DeepThought42";
	final static public int IX_GATEWAY_RESPONSE = 0;
	final static public int IX_SMS_STATUS = 1;
	final static public int IX_ERROR_MESSAGE = 2;

	public String getUser() {
    	return user;
    }
	public void setUser(String user) {
    	this.user = user;
    }
	public String getPassword() {
    	return password;
    }
	public void setPassword(String password) throws XASException {
    	this.password = password;
    }
	public String getDecryptedPassword() throws XASException {
        return decryptPassword(this.getPassword());
	}

	public boolean isSupportDeliveryReport() {
	    return false;
    }
	public boolean isSupportMobileOriginated() {
	    return false;
    }
	public boolean isSupportReplaceMap() {
	    return false;
    }
	
	/**
	 * Decrypt and encrypted Password.
	 * 
	 * Please use XAS_SLF_JAVAWEB\AesTest.java\test_aes_password() to encrypt.
	 * @param password
	 * @return
	 * @throws XASException
	 */
	static public String decryptPassword(String password) throws XASException {
		String decryptedPassword = null;
		try {
    		String binary = new String(Base64.decode(password), PROVIDER_ENCONDING);
    		decryptedPassword = Aes.decrypt(binary, PROVIDER_ENCRYPTING_KEY, 256);
        } catch (UnsupportedEncodingException e) {
        	throw new XASException(ConstantsSms.XAS04098E_MESSAGE, null, ConstantsSms.XAS04098E_CODE, new Object[] {"Error Encoding Password - Unsupported", ""});
        } catch (SLF_Exception e) {
        	throw new XASException(ConstantsSms.XAS04098E_MESSAGE, null, ConstantsSms.XAS04098E_CODE, new Object[] {"Error Encoding Password - SLF_Exception", ""});
        }
        return decryptedPassword;
		
	}
}