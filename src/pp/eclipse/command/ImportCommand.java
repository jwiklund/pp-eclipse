package pp.eclipse.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
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
import org.eclipse.ui.console.MessageConsoleStream;
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
                Job job = new Import(((IFileEditorInput) activeInput).getFile());
                job.setUser(true);
                job.schedule();
            }
        }
        return null;
    }

    private class Import extends Job {
        private final IFile file;

        public Import(IFile file) {
            super("pp.import " + file.getName());
            this.file = file;
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            try {
                importContent(file);
            } catch (PartInitException e) {
                return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Import.PartInit: " + e.getMessage());
            } catch (UnsupportedEncodingException e) {
                return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Import.Unknown Encoding");
            }
            return new Status(IStatus.OK, Activator.PLUGIN_ID, "Import.Success");
        }
    }

    void importContent(IFile file) throws PartInitException, UnsupportedEncodingException
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

        clearConsole(console);
        show(console);
        MessageConsoleStream stream = console.newMessageStream();
        PrintWriter pw = new PrintWriter(stream);
        try {
            pw.println("Importing " + file.getFullPath().toString());
            pw.println("To " + url);
            pw.flush();
            ImportResult result = new ContentImporter().importContent(new Content(contents, charset), url);
            pw.println("Result");
            pw.println(handleResult(result));
        } catch (MalformedURLException e) {
            e.printStackTrace(pw);
        } catch (IOException e) {
            e.printStackTrace(pw);
        } finally {
            pw.flush();
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private void show(final MessageConsole console) throws PartInitException
    {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                String id = IConsoleConstants.ID_CONSOLE_VIEW;
                IConsoleView view;
                try {
                    view = (IConsoleView) page.showView(id);
                    view.display(console);
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void clearConsole(final MessageConsole console) {
        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                console.clearConsole();
            }
        });
    }

    private MessageConsole findConsole(final String name) {
        final MessageConsole[] console = new MessageConsole[1];
        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                ConsolePlugin plugin = ConsolePlugin.getDefault();
                IConsoleManager conMan = plugin.getConsoleManager();
                IConsole[] existing = conMan.getConsoles();
                for (int i = 0; i < existing.length; i++)
                    if (name.equals(existing[i].getName())) {
                        console[0] = (MessageConsole) existing[i];
                        return ;
                    }
                //no console found, so create a new one
                MessageConsole myConsole = new MessageConsole(name, null);
                conMan.addConsoles(new IConsole[]{myConsole});
                console[0] = myConsole;
            }
        });
        return console[0];
    }
}
