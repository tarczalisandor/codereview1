package it.usi.xframe.xas.app;

import it.usi.xframe.xas.bfutil.security.MultiSecurityProfile;


public class ApplicationMethod
{
	private String name = null;
	private String title = null;
	private MultiSecurityProfile applicationProfile = null;
	
	public ApplicationMethod (String name, String title, MultiSecurityProfile applicationProfile)
	{
		this.name = name;
		this.title = title;
		this.applicationProfile = applicationProfile;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public MultiSecurityProfile getApplicationProfile()
	{
		return this.applicationProfile;
	}
	public void setSecurityProfile(MultiSecurityProfile applicationProfile)
	{
		this.applicationProfile = applicationProfile;
	}
}
