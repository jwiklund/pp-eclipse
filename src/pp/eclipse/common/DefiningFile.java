package pp.eclipse.common;

import java.util.List;

import org.eclipse.core.runtime.IPath;

public interface DefiningFile<Item extends DefinedItem>
{
	IPath path();
	long modified();
	List<Item> defines();
}
