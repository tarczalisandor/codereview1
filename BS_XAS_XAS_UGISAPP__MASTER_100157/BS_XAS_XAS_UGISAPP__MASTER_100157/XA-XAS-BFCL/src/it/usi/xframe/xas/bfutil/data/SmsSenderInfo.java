package it.usi.xframe.xas.bfutil.data;

import java.io.Serializable;

/**
 * Information about sender entity.
 * This information is used mainly for statistical/billing purposes.
 * 
 * @author us00081
 *
 */
public class SmsSenderInfo implements Serializable {

	// Alphanumeric string displayed on mobile phone as sender
	// Ex. 'Unicredit'
	private String alias;
	
	// Sender ABI code
	// Ex. '02008' (Unicredit) or '06170' (Fossano)
	private String ABICode;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getABICode() {
		return ABICode;
	}

	public void setABICode(String aBICode) {
		ABICode = aBICode;
	}

	public String toString() {
	    // TODO Auto-generated method stub
	    return "{\"alias\":\"" + alias + "\", \"ABICode\":\"" + ABICode + "\"}";
    }
	
	
	
}
