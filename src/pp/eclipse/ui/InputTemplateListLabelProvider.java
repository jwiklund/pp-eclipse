package pp.eclipse.ui;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.StyledString;

import pp.eclipse.domain.InputTemplate;


class InputTemplateListLabelProvider extends LabelProvider 
    implements ILabelProviderListener, IStyledLabelProvider 
{
    InputTemplateSelectionDialog dialog;
    
    public InputTemplateListLabelProvider(InputTemplateSelectionDialog inputTemplateSelectionDialog) {
        this.dialog = inputTemplateSelectionDialog;
    }

    @Override
    public StyledString getStyledText(Object element) {
        if (element instanceof InputTemplate) {
            InputTemplate template = (InputTemplate) element;
            StyledString result = new StyledString(template.inputTemplate);
            if (dialog.isDuplicateElement(element)) {
                String path = " - " + template.path.makeRelative().toString();
                result.append(new StyledString(path, StyledString.QUALIFIER_STYLER));
            }
            return result;
        }
        return new StyledString("");
    }

    @Override
    public void labelProviderChanged(LabelProviderChangedEvent event) {
        // TODO Auto-generated method stub        
    }

}
