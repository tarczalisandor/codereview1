package it.usi.xframe.xas.util;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocket;

import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CustomHttpsSocketFactory implements SecureProtocolSocketFactory {

	private final SecureProtocolSocketFactory base;
	private static Log log = LogFactory.getLog(CustomHttpsSocketFactory.class);
	
	public CustomHttpsSocketFactory(ProtocolSocketFactory base) {
		if (base == null || !(base instanceof SecureProtocolSocketFactory))
			throw new IllegalArgumentException();
		this.base = (SecureProtocolSocketFactory) base;
	}

	private Socket stripSSLv3(Socket socket) {
        if (!(socket instanceof SSLSocket))
            return socket;
        boolean tls12 = false;
        SSLSocket sslSocket = (SSLSocket) socket;
        String [] enabledProtocol = sslSocket.getEnabledProtocols();
        for (int i=0; i<enabledProtocol.length; i++){
            if (enabledProtocol[i] != null && enabledProtocol[i].length()>0 ){
            	if (enabledProtocol[i].equals (SplunkDataConstant.TLS12))
            		tls12 = true;
            }
        }
        if (!tls12 ) {
        	String[] newEnabledProtocol = this.add(enabledProtocol, SplunkDataConstant.TLS12);
        	sslSocket.setEnabledProtocols(newEnabledProtocol);
//            String [] ep = sslSocket.getEnabledProtocols();
//    		for (int i=0; i<ep.length; i++){
//    			System.out.println("stripSSLv3 New Protocol enabled : " + ep[i]);
//    		}
        }
        return sslSocket;
    }

	private String[] add(String[] originalArray, String newItem)
	{
	    int currentSize = originalArray.length;
	    int newSize = currentSize + 1;
	    String[] tempArray = new String[ newSize ];
	    for (int i=0; i < currentSize; i++)
	    {
	        tempArray[i] = originalArray [i];
	    }
	    tempArray[newSize- 1] = newItem;
	    return tempArray;   
	}
	
	private Socket acceptOnlyTLS12(Socket socket) {
		if (!(socket instanceof SSLSocket))
			return socket;
		SSLSocket sslSocket = (SSLSocket) socket;
		String [] enabledProtocol = sslSocket.getEnabledProtocols();
		for (int i=0; i<enabledProtocol.length; i++){
			log.debug("Protocol enabled : " + enabledProtocol[i]);
		}
		String [] suppProtocol = sslSocket.getSupportedProtocols();
		for (int i=0; i<suppProtocol.length; i++){
			log.debug("Protocol Supported : " + suppProtocol[i]);
		}
		sslSocket.setEnabledProtocols(new String[] { "TLSv1.2" });
		return sslSocket;
	}

	public Socket createSocket(String host, int port) throws IOException {
		return stripSSLv3(base.createSocket(host, port));
	}

	public Socket createSocket(String host, int port, InetAddress localAddress,
			int localPort) throws IOException {
		return stripSSLv3(base.createSocket(host, port, localAddress,
				localPort));
	}

	public Socket createSocket(String host, int port, InetAddress localAddress,
			int localPort, HttpConnectionParams params) throws IOException {
		return stripSSLv3(base.createSocket(host, port, localAddress,
				localPort, params));
	}

	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException {
		return stripSSLv3(base.createSocket(socket, host, port, autoClose));
	}

}
