package pp.eclipse.command;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.widgets.Shell;

import pp.eclipse.common.DialogFactory;
import pp.eclipse.common.Memento;
import pp.eclipse.common.SingleRepository;
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
				SingleRepository repository = new SingleRepository(root, new TemplateParser());
				SelectionDialog dialog = new SelectionDialog(shell, repository, new Memento(root, ItemType.InputTemplate));
				dialog.setTitle("Filtered TemplateDefinition Dialog");
				return dialog;
			}
		});
	}	
}
