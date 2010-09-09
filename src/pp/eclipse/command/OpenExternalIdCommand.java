package pp.eclipse.command;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.widgets.Shell;

import pp.eclipse.open.DialogFactory;
import pp.eclipse.open.MementoHandler;
import pp.eclipse.open.Repository;
import pp.eclipse.open.parse.Parser;
import pp.eclipse.ui.SelectionDialog;

public class OpenExternalIdCommand extends OpenCommandTemplate
{
	public OpenExternalIdCommand() {
		super(new DialogFactory() {
			public SelectionDialog createDialog(IContainer root) {
				Shell shell = new Shell();
				Repository repository = new Repository(root, new Parser());
				SelectionDialog dialog = new SelectionDialog(shell, repository, new MementoHandler(root));
				dialog.setTitle("Filtered ExternalId Dialog");
				return dialog;
			}
		});
	}
}
