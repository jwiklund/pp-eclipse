package pp.eclipse.open.parse;

import java.io.BufferedReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import pp.eclipse.open.Item;

public interface Parser {
    public static class ParserResult {
        public final List<Item> items;
        public final Map<String, Set<String>> inputTemplateClasses;

        public ParserResult(List<Item> items, Map<String, Set<String>> inputTemplateClasses) {
            this.items = items;
            this.inputTemplateClasses = inputTemplateClasses;
        }
        public ParserResult(List<Item> items) {
            this.items = items;
            this.inputTemplateClasses = Collections.emptyMap();
        }
        public ParserResult() {
            this.items = Collections.emptyList();
            this.inputTemplateClasses = Collections.emptyMap();
        }
    }

    public ParserResult parse(BufferedReader reader) throws XMLStreamException, JAXBException;
}
