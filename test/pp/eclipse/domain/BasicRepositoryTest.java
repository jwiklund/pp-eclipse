package pp.eclipse.domain;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

import org.eclipse.core.resources.IContainer;
import org.junit.Test;

import pp.eclipse.common.NoCache;
import pp.eclipse.common.Repository;
import pp.eclipse.dummy.BaseProgressMonitor;
import pp.eclipse.dummy.Path;
import pp.eclipse.dummy.Resource;
import pp.eclipse.parse.ContentParser;

public class BasicRepositoryTest {
	
	@Test
	public void testExtractExternalId() 
		throws Exception
	{
		IContainer root = Resource.root(Resource.content("simple.xml", "p.simple"));
		Repository<ExternalId, ContentXML>target = new Repository<ExternalId, ContentXML>(root, new NoCache(), new ContentXMLFactory(root), new ContentParser());
		ExternalId externalid = new ExternalId("p.simple", Path.path("/simple.xml"), 3);
		assertEquals(singletonList(new ContentXML(Path.path("/simple.xml"), 0, singletonList(externalid))), 
				target.list(new BaseProgressMonitor()));
	}
}
