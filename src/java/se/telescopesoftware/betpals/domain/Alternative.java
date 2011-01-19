package se.telescopesoftware.betpals.domain;

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
import javax.persistence.Transient;

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
	private String name;
	private String description;
	private boolean taken;
	
	//FIXME: set relation between alternative and bets
//	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
//    @JoinTable(
//            name="join_alternative_bet",
//            joinColumns = @JoinColumn(name="alternative_id"),
//            inverseJoinColumns = @JoinColumn(name="bet_id")
//    )
	@Transient
	private Set<Bet> bets = new HashSet<Bet>();
	
	
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
		StringBuffer sb = new StringBuffer("Alternative ");
		sb.append(this.id);
		sb.append(" ");
		sb.append(this.name);
		return sb.toString();
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

}
