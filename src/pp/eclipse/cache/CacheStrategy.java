package pp.eclipse.cache;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import pp.eclipse.common.DefinedItem;
import pp.eclipse.common.DefiningFile;
import pp.eclipse.common.IRepository;

public interface CacheStrategy<Item extends DefinedItem, Container extends DefiningFile<Item>> {

    void startup();
    void shutdown();
    
	void before(IProgressMonitor monitor, IRepository<Item, Container> repo);
	void after(IProgressMonitor monitor, IRepository<Item, Container> repo) throws CoreException;
	
	Iterable<Item> list(IProgressMonitor monitor, IRepository<Item, Container> repo) throws CoreException;
}
