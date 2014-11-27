package se.telescopesoftware.betpals.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="passwordrecoveryrequest")
public class PasswordRecoveryRequest {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long userId;
	private String requestHash;
	
	
	public PasswordRecoveryRequest() {
	}

	public PasswordRecoveryRequest(User user) {
		this.userId = user.getId();
		this.requestHash = user.getEncodedLink();
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getRequestHash() {
		return requestHash;
	}
	
	public void setRequestHash(String requestHash) {
		this.requestHash = requestHash;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "PasswordRecoveryRequest [id=" + id + ", userId=" + userId
				+ ", requestHash=" + requestHash + "]";
	}
	
}
