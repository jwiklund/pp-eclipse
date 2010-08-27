package pp.eclipse.command;

import pp.eclipse.domain.InputTemplate;
import pp.eclipse.domain.TemplateDefinition;
import pp.eclipse.ui.TemplateDefinitionDialogFactory;

public class OpenInputTemplateCommand extends OpenCommandTemplate<InputTemplate, TemplateDefinition>
{
	public OpenInputTemplateCommand() {
		super(new TemplateDefinitionDialogFactory());
	}	
}
