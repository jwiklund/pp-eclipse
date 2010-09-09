package pp.eclipse.open.parse;

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

import pp.eclipse.open.Item;
import pp.eclipse.open.ItemType;
import pp.eclipse.open.parse.content.Content;
import pp.eclipse.open.parse.template.InputTemplate;
import pp.eclipse.open.parse.template.OutputTemplate;

public class Parser
{
	private final Unmarshaller unmarshaller;

	private final XMLInputFactory xmlif = XMLInputFactory.newInstance();

	public Parser(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

	public Parser() {
		this(createDefaultContext());
	}

	private static Unmarshaller createDefaultContext() {
		try {
			return JAXBContext.newInstance(Content.class, InputTemplate.class, OutputTemplate.class).createUnmarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException(e); // This should have been tested
		}
	}

	public List<Item> parse(BufferedReader reader) 
		throws XMLStreamException, JAXBException 
	{
		XMLEventReader source = xmlif.createXMLEventReader(reader);
		XMLEvent documentElement = xmlif.createFilteredReader(source, createStartFilter()).peek();
		if (!supportedDocument(documentElement.asStartElement())) {
			return Collections.emptyList();
		}
		int[] lineOfContentStart = new int[1];
		lineOfContentStart[0] = -1;
		XMLEventReader contentStart = xmlif.createFilteredReader(source, createContentFilter(lineOfContentStart));
		List<Item> contents = new ArrayList<Item>();
		while (contentStart.peek() != null) {
			Object element = unmarshaller.unmarshal(source);
			Item item = null;
			if (element instanceof Content) {
			    item = convert((Content) element);
			} else if (element instanceof InputTemplate) {
			    item = convert((InputTemplate) element);
			} else if (element instanceof OutputTemplate) {
			    item = convert((OutputTemplate) element);
			}
			if (item != null) {
			    item = item.line(lineOfContentStart[0]);
			    contents.add(item);
			}
		}
		return contents;
	}

	private Item convert(OutputTemplate element) {
        if (element.externalid() != null) {
            return new Item(ItemType.OutputTemplate, element.externalid(), null, -1);
        }
        return null;
    }

    private Item convert(InputTemplate element) {
        if (element.externalid() != null) {
            return new Item(ItemType.InputTemplate, element.externalid(), null, -1);
        }
        return null;
    }

    private Item convert(Content content) 
	{
	    if (content != null && content.metadata != null && content.metadata.contentid != null && content.metadata.contentid.externalid != null) {
            return new Item(ItemType.Content, content.metadata.contentid.externalid, null, -1);
        }
	    return null;
    }
	
	private EventFilter createStartFilter() {
		return new EventFilter() {
			public boolean accept(XMLEvent event) {
				return event.isStartElement();
			}
			
		};
	}

	private EventFilter createContentFilter(final int[] lineOfContentStart) {
		return new EventFilter() {
			public boolean accept(XMLEvent event) {
				if (event.isStartElement() && supportedElement(event.asStartElement())) {
					lineOfContentStart[0] = event.getLocation().getLineNumber();
					return true;
				}
				return false;
			}

		};
	}
	
	private boolean supportedElement(StartElement startElement) {
	    return startOfElement(startElement, "http://www.polopoly.com/polopoly/cm/xmlio", "content")
	        || startOfElement(startElement, "http://www.polopoly.com/polopoly/cm/app/xml", "input-template")
	        || startOfElement(startElement, "http://www.polopoly.com/polopoly/cm/app/xml", "output-template");
	}
	
	private boolean supportedDocument(StartElement startElement) {
	    return startOfElement(startElement, "http://www.polopoly.com/polopoly/cm/xmlio", "batch")
	        || startOfElement(startElement, "http://www.polopoly.com/polopoly/cm/app/xml", "template-definition");
	}
	
	private boolean startOfElement(StartElement event, String namespace, String local) {
		return local.equals(event.getName().getLocalPart())
			&& namespace.equals(event.getName().getNamespaceURI());
	}
}
