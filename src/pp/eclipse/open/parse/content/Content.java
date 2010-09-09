package pp.eclipse.open.parse.content;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="content", namespace="http://www.polopoly.com/polopoly/cm/xmlio")
public class Content {
	@XmlElement(name="metadata", namespace="http://www.polopoly.com/polopoly/cm/xmlio")
	public MetaData metadata;

	public Content(MetaData metadata) {
		this.metadata = metadata;
	}

	public Content() {
	}

	@Override
	public String toString() {
		return "Content [metadata=" + metadata + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((metadata == null) ? 0 : metadata.hashCode());
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
		Content other = (Content) obj;
		if (metadata == null) {
			if (other.metadata != null)
				return false;
		} else if (!metadata.equals(other.metadata))
			return false;
		return true;
	}
}
