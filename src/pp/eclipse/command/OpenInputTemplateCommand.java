package pp.eclipse.command;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.widgets.Shell;

import pp.eclipse.Activator;
import pp.eclipse.common.DialogFactory;
import pp.eclipse.common.Repository;
import pp.eclipse.domain.InputTemplate;
import pp.eclipse.domain.TemplateDefinition;
import pp.eclipse.domain.TemplateDefinitionFactory;
import pp.eclipse.parse.TemplateParser;
import pp.eclipse.ui.SelectionDialog;

public class OpenInputTemplateCommand extends OpenCommandTemplate<InputTemplate, TemplateDefinition>
{
	public OpenInputTemplateCommand() {
		super(new DialogFactory<InputTemplate, TemplateDefinition>() {
			@Override
			public SelectionDialog<InputTemplate, TemplateDefinition> createDialog(IContainer root) {
				Shell shell = new Shell();
				TemplateDefinitionFactory factory = new TemplateDefinitionFactory(root);
				Repository<InputTemplate, TemplateDefinition> repository = new Repository<InputTemplate, TemplateDefinition>(root, Activator.getDefault().cache(), factory, new TemplateParser());
				SelectionDialog<InputTemplate, TemplateDefinition> dialog = new SelectionDialog<InputTemplate, TemplateDefinition>(shell, factory, repository);
				dialog.setTitle("Filtered TemplateDefinition Dialog");
				return dialog;
			}
		});
	}	
}
