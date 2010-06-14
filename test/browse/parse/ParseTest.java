package browse.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;

import org.junit.Before;
import org.junit.Test;

import browse.dummy.DummyResource;

public class ParseTest {
	
	private JAXBContext context;

	@Before
	public void setup()
		throws Exception
	{
		context = JAXBContext.newInstance(Content.class);
	}
	@Test
	public void verify_jaxb_context()
		throws Exception
	{
		StringWriter writer = new StringWriter();
		context.createMarshaller().marshal(new Content(new MetaData(new ContentId("test"))), writer);
		assertTrue(writer.toString(), writer.toString().matches(".*<content.*><metadata><contentid><externalid>test</externalid></contentid></metadata></content>"));
	}
	
	@Test
	public void parse_content_should_return_id()
		throws Exception
	{
		StringReader reader = new StringReader(DummyResource.content("", "test").content);
		List<Content> content = new ContentParser(context.createUnmarshaller()).parse(reader);
		assertEquals("test", content.get(0).metadata.contentid.externalid);
	}
}
