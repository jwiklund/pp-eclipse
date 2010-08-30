package pp.eclipse.cache;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import pp.eclipse.common.DefinedItem;
import pp.eclipse.common.DefiningFile;
import pp.eclipse.common.IRepository;

public class PostCacheStrategy<Item extends DefinedItem, Container extends DefiningFile<Item>> extends EmptyCacheStrategy<Item, Container>
{
	private List<Item> cached = null;
	private long lastUpdated = 0;

	@Override
	public Iterable<Item> list(IProgressMonitor monitor, IRepository<Item, Container> repo) 
		throws CoreException 
	{
		if (cached == null) {
			cached = refresh(monitor, repo);
			lastUpdated = System.currentTimeMillis();
		}
		return cached;
	}

	@Override
	public void after(IProgressMonitor monitor, IRepository<Item, Container> repo)
		throws CoreException
	{
		if (lastUpdated > System.currentTimeMillis() + 1000) {
			cached = refresh(monitor, repo);
			lastUpdated = System.currentTimeMillis();
		}
	}
	
	private List<Item> refresh(IProgressMonitor monitor, IRepository<Item, Container> repo) throws CoreException 
	{
	    monitor.beginTask("Delaying...", 1);
	    try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        monitor.worked(1);
        monitor.beginTask("Scanning...", 1000);
		List<Item> result = new ArrayList<Item>();
		List<Container> containers = repo.list(monitor);
		for (Container container : containers) {
			result.addAll(container.defines());
		}
		return result;
	}
}
