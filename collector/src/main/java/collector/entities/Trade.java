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

@Entity
@XmlRootElement
public class Trade {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer trade_id;

	@NotNull(message="user_id cannot be null")
	private Integer user_id;

	@NotNull(message="card_id cannot be null")
	private Integer card_id;
	
	private Integer ref_trade_id;
	
	@Temporal(TemporalType.DATE)
	private Date created = new Date();

	public Trade() {
	}

	public Integer getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(Integer trade_id) {
		this.trade_id = trade_id;
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

	public Integer getRef_trade_id() {
		return ref_trade_id;
	}

	public void setRef_trade_id(Integer ref_trade_id) {
		this.ref_trade_id = ref_trade_id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	
}
