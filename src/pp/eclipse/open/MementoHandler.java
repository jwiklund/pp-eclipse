package pp.eclipse.open;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IMemento;


public class MementoHandler {
    
    private final IContainer root;
    private final ItemType restricted;

    public MementoHandler(IContainer root, ItemType restricted) 
    {
        this.root = root;
        this.restricted = restricted;
    }
    
    public MementoHandler(IContainer root) 
    {
        this.root = root;
        this.restricted = null;
    }

    public void store(Object object, IMemento memento) {
        if (object instanceof Item) {
            Item item = (Item) object;
            if (restricted != null && item.type() != restricted) {
                return;
            }
            memento.putString("type", item.type().name());
            memento.putString("externalId", item.externalid());
            memento.putString("fullPath", item.path().toString());
            memento.putInteger("lineno", item.line());      
        }
    }

    public Item restore(IMemento memento) {
        String typestring = memento.getString("type");
        ItemType type = null;
        if (typestring != null) {
            type = ItemType.valueOf(typestring);
            if (restricted != null && type != restricted) {
                return null;
            }
        }
        String extneralid = memento.getString("externalId");
        String fullPath = memento.getString("fullPath");
        Integer lineno = memento.getInteger("lineno");
        if (type == null || extneralid == null || fullPath == null || lineno == null)
            return null;
        Path path = new Path(fullPath);
        if (root.findMember(path) == null)
            return null;
        return new Item(type, extneralid, path, lineno);
    }
}
