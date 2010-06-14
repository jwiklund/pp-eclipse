package browse.parse;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class ContentParser {
	private final Unmarshaller unmarshaller;
	
	private final XMLInputFactory xmlif = XMLInputFactory.newInstance();

	public ContentParser(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

	public List<Content> parse(Reader reader) 
		throws XMLStreamException, JAXBException 
	{
		XMLEventReader source = xmlif.createXMLEventReader(reader);
		XMLEventReader contentStart = xmlif.createFilteredReader(source, createContentFilter());
		List<Content> contents = new ArrayList<Content>();
		while (contentStart.peek() != null) {
			contents.add((Content) unmarshaller.unmarshal(source));
		}
		System.out.println(contents);
		return contents;
	}

	private EventFilter createContentFilter() {
		return new EventFilter() {
			@Override
			public boolean accept(XMLEvent event) {
				return event.isStartElement() && "content".equals(event.asStartElement().getName().getLocalPart());
			}
		};
	}

}
