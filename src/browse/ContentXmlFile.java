package browse;

import org.eclipse.core.runtime.IPath;

public class ContentXmlFile {
    public final String name;
    public final IPath path;
    
    public ContentXmlFile(String name, IPath path)
    {
        super();
        this.name = name;
        this.path = path;
    }
}