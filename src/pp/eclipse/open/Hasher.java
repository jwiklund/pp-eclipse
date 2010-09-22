package pp.eclipse.open;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {

	MessageDigest digester;
	
	public Hasher() {
		try {
			digester = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String hash(InputStream is) throws IOException 
	{
		byte[] arr = new byte[1024];
		int length;
		digester.reset();
		while ((length = is.read(arr)) != -1) {
			digester.update(arr, 0, length);
		}
		return toString(digester.digest());
	}
	
	public String hash(String content) throws IOException
	{
		return hash(new ByteArrayInputStream(content.getBytes("UTF8")));
	}

	static String toString(byte[] digest) {
		StringBuffer sb = new StringBuffer(digest.length * 2);
		for (byte d : digest) {
			sb.append(toChar((d & 0xF0) >>> 4));
			sb.append(toChar(d & 0xF));
		}
		return sb.toString();
	}

	static char toChar(int hex) {
		char c;
		if (hex < 10) c = (char) ('0' + hex);
		else c = (char) ('a' + (hex-10));
		return c;
	}
}
