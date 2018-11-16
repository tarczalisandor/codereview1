package it.usi.xframe.xas.bfimpl.sms.providers.vodafonepop;

public class VodafoneException extends Exception
{
	public VodafoneException(String message) {
		super(message);
	}

	public VodafoneException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
