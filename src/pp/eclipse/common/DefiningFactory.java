package pp.eclipse.common;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IMemento;

public interface DefiningFactory<Item extends DefinedItem, Container extends DefiningFile<Item>>
{
	Container create(IPath path, long modified, List<Item> items);
	
	void store(Object item, IMemento memento);
	Item restore(IMemento memento);

    Class<Container> containerClass();
}
