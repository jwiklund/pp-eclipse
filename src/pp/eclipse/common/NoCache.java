package pp.eclipse.common;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class NoCache implements Cache {

    @Override
    public <Item extends DefinedItem, Container extends DefiningFile<Item>> List<Container> list(
            Class<Container> containerType, 
            IProgressMonitor monitor,
            CacheRefresher<Item, Container> refresher)
        throws CoreException
    {
        return refresher.list(monitor);
    }
}
