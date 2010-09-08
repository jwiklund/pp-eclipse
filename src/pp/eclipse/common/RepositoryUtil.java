package pp.eclipse.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import pp.eclipse.domain.Container;
import pp.eclipse.domain.Item;
import pp.eclipse.parse.Parser;

public class RepositoryUtil 
{
    public static Container read(IFile iResource, Parser... parsers) 
        throws CoreException 
    {
        InputStream content = null;
        try {
            content = iResource.getContents();
            String charset = iResource.getCharset();
            if (charset == null) {
                charset = "UTF8";
            }
            try {
                List<Item> parsed = Collections.emptyList();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content, charset));
                reader.mark(1024);
                for (Parser parser : parsers) {
                    reader.reset();
                    parsed = parser.parse(reader);
                    if (parsed.size() > 0) { 
                        break;
                    }
                }
                List<Item> updated = new ArrayList<Item>();
                IPath fullPath = iResource.getFullPath();
                for (Item parse : parsed) {
                    updated.add(parse.path(fullPath));
                }
                return new Container(fullPath, iResource.getModificationStamp(), updated);
            } catch (UnsupportedEncodingException e) {
                Logger.getLogger("pp.eclipse.parse").fine("Parse of " + iResource.getName() + " failed: " + e.getMessage());
                Logger.getLogger("pp.eclipse.parse").log(Level.FINER, "Parse failure", e);
            } catch (Exception e) {
                Logger.getLogger("pp.eclipse.parse").fine("Parse of " + iResource.getName() + " failed: " + e.getMessage());
                Logger.getLogger("pp.eclipse.parse").log(Level.FINER, "Parse failure", e);
            }
        } finally {
            if (content != null) {
                try { 
                    content.close();
                } catch (Exception e) {
                    // Skip
                }
            }
        }
        return null;
    }
}
