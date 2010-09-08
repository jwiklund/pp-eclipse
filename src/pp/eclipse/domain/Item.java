package pp.eclipse.domain;

import org.eclipse.core.runtime.IPath;

public class Item implements Comparable<Item> {
    private final ItemType type;
    private final String externalid;
    private final IPath path;
    private final int line;
    
    public Item(ItemType type, String externalid, IPath path, int line) {
        this.type = type;
        this.externalid = externalid;
        this.path = path;
        this.line = line;
    }

    public ItemType type() {
        return type;
    }
    
    public String externalid() {
        return externalid;
    }
    
    public Item path(IPath newPath) {
        return new Item(type, externalid, newPath, line);
    }
    
    public IPath path() {
        return path;
    }
    
    public Item line(int newline) {
        return new Item(type, externalid, path, newline);
    }
    
    public int line() {
        return line;
    }
    
    @Override
    public String toString() {
        return externalid + " " + type + " " + path + ":" + line;
    }
    

    @Override
    public int compareTo(Item o) {
        if (!externalid.equals(o.externalid)) {
            return externalid.compareTo(o.externalid);
        }
        if (!path.equals(o.path)) {
            return path.toString().compareTo(o.path.toString());
        }
        if (!type.equals(o.type)) {
            return type.compareTo(o.type);
        }
        return Integer.valueOf(line).compareTo(Integer.valueOf(o.line));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((externalid == null) ? 0 : externalid.hashCode());
        result = prime * result + line;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Item other = (Item) obj;
        if (externalid == null) {
            if (other.externalid != null)
                return false;
        } else if (!externalid.equals(other.externalid))
            return false;
        if (line != other.line)
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (type != other.type)
            return false;
        return true;
    }
}
