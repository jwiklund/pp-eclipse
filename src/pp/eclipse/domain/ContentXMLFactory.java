package pp.eclipse.domain;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IMemento;

import pp.eclipse.common.DefiningFactory;

public class ContentXMLFactory implements DefiningFactory<ExternalId, ContentXML> {

	private final IContainer root;

	public ContentXMLFactory(IContainer root) {
		this.root = root;
	}
	
	@Override
	public ContentXML create(IPath path, long modified, List<ExternalId> items) {
		List<ExternalId> result = new ArrayList<ExternalId>();
		for (ExternalId externalid : items) {
			result.add(externalid.updatePath(path));
		}
		return new ContentXML(path, modified, result);
	}

	@Override
	public void store(Object object, IMemento memento) {
		if (object instanceof ExternalId) {
			ExternalId item = (ExternalId) object;
			memento.putString("externalId", item.externalid());
			memento.putString("fullPath", item.path().toString());
			memento.putInteger("lineno", item.line());		
		}
	}

	@Override
	public ExternalId restore(IMemento memento) {
		String inputTemplate = memento.getString("externalId");
        String fullPath = memento.getString("fullPath");
        Integer lineno = memento.getInteger("lineno");
        if (inputTemplate == null || fullPath == null || lineno == null)
            return null;
        Path path = new Path(fullPath);
        if (root.findMember(path) == null)
            return null;
        return new ExternalId(inputTemplate, path, lineno);
	}

    @Override
    public Class<ContentXML> containerClass() {
        return ContentXML.class;
    }
}
