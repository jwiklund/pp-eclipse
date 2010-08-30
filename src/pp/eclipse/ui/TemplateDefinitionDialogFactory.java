package pp.eclipse.ui;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.widgets.Shell;

import pp.eclipse.cache.Cache;
import pp.eclipse.common.Repository;
import pp.eclipse.common.DialogFactory;
import pp.eclipse.domain.InputTemplate;
import pp.eclipse.domain.TemplateDefinition;
import pp.eclipse.domain.TemplateDefinitionFactory;
import pp.eclipse.parse.TemplateParser;

public class TemplateDefinitionDialogFactory implements DialogFactory<InputTemplate, TemplateDefinition>
{
	public SelectionDialog<InputTemplate, TemplateDefinition> createDialog(IContainer root) 
	{
		Shell shell = new Shell();
		TemplateDefinitionFactory factory = new TemplateDefinitionFactory(root);
		Repository<InputTemplate, TemplateDefinition> repository = new Repository<InputTemplate, TemplateDefinition>(root, factory, new TemplateParser());
		SelectionDialog<InputTemplate, TemplateDefinition> dialog = new SelectionDialog<InputTemplate, TemplateDefinition>(shell, factory, repository, Cache.<InputTemplate, TemplateDefinition>none());
		dialog.setTitle("Filtered TemplateDefinition Dialog");
		return dialog;
	}
}
