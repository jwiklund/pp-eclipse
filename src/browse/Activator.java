package browse;

import java.util.logging.Logger;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import browse.domain.InputTemplateRepository;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "Browse"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;
    
    private InputTemplateRepository repository;

    /**
     * The constructor
     */
    public Activator()
    {
        Logger logger = Logger.getLogger(PLUGIN_ID);
        repository = new InputTemplateRepository(logger, ResourcesPlugin.getWorkspace().getRoot());
//        IResourceChangeListener listener = new MyResourceChangeReporter();
//        ResourcesPlugin.getWorkspace().addResourceChangeListener(
//           listener, IResourceChangeEvent.POST_CHANGE);
        
        // And import
        
//        The copies in the build directory should be marked as derived (IResource.setDerived()). 
//        Derived resources are then filtered out of the open resource dialog. Whoever is copying those resources should be marking the 
//        copies as derived - the Java builder does this, for example.
    }
    
    public InputTemplateRepository getRepository()
    {
        return repository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
     */
    public void stop(BundleContext context) throws Exception
    {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault()
    {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     * 
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path)
    {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
