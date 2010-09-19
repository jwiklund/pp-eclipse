package pp.eclipse.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.handlers.HandlerUtil;

import pp.eclipse.Activator;
import pp.eclipse.post.Content;
import pp.eclipse.post.ContentImporter;
import pp.eclipse.post.ImportResult;

public class ImportCommand extends AbstractHandler
{
    final static String CONSOLE_NAME = "Import ContentXML";

	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		Object activeEditor = HandlerUtil.getVariable(event, ISources.ACTIVE_EDITOR_NAME);
		if (activeEditor instanceof IEditorPart) {
			IEditorInput activeInput = ((IEditorPart) activeEditor).getEditorInput();
			if (activeInput instanceof IFileEditorInput) {
				try {
                    importContent(((IFileEditorInput) activeInput).getFile());
                } catch (PartInitException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
			}
		}
		return null;
	}

	private void importContent(IFile file) throws PartInitException, UnsupportedEncodingException
	{
		MessageConsole console = findConsole(CONSOLE_NAME);
		URL url = Activator.getDefault().preferences().importUrl();
        InputStream contents = null;
        try {
            contents = file.getContents();
        } catch (CoreException e) {
            e.printStackTrace();
        }
        if (contents == null) {
            return;
        }
        String charset = null;
        try {
            charset = file.getCharset();
        } catch (CoreException e) {
            e.printStackTrace();
        }
        if (charset == null) {
            charset = "UTF-8";
        }
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(console.newMessageStream(), "UTF-8"));
        console.clearConsole();
		show(console);
		try {
		    pw.println("Importing " + file.getFullPath().toString());
		    pw.println("To " + url);
            ImportResult result = new ContentImporter().importContent(new Content(contents, charset), url);
            pw.println("Result");
            pw.println(handleResult(result));
        } catch (MalformedURLException e) {
            e.printStackTrace(pw);
        } catch (IOException e) {
            e.printStackTrace(pw);
        } finally {
            pw.flush();
        }
	}

    private String handleResult(ImportResult result)
    {
        if (result.status >= 200 && result.status <= 300) {
            return result.data;
        }
        String error = result.data;
        if (error == null) {
            return "Failure, " + result.status + " and no response" ;
        }
        int errorStart = -1;
        int errorEnd = -1;
        String startMarker = "The text below is technical information about the problem. This text is very important to attach to any reports about the error";
        String endMarker = "ERROR END";
        errorStart = result.data.indexOf(startMarker);
        if (errorStart != -1) {
            errorStart = result.data.indexOf("<pre>", errorStart);
            if (errorStart != -1) {
                if (result.data.indexOf(": ", errorStart) != -1) {
                    int actualStart = result.data.indexOf(": ", errorStart);
                    while (actualStart > errorStart && ' ' != result.data.charAt(actualStart)) {
                        actualStart = actualStart - 1;
                    }
                    errorStart = actualStart + 1;
                }
                errorEnd = result.data.indexOf(endMarker, errorStart);
                if (errorEnd != -1) {
                    error = result.data.substring(errorStart, errorEnd - endMarker.length());
                }
            }
        }
        return error;
    }

    private void show(MessageConsole console) throws PartInitException
	{
	    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	    String id = IConsoleConstants.ID_CONSOLE_VIEW;
	    IConsoleView view = (IConsoleView) page.showView(id);
	    view.display(console);
    }

    private MessageConsole findConsole(String name) {
	    ConsolePlugin plugin = ConsolePlugin.getDefault();
	    IConsoleManager conMan = plugin.getConsoleManager();
	    IConsole[] existing = conMan.getConsoles();
	    for (int i = 0; i < existing.length; i++)
	        if (name.equals(existing[i].getName()))
	            return (MessageConsole) existing[i];
	    //no console found, so create a new one
	    MessageConsole myConsole = new MessageConsole(name, null);
	    conMan.addConsoles(new IConsole[]{myConsole});
	    return myConsole;
	}
}
