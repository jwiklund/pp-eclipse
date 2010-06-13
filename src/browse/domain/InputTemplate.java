package browse.domain;

import org.eclipse.core.runtime.IPath;

/**
 * @author Jonas Wiklund
 *
 */
public class InputTemplate implements Comparable<InputTemplate> 
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
    public int compareTo(InputTemplate arg0) {
        if (inputTemplate.equals(arg0.inputTemplate)) {
            return path.toString().compareTo(arg0.path.toString());
        }
        return inputTemplate.compareTo(arg0.inputTemplate);
    }
    
    @Override
    public String toString() {
        return "InputTemplate [inputTemplate=" + inputTemplate + ", path="
                + path + ", line=" + line + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((inputTemplate == null) ? 0 : inputTemplate.hashCode());
        result = prime * result + ((path == null) ? 0 : path.toString().hashCode());
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
        InputTemplate other = (InputTemplate) obj;
        if (inputTemplate == null) {
            if (other.inputTemplate != null)
                return false;
        } else if (!inputTemplate.equals(other.inputTemplate))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.toString().equals(other.path == null ? null : other.path.toString()))
            return false;
        return true;
    }
}
