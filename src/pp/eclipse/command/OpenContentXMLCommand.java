package pp.eclipse.command;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.widgets.Shell;

import pp.eclipse.common.DialogFactory;
import pp.eclipse.common.Memento;
import pp.eclipse.common.SingleRepository;
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
                SingleRepository repository = new SingleRepository(root, new ContentParser());
				SelectionDialog dialog = new SelectionDialog(shell, repository, new Memento(root, ItemType.Content));
				dialog.setTitle("Filtered ContentXml Dialog");
				return dialog;
			}
		});
	}
}
