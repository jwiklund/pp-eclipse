package pp.eclipse.open.parse.content;

import javax.xml.bind.annotation.XmlElement;


public class ContentId {
	@XmlElement(name="externalid", namespace="http://www.polopoly.com/polopoly/cm/xmlio")
	public String externalid;

	public ContentId(String externalid) {
		this.externalid = externalid;
	}

	public ContentId() {
	}

	@Override
	public String toString() {
		return "ContentId [externalid=" + externalid + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((externalid == null) ? 0 : externalid.hashCode());
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
		ContentId other = (ContentId) obj;
		if (externalid == null) {
			if (other.externalid != null)
				return false;
		} else if (!externalid.equals(other.externalid))
			return false;
		return true;
	}
}
