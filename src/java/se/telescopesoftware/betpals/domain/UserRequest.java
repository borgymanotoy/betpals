package se.telescopesoftware.betpals.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="userrequest")
public class UserRequest {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long ownerId;
	private Long inviteeId;
	private String ownerName;
	private String inviteeName;
	@Enumerated(EnumType.STRING)
	private UserRequestType requestType;
	private Long extensionId;
	private String extensionName;
	
	
	public UserRequest() {
	}
	
	public UserRequest(UserProfile ownerProfile, UserProfile inviteeProfile) {
		this.requestType = UserRequestType.FRIEND;
    	this.ownerId = ownerProfile.getUserId();
    	this.ownerName = ownerProfile.getFullName();
    	this.inviteeId = inviteeProfile.getUserId();
    	this.inviteeName = inviteeProfile.getFullName();
	}
	
	public UserRequest(UserProfile ownerProfile, Community community) {
		this.requestType = UserRequestType.COMMUNITY;
    	this.ownerId = ownerProfile.getUserId();
    	this.ownerName = ownerProfile.getFullName();
    	this.inviteeId = community.getOwnerId();
    	this.inviteeName = community.getOwner().getFullName();
    	this.extensionId = community.getId();
    	this.extensionName = community.getName();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	public Long getInviteeId() {
		return inviteeId;
	}
	
	public void setInviteeId(Long inviteeId) {
		this.inviteeId = inviteeId;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getInviteeName() {
		return inviteeName;
	}

	public void setInviteeName(String inviteeName) {
		this.inviteeName = inviteeName;
	}

	public UserRequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(UserRequestType requestType) {
		this.requestType = requestType;
	}

	public Long getExtensionId() {
		return extensionId;
	}

	public void setExtensionId(Long extensionId) {
		this.extensionId = extensionId;
	}

	public String getExtensionName() {
		return extensionName;
	}

	public void setExtensionName(String extensionName) {
		this.extensionName = extensionName;
	}

	@Override
	public String toString() {
		return "UserRequest [id=" + id + ", ownerId=" + ownerId
				+ ", inviteeId=" + inviteeId + ", ownerName=" + ownerName
				+ ", inviteeName=" + inviteeName + ", requestType="
				+ requestType + ", extensionId=" + extensionId
				+ ", extensionName=" + extensionName + "]";
	}
	

}
