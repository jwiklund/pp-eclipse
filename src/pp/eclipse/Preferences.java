package pp.eclipse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import org.eclipse.jface.preference.IPreferenceStore;

public class Preferences {
    
    public static final String DEFAULT_IMPORT_URL = "http://localhost:8080/polopoly/import";
    public static final String DEFAULT_IMPORT_USER = "sysadmin";
    public static final String DEFAULT_IMPORT_PASS = "sysadmin";
    public final static String SKIP_PATTERN = "skipPattern";
    public static final String IMPORT_PASS = "importpass";
    public static final String IMPORT_USER = "importuser";
    public static final String IMPORT_URL = "importurl";

    private final IPreferenceStore store;

    public Preferences(IPreferenceStore store) 
    {
        this.store = store;
    }
    
    public Pattern skipPattern() {
        String patterns = store.getString(SKIP_PATTERN);
        if (patterns == null || patterns.length() == 0) {
            return null;
        }
        String[] list = restoreSkipList(patterns);
        StringBuffer builder = new StringBuffer();
        for (String item : list) {
            if (builder.length() != 0) {
                builder.append("|");
            }
            builder.append(item);
        }
        return Pattern.compile(builder.toString());
    }
    
    public URL importUrl() 
    {
        String url = DEFAULT_IMPORT_URL;
        String user = DEFAULT_IMPORT_USER;
        String pass = DEFAULT_IMPORT_PASS;
        if (store.contains(IMPORT_URL)) {
            url = store.getString(IMPORT_URL);
        }
        if (store.contains(IMPORT_USER)) {
            user = store.getString(IMPORT_USER);
        }
        if (store.contains(IMPORT_PASS)) {
            pass = store.getString(IMPORT_PASS);
        }
        try {
            return new URL(url + "?result=true&username="+user+"&password="+pass);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] restoreSkipList(String storedSkipList) 
    {
        return storedSkipList.split("\n");
    }
    
    public static String storeSkipList(String[] skipList) 
    {
        StringBuffer result = new StringBuffer();
        for (String skip : skipList) {
            if (result.length() != 0) {
                result.append("\n");
            }
            result.append(skip);
        }
        return result.toString();
    }
}
