package pp.eclipse.ui;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.widgets.Shell;

import pp.eclipse.common.BasicRepository;
import pp.eclipse.common.DialogFactory;
import pp.eclipse.domain.ContentXML;
import pp.eclipse.domain.ContentXMLFactory;
import pp.eclipse.domain.ExternalId;
import pp.eclipse.parse.ContentParser;

public class ContentXMLDialogFactory implements DialogFactory<ExternalId, ContentXML>
{
	public SelectionDialog<ExternalId, ContentXML> createDialog(IContainer root) 
	{
		Shell shell = new Shell();
		ContentXMLFactory factory = new ContentXMLFactory(root);
		BasicRepository<ExternalId, ContentXML> repository = new BasicRepository<ExternalId, ContentXML>(root, factory, new ContentParser());
		SelectionDialog<ExternalId, ContentXML> dialog = new SelectionDialog<ExternalId, ContentXML>(shell, factory, repository);
		dialog.setTitle("Filtered ContentXml Dialog");
		return dialog;
	}
}
