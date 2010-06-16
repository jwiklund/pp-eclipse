package pp.eclipse.dummy;

import org.eclipse.core.runtime.IPath;

public class Path {
    private Path() {}

    public static IPath path(final String path) {
        return new BasePath() {
            @Override
            public String toString() {
                return path;
            }
            
            @Override
            public boolean equals(Object obj) {
            	return String.valueOf(this).equals(String.valueOf(obj));
            }
            
            @Override
            public int hashCode() {
            	return String.valueOf(this).hashCode();
            }
        };
    }
}
