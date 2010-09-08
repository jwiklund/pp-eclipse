package pp.eclipse.parse;

import java.io.BufferedReader;
import java.util.List;

import pp.eclipse.open.Item;

public interface Parser {
	List<Item> parse(BufferedReader reader) throws Exception ;
}
