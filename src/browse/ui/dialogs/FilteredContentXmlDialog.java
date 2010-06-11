package browse.ui.dialogs;

import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.IWorkspaceRoot;
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
import browse.ContentXmlFile;
import browse.InputTemplateResources;
import browse.ui.InputTemplate;

public class FilteredContentXmlDialog extends FilteredItemsSelectionDialog {

    private InputTemplateResources resourcesProvider;

    public FilteredContentXmlDialog(Shell shell, IWorkspaceRoot root)
    {
        super(shell, false);
        setTitle("Filtered ContentXml Dialog");
        setSelectionHistory(new ResourceSelectionHistory());
        resourcesProvider = new InputTemplateResources(root);
    }
    
    private class ResourceSelectionHistory extends SelectionHistory {
        protected Object restoreItemFromMemento(IMemento element) {
            return null; 
        }
        protected void storeItemToMemento(Object item, IMemento element) {
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
                return true;
            }
        };
    }

    @Override
    protected void fillContentProvider(AbstractContentProvider contentProvider,
        ItemsFilter filter, IProgressMonitor progressMonitor) throws CoreException
    {
        progressMonitor.beginTask("Initializing", 1);
        List<ContentXmlFile> resources = resourcesProvider.contentXMLFiles();
        progressMonitor.worked(1);
        progressMonitor.done();
        progressMonitor.beginTask("Searching", resources.size());
        for (ContentXmlFile file : resources) {
            List<InputTemplate> templates = resourcesProvider.inputTemplates(file);
            for (InputTemplate template : templates) {
                contentProvider.add(template, filter);
            }
            progressMonitor.worked(1);
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
                return String.valueOf(((InputTemplate) o1).inputTemplate).compareTo(((InputTemplate) o2).inputTemplate);
            }
        };
    }

    @Override
    protected IStatus validateItem(Object arg0)
    {
        return Status.OK_STATUS;
    }
}