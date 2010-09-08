package pp.eclipse.domain;

import java.util.List;

import org.eclipse.core.runtime.IPath;

public class Container {
    public final IPath path;
    public final long modified;
    public final List<Item> items;

    public Container(IPath path, long modified, List<Item> items) {
        super();
        this.path = path;
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
    
    @Override
    public String toString() {
        return path.toString() + " " + items.size() + " items";
    }
}
