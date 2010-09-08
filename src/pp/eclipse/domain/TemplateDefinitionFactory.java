package pp.eclipse.domain;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IMemento;

import pp.eclipse.common.DefiningFactory;

public class TemplateDefinitionFactory implements DefiningFactory<InputTemplate, TemplateDefinition> 
{
	private final IContainer root;

	public TemplateDefinitionFactory(IContainer root) {
		this.root = root;
	}

	@Override
	public TemplateDefinition create(IPath path, long modified, List<InputTemplate> items) {
		List<InputTemplate> result = new ArrayList<InputTemplate>();
		for (InputTemplate template : items) {
			result.add(template.updatePath(path));
		}
		return new TemplateDefinition(path, modified, result);
	}

	@Override
	public void store(Object object, IMemento memento) {
		if (object instanceof InputTemplate) {
			InputTemplate item = (InputTemplate) object;
			memento.putString("inputTemplate", item.externalid());
			memento.putString("fullPath", item.path().toString());
			memento.putInteger("lineno", item.line());			
		}
	}

	@Override
	public InputTemplate restore(IMemento memento) {
		String inputTemplate = memento.getString("inputTemplate");
        String fullPath = memento.getString("fullPath");
        Integer lineno = memento.getInteger("lineno");
        if (inputTemplate == null || fullPath == null || lineno == null)
            return null;
        Path path = new Path(fullPath);
        if (root.findMember(path) == null)
            return null;
        return new InputTemplate(inputTemplate, path, lineno);
	}

    @Override
    public Class<TemplateDefinition> containerClass() {
        return TemplateDefinition.class;
    }
}
