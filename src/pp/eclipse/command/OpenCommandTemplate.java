package pp.eclipse.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import pp.eclipse.Activator;
import pp.eclipse.open.DialogFactory;
import pp.eclipse.open.Item;
import pp.eclipse.ui.SelectionDialog;

public abstract class OpenCommandTemplate
	extends AbstractHandler
{
	private DialogFactory dialogFactory;

	public OpenCommandTemplate(DialogFactory dialogFactory)
	{
		this.dialogFactory = dialogFactory;
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        Activator activator = Activator.getDefault();

		SelectionDialog dialog = dialogFactory.createDialog(activator.getWorkspaceRoot());
        Item item = dialog.select();
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
