package pp.eclipse.open;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import pp.eclipse.open.dummy.Resource;
import pp.eclipse.open.parse.Parser.ParserResult;
import pp.eclipse.open.parse.StreamParser;

public class ParseTest {
	@Test
	public void parse_content_should_return_id()
		throws Exception
	{
		List<Item> content = parse(Resource.content("", "test").content).items;
		assertEquals("test", content.get(0).externalid());
	}

	@Test
	public void parse_input_template_should_return_id()
		throws Exception
	{
		List<Item> content = parse(Resource.inputTemplate("", "test").content).items;
		assertEquals("test", content.get(0).externalid());
	}

	@Test
	public void parse_output_template_should_return_id()
		throws Exception
	{
		List<Item> content = parse(Resource.outputTemplate("", "test").content).items;
		assertEquals("test", content.get(0).externalid());
	}

	@Test
	public void parse_it_should_return_policy()
	    throws Exception
	{
	    ParserResult result = parse(Resource.inputTemplate("", "test", Resource.policy("test.policy")).content);
	    assertEquals(String.valueOf(result.references),
	            Collections.singleton("test.policy"),
	            names(result.references.get(ItemType.InputTemplate.getName("test"))));
	}

//	@Test
//	public void parse_it_should_continue_parsing_after_reading_references()
//	    throws Exception
//	{
//	   Entry entry = templateDefinition("", inputTemplate("test", policy("test.policy")), inputTemplate("test2"));
//	   ParserResult result = parse(entry);
//	   assertEquals(String.valueOf(result.))
//	}

	@Test
	public void parse_it_should_return_editors_and_viewers_from_different_contexts()
	    throws Exception
	{
	    ParserResult result = parse(Resource.inputTemplate("", "test",
	            Resource.viewer("test.widget"), Resource.editor("orchid_search", "test.editorwidget")).content);
        assertEquals(String.valueOf(result.references),
                    new HashSet<String>(Arrays.asList("test.widget", "test.editorwidget")),
                    names(result.references.get(ItemType.InputTemplate.getName("test"))));
	}

	private Set<String> names(Set<Item> items) {
	    if (items == null) {
	        return Collections.emptySet();
	    }
	    Set<String> result = new HashSet<String>();
	    for (Item item : items) {
	        result.add(item.externalid());
	    }
	    return result;
	}

	@Test
	public void parse_content_should_return_found_on_line()
		throws Exception
	{
		String xml = Resource.content("", "test").content;
        List<Item> content = parse(xml).items;
		assertEquals(xml, 7, content.get(0).line());
	}

	private ParserResult parse(String data)
		throws XMLStreamException, JAXBException
	{
		StringReader reader = new StringReader(data);
		return new StreamParser().parse(new BufferedReader(reader));
	}
}
