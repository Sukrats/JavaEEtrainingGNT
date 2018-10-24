package collector.model;

import java.net.URI;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Link {
	
	private String rel;
	private URI uri;
	
	public Link(URI uri, String string) {
		this.uri=uri;
		this.rel = string;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public URI getUri() {
		return uri;
	}
	public void setUri(URI uri) {
		this.uri = uri;
	}
	
	

}
