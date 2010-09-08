package pp.eclipse.common;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import pp.eclipse.domain.Container;
import pp.eclipse.domain.Item;
import pp.eclipse.parse.Parser;

public class Repository implements IRepository {

    private final IContainer root;
    private List<Parser> parsers;

    public Repository(IContainer root, Parser... parsers) 
    {
        this.root = root;
        this.parsers = Arrays.asList(parsers);
    }
    
    
    @Override
    public List<Container> list(IProgressMonitor monitor)
        throws CoreException 
    {
        return null;
    }

    @Override
    public boolean validate(Item item) 
    {
        return false;
    }
}
