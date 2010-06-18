package pp.eclipse.domain;

import java.util.List;

import org.eclipse.core.runtime.IPath;

import pp.eclipse.common.DefinedItem;
import pp.eclipse.common.DefiningFile;


public class ContentXML<Item extends DefinedItem> implements DefiningFile<Item> {
    public final IPath path;
    public final long modified;
    public final List<Item> templates;
    
    public ContentXML(IPath path, long modified, List<Item> templates) {
        this.path = path;
        this.modified = modified;
        this.templates = templates;
    }

    @Override
    public String toString() {
        return "TemplateDefinition [path=" + path + ", modified=" + modified
                + ", templates=" + templates + "]";
    }

	@Override
	public IPath path() 
	{
		return path;
	}
	
	@Override
	public long modified() 
	{
		return modified;
	}

	@Override
	public List<Item> defines() 
	{
		return templates;
	}
}
