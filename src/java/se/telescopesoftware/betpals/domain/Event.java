package se.telescopesoftware.betpals.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="event")
public class Event {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long competitionId;
	private String name;
	private String description;
	
	//TODO: Check cascade type for possible side effects
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="join_event_alternative",
            joinColumns = @JoinColumn(name="event_id"),
            inverseJoinColumns = @JoinColumn(name="alternative_id")
    )
	private Set<Alternative> alternatives = new HashSet<Alternative>();

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinTable(name="join_competition_event",
        joinColumns = @JoinColumn(name="event_id"),
        inverseJoinColumns = @JoinColumn(name="competition_id")
    )
	private Competition competition;
	
	public Event() {
	}

	public Event(String name) {
		this.name = name;
	}
	
	public Set<Alternative> getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(Set<Alternative> alternatives) {
		this.alternatives = alternatives;
	}
	
	public void addAlternative(Alternative alternative) {
		this.alternatives.add(alternative);
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

	public Long getCompetitionId() {
		return competitionId;
	}

	public void setCompetitionId(Long competitionId) {
		this.competitionId = competitionId;
	}

	public Competition getCompetition() {
		return competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}
	
	
}
