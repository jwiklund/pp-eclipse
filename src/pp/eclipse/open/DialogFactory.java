package pp.eclipse.open;

import org.eclipse.core.resources.IContainer;

import pp.eclipse.ui.SelectionDialog;

public interface DialogFactory
{
	SelectionDialog createDialog(IContainer root); 
}
