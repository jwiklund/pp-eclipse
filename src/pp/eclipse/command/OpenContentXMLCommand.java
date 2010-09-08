package pp.eclipse.command;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.widgets.Shell;

import pp.eclipse.Activator;
import pp.eclipse.common.DialogFactory;
import pp.eclipse.common.Repository;
import pp.eclipse.domain.ContentXML;
import pp.eclipse.domain.ContentXMLFactory;
import pp.eclipse.domain.ExternalId;
import pp.eclipse.parse.ContentParser;
import pp.eclipse.ui.SelectionDialog;

public class OpenContentXMLCommand extends OpenCommandTemplate<ExternalId, ContentXML>
{
	public OpenContentXMLCommand() {
		super(new DialogFactory<ExternalId, ContentXML>() {
			@Override
			public SelectionDialog<ExternalId, ContentXML> createDialog(IContainer root) {
				Shell shell = new Shell();
				ContentXMLFactory factory = new ContentXMLFactory(root);
                Repository<ExternalId, ContentXML> repository = new Repository<ExternalId, ContentXML>(root, Activator.getDefault().cache(), factory, new ContentParser());
				SelectionDialog<ExternalId, ContentXML> dialog = new SelectionDialog<ExternalId, ContentXML>(shell, factory, repository);
				dialog.setTitle("Filtered ContentXml Dialog");
				return dialog;
			}
		});
	}
}
