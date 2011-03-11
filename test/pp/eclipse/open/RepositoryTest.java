package pp.eclipse.open;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.junit.Test;

import pp.eclipse.Preferences;
import pp.eclipse.open.Container;
import pp.eclipse.open.Item;
import pp.eclipse.open.ItemType;
import pp.eclipse.open.Repository;
import pp.eclipse.open.dummy.BasePreferencesStore;
import pp.eclipse.open.dummy.BaseProgressMonitor;
import pp.eclipse.open.dummy.Path;
import pp.eclipse.open.dummy.Resource;
import pp.eclipse.open.parse.StreamParser;

public class RepositoryTest {

	@Test
	public void testExtractExternalId()
		throws Exception
	{
		IContainer root = Resource.root(Resource.batch("simple.xml", Resource.content("p.simple")));
		Repository target = new Repository(root, new Preferences(new BasePreferencesStore()), new StreamParser(), new HashMap<String, List<Item>>());
		Item externalid = new Item(ItemType.Content, "p.simple", Path.path("/simple.xml"), 7);
		assertEquals(singletonList(new Container(Path.path("/simple.xml"), 0, singletonList(externalid))),
				target.list(new BaseProgressMonitor()));
	}
}
