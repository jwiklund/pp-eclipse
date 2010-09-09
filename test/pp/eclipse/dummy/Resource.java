package pp.eclipse.dummy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class Resource
{

	private Resource()
	{
	}

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

	public static Entry inputTemplate(String filename, String inputTemplate)
	{
		return new Entry(filename, templateDefinition(inputTemplate(inputTemplate)));
	}

	public static Entry outputTemplate(String filename, String outputTemplate)
	{
	    return new Entry(filename, templateDefinition(outputTemplate(outputTemplate)));
	}

    public static Entry content(String filename, String externalid)
	{
		return new Entry(filename, batch(content(externalid)));
	}

	public static Entry content(String filename, String externalid, String... contentreferences)
	{
		return new Entry(filename, batch(content(externalid, contentreferences)));
	}

	private static String batch(String... contents)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<batch xmlns=\"http://www.polopoly.com/polopoly/cm/xmlio\">\n");
		for (String content : contents) {
			sb.append(content);
		}
		sb.append("</batch>\n");
		return sb.toString();
	}

	private static String content(String externalid, String... contentreferences)
	{
		return " <content>\n" +
		       "  <metadata>\n" +
		       "   <contentid>\n" +
		       "    <major>Department</major>\n" +
		       "    <externalid>"+externalid+"</externalid>\n" +
		       "   </contentid>\n" +
		       "  </metadata>\n" +
		       contentlist("  ", contentreferences) +
		       " </content>\n";
	}

	private static String contentlist(String indent, String... contentreferences) {
		if (contentreferences.length == 0) {
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

	private static String templateDefinition(String... inputTemplates) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<template-definition version=\"1.0\" xmlns=\"http://www.polopoly.com/polopoly/cm/app/xml\">\n");
		for( String inputTemplate : inputTemplates) {
			sb.append(inputTemplate);
		}
		sb.append("</template-definition>\n");
		return sb.toString();
	}

	private static String inputTemplate(String inputtemplate) {
		return " <input-template name=\"" + inputtemplate + "\">\n"+
		       "  <policy>com.polopoly.cm.app.policy.SingleValuePolicy</policy>\n"+
		       "  <editor>com.polopoly.cm.app.widget.OTextInputPolicyWidget</editor>\n"+
		       "  <viewer>com.polopoly.cm.app.widget.OTextOutputPolicyWidget</viewer>\n"+
		       " </input-template>\n";
	}

	private static String outputTemplate(String outputTemplate) {
	    return " <output-template name=\"" + outputTemplate + "\">\n"+
	           " </output-template>";
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
