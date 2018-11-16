/*
 * Created on Mar 18, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfimpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

/**
 * @author us01170
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BinaryDataSource implements DataSource {
	private String name;
	private String contentType;
	private byte[] data;

	/**
	 * 
	 * @param name
	 * @param stream
	 * @param contentType
	 */
	public BinaryDataSource(String name, byte[] data, String contentType) {
		this.name = name;
		this.contentType = contentType;
		this.data = data;
	}

	/**
	 * @see javax.activation.DataSource#getContentType()
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @see javax.activation.DataSource#getInputStream()
	 */
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(data);
	}

	/**
	 * @see javax.activation.DataSource#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see javax.activation.DataSource#getOutputStream()
	 */
	public OutputStream getOutputStream() throws IOException {
		throw new IOException("Not supported");
	}
}
