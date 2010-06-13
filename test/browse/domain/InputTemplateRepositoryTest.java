package browse.domain;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.eclipse.core.resources.IContainer;
import org.junit.Before;
import org.junit.Test;

public class InputTemplateRepositoryTest {
	
	InputTemplateRepository target;
	IContainer root;
	
	@Before
	public void setup() 
	{
		root = DummyResource.root(DummyResource.empty("something.java"),
		                          DummyResource.empty("empty.xml"), 
								  DummyResource.inputTemplate("input.xml", "p.InputTemplate"),
								  DummyResource.content("content.xml", "p.InputTemplateBootstrap"));
		root = DummyResource.rootWithUpdate(root, DummyResource.inputTemplate("input.xml", "p.InputTemplate2"));
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
	
	@Test
	public void templates_should_only_work_if_changed()
	{
	    target.templates(DummyCounter.counter());
	    DummyCounter counter = DummyCounter.counter();
	    target.templates(counter);
	    assertEquals(0, counter.count());
	}
	
	@Test
	public void refresh_should_refresh_all()
	{
	    target.templates(DummyCounter.counter());
	    target.refresh();
	    DummyCounter counter = DummyCounter.counter();
	    target.templates(counter);
	    assertEquals(3, counter.count());
	}
}
