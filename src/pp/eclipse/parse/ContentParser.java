package pp.eclipse.parse;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import pp.eclipse.domain.ExternalId;
import pp.eclipse.parse.content.Content;

public class ContentParser implements Parser<ExternalId> {
	
	private final Unmarshaller unmarshaller;

	private final XMLInputFactory xmlif = XMLInputFactory.newInstance();

	public ContentParser(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

	public ContentParser() {
		this(createDefaultContext());
	}

	private static Unmarshaller createDefaultContext() {
		try {
			return JAXBContext.newInstance(Content.class).createUnmarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException(e); // This should have been tested
		}
	}

	public List<ExternalId> parse(BufferedReader reader) 
		throws XMLStreamException, JAXBException 
	{
		XMLEventReader source = xmlif.createXMLEventReader(reader);
		XMLEvent documentElement = xmlif.createFilteredReader(source, createStartFilter()).peek();
		if (!startOfElement("batch", documentElement.asStartElement())) {
			return Collections.emptyList();
		}
		int[] lineOfContentStart = new int[1];
		lineOfContentStart[0] = -1;
		XMLEventReader contentStart = xmlif.createFilteredReader(source, createContentFilter(lineOfContentStart));
		List<ExternalId> contents = new ArrayList<ExternalId>();
		while (contentStart.peek() != null) {
			Content content = (Content) unmarshaller.unmarshal(source);
			addLineInfo(content, lineOfContentStart);
			if (content != null && content.metadata != null && 
					content.metadata.contentid != null && 
					content.metadata.contentid.externalid != null) {
				contents.add(new ExternalId(content.metadata.contentid.externalid, null, content.foundOnLine));
			}
		}
		return contents;
	}

	private void addLineInfo(Content content, int[] lineOfContentStart) {
		if (content != null) {
			content.foundOnLine = lineOfContentStart[0];
		}
		lineOfContentStart[0] = -1;
	}
	
	private EventFilter createStartFilter() {
		return new EventFilter() {
			@Override
			public boolean accept(XMLEvent event) {
				return event.isStartElement();
			}
			
		};
	}

	private EventFilter createContentFilter(final int[] lineOfContentStart) {
		return new EventFilter() {
			@Override
			public boolean accept(XMLEvent event) {
				if (event.isStartElement() && startOfElement("content", event.asStartElement())) {
					lineOfContentStart[0] = event.getLocation().getLineNumber();
					return true;
				}
				return false;
			}
		};
	}
	
	private boolean startOfElement(String elementName, StartElement event) {
		return elementName.equals(event.getName().getLocalPart()) &&
			"http://www.polopoly.com/polopoly/cm/xmlio".equals(event.getName().getNamespaceURI());
	}
}
