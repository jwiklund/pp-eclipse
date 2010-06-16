package pp.eclipse.domain;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.eclipse.core.resources.IContainer;
import org.junit.Before;
import org.junit.Test;

import pp.eclipse.domain.InputTemplate;
import pp.eclipse.domain.InputTemplateRepository;
import pp.eclipse.dummy.Counter;
import pp.eclipse.dummy.Path;
import pp.eclipse.dummy.Resource;


public class InputTemplateRepositoryTest {
	
	InputTemplateRepository target;
	IContainer root;
	
	@Before
	public void setup() 
	{
		root = Resource.root(Resource.empty("something.java"),
								  Resource.empty("empty.xml"), 
								  Resource.inputTemplate("input.xml", "p.InputTemplate"),
								  Resource.content("content.xml", "p.InputTemplateBootstrap"));
		root = Resource.rootWithUpdate(root, Resource.inputTemplate("input.xml", "p.InputTemplate2"));
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
	    InputTemplate template = new InputTemplate("p.InputTemplate", Path.path("/input.xml"), 3);
	    assertEquals(Collections.singletonList(template), target.templates(Counter.counter()));
	}
	
	@Test
	public void templates_should_count_files_worked()
	{
	    Counter counter = Counter.counter();
	    target.templates(counter);
	    assertEquals(3, counter.count());
	}
	
	@Test
	public void templates_should_only_work_if_changed()
	{
	    target.templates(Counter.counter());
	    Counter counter = Counter.counter();
	    target.templates(counter);
	    assertEquals(0, counter.count());
	}
	
	@Test
	public void refresh_should_refresh_all()
	{
	    target.templates(Counter.counter());
	    target.refresh();
	    Counter counter = Counter.counter();
	    target.templates(counter);
	    assertEquals(3, counter.count());
	}
}
