package pp.eclipse.cache;

import org.eclipse.core.runtime.IProgressMonitor;

import pp.eclipse.common.DefinedItem;
import pp.eclipse.common.DefiningFile;
import pp.eclipse.common.Repository;

public interface CacheStrategy<Item extends DefinedItem, Container extends DefiningFile<Item>> {
	
	void before(IProgressMonitor monitor, Repository<Item, Container> repo);
	Iterable<Item> list(IProgressMonitor monitor, Repository<Item, Container> repo);
	void after(IProgressMonitor monitor, Repository<Item, Container> repo);
}
