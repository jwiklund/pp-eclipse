package browse.domain;

import java.util.List;

import org.eclipse.core.runtime.IPath;

public class TemplateDefinition {
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
}
