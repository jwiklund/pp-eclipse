package pp.eclipse.open.dummy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class Resource
{

    private Resource(){}

    public static IContainer root(final Resource.Entry... entries) {
        return new BaseContainer() {
            @Override
            public void accept(IResourceProxyVisitor visitor, int memberFlags)
                throws CoreException
            {
                for (Entry entry : entries) {
                    visitor.visit(entry.proxy());
                }
            }
        };
    }

    public static IContainer rootWithUpdate(final IContainer root, final Entry... updateTemplates)
    {
        return new BaseContainer() {
            @Override
            public void accept(IResourceProxyVisitor visitor, int memberFlags) throws CoreException
            {
                root.accept(visitor, memberFlags);
            }

            @Override
            public IResource findMember(IPath path)
            {
                for (Entry entry : updateTemplates) {
                    final Entry theEntry = entry;
                    String fullPath = "/" + entry.name;
                    if (fullPath.equals(path.toString())) {
                        return new BaseResource() {
                            @Override
                            public IResourceProxy createProxy() {
                                return theEntry.proxy();
                            }
                        };
                    }
                }
                return null;
            }
        };
    }


    public static Entry empty(String filename)
    {
        return new Entry(filename, "");
    }
    
    public static Entry templateDefinition(String fileName, Template... templates)
    {
        return new Entry(fileName, new TemplateDefinition(Arrays.asList(templates)).toString());
    }

    public static InputTemplate inputTemplate(String inputTemplate, ClassReference... references)
    {
        return new InputTemplate(inputTemplate, Arrays.asList(references));
    }
    
    public static OutputTemplate outputTemplate(String outputTemplate)
    {
        return new OutputTemplate(outputTemplate);
    }
    
    public static Entry batch(String filename, Content... contents)
    {
        return new Entry(filename, new Batch(Arrays.asList(contents)).toString());
    }
    
    public static Entry batch(String filename, String externalid)
    {
        return batch(filename, content(externalid));
    }

    public static Entry batch(String filename, String externalid, String... contentreferences)
    {
        return batch(filename, content(externalid, contentreferences));
    }
    
    public static Content content(String externalid, String... contentReferences)
    {
        return new Content(externalid, new ContentList(Arrays.asList(contentReferences)));
    }

    public static ClassReference policy(String policy) {
        return new ClassReference("policy", null, policy);
    }

    public static ClassReference viewer(String context, String widget) {
        return new ClassReference("viewer", context, widget);
    }

    public static ClassReference viewer(String widget) {
        return new ClassReference("viewer", null, widget);
    }

    public static ClassReference editor(String context, String widget) {
        return new ClassReference("editor", context, widget);
    }

    public static ClassReference editor(String widget) {
        return new ClassReference("editor", null, widget);
    }

    public static class Batch {
        private List<Content> contents;
        public Batch(List<Content> contents) {
            this.contents = contents;
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            sb.append("<batch xmlns=\"http://www.polopoly.com/polopoly/cm/xmlio\">\n");
            for (Content content : contents) {
                sb.append(content.toString("  "));
            }
            sb.append("</batch>\n");
            return sb.toString();
        }
    }
    
    public static class Content {
        private String externalid;
        private ContentList contentList;
        public Content(String externalid, ContentList contentList) {
            this.externalid = externalid;
            this.contentList = contentList;
        }
        public String toString(String indent) {
            return indent + "<content>\n" +
                   indent + "  <metadata>\n" +
                   indent + "   <contentid>\n" +
                   indent + "    <major>Department</major>\n" +
                   indent + "    <externalid>"+externalid+"</externalid>\n" +
                   indent + "   </contentid>\n" +
                   indent + "  </metadata>\n" +
                   contentList.toString(indent + "  ") +
                   indent + " </content>\n";
        }
    }
    
    public static class ContentList {
        private List<String> contentreferences;
        public ContentList(List<String> contentreferences) {
            this.contentreferences = contentreferences;
        }

        public String toString(String indent) {
            if (contentreferences.size() == 0) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(indent).append("<contentlist>\n");
            for (String reference : contentreferences) {
                sb.append(indent).append(" <entry>\n");
                sb.append(indent).append("  <metadata>\n");
                sb.append(indent).append("   <referredContent>\n");
                sb.append(indent).append("    <contentid>\n");
                sb.append(indent).append("     <externalid>").append(reference).append("</externalid>");
                sb.append(indent).append("    </contentid>\n");
                sb.append(indent).append("   </referredContent>\n");
                sb.append(indent).append("  </metadata>\n");
                sb.append(indent).append(" </entry>\n");
            }
            sb.append(indent).append("</contentlist>\n");
            return sb.toString();
        }
    }

    public static interface Template {
        public String toString(String indent);
    }
    
    public static class TemplateDefinition {
        private List<Template> templates;
        public TemplateDefinition(List<Template> templates) {
            this.templates = templates;
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            sb.append("<template-definition version=\"1.0\" xmlns=\"http://www.polopoly.com/polopoly/cm/app/xml\">\n");
            for( Template template : templates) {
                sb.append(template.toString("  "));
            }
            sb.append("</template-definition>\n");
            return sb.toString();
        }
    }

    public static class InputTemplate implements Template {
        private String externalid;
        private List<ClassReference> references;
        public InputTemplate(String externalid, List<ClassReference> references) {
            super();
            this.externalid = externalid;
            this.references = references;
        }
        @Override
        public String toString(String indent) {
            StringBuilder sb = new StringBuilder();
            sb.append(indent).append("<input-template name=\"").append(externalid).append("\">\n");
            if (references.size() == 0) {
                sb.append(indent).append("  <policy>com.polopoly.cm.app.policy.SingleValuePolicy</policy>\n");
                sb.append(indent).append("  <editor>com.polopoly.cm.app.widget.OTextInputPolicyWidget</editor>\n");
                sb.append(indent).append("  <viewer>com.polopoly.cm.app.widget.OTextOutputPolicyWidget</viewer>\n");
            } else {
                for (ClassReference ref : references) {
                    sb.append(ref.toString(indent + "  "));
                }
            }
            sb.append(" </input-template>\n");
            return sb.toString();
        }
    }
    
    public static class OutputTemplate implements Template {
        private String externalid;
        public OutputTemplate(String externalid) {
            super();
            this.externalid = externalid;
        }
        @Override
        public String toString(String indent) {
            return indent + "<output-template name=\"" + externalid + "\">\n"+
                   indent + "</output-template>";
        }
    }

    public final static class ClassReference {
        public final String type;
        public final String context;
        public final String reference;
        public ClassReference(String type, String context, String reference) {
            super();
            this.type = type;
            this.context = context;
            this.reference = reference;
        }
        public String toString(String indent) {
            return indent + "<" + type + (context == null ? "" : " contextName=\"" + context + "\"") + ">" + reference + "</" + type + ">";
        }
    }

    public final static class Entry
    {
        public final String name;
        public final String content;

        public Entry(String name, String content)
        {
            this.name = name;
            this.content = content;
        }

        public IResourceProxy proxy()
        {
            return new BaseProxy() {
                @Override
                public String getName() {
                    return name;
                }
                @Override
                public IPath requestFullPath() {
                	return new org.eclipse.core.runtime.Path("/" + name);
                }
                @Override
                public IResource requestResource() {
                    return new BaseFile() {
                        @Override
                        public InputStream getContents() throws CoreException {
                            try {
                                return new ByteArrayInputStream(content.getBytes("UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException("UTF-8 not supportd");
                            }
                        }
                        @Override
                        public IPath getFullPath() {
                            return Path.path("/" + name);
                        }
                    };
                }
            };
        }
    }
}
