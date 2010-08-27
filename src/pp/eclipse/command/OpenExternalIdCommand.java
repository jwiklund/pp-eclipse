package pp.eclipse.command;

import pp.eclipse.domain.ContentXML;
import pp.eclipse.domain.ExternalId;
import pp.eclipse.ui.ContentXMLDialogFactory;

public class OpenExternalIdCommand extends OpenCommandTemplate<ExternalId, ContentXML>
{
	public OpenExternalIdCommand() {
		super(new ContentXMLDialogFactory());
	}
}
