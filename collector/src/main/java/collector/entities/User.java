package collector.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

import collector.model.Link;

@Entity
@XmlRootElement
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer user_id;
	
	@NotNull
	@Column(unique=true)
	@NotBlank(message = "username can't be blank")
	private String username;
	private String password;
	
	@Temporal(TemporalType.DATE)
	private Date created = new Date();
	
	@Transient
	private List<Link> links = new ArrayList<Link>();

	public User(String name, String password) {
		this.username = name;
		this.password = password;
	}

	public User() {	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public void addLink(Link link) {
		this.links.add(link);
	}
	
	@Override
	public String toString() {
		return "User:[id:"+this.getUser_id()+", username: "+this.getUsername()+", password: "+this.getPassword()+", creted: "+this.created+"]";
	}

}
