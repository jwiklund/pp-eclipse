package browse.domain;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.eclipse.core.resources.IResource;
import org.junit.Before;
import org.junit.Test;

public class InputTemplateRepositoryTest {
	
	InputTemplateRepository target;
	IResource root;
	
	@Before
	public void setup() 
	{
		root = DummyResource.root(DummyResource.empty("something.java"),
		                          DummyResource.empty("empty.xml"), 
								  DummyResource.inputTemplate("input.xml", "p.InputTemplate"),
								  DummyResource.content("content.xml", "p.InputTemplateBootstrap"));
		target = new InputTemplateRepository(null, root);
	}

	@Test
	public void estimate_should_count_number_of_xml_files()
	{
		assertEquals(3, target.estimatedXMLFiles());
	}
	
	@Test
	public void templates_should_return_input_templates_with_location()
	{
	    InputTemplate template = new InputTemplate("p.InputTemplate", DummyPath.path("/input.xml"), 3);
	    assertEquals(Collections.singletonList(template), target.templates(DummyCounter.counter()));
	}
	
	@Test
	public void templates_should_count_files_worked()
	{
	    DummyCounter counter = DummyCounter.counter();
	    target.templates(counter);
	    assertEquals(3, counter.count());
	}
}
