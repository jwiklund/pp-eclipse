package pp.eclipse.parse;

import java.io.BufferedReader;
import java.util.List;

public interface Parser<Entry> {
	List<Entry> parse(BufferedReader reader) throws Exception ;
}
