package se.telescopesoftware.betpals.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Proxy;

@Entity
@Proxy(lazy=false)
@Table(name="communities")
public class Community {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long ownerId;
	private Date created;
	private String name;
	private String description;
	
	@OneToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="community_members",
            joinColumns = @JoinColumn(name="community_id"),
            inverseJoinColumns = @JoinColumn(name="member_id")
    )
	private Set<UserProfile> members = new HashSet<UserProfile>();

	@Transient
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

	public void setMembers(Set<UserProfile> members) {
		this.members = members;
	}

	public Set<UserProfile> getMembers() {
		return members;
	}
	
	public void addMember(UserProfile userProfile) {
		this.members.add(userProfile);
	}

	public Set<Long> getMembersIdSet() {
		if (membersIdSet != null && !membersIdSet.isEmpty()) {
			return membersIdSet;
		}
		
		Set<Long> idSet = new HashSet<Long>();
		for (UserProfile member : members) {
			idSet.add(member.getId());
		}
		
		return idSet;
	}

	public void setMembersIdSet(Set<Long> membersIdSet) {
		this.membersIdSet = membersIdSet;
	}
	
	
}
