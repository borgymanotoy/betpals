package se.telescopesoftware.betpals.domain;

import java.math.BigDecimal;
import java.util.Comparator;

public class CompetitionTurnoverComparator implements Comparator<Competition> {

	public int compare(Competition competition1, Competition competition2) {
		BigDecimal turnover1 = competition1.getTurnover();
		BigDecimal turnover2 = competition2.getTurnover();

        return turnover2.compareTo(turnover1);
	}

}
