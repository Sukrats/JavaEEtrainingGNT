package collector.entities;

import java.io.Serializable;

public class CollectionPK implements Serializable{

	private static final long serialVersionUID = -699612558721892636L;
	
	private Integer user_id;
	private Integer card_id;
	
	public CollectionPK() {}

	public CollectionPK(Integer userId, Integer cardId) {
		this.user_id = userId;
		this.card_id=cardId;
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
	
	 public int hashCode() {
         return (int)this.user_id.hashCode();
     }
 
     public boolean equals(Object obj) {
         if (obj == this) return true;
         if (!(obj instanceof CollectionPK)) return false;
         CollectionPK pk = (CollectionPK) obj;
         return pk.user_id.equals(this.user_id) && pk.card_id.equals(this.card_id);
     }
	
}
