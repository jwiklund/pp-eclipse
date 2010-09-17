package pp.eclipse;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

public class Preferences extends AbstractPreferenceInitializer 
{
	
	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences node = new DefaultScope().getNode(Activator.PLUGIN_ID);
		node.put("skipFolders", "");
	}
	
	public static List<String> skipFolders() {
		String list = Platform.getPreferencesService().get("skipFolders", "", null);
		return Arrays.asList(list.split(":"));
	}

}
