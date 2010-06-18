package pp.eclipse.ui;

import java.util.Comparator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

import pp.eclipse.Activator;
import pp.eclipse.cache.CacheStrategy;
import pp.eclipse.common.DefinedItem;
import pp.eclipse.common.DefiningFile;
import pp.eclipse.common.Repository;


public class SelectionDialogTemplate<Container extends DefiningFile<DefinedItem>> extends FilteredItemsSelectionDialog 
{

	private CacheStrategy<DefinedItem, Container> cacheStrategy;
    private Repository<DefinedItem, Container> repository;

    public SelectionDialogTemplate(Shell shell, 
    		CacheStrategy<DefinedItem, Container> cacheStrategy, 
    		Repository<DefinedItem, Container> repository)
    {
        super(shell, false);
        this.cacheStrategy = cacheStrategy;
        this.repository = repository;
        //setTitle("Filtered ContentXml Dialog");
        //setSelectionHistory(new InputTemplateSelectionHistory());
        //setListLabelProvider(new InputTemplateListLabelProvider(this));
    }
    
    @Override
    protected Control createExtendedContentArea(Composite arg0)
    {
        // This method creates an extra content area located above the details. 
        // For now, we will just return null because we don't need any extra fields for this simple example:
        return null;
    }

    @Override
    protected ItemsFilter createFilter()
    {
        return new ItemsFilter() {
            
            @Override
            public boolean matchItem(Object arg0)
            {
                return matches(((DefinedItem) arg0).externalid());
            }
            
            @Override
            public boolean isConsistentItem(Object arg0)
            {
                if (arg0 instanceof DefinedItem) {
                	DefinedItem item = (DefinedItem) arg0;
                    return repository.validate(item);
                }
                return false;
            }
        };
    }

    @Override
    protected void fillContentProvider(AbstractContentProvider contentProvider,
        ItemsFilter filter, IProgressMonitor progressMonitor) throws CoreException
    {
    	cacheStrategy.before(progressMonitor, repository);
        for (DefinedItem item : cacheStrategy.list(progressMonitor, repository)) {
        	contentProvider.add(item, filter);
        }
        cacheStrategy.after(progressMonitor, repository);
    }
    
    private static final String DIALOG_SETTINGS = "FilteredContentXmlDialogSettings";

    @Override
    protected IDialogSettings getDialogSettings()
    {
        IDialogSettings settings = Activator.getDefault().getDialogSettings().getSection(DIALOG_SETTINGS);
        if (settings == null) {
            settings = Activator.getDefault().getDialogSettings().addNewSection(DIALOG_SETTINGS);
        }       
        return settings;
    }

    @Override
    public String getElementName(Object item)
    {
        return ((DefinedItem) item).externalid();
    }

    @Override
    protected Comparator<Object> getItemsComparator()
    {
        return new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2)
            {
                return ((DefinedItem) o1).compareTo((DefinedItem) o2);
            }
        };
    }

    @Override
    protected IStatus validateItem(Object arg0)
    {
        return Status.OK_STATUS;
    }
}