package pp.eclipse.open;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IMemento;


public class MementoHandler {

    private final IContainer root;
    private final ItemType restricted;
    private final boolean required;

    public MementoHandler(IContainer root, ItemType restricted, boolean required)
    {
        this.root = root;
        this.restricted = restricted;
        this.required = required;
    }

    public MementoHandler(IContainer root, ItemType restricted)
    {
        this(root, restricted, true);
    }

    public MementoHandler(IContainer root)
    {
        this(root, null);
    }

    public void store(Object object, IMemento memento) {
        if (object instanceof Item) {
            Item item = (Item) object;
            if (!check(item.type())) {
                return ;
            }
            memento.putString("type", item.type().name());
            memento.putString("externalId", item.externalid());
            memento.putString("fullPath", item.path().toString());
            memento.putInteger("lineno", item.line());
        }
    }

    private boolean check(ItemType type) {
        if (restricted == null) {
            return true;
        }
        if (required) {
            return type == restricted;
        }
        return type != restricted;
    }

    public Item restore(IMemento memento) {
        String typestring = memento.getString("type");
        ItemType type = null;
        if (typestring != null) {
            type = ItemType.valueOf(typestring);
            if (!check(type)) {
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
