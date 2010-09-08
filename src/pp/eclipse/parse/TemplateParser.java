package pp.eclipse.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pp.eclipse.open.Item;
import pp.eclipse.open.ItemType;

public class TemplateParser implements Parser {
	
	public List<Item> parse(BufferedReader reader) 
		throws IOException 
	{
        int[] lineno = new int[1];
        String topElement = readTopElement(reader, lineno);
        if (!"template-definition".equalsIgnoreCase(topElement)) {
            return Collections.emptyList();
        }
        String line = null;
        Pattern pattern = Pattern.compile("<input-template[^>]+name=\"([^\"]+)\"");
        List<Item> templates = new ArrayList<Item>();
        while ((line = reader.readLine()) != null) {
            lineno[0] = lineno[0] + 1;
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                templates.add(new Item(ItemType.InputTemplate, matcher.group(1), null, lineno[0]));
            }
        }
        return templates;
	}
	
	private static String readTopElement(BufferedReader reader, int[] lineno) 
		throws IOException 
	{
        Pattern pattern = Pattern.compile("<([^?>][^ >]+)[^>]+>");
        String line = null;
        while ((line = reader.readLine()) != null) {
            lineno[0] = lineno[0] + 1;
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }
}
