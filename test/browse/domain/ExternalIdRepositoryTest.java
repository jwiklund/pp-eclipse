package browse.domain;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.eclipse.core.resources.IContainer;
import org.junit.Test;

import browse.dummy.DummyPath;
import browse.dummy.DummyResource;

public class ExternalIdRepositoryTest {
	
	ExternalIdRepository target;

	@Test
	public void testExtractExternalId() 
	{
		IContainer root = DummyResource.root(DummyResource.content("simple.xml", "p.simple"));
		target = new ExternalIdRepository(null, root);
		ExternalId externalid = new ExternalId("p.simple", DummyPath.path("/simple.xml"), 3);
		assertEquals(Collections.singletonList(externalid), target.externals());
	}
}
