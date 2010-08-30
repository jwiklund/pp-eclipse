package pp.eclipse.common;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;


public interface IRepository<Item extends DefinedItem, Container extends DefiningFile<Item>>
{

	List<Container> list(IProgressMonitor monitor) throws CoreException;

	boolean validate(DefinedItem item);
	
}
