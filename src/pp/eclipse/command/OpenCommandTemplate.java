package pp.eclipse.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import pp.eclipse.Activator;
import pp.eclipse.common.DefinedItem;
import pp.eclipse.common.DefiningFile;
import pp.eclipse.common.Repository;
import pp.eclipse.ui.Dialog;
import pp.eclipse.ui.DialogFactory;

public abstract class OpenCommandTemplate<Container extends DefiningFile<DefinedItem>> extends AbstractHandler 
{
	private DialogFactory<DefinedItem, Container> dialogFactory;
	private Repository<DefinedItem, Container> repository;
	
	
	
	public OpenCommandTemplate(DialogFactory<DefinedItem, Container> dialogFactory,
							   Repository<DefinedItem, Container> repository) 
	{
		this.dialogFactory = dialogFactory;
		this.repository = repository;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = new Shell();
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        Activator activator = Activator.getDefault();
        
		Dialog<DefinedItem> dialog = dialogFactory.create(shell, activator.getWorkspaceRoot(), repository);
        DefinedItem item = dialog.select();
        if (item == null) {
        	return null;
        }
        try {
            IFile file = (IFile) root.findMember(item.path());
            IMarker marker = file.createMarker(IMarker.TEXT);
            marker.setAttribute(IMarker.LINE_NUMBER, item.line());
            IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), marker);
            marker.delete();
        } catch (PartInitException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        }
        return null;
	}
}
