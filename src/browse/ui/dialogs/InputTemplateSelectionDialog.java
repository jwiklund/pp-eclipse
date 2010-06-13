package browse.ui.dialogs;

import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

import browse.Activator;
import browse.domain.InputTemplate;
import browse.domain.InputTemplateRepository;

public class InputTemplateSelectionDialog extends FilteredItemsSelectionDialog {

    private InputTemplateRepository repository;
    private IContainer root;

    public InputTemplateSelectionDialog(Shell shell, IContainer root, InputTemplateRepository repository)
    {
        super(shell, false);
        this.repository = repository;
        this.root = root;
        setTitle("Filtered ContentXml Dialog");
        setSelectionHistory(new InputTemplateSelectionHistory());
        setListLabelProvider(new InputTemplateListLabelProvider(this));
    }
    
    class InputTemplateSelectionHistory extends SelectionHistory {
        protected Object restoreItemFromMemento(IMemento element) {
            return InputTemplate.restore(root, element);
        }
        protected void storeItemToMemento(Object item, IMemento element) {
            if (item instanceof InputTemplate) {
                InputTemplate.store((InputTemplate) item, element);
            }
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
                return matches(((InputTemplate) arg0).inputTemplate);
            }
            
            @Override
            public boolean isConsistentItem(Object arg0)
            {
                if (arg0 instanceof InputTemplate) {
                    InputTemplate template = (InputTemplate) arg0;
                    return repository.validate(template);
                }
                return false;
            }
        };
    }

    @Override
    protected void fillContentProvider(AbstractContentProvider contentProvider,
        ItemsFilter filter, IProgressMonitor progressMonitor) throws CoreException
    {
        progressMonitor.beginTask("Initializing", 1);
        int workToDo = repository.estimatedXMLFiles();
        progressMonitor.worked(1);
        progressMonitor.done();
        progressMonitor.beginTask("Searching", workToDo);
        List<InputTemplate> templates = repository.templates(progressMonitor);
        progressMonitor.done();
        for (InputTemplate template : templates) {
            contentProvider.add(template, filter);
        }
        progressMonitor.done();
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
        return ((InputTemplate) item).inputTemplate;
    }

    @Override
    protected Comparator<Object> getItemsComparator()
    {
        return new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2)
            {
                return ((InputTemplate) o1).compareTo((InputTemplate) o2);
            }
        };
    }

    @Override
    protected IStatus validateItem(Object arg0)
    {
        return Status.OK_STATUS;
    }
}