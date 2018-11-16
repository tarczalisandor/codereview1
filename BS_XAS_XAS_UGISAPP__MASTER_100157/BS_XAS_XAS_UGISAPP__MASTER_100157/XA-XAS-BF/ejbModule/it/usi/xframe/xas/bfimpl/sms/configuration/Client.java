package it.usi.xframe.xas.bfimpl.sms.configuration;



public class Client
{
	private String mName                     = null;
	private String originator                = null;
	private String mAlias                    = null;
	private boolean mNormalizePhoneMessage   = false;
	private boolean mUseBlackList		     = false;
	private boolean mValidatePhoneNumber     = false;
	private String mValidatePhoneNumberRegEx = null;
	private String mDescription              = null;
	private Integer defaultValidity          = null;
		
	public Client clone(String name)
	{
		Client c = new Client();
		c.setName(name);
		//c.setDescription(description);
		c.setAlias(this.getAlias());
		c.setNormalizePhoneMessage(this.isNormalizePhoneMessage());
		c.setValidatePhoneNumber(this.isValidatePhoneNumber());
		c.setValidatePhoneNumberRegEx(this.getValidatePhoneNumberRegEx());
		c.setOriginator(this.originator);
		c.setDefaultValidity(this.defaultValidity);
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

	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
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
	public Integer getDefaultValidity() {
		return defaultValidity;
	}

	public void setDefaultValidity(Integer defaultValidity) {
		this.defaultValidity = defaultValidity;
	}
	
}
