package pp.eclipse.command;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.widgets.Shell;

import pp.eclipse.common.DialogFactory;
import pp.eclipse.common.MementoHandler;
import pp.eclipse.common.Repository;
import pp.eclipse.domain.ItemType;
import pp.eclipse.parse.TemplateParser;
import pp.eclipse.ui.SelectionDialog;

public class OpenInputTemplateCommand extends OpenCommandTemplate
{
	public OpenInputTemplateCommand() {
		super(new DialogFactory() {
			@Override
			public SelectionDialog createDialog(IContainer root) {
				Shell shell = new Shell();
				Repository repository = new Repository(root, new TemplateParser());
				SelectionDialog dialog = new SelectionDialog(shell, repository, new MementoHandler(root, ItemType.InputTemplate));
				dialog.setTitle("Filtered TemplateDefinition Dialog");
				return dialog;
			}
		});
	}
}
