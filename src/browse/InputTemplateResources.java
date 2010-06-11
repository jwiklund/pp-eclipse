package browse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;

import browse.ui.InputTemplate;

public class InputTemplateResources {
    private IWorkspaceRoot root;

    public InputTemplateResources(IWorkspaceRoot root)
    {
        this.root = root;
    }
    
    public List<ContentXmlFile> contentXMLFiles() throws CoreException 
    {
        ArrayList<ContentXmlFile> result = new ArrayList<ContentXmlFile>();
        root.accept(getXmlFiles(result), 0);
        return result;
    }

    private static IResourceProxyVisitor getXmlFiles(final ArrayList<ContentXmlFile> result)
    {
        return new IResourceProxyVisitor() {

            @Override
            public boolean visit(IResourceProxy resource)
            {
                if (resource.getName().endsWith(".xml")) {
                    result.add(new ContentXmlFile(resource.getName(), resource.requestFullPath()));
                }
                return true ;
            }
        };
    }

    public List<InputTemplate> inputTemplates(ContentXmlFile file) throws CoreException
    {
        IResource resource = root.findMember(file.path);
        List<InputTemplate> result = new ArrayList<InputTemplate>();
        if (resource instanceof IFile) {
            IFile xmlFile = (IFile) resource;
            InputStream content = xmlFile.getContents();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(content, xmlFile.getCharset(true)));
                String line = null;
                Pattern pattern = Pattern.compile("<input-template[^>]+name=\"([^\"]+)\"");
                int lineno = 0;
                while ((line = reader.readLine()) != null) {
                    lineno = lineno + 1;
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        result.add(new InputTemplate(matcher.group(1), file.path, lineno));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    content.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return result;
    }
}
