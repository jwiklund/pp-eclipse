package browse.domain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class DummyResource 
{
	
	private DummyResource() 
	{
	}
	
	public static IContainer root(final DummyResource.Entry... entries) {
		return new BaseDummyContainer() {
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
        return new BaseDummyContainer() {
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
                        return new BaseDummyResource() {
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
	
	public static Entry content(String filename, String externalid) 
	{
		return new Entry(filename, batch(content(externalid)));
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

	private static String content(String externalid) 
	{
		return " <content>\n" + 
		       "  <metadata>\n" + 
		       "   <contentid>\n" + 
		       "    <major>InputTemplate</major>\n" + 
		       "    <externalid>"+externalid+"</externalid>\n" + 
		       "   </contentid>\n" + 
		       "  </metadata>\n" + 
		       " </content>\n";
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

	final static class Entry
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
			return new BaseDummyProxy() {
				@Override
				public String getName() {
					return name;
				}
				@Override
				public IResource requestResource() {
					return new BaseDummyFile() {
						@Override
						public InputStream getContents() throws CoreException {
							try {
								return new ByteArrayInputStream(content.getBytes("UTF-8"));
							} catch (UnsupportedEncodingException e) {
								throw new RuntimeException("UTF-8 not supportd");
							}
						}
					};
				}
			};
		}
	}
}
