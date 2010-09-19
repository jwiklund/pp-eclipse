package pp.eclipse.open.dummy;

import java.io.File;

import org.eclipse.core.runtime.IPath;

public class BasePath implements IPath {

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public IPath addFileExtension(String extension) {
        return null;
    }

    public IPath addTrailingSeparator() {
        return null;
    }

    public IPath append(String path) {
        return null;
    }

    public IPath append(IPath path) {
        return null;
    }

    public String getDevice() {
        return null;
    }

    public String getFileExtension() {
        return null;
    }

    public boolean hasTrailingSeparator() {
        return false;
    }

    public boolean isAbsolute() {
        return false;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean isPrefixOf(IPath anotherPath) {
        return false;
    }

    public boolean isRoot() {
        return false;
    }

    public boolean isUNC() {
        return false;
    }

    public boolean isValidPath(String path) {
        return false;
    }

    public boolean isValidSegment(String segment) {
        return false;
    }

    public String lastSegment() {
        return null;
    }

    public IPath makeAbsolute() {
        return null;
    }

    public IPath makeRelative() {
        return null;
    }

    public IPath makeRelativeTo(IPath base) {
        return null;
    }

    public IPath makeUNC(boolean toUNC) {
        return null;
    }

    public int matchingFirstSegments(IPath anotherPath) {
        return 0;
    }

    public IPath removeFileExtension() {
        return null;
    }

    public IPath removeFirstSegments(int count) {
        return null;
    }

    public IPath removeLastSegments(int count) {
        return null;
    }

    public IPath removeTrailingSeparator() {
        return null;
    }

    public String segment(int index) {
        return null;
    }

    public int segmentCount() {
        return 0;
    }

    public String[] segments() {
        return null;
    }

    public IPath setDevice(String device) {
        return null;
    }

    public File toFile() {
        return null;
    }

    public String toOSString() {
        return null;
    }

    public String toPortableString() {
        return null;
    }

    public IPath uptoSegment(int count) {
        return null;
    }
}
