package pp.eclipse.open.parse;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import pp.eclipse.open.Item;
import pp.eclipse.open.ItemType;

public class StreamParser implements Parser
{
    private final XMLInputFactory xmlif = XMLInputFactory.newInstance();

    public StreamParser() {
        xmlif.setXMLResolver(new BogusResolver());
	}

    public ParserResult parse(BufferedReader reader)
        throws XMLStreamException
    {
        XMLEventReader source = xmlif.createXMLEventReader(reader);
        List<Item> result = new ArrayList<Item>();
        Map<String, Set<String>> itClasses = new HashMap<String, Set<String>>();
        StateHandler handler = new StateHandler(result, itClasses);
        List<QName> path = new ArrayList<QName>();
        for (; source.hasNext(); ) {
            XMLEvent nextEvent = source.nextEvent();
            if (nextEvent.isStartElement()) {
                path.add(nextEvent.asStartElement().getName());
            }
            if (!handler.handle(path, nextEvent)) {
                break;
            }
            if (nextEvent.isEndElement()) {
                QName thisName = nextEvent.asEndElement().getName();
                boolean ok = false;
                if (path.size() > 0) {
                    if (path.get(path.size()-1).equals(thisName)) {
                        path.remove(path.size()-1);
                        ok = true;
                    }
                }
                if (!ok) {
                    throw new XMLStreamException("Missmatched end document " + thisName + " at line " + nextEvent.getLocation().getLineNumber() + " path " + path);
                }
            }
        }
        return new ParserResult(result, itClasses);
    }

    private enum State {
        Initial, Batch, Templates, InputTemplate
    }

    private static class Elements {
        private final static String nsBatch = "http://www.polopoly.com/polopoly/cm/xmlio";
        private final static String nsTempl = "http://www.polopoly.com/polopoly/cm/app/xml";
        public final static QName Batch = new QName(nsBatch, "batch");
        public final static List<QName> ExternalId = Arrays.asList(Batch,
                new QName(nsBatch, "content"),
                new QName(nsBatch, "metadata"),
                new QName(nsBatch, "contentid"),
                new QName(nsBatch, "externalid"));
        public final static QName Templates = new QName(nsTempl, "template-definition");
        public final static QName InputTemplate = new QName(nsTempl, "input-template");
        public final static QName OutputTemplate = new QName(nsTempl, "output-template");
        public final static QName NameAttribute = new QName("name");
        public final static Set<QName> InputTemplateClasses = new HashSet<QName>(Arrays.asList(
                new QName(nsTempl, "policy"),
                new QName(nsTempl, "editor"),
                new QName(nsTempl, "viewer")));
    }

    private static class StateHandler
    {
        State state = State.Initial;
        List<Item> result;
        private Item currentInputTemplate;
        Map<String, Set<String>> itClasses;

        public StateHandler(List<Item> result, Map<String, Set<String>> itClasses) {
            this.result = result;
            this.itClasses = itClasses;
        }

        public boolean handle(List<QName> path, XMLEvent event) {
            switch (state) {
            case Initial:
                if (event.isStartElement()) {
                    if (Elements.Batch.equals(path.get(0))) {
                        state = State.Batch;
                    } else if (Elements.Templates.equals(path.get(0))) {
                        state = State.Templates;
                    } else {
                        return false;
                    }
                }
                break;
            case Batch:
                if (event.isCharacters() && path.equals(Elements.ExternalId)) {
                    String data = event.asCharacters().getData().trim();
                    if (data.length() > 0) {
                        result.add(new Item(ItemType.Content, data, null, event.getLocation().getLineNumber()));
                    }
                }
                break;
            case Templates:
                if (event.isStartElement() && path.size() == 2) {
                    if (path.get(1).equals(Elements.InputTemplate)) {
                        String externalid = getAttribute(event);
                        if (externalid != null) {
                            currentInputTemplate = new Item(ItemType.InputTemplate, externalid, null, event.getLocation().getLineNumber());
                            state = State.InputTemplate;
                            result.add(currentInputTemplate);
                        }
                    } else if (path.get(1).equals(Elements.OutputTemplate)) {
                        String externalid = getAttribute(event);
                        if (externalid != null) {
                            result.add(new Item(ItemType.OutputTemplate, externalid, null, event.getLocation().getLineNumber()));
                        }
                    }
                }
                break;
            case InputTemplate:
                if (event.isEndElement() && path.size() == 2) {
                    state = State.Templates;
                    currentInputTemplate = null;
                } else if (path.size() == 3 && Elements.InputTemplateClasses.contains(path.get(2)) && event.isCharacters()) {
                    String data = event.asCharacters().getData().trim();
                    if (data.length() > 0) {
                        if (!itClasses.containsKey(currentInputTemplate.externalid())) {
                            itClasses.put(currentInputTemplate.externalid(), new HashSet<String>());
                        }
                        itClasses.get(currentInputTemplate.externalid()).add(data);
                    }
                }
                break;
            }
            return true;
        }

        private String getAttribute(XMLEvent event) {
            Attribute attribute = event.asStartElement().getAttributeByName(Elements.NameAttribute);
            if (attribute != null) {
                return attribute.getValue().trim();
            }
            return null;
        }
    }
}
