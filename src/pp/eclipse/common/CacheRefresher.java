package pp.eclipse.common;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public interface CacheRefresher<Item extends DefinedItem, Container extends DefiningFile<Item>> {

    public List<Container> list(IProgressMonitor monitor) throws CoreException;
    public Container read(IFile file) throws CoreException;
}
