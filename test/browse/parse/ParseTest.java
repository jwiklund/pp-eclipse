package browse.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

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
		List<Content> content = parse(DummyResource.content("", "test").content);
		assertEquals(new ContentId("test"), content.get(0).metadata.contentid);
	}
	
	@Test
	public void parse_content_should_return_found_on_line()
		throws Exception
	{
		List<Content> content = parse(DummyResource.content("", "test").content);
		assertEquals(3, content.get(0).foundOnLine);
	}
	
	@Test
	public void parse_content_should_skip_input_templates()
		throws Exception
	{
		List<Content> content = parse(DummyResource.inputTemplate("", "test").content);
		assertEquals(0, content.size());
	}
	
	private List<Content> parse(String data)
		throws XMLStreamException, JAXBException 
	{
		StringReader reader = new StringReader(data);
		List<Content> content = new ContentParser(context.createUnmarshaller()).parse(reader);
		return content;
	}
}
