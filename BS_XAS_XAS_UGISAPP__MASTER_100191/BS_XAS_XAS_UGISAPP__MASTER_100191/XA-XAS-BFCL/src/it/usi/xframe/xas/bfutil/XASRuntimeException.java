package it.usi.xframe.xas.bfutil;

public class XASRuntimeException extends RuntimeException
{
	/**
     * Serial Version.
     */
    private static final long serialVersionUID = 1L;

	public XASRuntimeException(String message) {
		super(message);
	}

	public XASRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public XASRuntimeException(Throwable cause) {
		super(cause);
	}
}
