package pp.eclipse.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISources;
import org.eclipse.ui.handlers.HandlerUtil;

public class ImportCommand extends AbstractHandler 
{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException 
	{
		Object activeEditor = HandlerUtil.getVariable(event, ISources.ACTIVE_EDITOR_NAME);
		if (activeEditor instanceof IEditorPart) {
			IEditorInput activeInput = ((IEditorPart) activeEditor).getEditorInput();
			if (activeInput instanceof IFileEditorInput) {
				importContent(((IFileEditorInput) activeInput).getFile());
			}
		}
		return null;
	}

	private void importContent(IFile file) {
		System.out.println(file.getFullPath());
	}

}
