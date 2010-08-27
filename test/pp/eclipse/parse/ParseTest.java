package pp.eclipse.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import pp.eclipse.domain.ExternalId;
import pp.eclipse.dummy.Resource;
import pp.eclipse.parse.ContentParser;
import pp.eclipse.parse.content.Content;
import pp.eclipse.parse.content.ContentId;
import pp.eclipse.parse.content.MetaData;


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
		List<ExternalId> content = parse(Resource.content("", "test").content);
		assertEquals("test", content.get(0).externalid());
	}
	
	@Test
	public void parse_content_should_return_found_on_line()
		throws Exception
	{
		List<ExternalId> content = parse(Resource.content("", "test").content);
		assertEquals(3, content.get(0).line());
	}
	
	@Test
	public void parse_content_should_skip_input_templates()
		throws Exception
	{
		List<ExternalId> content = parse(Resource.inputTemplate("", "test").content);
		assertEquals(0, content.size());
	}
	
	private List<ExternalId> parse(String data)
		throws XMLStreamException, JAXBException 
	{
		StringReader reader = new StringReader(data);
		List<ExternalId> content = new ContentParser(context.createUnmarshaller()).parse(new BufferedReader(reader));
		return content;
	}
}
