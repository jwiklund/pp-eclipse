package pp.eclipse.domain;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.eclipse.core.resources.IContainer;
import org.junit.Test;

import pp.eclipse.domain.ExternalId;
import pp.eclipse.domain.ExternalIdRepository;
import pp.eclipse.dummy.Path;
import pp.eclipse.dummy.Resource;


public class ExternalIdRepositoryTest {
	
	ExternalIdRepository target;

	@Test
	public void testExtractExternalId() 
	{
		IContainer root = Resource.root(Resource.content("simple.xml", "p.simple"));
		target = new ExternalIdRepository(null, root);
		ExternalId externalid = new ExternalId("p.simple", Path.path("/simple.xml"), 3);
		assertEquals(Collections.singletonList(externalid), target.externals());
	}
}
