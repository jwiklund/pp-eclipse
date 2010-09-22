package pp.eclipse.open;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class HashTest {

	private Hasher target = new Hasher();

	@Test
	public void hashFormat() throws Exception {
		assertEquals("a17c9aaa61e80a1bf71d0d850af4e5baa9800bbd", target.hash(stream("data")));
	}

	private InputStream stream(String string) throws UnsupportedEncodingException {
		return new ByteArrayInputStream(string.getBytes("UTF8"));
	}
}
