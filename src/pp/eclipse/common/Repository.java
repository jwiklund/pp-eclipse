package pp.eclipse.common;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import pp.eclipse.domain.Container;
import pp.eclipse.domain.Item;
import pp.eclipse.parse.Parser;

public class Repository implements IRepository
{
	private final IContainer root;
	private final Parser[] parsers;

	public Repository(IContainer root, Parser... parsers) 
	{
		this.root = root;
		this.parsers = parsers;
	}
	
	@Override
	public List<Container> list(final IProgressMonitor monitor) 
		throws CoreException
	{
	    final List<Container> containers = new ArrayList<Container>();
	    root.accept(new IResourceProxyVisitor() {
	        @Override
	        public boolean visit(IResourceProxy proxy) throws CoreException {
	            if (proxy.getName().matches(".*\\.xml")) {
	                IResource resource = proxy.requestResource();
	                if (resource instanceof IFile) {
	                    Container read = RepositoryUtil.read((IFile) resource, parsers);
	                    if (read != null) {
	                        containers.add(read);
	                        monitor.worked(1);
	                    }
	                }
	            }
	            return true;
	        }

	    }, 0);
	    return containers;
	}
	       
	@Override
	public boolean validate(Item item) {
		return true;
	}
}
