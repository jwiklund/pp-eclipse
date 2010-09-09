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

import pp.eclipse.dummy.Resource;
import pp.eclipse.open.Item;
import pp.eclipse.open.parse.Parser;
import pp.eclipse.open.parse.content.Content;
import pp.eclipse.open.parse.content.ContentId;
import pp.eclipse.open.parse.content.MetaData;
import pp.eclipse.open.parse.template.InputTemplate;
import pp.eclipse.open.parse.template.OutputTemplate;


public class ParseTest {
	
	private JAXBContext context;

	@Before
	public void setup()
		throws Exception
	{
		context = JAXBContext.newInstance(Content.class, InputTemplate.class, OutputTemplate.class); 
	}
	@Test
	public void verify_jaxb_context_content()
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
		List<Item> content = parse(Resource.content("", "test").content);
		assertEquals("test", content.get(0).externalid());
	}
	
	@Test
	public void parse_input_template_should_return_id()
		throws Exception
	{
		List<Item> content = parse(Resource.inputTemplate("", "test").content);
		assertEquals("test", content.get(0).externalid());
	}
	
	@Test
	public void parse_output_template_should_return_id()
		throws Exception
	{
		List<Item> content = parse(Resource.inputTemplate("", "test").content.replaceAll("input-template", "output-template"));
		assertEquals("test", content.get(0).externalid());
	}
	
	@Test
	public void parse_content_should_return_found_on_line()
		throws Exception
	{
		List<Item> content = parse(Resource.content("", "test").content);
		assertEquals(3, content.get(0).line());
	}
	
	private List<Item> parse(String data)
		throws XMLStreamException, JAXBException 
	{
		StringReader reader = new StringReader(data);
		List<Item> content = new Parser().parse(new BufferedReader(reader));
		return content;
	}
}
