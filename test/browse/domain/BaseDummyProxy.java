package browse.domain;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.QualifiedName;

class BaseDummyProxy implements IResourceProxy {

	@Override
	public long getModificationStamp() {

		return 0;
	}

	@Override
	public boolean isAccessible() {

		return false;
	}

	@Override
	public boolean isDerived() {

		return false;
	}

	@Override
	public boolean isLinked() {

		return false;
	}

	@Override
	public boolean isPhantom() {

		return false;
	}

	@Override
	public boolean isHidden() {

		return false;
	}

	@Override
	public boolean isTeamPrivateMember() {

		return false;
	}

	@Override
	public String getName() {

		return null;
	}

	@Override
	public Object getSessionProperty(QualifiedName key) {

		return null;
	}

	@Override
	public int getType() {
	    return requestResource().getType();
	}

	@Override
	public IPath requestFullPath() {
		return DummyPath.path("/"+getName());
	}

	@Override
	public IResource requestResource() {

		return null;
	}

}
