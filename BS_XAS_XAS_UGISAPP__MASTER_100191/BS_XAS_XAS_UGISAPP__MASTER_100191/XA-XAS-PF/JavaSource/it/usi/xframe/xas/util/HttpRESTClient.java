package it.usi.xframe.xas.util;


import java.io.IOException;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpRESTClient
{
	private static Log log = LogFactory.getLog(HttpRESTClient.class);
	private static String host = SplunkDataConstant.HOSTNAME;
	private static int port = SplunkDataConstant.PORT;
	private static boolean dump = true;

	public static String postRESTHttp(String requestURI,String uuid, NameValuePair[] data) throws Exception
	{
		if(dump) {
			log.info("Request URI (" + requestURI + "):");
		}
		// Creo una connessione SSL abilitando il protocol TLSv1.2 se non attivo
		setTLSforSplunk();
		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
		String connectionString = host + ":" + port + requestURI;
		if(dump) {
			log.info("connectionString : (" + connectionString + ")");
		}

		// Create a method instance.
		PostMethod method = new PostMethod(connectionString);
		if(dump) {
			log.info("Connection Path : " + method.getPath());
			log.info("Connection Name : " + method.getName());
		}
		
       if (data != null )
			method.setRequestBody(data);  
		HttpMethodParams httpParams = new HttpMethodParams();
		httpParams.setVersion(HttpVersion.HTTP_1_1);
        method.setParams(httpParams);
				
		String userPassword = SplunkDataConstant.USERNAME + SplunkDataConstant.SEPARATOR + SplunkDataConstant.PASSWORD;
		byte[] encodeBase64 = Base64.encodeBase64(userPassword.getBytes());
		
		method.setRequestHeader("Authorization", "BASIC " + new String(encodeBase64));
		method.setRequestHeader("Cache-Control", "no store, no-cache, must-revalidate,max-age=0");
		method.setRequestHeader("Pragma", "no-cache");
		method.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");
		method.setRequestHeader("Accept", "*/*");
		method.setRequestHeader("Connection", "Keep-Alive");
		method.setRequestHeader("Date", String.valueOf(new Date())); 

		String rstream = null;
		try
		{
			int result = client.executeMethod(method);
			log.info("Result from Splunk call : " + result);
			// test server result
			if(result==HttpStatus.SC_OK || result == HttpStatus.SC_CREATED)
			{
				rstream = method.getResponseBodyAsString();
//				rstream = method.getResponseBodyAsStream());
			}
				if(dump) {
					log.info("POST Splunk Response : OK");
					log.info("Request  char set : " + method.getRequestCharSet());
					log.info("Response char set : " + method.getResponseCharSet());
					log.info("Response Length   : " + method.getResponseContentLength());
			}
			else if(result>=400)
			{
				throw new Exception("Remote server " + connectionString + " answered with code:" + result);
			}
		} catch (HttpException e) {
			log.error("Fatal protocol violation: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
			throw new Exception(e); 
		} catch (IOException e) {
			log.error("Fatal transport error: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
			throw new Exception(e); 
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw new Exception(e); 
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		return rstream;

	}

	public static String getRESTHttp(String requestURI, String uuid , NameValuePair[] data) throws Exception
	{
		String rstream = null;
		if(dump) {
			log.info("requestURI (" + requestURI + ")");
		}
		// Creo una connessione SSL abilitando il protocol TLSv1.2 se non attivo
		setTLSforSplunk();
		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
		String connectionString = host + ":" + port + requestURI;
		if(dump) 
			log.info("connectionString : (" + connectionString + ")");

		// Create a method instance.
		GetMethod method = new GetMethod(connectionString);
		if(dump) {
			log.info("Connection Path : " + method.getPath());
			log.info("Connection Name : " + method.getName());
		}
        if (data != null )
			method.setQueryString(data);  
		HttpMethodParams httpParams = new HttpMethodParams();
		httpParams.setVersion(HttpVersion.HTTP_1_1);
        method.setParams(httpParams);

		String userPassword = SplunkDataConstant.USERNAME + SplunkDataConstant.SEPARATOR + SplunkDataConstant.PASSWORD;
		byte[] encodeBase64 = Base64.encodeBase64(userPassword.getBytes());
		
		method.setRequestHeader("Authorization", "BASIC " + new String(encodeBase64));
		method.setRequestHeader("Cache-Control", "no-cache");
		method.setRequestHeader("Pragma", "no-cache");
		method.setRequestHeader("Accept", "*/*");
		method.setRequestHeader("Content-Type", method.getResponseCharSet());
		if(dump) 
			log.info("Query String  : " + method.getQueryString());
		try
		{
			int result = client.executeMethod(method);
			log.info("Result from Splunk call : " + result);
			// test server result
			if(result < 400 )
			{
				rstream = method.getResponseBodyAsString();
//				rstream = method.getResponseBodyAsStream());
				if(dump) {
					log.info("GET Splunk Response : OK");
					log.info("Request  char set : " + method.getRequestCharSet());
					log.info("Response char set : " + method.getResponseCharSet());
					log.info("Response Length   : " + method.getResponseContentLength());
				}
			}
			else if(result>=400)
			{
				throw new Exception("Remote server " + connectionString + " answered with code:" + result);
			}
			// Read the response body.
			//byte[] responseBody = method.getResponseBody();
		} catch (HttpException e) {
			log.error("Fatal protocol violation: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
			throw new Exception(e); 
		} catch (IOException e) {
			log.error("Fatal transport error: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
			throw new Exception(e); 
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw new Exception(e); 
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		return rstream;

	}

	public static String deleteRESTHttp(String requestURI, String uuid , NameValuePair[] data) throws Exception
	{
		String rstream = null;
		if(dump) {
			log.info("requestURI (" + requestURI + ")");
		}
		// Creo una connessione SSL abilitando il protocol TLSv1.2 se non attivo
		setTLSforSplunk();
		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
		String connectionString = host + ":" + port + requestURI;
		if(dump) 
			log.info("connectionString : (" + connectionString + ")");

		// Create a method instance.
		DeleteMethod method = new DeleteMethod(connectionString);
		if(dump) {
			log.info("Connection Path : " + method.getPath());
			log.info("Connection Name : " + method.getName());
		}
        if (data != null )
			method.setQueryString(data);  

		HttpMethodParams httpParams = new HttpMethodParams();
		httpParams.setVersion(HttpVersion.HTTP_1_1);
        method.setParams(httpParams);

		String userPassword = SplunkDataConstant.USERNAME + SplunkDataConstant.SEPARATOR + SplunkDataConstant.PASSWORD;
		byte[] encodeBase64 = Base64.encodeBase64(userPassword.getBytes());
		
		method.setRequestHeader("Authorization", "BASIC " + new String(encodeBase64));
		method.setRequestHeader("Cache-Control", "no-cache");
		method.setRequestHeader("Pragma", "no-cache");
		method.setRequestHeader("accept", "application/xml");
		method.setRequestHeader("Content-Type", method.getResponseCharSet());
		if(dump) 
			log.info("Query String  : " + method.getQueryString());
		try
		{
			int result = client.executeMethod(method);
			log.info("Result from Splunk call : " + result);
			// test server result
			if(result < 400 )
			{
				rstream = method.getResponseBodyAsString();
//				rstream = method.getResponseBodyAsStream());
				if(dump){ 
					log.info("DELETE Splunk Response : OK");
					log.info("Request  char set : " + method.getRequestCharSet());
					log.info("Response char set : " + method.getResponseCharSet());
					log.info("Response Length   : " + method.getResponseContentLength());
				}
			}
			else if(result>=400)
			{
				throw new Exception("Remote server " + connectionString + " answered with code:" + result);
			}
			// Read the response body.
			//byte[] responseBody = method.getResponseBody();
		} catch (HttpException e) {
			log.error("Fatal protocol violation: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
			throw new Exception(e); 
		} catch (IOException e) {
			log.error("Fatal transport error: " + e.getMessage());
			log.error(e);
			e.printStackTrace();
			throw new Exception(e); 
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw new Exception(e); 
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		return rstream;

	}
	
	/*
	 * Method to force the protocol TLSv1.2 to call Splunk Server
	 */
	private static void setTLSforSplunk(){
		Protocol baseHttps = Protocol.getProtocol(SplunkDataConstant.SCHEMA);
		ProtocolSocketFactory baseFactory = baseHttps.getSocketFactory();
		ProtocolSocketFactory customFactory = new CustomHttpsSocketFactory(baseFactory);
		Protocol customHttps = new Protocol(SplunkDataConstant.SCHEMA, customFactory, baseHttps.getDefaultPort());
		Protocol.registerProtocol(SplunkDataConstant.SCHEMA, customHttps); 
	}

}




