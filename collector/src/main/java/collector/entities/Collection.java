package collector.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ParameterMode;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.annotations.NamedStoredFunctionQuery;
import org.eclipse.persistence.annotations.StoredProcedureParameter;

@IdClass(CollectionPK.class)
@Entity
@XmlRootElement
@NamedStoredFunctionQuery(
		functionName = "Check_duplicate_card", 
		name = "Check_duplicate_card", 
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, queryParameter = "userid"),
				@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, queryParameter = "cardid") },
		returnParameter = 
				@StoredProcedureParameter(mode = ParameterMode.OUT, type = Integer.class, queryParameter = "result") )
public class Collection {

	@Id
	@NotNull(message="user_id cannot be null")
	private Integer user_id;
	
	@Id
	@NotNull(message="card_id cannot be null")
	private Integer card_id;

	private Integer copies;

	@Transient
	private Card card;

	public Collection() {
	}

	public Integer getUser_id() {
		return user_id;
	}


	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}


	public Integer getCard_id() {
		return card_id;
	}


	public void setCard_id(Integer card_id) {
		this.card_id = card_id;
	}


	public Integer getCopies() {
		return copies;
	}


	public void setCopies(Integer copies) {
		this.copies = copies;
	}


	@XmlTransient
	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

}
