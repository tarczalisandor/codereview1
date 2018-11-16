package it.usi.xframe.xas.bfimpl.sms.providers.vodafonepop;

public class VodafoneRuntimeException extends RuntimeException
{
	/**
     * Serial Version.
     */
    private static final long serialVersionUID = 1L;

	public VodafoneRuntimeException(String message) {
		super(message);
	}

	public VodafoneRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
