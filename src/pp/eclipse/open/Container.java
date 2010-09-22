package pp.eclipse.open;

import java.util.List;

import org.eclipse.core.runtime.IPath;

import pp.eclipse.open.Item;

public class Container {
    public final IPath path;
    public final String hash;
    public final long modified;
    public final List<Item> items;

    public Container(IPath path, long modified, List<Item> items) {
        this.path = path;
        this.hash = null;
        this.modified = modified;
        this.items = items;
    }
    
    public Container(IPath path, long modified, String hash, List<Item> items) {
        this.path = path;
        this.hash = hash;
        this.modified = modified;
        this.items = items;
    }

    public IPath path() {
        return path;
    }

    public long modified() {
        return modified;
    }

    public List<Item> items() {
        return items;
    }
    
    public Container withHash(String newHash) {
    	return new Container(path, modified, newHash, items);
    }
    
	public Container withModification(long newModification) {
		return new Container(path, newModification, hash, items);
	}

    @Override
    public String toString() {
        return path.toString() + " " + items.size() + " items";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((items == null) ? 0 : items.hashCode());
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
        Container other = (Container) obj;
        if (items == null) {
            if (other.items != null)
                return false;
        } else if (!items.equals(other.items))
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
