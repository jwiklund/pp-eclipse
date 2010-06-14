package browse.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class ExternalIdRepository {

	private final Logger logger;
	private final IContainer root;
	
	private Pattern xmlFiles = Pattern.compile(".*\\.xml");

	public ExternalIdRepository(Logger logger, IContainer root) {
		this.logger = logger;
		this.root = root;
	}

	public List<ExternalId> externals() {
		List<ExternalId> result = new ArrayList<ExternalId>();
		try {
			root.accept(createVisitor(result), 0);
		} catch (CoreException e) {
			logger.log(Level.FINE, "readExternals failed", e);
		}
		return result;
	}
	
	private IResourceProxyVisitor createVisitor(final List<ExternalId> result) {
		return new IResourceProxyVisitor() {
			@Override
			public boolean visit(IResourceProxy proxy) throws CoreException {
				if (xmlFiles.matcher(proxy.getName()).matches()) {
					result.addAll(readExternals(proxy.requestFullPath(), proxy.requestResource()));
				}
				return true;
			}
		};
	}
	
	private List<ExternalId> readExternals(IPath requestFullPath, IResource requestResource) 
	{
		ArrayList<ExternalId> result = new ArrayList<ExternalId>();
		return result;
	}


}
