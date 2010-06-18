package pp.eclipse.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import pp.eclipse.parse.Template;
import pp.eclipse.parse.TemplateParser;

public class InputTemplateRepository {

	private Logger logger;
	private IContainer root;
	
	private List<InputTemplate> templates = null;
	private Map<IPath, TemplateDefinition> templateDefinitions = new HashMap<IPath, TemplateDefinition>();
	private volatile boolean dirty;
	private int estimatedSourceFiles = -1;
	private Pattern xmlFiles = Pattern.compile(".*\\.xml");
	private IResourceChangeListener resourceListener = createChangeListener();
	
	public InputTemplateRepository(Logger logger, IContainer root) 
	{
		this.logger = logger;
		this.root = root;
	}

    public synchronized int estimatedXMLFiles() 
	{
	    if (estimatedSourceFiles == -1) {
	        estimatedSourceFiles = countSourceFiles();
	    }
		return estimatedSourceFiles;
	}
	
	public synchronized List<InputTemplate> templates(IProgressMonitor progress) 
	{
	    if (templates == null) {
	        dirty = false;
	        templates = readTemplates(progress);
	    } else if (dirty) {
	        dirty = false;
	        templates = readTemplates(progress);
	    }
	    return templates;
	}
	
	    
	public synchronized boolean validate(InputTemplate template) {
	    TemplateDefinition def = templateDefinitions.get(template.path);
	    if (def == null) {
	        IResource resource = root.findMember(template.path);
	        if (resource == null) {
	            return false;
	        }
	        def = readTemplates(resource.createProxy());
	    }
	    if (def == null) {
	        return false;
	    }
	    for (InputTemplate templateInFile : def.templates) {
	        if (templateInFile.equals(template)) {
                template.line = templateInFile.line;
                return true;
            }
	    }
        return false;
    }
    
    public synchronized void refresh() 
    {
        dirty = true;
    }

    public IResourceChangeListener getResourceListener() 
    {
        return resourceListener;
    }

	private List<InputTemplate> readTemplates(final IProgressMonitor progress) 
	{
	    final List<InputTemplate> result = new ArrayList<InputTemplate>();
	    final List<IPath> visited = new ArrayList<IPath>();
	    IResourceProxyVisitor visitor = new IResourceProxyVisitor() {
            @Override
            public boolean visit(IResourceProxy proxy) throws CoreException {
                if (xmlFiles.matcher(proxy.getName()).matches()) {
                    TemplateDefinition def = readTemplates(proxy);
                    if (def != null) {
                        result.addAll(def.templates);
                        visited.add(proxy.requestFullPath());
                    }
                    if (progress != null) {
                        progress.worked(1);
                    }
                }
                return true;
            }
        };
        try {
            root.accept(visitor, 0);
        } catch (CoreException e) {
            logger.log(Level.FINE, "readTemplates failed", e);
        }
        HashSet<IPath> allKeys = new HashSet<IPath>(templateDefinitions.keySet());
        allKeys.removeAll(visited);
        for (IPath path : allKeys) {
            templateDefinitions.remove(path);
        }
	    return result;
    }
	
	private TemplateDefinition readTemplates(IResourceProxy proxy) 
	{
	    if (proxy.getType() != IResource.FILE) 
	        return null;
	    IPath fullPath = proxy.requestFullPath();
        TemplateDefinition cached = templateDefinitions.get(fullPath);
	    if (cached != null) {
	        if (cached.modified >= proxy.getModificationStamp()) {
	            return cached;
	        }
	        templateDefinitions.remove(fullPath);
	    }
	    IFile file = (IFile) proxy.requestResource();
	    InputStream contents = null;
        try {
            contents = file.getContents();
        } catch (CoreException e) {
            logger.log(Level.FINER, "Could not get input stream for resource", e);
        }
        if (contents == null) {
            return null;
        }
        String charset = "UTF-8";
        try {
            charset = file.getCharset();
        } catch (CoreException e) {
            logger.log(Level.FINE, "Failed to get charset", e);
        }
	    try {
	        List<Template> templates = TemplateParser.parse(contents, charset);
	        List<InputTemplate> inputs = new ArrayList<InputTemplate>();
	        for (Template template : templates) {
	        	inputs.add(new InputTemplate(template.inputTemplate, fullPath, template.line));
	        }
	        TemplateDefinition templateDefinition = new TemplateDefinition(fullPath, file.getModificationStamp(), inputs);
	        templateDefinitions.put(fullPath, templateDefinition);
            return templateDefinition;
	    } catch (UnsupportedEncodingException e) {
	        logger.log(Level.FINE, "Bad charset " + charset, e);
	        return null;
        } catch (IOException e) {
            logger.log(Level.FINER, "Failed reading file", e);
            return null;
        } finally {
	        try {
	            contents.close();
            } catch (IOException e) {
                logger.log(Level.FINEST, "Unexpected exception on close", e);
            }
	    }
	}

    private int countSourceFiles() 
	{
		final int[] result = new int[1];
		IResourceProxyVisitor visitor = new IResourceProxyVisitor() {
			@Override
			public boolean visit(IResourceProxy proxy) throws CoreException {
				if (xmlFiles.matcher(proxy.getName()).matches()) {
					result[0] = result[0] + 1;
				}
				return true;
			}
		};
		try {
			root.accept(visitor, 0);
		} catch (CoreException e) {
			logger.log(Level.FINE, "countSourceFiles failed", e);
		}
		return result[0];
	}
    
    private IResourceChangeListener createChangeListener() {
        return new IResourceChangeListener() {
            @Override
            public void resourceChanged(IResourceChangeEvent event) {
                refresh();
            }
        };
    }
}
