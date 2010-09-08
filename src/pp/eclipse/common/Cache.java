package pp.eclipse.common;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;


public interface Cache 
{     
    public <Item extends DefinedItem, Container extends DefiningFile<Item>> 
    List<Container> list(Class<Container> containerType, 
                         IProgressMonitor monitor, 
                         CacheRefresher<Item, Container> refresher)
        throws CoreException;
}
