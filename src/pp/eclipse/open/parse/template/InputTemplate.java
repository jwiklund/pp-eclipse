package pp.eclipse.open.parse.template;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="input-template", namespace="http://www.polopoly.com/polopoly/cm/app/xml")
public class InputTemplate {
    @XmlAttribute(name="name", namespace="http://www.polopoly.com/polopoly/cm/app/xml")
    public String externalid;

    public InputTemplate(String externalid) {
    	this.externalid = externalid;
	}
    
    public InputTemplate() {
    }

	@Override
    public String toString() {
        return "InputTemplate [externalid=" + externalid + "]";
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
        InputTemplate other = (InputTemplate) obj;
        if (externalid == null) {
            if (other.externalid != null)
                return false;
        } else if (!externalid.equals(other.externalid))
            return false;
        return true;
    }
}
