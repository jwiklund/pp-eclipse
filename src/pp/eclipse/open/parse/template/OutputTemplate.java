package pp.eclipse.open.parse.template;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

@XmlRootElement(name="output-template", namespace="http://www.polopoly.com/polopoly/cm/app/xml")
public class OutputTemplate {
    @XmlAnyAttribute
    public Map<QName, String> attributes = new HashMap<QName, String>();

    public OutputTemplate(String externalid) {
    	attributes.put(externalidQName(), externalid);
	}

	public OutputTemplate() {
    }

	public String externalid() {
    	String string = attributes.get(externalidQName());
    	if (string == null) {
    		string = attributes.get(new QName("name"));
    	}
		return string;
    }

	private QName externalidQName() {
		return new QName("http://www.polopoly.com/polopoly/cm/app/xml", "name");
	}

    @Override
    public String toString() {
        return "OutputTemplate [externalid=" + externalid() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((externalid() == null) ? 0 : externalid().hashCode());
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
        if (externalid() == null) {
            if (other.externalid() != null)
                return false;
        } else if (!externalid().equals(other.externalid()))
            return false;
        return true;
    }
}
