package pp.eclipse.open.parse.template;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="output-template", namespace="http://www.polopoly.com/polopoly/cm/app/xml")
public class OutputTemplate {
    @XmlAttribute(name="name", namespace="http://www.polopoly.com/polopoly/cm/app/xml")
    public String externalid;

    @Override
    public String toString() {
        return "OutputTemplate [externalid=" + externalid + "]";
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
        OutputTemplate other = (OutputTemplate) obj;
        if (externalid == null) {
            if (other.externalid != null)
                return false;
        } else if (!externalid.equals(other.externalid))
            return false;
        return true;
    }
}
