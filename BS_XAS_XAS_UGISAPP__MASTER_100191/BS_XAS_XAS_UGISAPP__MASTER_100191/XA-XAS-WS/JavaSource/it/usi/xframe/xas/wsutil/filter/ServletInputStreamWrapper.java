package it.usi.xframe.xas.wsutil.filter;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;

/**
 * @author EE01995
 */
public class ServletInputStreamWrapper extends ServletInputStream {

	private ByteArrayInputStream bais = null;

	public ServletInputStreamWrapper(ByteArrayInputStream arg) {
		this.bais = arg;
	}

	public int read() throws IOException {
		return bais.read();
	}

}
