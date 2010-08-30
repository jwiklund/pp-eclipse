package pp.eclipse.command;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.widgets.Shell;

import pp.eclipse.Activator;
import pp.eclipse.cache.CacheStrategy;
import pp.eclipse.common.DialogFactory;
import pp.eclipse.common.Repository;
import pp.eclipse.domain.ContentXML;
import pp.eclipse.domain.ContentXMLFactory;
import pp.eclipse.domain.ExternalId;
import pp.eclipse.parse.ContentParser;
import pp.eclipse.ui.SelectionDialog;

public class OpenExternalIdCommand extends OpenCommandTemplate<ExternalId, ContentXML>
{
	public OpenExternalIdCommand() {
		super(new DialogFactory<ExternalId, ContentXML>() {
			@Override
			public SelectionDialog<ExternalId, ContentXML> createDialog(IContainer root) {
				Shell shell = new Shell();
				ContentXMLFactory factory = new ContentXMLFactory(root);
				Repository<ExternalId, ContentXML> repository = new Repository<ExternalId, ContentXML>(root, factory, new ContentParser());
				CacheStrategy<ExternalId, ContentXML> cache = Activator.getDefault().cacheStrategy(ExternalId.class);
				SelectionDialog<ExternalId, ContentXML> dialog = new SelectionDialog<ExternalId, ContentXML>(shell, factory, repository, cache);
				dialog.setTitle("Filtered ContentXml Dialog");
				return dialog;
			}
		});
	}
}
