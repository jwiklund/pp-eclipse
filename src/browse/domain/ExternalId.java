package browse.domain;

import org.eclipse.core.runtime.IPath;

public class ExternalId {

	private final String externalid;
	private final IPath path;
	private final int line;

	public ExternalId(String externalid, IPath path, int line) {
		this.externalid = externalid;
		this.path = path;
		this.line = line;
	}

	@Override
	public String toString() {
		return "ExternalId [externalid=" + externalid + ", path=" + path
				+ ", line=" + line + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((externalid == null) ? 0 : externalid.hashCode());
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
		ExternalId other = (ExternalId) obj;
		if (externalid == null) {
			if (other.externalid != null)
				return false;
		} else if (!externalid.equals(other.externalid))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
}
