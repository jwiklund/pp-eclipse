package pp.eclipse.ui;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
	@Override
	protected void createFieldEditors() {
		addField(new SkipEditor());
	}

	public void init(IWorkbench workbench) {
	}
	
	private class SkipEditor extends ListEditor 
	{
		public SkipEditor() {
			super("skipFolders", "Skip Folders", getFieldEditorParent());
		}
		
		@Override
		protected String createList(String[] items) {
			StringBuilder sb = new StringBuilder();
			for (String item : items) {
				if (sb.length() > 0) {
					sb.append(":");
				}
				sb.append(item);
			}
			return sb.toString();
		}

		@Override
		protected String[] parseString(String stringList) {
			return stringList.split(":");
		}
		
		@Override
		protected String getNewInputObject() {
			return "FILL ME UP";
		}
	}
}
