package pp.eclipse.ui;

import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import pp.eclipse.Activator;
import pp.eclipse.Preferences;

public class PreferencesPage 
    extends FieldEditorPreferencePage 
    implements IWorkbenchPreferencePage
{
    public PreferencesPage() 
    {
        setDescription("pp-eclipse Preferences");
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }
    
	@Override
	protected void createFieldEditors() {
		addField(new SkipEditor());
		addField(new StringFieldEditor(Preferences.IMPORT_URL, "Import Url", getFieldEditorParent()));
		addField(new StringFieldEditor(Preferences.IMPORT_USER, "Import User", getFieldEditorParent()));
		addField(new StringFieldEditor(Preferences.IMPORT_PASS, "Import Pass", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	    IPreferenceStore store = getPreferenceStore();
        if (!store.contains(Preferences.IMPORT_URL)) {
	        store.setValue(Preferences.IMPORT_URL, Preferences.DEFAULT_IMPORT_URL);
	        store.setValue(Preferences.IMPORT_USER, Preferences.DEFAULT_IMPORT_USER);
	        store.setValue(Preferences.IMPORT_PASS, Preferences.DEFAULT_IMPORT_PASS);
	    }
	}
	
	private class SkipEditor extends ListEditor 
	{
		public SkipEditor() {
			super(Preferences.SKIP_PATTERN, "Skip Patterns", getFieldEditorParent());
		}
		
		@Override
		protected String createList(String[] items) {
			return Preferences.storeSkipList(items);
		}

		@Override
		protected String[] parseString(String stringList) {
			return Preferences.restoreSkipList(stringList);
		}
		
		@Override
		protected String getNewInputObject() {
		    InputDialog input = new InputDialog(getShell(), "Input Pattern", "Input Pattern to Skip", null, new ValidRegexpValidator());
		    input.setBlockOnOpen(true);
		    input.open();
		    if (input.getReturnCode() == InputDialog.OK) {
		        return input.getValue();
		    }
			return null;
		}
	}
	
	private class ValidRegexpValidator implements IInputValidator 
	{
	    public String isValid(String newText) {
	        if (newText == null || newText.length() == 0) 
	            return "Pattern required";
	        if (newText.contains("\n"))
	            return "New line not allowed in pattern";
	        try {
                Pattern.compile(newText);
            } catch (Exception e) {
                return e.getMessage();
            }
	        return null;
        }
	}
}
