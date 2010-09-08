package pp.eclipse.common;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import pp.eclipse.domain.Container;
import pp.eclipse.domain.Item;

public interface IRepository
{
    List<Container> list(IProgressMonitor monitor) throws CoreException;
    
	boolean validate(Item item);
	
}
