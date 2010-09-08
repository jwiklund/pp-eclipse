package pp.eclipse.command;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.widgets.Shell;

import pp.eclipse.open.DialogFactory;
import pp.eclipse.open.ItemType;
import pp.eclipse.open.MementoHandler;
import pp.eclipse.open.Repository;
import pp.eclipse.parse.ContentParser;
import pp.eclipse.ui.SelectionDialog;

public class OpenContentXMLCommand extends OpenCommandTemplate
{
	public OpenContentXMLCommand() {
		super(new DialogFactory() {
			@Override
			public SelectionDialog createDialog(IContainer root) {
				Shell shell = new Shell();
                Repository repository = new Repository(root, new ContentParser());
				SelectionDialog dialog = new SelectionDialog(shell, repository, new MementoHandler(root, ItemType.Content));
				dialog.setTitle("Filtered ContentXml Dialog");
				return dialog;
			}
		});
	}
}
