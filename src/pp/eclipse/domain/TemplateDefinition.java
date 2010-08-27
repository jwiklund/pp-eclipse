package pp.eclipse.domain;

import java.util.List;

import org.eclipse.core.runtime.IPath;

import pp.eclipse.common.DefiningFile;


public class TemplateDefinition implements DefiningFile<InputTemplate> {
    public final IPath path;
    public final long modified;
    public final List<InputTemplate> templates;
    
    public TemplateDefinition(IPath path, long modified, List<InputTemplate> templates) {
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
	public List<InputTemplate> defines() 
	{
		return templates;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (modified ^ (modified >>> 32));
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result
				+ ((templates == null) ? 0 : templates.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TemplateDefinition other = (TemplateDefinition) obj;
		if (modified != other.modified)
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (templates == null) {
			if (other.templates != null)
				return false;
		} else if (!templates.equals(other.templates))
			return false;
		return true;
	}
}
