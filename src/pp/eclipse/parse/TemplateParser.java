package pp.eclipse.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateParser {
	
	public static List<Template> parse(InputStream input, String charset) 
		throws IOException 
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset));
        int[] lineno = new int[1];
        String topElement = readTopElement(reader, lineno);
        if (!"template-definition".equalsIgnoreCase(topElement)) {
            return Collections.emptyList();
        }
        String line = null;
        Pattern pattern = Pattern.compile("<input-template[^>]+name=\"([^\"]+)\"");
        List<Template> templates = new ArrayList<Template>();
        while ((line = reader.readLine()) != null) {
            lineno[0] = lineno[0] + 1;
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                templates.add(new Template(matcher.group(1), lineno[0]));
            }
        }
        return templates;
	}
	
	private static String readTopElement(BufferedReader reader, int[] lineno) throws IOException {
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
