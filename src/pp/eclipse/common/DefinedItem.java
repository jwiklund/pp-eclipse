package pp.eclipse.common;

import org.eclipse.core.runtime.IPath;

public interface DefinedItem extends Comparable<DefinedItem>
{
	IPath path();
	int line();
	String externalid();
}
