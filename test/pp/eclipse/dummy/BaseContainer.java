package pp.eclipse.dummy;

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

    @Override
    public boolean exists(IPath path) {

        return false;
    }

    @Override
    public IResource findMember(String name) {

        return null;
    }

    @Override
    public IResource findMember(String name, boolean includePhantoms) {

        return null;
    }

    @Override
    public IResource findMember(IPath path) {

        return null;
    }

    @Override
    public IResource findMember(IPath path, boolean includePhantoms) {

        return null;
    }

    @Override
    public String getDefaultCharset() throws CoreException {

        return null;
    }

    @Override
    public String getDefaultCharset(boolean checkImplicit) throws CoreException {

        return null;
    }

    @Override
    public IFile getFile(IPath path) {

        return null;
    }

    @Override
    public IFolder getFolder(IPath path) {

        return null;
    }

    @Override
    public IResource[] members() throws CoreException {

        return null;
    }

    @Override
    public IResource[] members(boolean includePhantoms) throws CoreException {

        return null;
    }

    @Override
    public IResource[] members(int memberFlags) throws CoreException {

        return null;
    }

    @Override
    public IFile[] findDeletedMembersWithHistory(int depth,
            IProgressMonitor monitor) throws CoreException {

        return null;
    }

    @Override
    public void setDefaultCharset(String charset) throws CoreException {

        
    }

    @Override
    public void setDefaultCharset(String charset, IProgressMonitor monitor)
            throws CoreException {

        
    }

    @Override
    public IResourceFilterDescription createFilter(int type,
            FileInfoMatcherDescription matcherDescription, int updateFlags,
            IProgressMonitor monitor) throws CoreException {

        return null;
    }

    @Override
    public IResourceFilterDescription[] getFilters() throws CoreException {

        return null;
    }

}
