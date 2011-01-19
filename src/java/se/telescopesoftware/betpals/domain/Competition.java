package se.telescopesoftware.betpals.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name="competition")
public class Competition {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long ownerId;
	private Long accountId;
	@Enumerated(EnumType.STRING)
	private CompetitionType competitionType;
	@Enumerated(EnumType.STRING)
	private AccessType accessType;
	@NotNull
	private String name;
	private String description;
	private Date created;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	private Date deadline;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
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


	@Transient
	private MultipartFile imageFile;
	@Transient
	private boolean goToNextStep;
	
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
	public void setId(Long id) {
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
	
	public Alternative getAlternativeById(Long alternativeId) {
		for (Event event : events) {
			for (Alternative alternative : event.getAlternatives()) {
				if (alternative.getId().compareTo(alternativeId) == 0) {
					return alternative;
				}
			}
		}
		return null;
	}
	
	public List<Alternative> getAllAlternatives() {
		List<Alternative> result = new ArrayList<Alternative>();
		for (Event event : events) {
			result.addAll(event.getAlternatives());
		}
		
		return result;
	}
	
	public int getNumberOfParticipants() {
		int result = 0;
		for ( Alternative alternative : getAllAlternatives() ) {
			result += alternative.getBets().size();
		}
		return result; 
	}
	
	public BigDecimal getTurnover() {
		BigDecimal result = BigDecimal.ZERO;
		for ( Alternative alternative : getAllAlternatives() ) {
			for ( Bet bet : alternative.getBets() ) {
				result = result.add(bet.getStake());
			}
		}
		return result; 
	}
	
	/**
	 * Find and return default event for competition, currently first (and only) event in list.
	 * 
	 */
	public Event getDefaultEvent() {
		Set<Event> events = getEvents();
		if (events == null || events.isEmpty()) {
			Event event = new Event(getName());
			event.setCompetitionId(getId());
			addEvent(event);
		}
		return getEvents().iterator().next();
	}
	
	public boolean isPublic() {
		return getAccessType() == AccessType.PUBLIC;
	}
	
	public void setPublic(boolean bool) {
		setAccessType(bool ? AccessType.PUBLIC : AccessType.PRIVATE);
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	public boolean isGoToNextStep() {
		return goToNextStep;
	}

	public void setGoToNextStep(boolean goToNextStep) {
		this.goToNextStep = goToNextStep;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Competition: ");
		sb.append(this.id);
		sb.append(" ");
		sb.append(this.name);
		return sb.toString();
	}
	
}
