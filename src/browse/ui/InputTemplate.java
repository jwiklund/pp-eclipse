package browse.ui;

import org.eclipse.core.runtime.IPath;

public class InputTemplate 
{   
    public final String inputTemplate;
    public final IPath path;
    public final int line;
    
    public InputTemplate(String inputTemplate, IPath path, int line)
    {
        super();
        this.inputTemplate = inputTemplate;
        this.path = path;
        this.line = line;
    }
    
    @Override
    public String toString()
    {
        return inputTemplate;
    }
}
