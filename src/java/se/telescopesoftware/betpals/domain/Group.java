package se.telescopesoftware.betpals.domain;

import java.util.Date;
import java.util.Set;

public class Group {

	private Long id;
	private Long ownerId;
	private Date created;
	private String name;
	private String description;
	
	private Set<Long> membersIdSet;

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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Long> getMembersIdSet() {
		return membersIdSet;
	}

	public void setMembersIdSet(Set<Long> membersIdSet) {
		this.membersIdSet = membersIdSet;
	}
	
	
}
