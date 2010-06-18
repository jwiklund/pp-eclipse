package pp.eclipse.common;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;


public interface Repository<Item extends DefinedItem, Container extends DefiningFile<Item>>
{

	List<Container> list(IProgressMonitor monitor);

	boolean validate(Item item);
	
}
