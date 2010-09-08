package pp.eclipse.ui;

import java.util.Comparator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

import pp.eclipse.Activator;
import pp.eclipse.open.Container;
import pp.eclipse.open.Item;
import pp.eclipse.open.MementoHandler;
import pp.eclipse.open.Repository;

public class SelectionDialog extends FilteredItemsSelectionDialog 
{
	private final Repository repository;
	private final MementoHandler memento;

    public SelectionDialog(Shell shell, Repository repository, MementoHandler memento)
    {
        super(shell, false);
        this.repository = repository;
        this.memento = memento;
        setSelectionHistory(new DefiningSelectionHistory());
        setListLabelProvider(new DefiningListLabelProvider());
    }
    
	public Item select() {
    	setInitialPattern("p.");
        open();
        Object[] result = getResult();
        if (result == null) {
            return null;
        }
        return (Item) result[0];
    }
    
    protected class DefiningSelectionHistory extends SelectionHistory {

		@Override
		protected Object restoreItemFromMemento(IMemento imemento) {
			return memento.restore(imemento);
		}

		@Override
		protected void storeItemToMemento(Object item, IMemento imemento) {
		    memento.store(item, imemento);
		}
    }
    
    protected class  DefiningListLabelProvider extends LabelProvider 
    	implements ILabelProviderListener, IStyledLabelProvider 
    {

		@Override
		public StyledString getStyledText(Object element) {
			if (element instanceof Item) {
			    Item item = (Item) element;
	            StyledString result = new StyledString(item.externalid());
	            if (isDuplicateElement(element)) {
	                String path = " - " + item.path().makeRelative().toString();
	                result.append(new StyledString(path, StyledString.QUALIFIER_STYLER));
	            }
	            return result;
	        }
	        return new StyledString("?");
		}

		@Override
		public void labelProviderChanged(LabelProviderChangedEvent event) {
		}
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
                return matches(((Item) arg0).externalid());
            }
            
            @Override
            public boolean isConsistentItem(Object arg0)
            {
                if (arg0 instanceof Item) {
                    Item item = (Item) arg0;
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
        for (Container items : repository.list(progressMonitor)) {
            for (Item item : items.items()) {
                contentProvider.add(item, filter);
            }
        }
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
        return ((Item) item).externalid();
    }

    @Override
    protected Comparator<Object> getItemsComparator()
    {
        return new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2)
            {
                return ((Item) o1).compareTo((Item) o2);
            }
        };
    }

    @Override
    protected IStatus validateItem(Object arg0)
    {
        return Status.OK_STATUS;
    }
}