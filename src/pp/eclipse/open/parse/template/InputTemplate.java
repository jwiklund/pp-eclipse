package pp.eclipse.open.parse.template;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

@XmlRootElement(name="input-template", namespace="http://www.polopoly.com/polopoly/cm/app/xml")
public class InputTemplate {
	/* JAXB is broken stupid (at least the version I tested this on) ... 
	 * Adding an @XmlAttribute forced it to add a ns: name even when it uses the default namespace.
	 * When parsing, it does not understand that the default namespace should be applied to non 
	 * qualified attributes (and if you add a namespace it goes totally bonkers and gives me nothing). 
	 */
	@XmlAnyAttribute
	public Map<QName, String> attributes = new HashMap<QName, String>();
    //@XmlAttribute(name="name", namespace="http://www.polopoly.com/polopoly/cm/app/xml")
    //public String externalid;

    public InputTemplate(String externalid) {
    	attributes.put(externalidQName(), externalid);
	}
    
    public InputTemplate() {
    }

    /* I think it really should be qualified, but lets fallback to no namespace since that seems
     * to work on my machine...
     */
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
        return "InputTemplate [externalid=" + externalid() + "]";
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
        InputTemplate other = (InputTemplate) obj;
        if (externalid() == null) {
            if (other.externalid() != null)
                return false;
        } else if (!externalid().equals(other.externalid()))
            return false;
        return true;
    }
}
