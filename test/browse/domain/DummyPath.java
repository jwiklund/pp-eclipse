package browse.domain;

import org.eclipse.core.runtime.IPath;

public class DummyPath {
    private DummyPath() {}

    public static IPath path(final String path) {
        return new BaseDummyPath() {
            @Override
            public String toString() {
                return path;
            }
        };
    }
}
