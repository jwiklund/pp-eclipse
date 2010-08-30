package pp.eclipse.cache;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import pp.eclipse.common.DefinedItem;
import pp.eclipse.common.DefiningFile;
import pp.eclipse.common.IRepository;

public abstract class EmptyCacheStrategy<Item extends DefinedItem, Container extends DefiningFile<Item>> 
    implements CacheStrategy<Item, Container>
{

    @Override
    public void startup() {
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void before(IProgressMonitor monitor, IRepository<Item, Container> repo) {
    }

    @Override
    public void after(IProgressMonitor monitor, IRepository<Item, Container> repo) throws CoreException {
    }

    @Override
    public Iterable<Item> list(IProgressMonitor monitor, IRepository<Item, Container> repo) throws CoreException {
        return null;
    }
}
