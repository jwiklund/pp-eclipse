package pp.eclipse.domain;

import org.eclipse.core.runtime.IPath;

import pp.eclipse.common.DefinedItem;

public class InputTemplate implements DefinedItem
{   
    private final String inputTemplate;
    private final IPath path;
    private final int line;
    
    public InputTemplate(String inputTemplate, IPath path, int line)
    {
        super();
        this.inputTemplate = inputTemplate;
        this.path = path;
        this.line = line;
    }

	@Override
	public String toString() {
		return "InputTemplate [inputTemplate=" + inputTemplate + ", path="
				+ path + ", line=" + line + "]";
	}
	
	@Override
	public int compareTo(DefinedItem arg0) {
		if (externalid().equals(arg0.externalid())) {
			return path().toString().compareTo(arg0.path().toString());
		}
		return externalid().compareTo(externalid());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((inputTemplate == null) ? 0 : inputTemplate.hashCode());
		result = prime * result + line;
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
		InputTemplate other = (InputTemplate) obj;
		if (inputTemplate == null) {
			if (other.inputTemplate != null)
				return false;
		} else if (!inputTemplate.equals(other.inputTemplate))
			return false;
		if (line != other.line)
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	@Override
	public IPath path() {
		return path;
	}

	public InputTemplate updatePath(IPath newPath) {
		return new InputTemplate(inputTemplate, newPath, line);
	}
	
	@Override
	public int line() {
		return line;
	}
	
	@Override
	public String externalid() {
		return inputTemplate;
	}

}
