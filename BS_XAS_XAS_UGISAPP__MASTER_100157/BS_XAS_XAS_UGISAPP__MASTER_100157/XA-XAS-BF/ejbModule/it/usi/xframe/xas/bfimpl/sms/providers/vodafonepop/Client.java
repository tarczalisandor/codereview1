package it.usi.xframe.xas.bfimpl.sms.providers.vodafonepop;

import it.usi.xframe.xas.bfutil.XASException;

import java.util.ArrayList;


public class Client
{
	private String mName                     = null;
	private ArrayList mAccounts              = new ArrayList();
	private String mAlias                    = null;
	private boolean mNormalizePhoneMessage   = false;
	private boolean mUseBlackList		     = false;
	private boolean mValidatePhoneNumber     = false;
	private String mValidatePhoneNumberRegEx = null;
	private String mDescription              = null;
	private Integer mValidityPeriod 		 = null; // #369 
	private String mPrefix		             = null; // #370
	private String mPrefixRE	             = null; // #370
		
	public Client clone(String name)
	{
		Client c = new Client();
		c.setName(name);
		//c.setDescription(description);
		c.setAlias(this.getAlias());
		c.setNormalizePhoneMessage(this.isNormalizePhoneMessage());
		c.setValidatePhoneNumber(this.isValidatePhoneNumber());
		c.setValidatePhoneNumberRegEx(this.getValidatePhoneNumberRegEx());
		ArrayList a = this.getAccounts();
		for(int x=0; x<a.size(); x++)
            c.addAccount((String) a.get(x));
		return c;
	}

	public String getName() {
		return mName;
	}
	public void setName(String name) {
		this.mName = name;
	}

	public String getAlias() {
		return mAlias;
	}

	public void setAlias(String alias) {
		this.mAlias = alias;
	}

	public ArrayList getAccounts() {
		return mAccounts;
	}

	public void setAccounts(ArrayList accounts) {
		mAccounts = accounts;
	}

	public int getAccountsSize() {
		return mAccounts.size();
	}

	public void addAccount(String systemId) {
		mAccounts.add(systemId);
	}

	public String getAccount(int index) {
		return (String) mAccounts.get(index);
	}

	public String getRandomAccount() throws XASException{
		if(mAccounts.size()==0)
			throw new XASException("No account defined for client=" + this.getName());
		int total = mAccounts.size();
		double ran = Math.random() * (total-1);
		int index = (int)Math.round(ran);
		return (String) mAccounts.get(index);
	}

	public boolean isPresentAccount(String systemId) {
		for(int i=0; i<mAccounts.size(); i++) {
			MamAccount account = (MamAccount) mAccounts.get(i);
			if(account.getSystemId().equals(systemId))
				return true;
		}
		return false;
	}

	public boolean isNormalizePhoneMessage() {
		return mNormalizePhoneMessage;
	}
	public void setNormalizePhoneMessage(boolean normalizePhoneMessage) {
		this.mNormalizePhoneMessage = normalizePhoneMessage;
	}

	public boolean isUseBlackList() {
		return mUseBlackList;
	}
	public void setUseBlackList(boolean useBlackList) {
		this.mUseBlackList = useBlackList;
	}

	public boolean isValidatePhoneNumber() {
		return mValidatePhoneNumber;
	}
	public void setValidatePhoneNumber(boolean validatePhoneNumber) {
		this.mValidatePhoneNumber = validatePhoneNumber;
	}
	
	public String getValidatePhoneNumberRegEx() {
		return mValidatePhoneNumberRegEx;
	}
	public void setValidatePhoneNumberRegEx(String validatePhoneNumberRegEx) {
		this.mValidatePhoneNumberRegEx = validatePhoneNumberRegEx;
	}

	public String getDescription() {
		return mDescription;
	}
	public void setDescription(String description) {
		this.mDescription = description;
	}
	public Integer getValidityPeriod() {
    	return mValidityPeriod;
    }

	public void setValidityPeriod(Integer validityPeriod) {
    	this.mValidityPeriod = validityPeriod;
    }

	public String getPrefix() {
		return mPrefix;
	}
	public void setPrefix(String Prefix) {
		this.mPrefix = Prefix;
	}
	public String getPrefixRE() {
		return mPrefixRE;
	}
	public void setPrefixRE(String PrefixRE) {
		this.mPrefixRE = PrefixRE;
	}
}
