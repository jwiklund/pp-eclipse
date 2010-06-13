package browse.command;

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

import browse.Activator;
import browse.domain.InputTemplate;
import browse.ui.dialogs.InputTemplateSelectionDialog;

public class OpenInputTemplateCommand extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent arg0) throws ExecutionException
    {
        Shell shell = new Shell();
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        Activator activator = Activator.getDefault();
        InputTemplateSelectionDialog dialog = new InputTemplateSelectionDialog(shell, activator.getWorkspaceRoot(), activator.getRepository());
        dialog.setInitialPattern("p.");
        dialog.open();
        Object[] result = dialog.getResult();
        if (result == null) {
            return null;
        }
        InputTemplate inputTemplate = (InputTemplate) result[0];
        if (inputTemplate == null) {
            return null;
        }
        try {
            IFile file = (IFile) root.findMember(inputTemplate.path);
            IMarker marker = file.createMarker(IMarker.TEXT);
            marker.setAttribute(IMarker.LINE_NUMBER, inputTemplate.line);
            IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), marker);
            marker.delete();
        } catch (PartInitException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
