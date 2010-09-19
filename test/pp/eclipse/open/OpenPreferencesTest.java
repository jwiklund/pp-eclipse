package pp.eclipse.open;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

import pp.eclipse.Preferences;
import pp.eclipse.open.dummy.BasePreferencesStore;

public class OpenPreferencesTest {

    @Test
    public void configPatternTests()
    {
        assertEquals("Expected no pattern to return null pattern", null, pref(null));
        assertEquals("Expected empty pattern to return null pattern", null, pref(""));
        assertEquals("Expected single pattern to return pattern", "test", pref("test").toString());
        assertEquals("Expected multiple patterns to be ored togheter", "test1|test2", pref("test1\ntest2").toString());
    }
    
    @Test
    public void simpleMatches()
    {
        assertTrue("Expected a single item to match", pref("test").matcher("test").matches());
        assertFalse("Expected a wrong single item to not match", pref("test").matcher("tets").matches());
        assertTrue("Expected the first item in a list to match", pref("test1\ntest2").matcher("test1").matches());
        assertTrue("Expected the second item in a list to match", pref("test1\ntest2").matcher("test2").matches());        
    }
    
    private Pattern pref(final String value) {
        return new Preferences(new BasePreferencesStore() {
            @Override
            public String getString(String name) {
                return value;
            }
        }).skipPattern();
    }
}
