package it.usi.xframe.xas.util;

import it.usi.xframe.xas.bfutil.Constants;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.bfutil.data.ENC_TYPE;

import java.io.PrintWriter;
import java.io.StringWriter;

import eu.unicreditgroup.e2e.crypto.library.CryptoLib;
import eu.unicreditgroup.e2e.crypto.library.exception.CryptoLibException;

public class Crypto {
	
	public static String encrypt(ENC_TYPE encryptType, String input, String[] encryptParams) throws XASException {
		
		String encrypted = null;
		if (input != null) {
			if (ENC_TYPE.ENCRYPT_SEG.equals(encryptType)) {
				try {
					encrypted = CryptoLib.encrypt(input, encryptParams);
		        } catch (CryptoLibException e) {
		        	StringWriter errors = new StringWriter();
		        	e.printStackTrace(new PrintWriter(errors));
		    		throw new XASException(Constants.XAS00097E_MESSAGE, errors.toString(), Constants.XAS00097E_CODE, new Object[] {"encrypt", CryptoLib.class.getName(), errors.toString()});
		        }
			} else throw new XASException(Constants.XAS00096E_MESSAGE, null, Constants.XAS00096E_CODE, new Object[] {"encrypt", encryptType.getValue()});

		}
        return encrypted;
	}

	public static String decrypt(ENC_TYPE encryptType, String input, String[] encryptParams) throws XASException {
		String decrypted = null;
		if (input != null) {
			if (ENC_TYPE.ENCRYPT_SEG.equals(encryptType)) {
				try {
					decrypted = CryptoLib.decrypt(input, encryptParams);
		        } catch (CryptoLibException e) {
		        	StringWriter errors = new StringWriter();
		        	e.printStackTrace(new PrintWriter(errors));
		    		throw new XASException(Constants.XAS00097E_MESSAGE, errors.toString(), Constants.XAS00097E_CODE, new Object[] {"decrypt", CryptoLib.class.getName(), errors.toString()});
		        }
			} else throw new XASException(Constants.XAS00096E_MESSAGE, null, Constants.XAS00096E_CODE, new Object[] {"decrypt", encryptType.getValue()});
		}
        return decrypted;
	}
}
