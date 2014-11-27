package se.telescopesoftware.betpals.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class SettledCompetitionsProcessor  {

    private CompetitionService competitionService;
    
	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}
	
	@Scheduled(cron = "0 0 0 * * *")
	public void process() {
		competitionService.processExpiredCompetitions();
	}


}
