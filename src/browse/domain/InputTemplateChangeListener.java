package browse.domain;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;

public class InputTemplateChangeListener implements IResourceChangeListener
{
    private final InputTemplateRepository repository;

    public InputTemplateChangeListener(InputTemplateRepository repository) 
    {
        this.repository = repository;
    }

    @Override
    public void resourceChanged(IResourceChangeEvent event) {
        repository.refresh();
    }
}
