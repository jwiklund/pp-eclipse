package pp.eclipse.post;

import java.net.MalformedURLException;
import java.net.URL;

public class Host {
	public final String url;
	public final String user;
	public final String pass;
	
	public Host(String url, String user, String pass) {
		this.url = url;
		this.user = user;
		this.pass = pass;
	}
	
	public URL createURL() throws MalformedURLException 
	{
		return new URL(url + "?result=true&username="+user+"&password="+pass);
	}
}
