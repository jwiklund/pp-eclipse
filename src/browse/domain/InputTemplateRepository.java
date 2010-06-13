package browse.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

public class InputTemplateRepository {

	private Logger logger;
	private IResource root;
	
	private List<InputTemplate> templates = null;
	private int estimatedSourceFiles = -1;
	private Pattern xmlFiles = Pattern.compile(".*\\.xml");
	
	public InputTemplateRepository(Logger logger, IResource root) 
	{
		this.logger = logger;
		this.root = root;
	}

	public synchronized int estimatedXMLFiles() 
	{
		if (estimatedSourceFiles == -1) {
			estimatedSourceFiles = countSourceFiles(logger, root, xmlFiles);
		}
		return estimatedSourceFiles;
	}
	
	public synchronized List<InputTemplate> templates(IProgressMonitor progress) 
	{
	    if (templates == null) {
	        templates = readTemplates(logger, progress, root, xmlFiles);
	    }
	    return templates;
	}

	private static List<InputTemplate> readTemplates(final Logger logger, final IProgressMonitor progress, final IResource resource, final Pattern pattern) 
	{
	    final List<InputTemplate> result = new ArrayList<InputTemplate>();
	    IResourceProxyVisitor visitor = new IResourceProxyVisitor() {
            @Override
            public boolean visit(IResourceProxy proxy) throws CoreException {
                if (pattern.matcher(proxy.getName()).matches()) {
                    readTemplates(logger, proxy, result);
                    progress.worked(1);
                }
                return true;
            }
        };
        try {
            resource.accept(visitor, 0);
        } catch (CoreException e) {
            logger.log(Level.FINE, "readTemplates failed", e);
        }
	    return result;
    }
	
	private static void readTemplates(Logger logger, IResourceProxy proxy, List<InputTemplate> result) 
	{
	    if (proxy.getType() != IResource.FILE) 
	        return ;
	    IPath fullPath = proxy.requestFullPath();
	    IFile file = (IFile) proxy.requestResource();
	    InputStream contents = null;
        try {
            contents = file.getContents();
        } catch (CoreException e) {
            logger.log(Level.FINER, "Could not get input stream for resource", e);
        }
        if (contents == null) {
            return ;
        }
        String charset = "UTF-8";
        try {
            charset = file.getCharset();
        } catch (CoreException e) {
            logger.log(Level.FINE, "Failed to get charset", e);
        }
	    try {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(contents, charset));
	        int[] lineno = new int[1];
	        String topElement = readTopElement(reader, lineno);
	        if (!"template-definition".equalsIgnoreCase(topElement)) {
	            return;
	        }
	        String line = null;
	        Pattern pattern = Pattern.compile("<input-template[^>]+name=\"([^\"]+)\"");
	        while ((line = reader.readLine()) != null) {
	            lineno[0] = lineno[0] + 1;
	            Matcher matcher = pattern.matcher(line);
	            while (matcher.find()) {
	                result.add(new InputTemplate(matcher.group(1), fullPath, lineno[0]));
	            }
	        }
	    } catch (UnsupportedEncodingException e) {
	        logger.log(Level.FINE, "Bad charset " + charset, e);
        } catch (IOException e) {
            logger.log(Level.FINER, "Failed reading file", e);
        } finally {
	        try {
	            contents.close();
            } catch (IOException e) {
                logger.log(Level.FINEST, "Unexpected exception on close", e);
            }
	    }
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

    private static int countSourceFiles(final Logger logger, final IResource resource, final Pattern pattern) 
	{
		final int[] result = new int[1];
		IResourceProxyVisitor visitor = new IResourceProxyVisitor() {
			@Override
			public boolean visit(IResourceProxy proxy) throws CoreException {
				if (pattern.matcher(proxy.getName()).matches()) {
					result[0] = result[0] + 1;
				}
				return true;
			}
		};
		try {
			resource.accept(visitor, 0);
		} catch (CoreException e) {
			logger.log(Level.FINE, "countSourceFiles failed", e);
		}
		return result[0];
	}
	
	
	
	
}
