package pp.eclipse.open.dummy;

import org.eclipse.core.resources.FileInfoMatcherDescription;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

public class BaseContainer extends BaseResource implements IContainer {

    public boolean exists(IPath path) {
        return false;
    }

    public IResource findMember(String name) {
        return null;
    }

    public IResource findMember(String name, boolean includePhantoms) {
        return null;
    }

    public IResource findMember(IPath path) {
        return null;
    }

    public IResource findMember(IPath path, boolean includePhantoms) {
        return null;
    }

    public String getDefaultCharset() throws CoreException {
        return null;
    }

    public String getDefaultCharset(boolean checkImplicit) throws CoreException {

        return null;
    }

    public IFile getFile(IPath path) {

        return null;
    }


    public IFolder getFolder(IPath path) {

        return null;
    }


    public IResource[] members() throws CoreException {

        return null;
    }


    public IResource[] members(boolean includePhantoms) throws CoreException {

        return null;
    }


    public IResource[] members(int memberFlags) throws CoreException {

        return null;
    }


    public IFile[] findDeletedMembersWithHistory(int depth,
            IProgressMonitor monitor) throws CoreException {

        return null;
    }


    public void setDefaultCharset(String charset) throws CoreException {


    }


    public void setDefaultCharset(String charset, IProgressMonitor monitor)
            throws CoreException {


    }


    public IResourceFilterDescription createFilter(int type,
            FileInfoMatcherDescription matcherDescription, int updateFlags,
            IProgressMonitor monitor) throws CoreException {

        return null;
    }


    public IResourceFilterDescription[] getFilters() throws CoreException {

        return null;
    }

}
