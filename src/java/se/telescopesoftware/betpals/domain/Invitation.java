package se.telescopesoftware.betpals.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="invitation")
public class Invitation implements Serializable {

	
	private static final long serialVersionUID = 4773623200130565344L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long ownerId;
	private Long inviteeId;
	private Long competitionId;
	private Date deadline;
	private String ownerName;
	private String competitionName;
	
	
	public Invitation() {
	}

	public Invitation(Competition competition, Long inviteeId) {
		this(competition, competition.getOwner(), inviteeId);
	}

	public Invitation(Competition competition, UserProfile owner, Long inviteeId) {
		this.ownerId = owner.getUserId();
		this.ownerName = owner.getFullName();
		this.inviteeId = inviteeId;
		this.competitionId = competition.getId();
		this.competitionName = competition.getName();
		this.deadline = competition.getDeadline();
	}
	
	public Long getId() {
		return id;
	}
	
	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	public Long getCompetitionId() {
		return competitionId;
	}
	
	public void setCompetitionId(Long competitionId) {
		this.competitionId = competitionId;
	}
	
	public Date getDeadline() {
		return deadline;
	}
	
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public String getCompetitionName() {
		return competitionName;
	}
	
	public void setCompetitionName(String competitionName) {
		this.competitionName = competitionName;
	}

	public Long getInviteeId() {
		return inviteeId;
	}

	public void setInviteeId(Long inviteeId) {
		this.inviteeId = inviteeId;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Invitation from ");
		sb.append(getOwnerName());
		sb.append(" id ");
		sb.append(getOwnerId());
		sb.append(" to user id ");
		sb.append(getInviteeId());
		sb.append(" for ");
		sb.append(getCompetitionName());
		sb.append(" with id ");
		sb.append(getCompetitionId());
		sb.append(" and deadline at ");
		sb.append(getDeadline());
		
		return sb.toString();
	}
	
	
}
