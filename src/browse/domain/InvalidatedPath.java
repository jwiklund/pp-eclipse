package browse.domain;

import org.eclipse.core.runtime.IPath;

public class InvalidatedPath {
    public final String fullPath;
    public final IPath path;
    
    public InvalidatedPath(String fullPath, IPath path) 
    {
        this.fullPath = fullPath;
        this.path = path;
    }

    @Override
    public String toString() {
        return "InvalidatedPath [fullPath=" + fullPath + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((fullPath == null) ? 0 : fullPath.hashCode());
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
        InvalidatedPath other = (InvalidatedPath) obj;
        if (fullPath == null) {
            if (other.fullPath != null)
                return false;
        } else if (!fullPath.equals(other.fullPath))
            return false;
        return true;
    }
}
