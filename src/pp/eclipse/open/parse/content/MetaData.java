package pp.eclipse.open.parse.content;

import javax.xml.bind.annotation.XmlElement;

public class MetaData {
	@XmlElement(name="contentid", namespace="http://www.polopoly.com/polopoly/cm/xmlio")
	public ContentId contentid;
	
	public MetaData() {
	}

	public MetaData(ContentId contentid) {
		this.contentid = contentid;
	}

	@Override
	public String toString() {
		return "MetaData [contentid=" + contentid + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contentid == null) ? 0 : contentid.hashCode());
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
		MetaData other = (MetaData) obj;
		if (contentid == null) {
			if (other.contentid != null)
				return false;
		} else if (!contentid.equals(other.contentid))
			return false;
		return true;
	}
	
}
