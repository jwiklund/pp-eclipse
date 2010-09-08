package pp.eclipse.command;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.widgets.Shell;

import pp.eclipse.common.DialogFactory;
import pp.eclipse.common.MementoHandler;
import pp.eclipse.common.Repository;
import pp.eclipse.parse.ContentParser;
import pp.eclipse.parse.TemplateParser;
import pp.eclipse.ui.SelectionDialog;

public class OpenExternalIdCommand extends OpenCommandTemplate
{
	public OpenExternalIdCommand() {
		super(new DialogFactory() {
			@Override
			public SelectionDialog createDialog(IContainer root) {
				Shell shell = new Shell();
				Repository repository = new Repository(root, new TemplateParser(), new ContentParser());
				SelectionDialog dialog = new SelectionDialog(shell, repository, new MementoHandler(root));
				dialog.setTitle("Filtered ExternalId Dialog");
				return dialog;
			}
		});
	}
}
