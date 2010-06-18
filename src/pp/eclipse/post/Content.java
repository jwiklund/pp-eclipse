package pp.eclipse.post;

import java.io.InputStream;

public class Content {

	public final InputStream stream;
	public final String charset;
	
	public Content(InputStream stream, String charset) {
		this.stream = stream;
		this.charset = charset;
	}
}
