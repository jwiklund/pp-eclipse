package pp.eclipse.domain;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

import org.eclipse.core.resources.IContainer;
import org.junit.Test;

import pp.eclipse.dummy.BaseProgressMonitor;
import pp.eclipse.dummy.Path;
import pp.eclipse.dummy.Resource;
import pp.eclipse.open.Container;
import pp.eclipse.open.Item;
import pp.eclipse.open.ItemType;
import pp.eclipse.open.Repository;
import pp.eclipse.open.parse.ContentParser;

public class BasicRepositoryTest {
	
	@Test
	public void testExtractExternalId() 
		throws Exception
	{
		IContainer root = Resource.root(Resource.content("simple.xml", "p.simple"));
		Repository target = new Repository(root, new ContentParser());
		Item externalid = new Item(ItemType.Content, "p.simple", Path.path("/simple.xml"), 3);
		assertEquals(singletonList(new Container(Path.path("/simple.xml"), 0, singletonList(externalid))), 
				target.list(new BaseProgressMonitor()));
	}
}
