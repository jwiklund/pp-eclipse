package pp.eclipse.dummy;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.QualifiedName;

class BaseProxy implements IResourceProxy {


	public long getModificationStamp() {

		return 0;
	}


	public boolean isAccessible() {

		return false;
	}


	public boolean isDerived() {

		return false;
	}


	public boolean isLinked() {

		return false;
	}


	public boolean isPhantom() {

		return false;
	}


	public boolean isHidden() {

		return false;
	}


	public boolean isTeamPrivateMember() {

		return false;
	}


	public String getName() {

		return null;
	}


	public Object getSessionProperty(QualifiedName key) {

		return null;
	}


	public int getType() {
	    return requestResource().getType();
	}


	public IPath requestFullPath() {
		return Path.path("/"+getName());
	}


	public IResource requestResource() {

		return null;
	}

}
