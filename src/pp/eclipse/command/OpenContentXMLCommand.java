package pp.eclipse.command;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.widgets.Shell;

import pp.eclipse.common.DialogFactory;
import pp.eclipse.common.MementoHandler;
import pp.eclipse.common.Repository;
import pp.eclipse.domain.ItemType;
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