package collector.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@XmlRootElement
public class Card {
	
	@Id
	private Integer card_id;
	
	@NotNull
	@NotBlank(message = "card_name can't be blank")
	private String card_name;

	@NotBlank(message = "card_type can't be blank")
	private String card_type;
	
	@NotNull
	private Float price;
	
	@NotNull
	private Integer set_id;

	public Card() {
	}

	public Integer getCardId() {
		return card_id;
	}

	public void setCardId(Integer cardId) {
		this.card_id = cardId;
	}

	public String getCardName() {
		return card_name;
	}

	public void setCardName(String cardName) {
		this.card_name = cardName;
	}

	public String getCardType() {
		return card_type;
	}

	public void setCardType(String cardType) {
		this.card_type = cardType;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getSetId() {
		return set_id;
	}

	public void setSetId(Integer setId) {
		this.set_id = setId;
	}

}
