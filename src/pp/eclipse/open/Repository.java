package pp.eclipse.open;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import pp.eclipse.open.parse.Parser;

public class Repository
{
    private final IContainer root;
    private final Parser parser;
    private final Map<IPath, Container> cache = new HashMap<IPath, Container>();

    public Repository(IContainer root, Parser parser)
    {
        this.root = root;
        this.parser = parser;
    }

    public List<Container> list(final IProgressMonitor monitor)
        throws CoreException
    {
        final List<Container> containers = new ArrayList<Container>();
        monitor.beginTask("Searching", cache.size() != 0 ? cache.size() : 1000);
        //System.out.println("Started");
        //final long[] counter = new long[1];
        //long started = System.currentTimeMillis();
        root.accept(new IResourceProxyVisitor() {
            public boolean visit(IResourceProxy proxy) throws CoreException {
                if (monitor.isCanceled()) {
                    return false;
                }
                if (proxy.getName().matches(".*\\.xml")) {
                    //counter[0] = counter[0] + 1;
                    Container container = cache.get(proxy.requestFullPath());
                    if (container != null && container.modified() != proxy.getModificationStamp()) {
                        container = null;
                    }
                    if (container == null) {
                        IResource resource = proxy.requestResource();
                        if (resource instanceof IFile) {
                            container = read((IFile) resource);
                        }
                        if (container != null) {
                            cache.put(container.path(), container);
                        }
                    }
                    if (container != null) {
                        containers.add(container);
                        monitor.worked(1);
                    }
                }
                return true;
            }
        }, 0);
        //System.out.println("Done " + ((System.currentTimeMillis() - started) / 1000.0));
        //System.out.println("XML files " + counter[0]);
        //System.out.println("Read " + cache.size());
        return containers;
    }

    public boolean validate(Item item) {
        return true;
    }

    private Container read(IFile iResource)
    {
        InputStream content = null;
        try {
            content = iResource.getContents();
            String charset = iResource.getCharset();
            if (charset == null) {
                charset = "UTF8";
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(content, charset));
                List<Item> parsed = parser.parse(reader);
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
        } catch (CoreException e) {
            // TODO: If is out of sync, try to refresh
            Logger.getLogger("pp.eclipse.read").fine("Read of " + iResource.getName() + " failed: " + e.getMessage());
            Logger.getLogger("pp.eclipse.parse").log(Level.FINER, "Read failure", e);
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
