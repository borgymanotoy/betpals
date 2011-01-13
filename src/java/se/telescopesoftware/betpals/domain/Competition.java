package se.telescopesoftware.betpals.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="competition")
public class Competition {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long ownerId;
	@Enumerated(EnumType.STRING)
	private CompetitionType competitionType;
	@Enumerated(EnumType.STRING)
	private AccessType accessType;
	private String name;
	private String description;
	private Date created;
	private Date deadline;
	private Date settlingDeadline;
	private String currency;
	private BigDecimal fixedStake;
	
	//TODO: Check cascade type for possible side effects
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="join_competition_event",
            joinColumns = @JoinColumn(name="competition_id"),
            inverseJoinColumns = @JoinColumn(name="event_id")
    )
	private Set<Event> events = new HashSet<Event>();

	
	public Competition() {
	}

	public Competition(String name) {
		this.name = name;
		this.created = new Date();
	}

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}
	
	public void addEvent(Event event) {
		this.events.add(event);
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

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Date getSettlingDeadline() {
		return settlingDeadline;
	}

	public void setSettlingDeadline(Date settlingDeadline) {
		this.settlingDeadline = settlingDeadline;
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

	public CompetitionType getCompetitionType() {
		return competitionType;
	}

	public void setCompetitionType(CompetitionType competitionType) {
		this.competitionType = competitionType;
	}

	public AccessType getAccessType() {
		return accessType;
	}

	public void setAccessType(AccessType accessType) {
		this.accessType = accessType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getFixedStake() {
		return fixedStake;
	}

	public void setFixedStake(BigDecimal fixedStake) {
		this.fixedStake = fixedStake;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	/**
	 * Find and return the alternative id for quick competition.
	 * It will be alternative with type TRUE.
	 */
	public Long getOwnerAlternativeId() {
		for (Event event : events) {
			for (Alternative alternative : event.getAlternatives()) {
				if (alternative.getAlternativeType() == AlternativeType.TRUE) {
					return alternative.getId();
				}
			}
		}
		return null;
	}
	
}
