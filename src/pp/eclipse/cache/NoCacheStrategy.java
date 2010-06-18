package pp.eclipse.cache;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import pp.eclipse.common.DefinedItem;
import pp.eclipse.common.DefiningFile;
import pp.eclipse.common.Repository;

public class NoCacheStrategy<Item extends DefinedItem, Container extends DefiningFile<Item>> implements CacheStrategy<Item, Container> 
{

	@Override
	public void before(IProgressMonitor monitor, Repository<Item, Container> repo) 
	{
		// NOP
	}

	@Override
	public Iterable<Item> list(IProgressMonitor monitor, Repository<Item, Container> repo) {
		List<Item> result = new ArrayList<Item>();
		List<Container> containers = repo.list(monitor);
		for (Container container : containers) {
			result.addAll(container.defines());
		}
		return result;
	}

	@Override
	public void after(IProgressMonitor monitor, Repository<Item, Container> repo) 
	{
		// NOP
	}

}
