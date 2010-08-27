package pp.eclipse.common;

import org.eclipse.core.resources.IContainer;

import pp.eclipse.ui.SelectionDialog;

public interface DialogFactory<Item extends DefinedItem, Container extends DefiningFile<Item>>
{
	SelectionDialog<Item, Container> createDialog(IContainer root); 
}
