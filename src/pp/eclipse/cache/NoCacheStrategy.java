package pp.eclipse.cache;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import pp.eclipse.common.DefinedItem;
import pp.eclipse.common.DefiningFile;
import pp.eclipse.common.IRepository;

public class NoCacheStrategy<Item extends DefinedItem, Container extends DefiningFile<Item>> extends EmptyCacheStrategy<Item, Container> 
{
	@Override
	public Iterable<Item> list(IProgressMonitor monitor, IRepository<Item, Container> repo) 
		throws CoreException 
	{
		List<Item> result = new ArrayList<Item>();
		List<Container> containers = repo.list(monitor);
		monitor.beginTask("Scanning...", 1000);
		for (Container container : containers) {
			result.addAll(container.defines());
		}
		return result;
	}
}
