package collector.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@XmlRootElement
public class StandardSet {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer set_id;

	@NotNull
	@NotBlank(message = "set_name can't be blank")
	private String set_name;

	@Temporal(TemporalType.DATE)
	private Date released = new Date();

	public StandardSet() {}

	public Integer getSetId() {
		return set_id;
	}

	public void setSetId(Integer setId) {
		this.set_id = setId;
	}

	public String getSetName() {
		return set_name;
	}

	public void setSetName(String setName) {
		this.set_name = setName;
	}

	public Date getReleased() {
		return released;
	}

	public void setReleased(Date released) {
		this.released = released;

	}

}
