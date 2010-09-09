package pp.eclipse.open.parse;

import java.io.BufferedReader;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import pp.eclipse.open.Item;

public interface Parser {
    public List<Item> parse(BufferedReader reader)
        throws XMLStreamException, JAXBException;
}
