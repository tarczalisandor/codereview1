package it.usi.xframe.xas.jaxrpc.auth;

public interface IUsernameTokenCache {
	
	public UsernameToken getUsernameToken(String profile);
	
	public void setUsernameToken(String profile, UsernameToken ut);
	
}
