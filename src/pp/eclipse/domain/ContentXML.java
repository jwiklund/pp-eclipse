package pp.eclipse.domain;

import java.util.List;

import org.eclipse.core.runtime.IPath;

import pp.eclipse.common.DefiningFile;

public class ContentXML implements DefiningFile<ExternalId> {
    public final IPath path;
    public final long modified;
    public final List<ExternalId> externalids;
    
    public ContentXML(IPath path, long modified, List<ExternalId> externalids) {
        this.path = path;
        this.modified = modified;
        this.externalids = externalids;
    }

    @Override
    public String toString() {
        return "ContentXML [path=" + path + ", modified=" + modified
                + ", externalids=" + externalids + "]";
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
	public List<ExternalId> defines() 
	{
		return externalids;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((externalids == null) ? 0 : externalids.hashCode());
		result = prime * result + (int) (modified ^ (modified >>> 32));
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		ContentXML other = (ContentXML) obj;
		if (externalids == null) {
			if (other.externalids != null)
				return false;
		} else if (!externalids.equals(other.externalids))
			return false;
		if (modified != other.modified)
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
}
