package pp.eclipse.domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import pp.eclipse.common.Repository;
import pp.eclipse.parse.Content;
import pp.eclipse.parse.ContentParser;


public class ExternalIdRepository implements Repository<ExternalId, ContentXML<ExternalId>> {

	private final Logger logger;
	private final IContainer root;
	
	private Pattern xmlFiles = Pattern.compile(".*\\.xml");
	private ContentParser parser = new ContentParser();

	public ExternalIdRepository(Logger logger, IContainer root) {
		this.logger = logger;
		this.root = root;
	}
	
	@Override
	public boolean validate(ExternalId item) {
		return true;
	}

	@Override
	public List<ContentXML<ExternalId>> list(IProgressMonitor monitor) {
		List<ContentXML<ExternalId>> result = new ArrayList<ContentXML<ExternalId>>();
		try {
			root.accept(createVisitor(monitor, result), 0);
		} catch (CoreException e) {
			logger.log(Level.FINE, "readExternals failed", e);
		}
		return result;
	}
	
	private IResourceProxyVisitor createVisitor(final IProgressMonitor monitor, final List<ContentXML<ExternalId>> result) {
		return new IResourceProxyVisitor() {
			@Override
			public boolean visit(IResourceProxy proxy) throws CoreException {
				if (xmlFiles.matcher(proxy.getName()).matches()) {
					result.addAll(readExternals(proxy.requestFullPath(), proxy.requestResource()));
					monitor.worked(1);
				}
				return true;
			}
		};
	}
	
	private List<ContentXML<ExternalId>> readExternals(IPath path, IResource resource) 
	{
		IFile file = (IFile) resource;
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
	        Reader reader = new InputStreamReader(contents, charset);
	        List<Content> contentList = parser.parse(reader);
	        return Collections.singletonList(new ContentXML<ExternalId>(path, resource.getModificationStamp(), convert(path, contentList)));
	    } catch (IOException e) {
	    	logger.log(Level.FINER, "Failed reading file", e);
	    } catch (XMLStreamException e) {
	    	logger.log(Level.FINER, "Failed parsing file", e);
	    } catch (JAXBException e) {
	    	logger.log(Level.FINER, "Failed parsing file", e);
		} finally {
	    	try {
	    		contents.close();
	    	} catch (IOException e) {
	    		logger.log(Level.FINEST, "Unexpected exception on close", e);
	    	}
	    }
		return Collections.emptyList();
	}

	private List<ExternalId> convert(IPath path, List<Content> contentList) {
		ArrayList<ExternalId> result = new ArrayList<ExternalId>();
		for (Content content : contentList) {
			if (content != null && content.metadata != null && content.metadata.contentid != null) {
				result.add(new ExternalId(content.metadata.contentid.externalid, path, content.foundOnLine));
			}
		}
		return result;
	}
}
