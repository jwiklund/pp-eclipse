package pp.eclipse.post;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class ContentImporter {

	public ImportResult importContent(Content content, Host host) 
		throws MalformedURLException, IOException 
	{
		HttpURLConnection connection = (HttpURLConnection) host.createURL().openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("PUT");
		connection.setRequestProperty("Content-Type", "text/xml&charset="+content.charset);
		connection.setUseCaches(false);
		OutputStream writeTo = connection.getOutputStream();
		flush(content.stream, writeTo);
		writeTo.close();
		
		int result = connection.getResponseCode();
		InputStream readFrom;
		if (result >= 200 && result <= 300) {
		    readFrom = connection.getInputStream();
		} else {
		    readFrom = connection.getErrorStream();
		}
		ImportResult importResult = new ImportResult(result, read(readFrom, connection.getContentEncoding()));
		connection.disconnect();
		return importResult;
	}

	private String read(InputStream readFrom, String encoding) throws IOException 
	{
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		flush(readFrom, writer);
		if (encoding == null) {
			encoding = "UTF8";
		}
		return new String(writer.toByteArray(), encoding);
	}

	private void flush(InputStream stream, OutputStream writeTo) throws IOException {
		byte[] bytes = new byte[1024];
		int len;
		
		while ((len = stream.read(bytes)) > 0) {
			writeTo.write(bytes, 0, len);
		}
		
		writeTo.flush();
	}

	
}
