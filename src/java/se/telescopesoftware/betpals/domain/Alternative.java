package se.telescopesoftware.betpals.domain;

import java.math.BigDecimal;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name="alternative")
public class Alternative {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long eventId;
	@Enumerated(EnumType.STRING)
	private AlternativeType alternativeType;
	@NotBlank
	private String name;
	private String description;
	private boolean taken;
	private Integer priority;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="join_alternative_bet",
            joinColumns = @JoinColumn(name="alternative_id"),
            inverseJoinColumns = @JoinColumn(name="bet_id")
    )
	private Set<Bet> bets = new HashSet<Bet>();
	
	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinTable(name="join_event_alternative",
        joinColumns = @JoinColumn(name="alternative_id"),
        inverseJoinColumns = @JoinColumn(name="event_id")
    )
	private Event event;
	
	@Transient
	private MultipartFile imageFile;
	@Transient
	private Long competitionId;
	
	public Alternative() {
	}

	public Alternative(String name) {
		this.name = name;
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

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public AlternativeType getAlternativeType() {
		return alternativeType;
	}

	public void setAlternativeType(AlternativeType alternativeType) {
		this.alternativeType = alternativeType;
	}

	public boolean isTaken() {
		return taken;
	}

	public void setTaken(boolean taken) {
		this.taken = taken;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	public Long getCompetitionId() {
		return competitionId;
	}

	public void setCompetitionId(Long competitionId) {
		this.competitionId = competitionId;
	}

	@Override
	public String toString() {
		return "Alternative [id=" + id + ", eventId=" + eventId
				+ ", alternativeType=" + alternativeType + ", name=" + name
				+ ", taken=" + taken + ", priority=" + priority
				+ ", competitionId=" + competitionId + "]";
	}

	public Set<Bet> getBets() {
		return bets;
	}

	public void setBets(Set<Bet> bets) {
		this.bets = bets;
	}
	
	public void addBet(Bet bet) {
		getBets().add(bet);
	}
	
	public void removeBet(Bet bet) {
		Set<Bet> filteredBets = new HashSet<Bet>();
		for (Bet b : getBets()) {
			if (bet.getId().compareTo(b.getId()) != 0) {
				filteredBets.add(b);
			}
		}
		setBets(filteredBets);
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	public String getParticipantName() {
		//FIXME: Why next? What about order of bets. Rethink. 
		Bet bet = getBets().iterator().next();
		if (bet != null) {
			return bet.getOwnerName();
		}
		return "";
	}
	
	public Long getParticipantId() {
		//FIXME: Why next? What about order of bets. Rethink. 
		Bet bet = getBets().iterator().next();
		if (bet != null) {
			return bet.getOwnerId();
		}
		return null;
	}
	
	public Set<Long> getParticipantsIdSet() {
		Set<Long> result = new HashSet<Long>();
		for (Bet bet : getBets()) {
			result.add(bet.getOwnerId());
		}
		
		return result;
	}
	
	public BigDecimal getTurnover() {
		BigDecimal result = BigDecimal.ZERO;
		for ( Bet bet : getBets() ) {
			result = result.add(bet.getStake());
		}
		return result; 
	}

	public Integer getPriority() {
		return priority != null ? priority : new Integer(1);
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

    public void increasePriority() {
        this.priority = new Integer( getPriority().intValue() +1);
     }

     public void decreasePriority() {
         int priorityValue = getPriority().intValue() - 1;
         if (priorityValue < 1) {
             priorityValue = 1;
         }
         this.priority = new Integer( priorityValue);
     }

}
