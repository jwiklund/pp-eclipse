package pp.eclipse.open.parse;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import pp.eclipse.open.Item;
import pp.eclipse.open.dummy.Resource;

public class ParseTest {
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
		List<Item> content = parse(Resource.outputTemplate("", "test").content);
		assertEquals("test", content.get(0).externalid());
	}

	@Test
	public void parse_content_should_return_found_on_line()
		throws Exception
	{
		String xml = Resource.content("", "test").content;
        List<Item> content = parse(xml);
		assertEquals(xml, 7, content.get(0).line());
	}

	private List<Item> parse(String data)
		throws XMLStreamException, JAXBException
	{
		StringReader reader = new StringReader(data);
		List<Item> content = new StreamParser().parse(new BufferedReader(reader));
		return content;
	}
}
