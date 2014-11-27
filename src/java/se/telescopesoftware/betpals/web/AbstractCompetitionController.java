package se.telescopesoftware.betpals.web;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import se.telescopesoftware.betpals.domain.Alternative;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.Event;

public class AbstractCompetitionController extends AbstractPalsController {
    protected List<Alternative> getSortedAlternativeList(Competition competition) {
        List<Alternative> alternativeSet = null;
        if (competition != null) {
            Event e = competition.getDefaultEvent();
            if (e != null) {
                 alternativeSet = e.getSortedAlternatives();
            }
        }
        return alternativeSet;
    }
    /** Returns a sorted alternative set that will be used in the JSP(view).
     *  <p>
     *  This method returns the sorted alternative set whether the alternative list is sorted or not.
     *  @param list  the alternative list to be converted into a sorted alternative set.
     *  @return The sorted alternative set.
     */
    protected Set<Alternative> sortAlternativeSet(List<Alternative> list){
        Set<Alternative> set = new TreeSet<Alternative>(list);
        return set;
    }
}