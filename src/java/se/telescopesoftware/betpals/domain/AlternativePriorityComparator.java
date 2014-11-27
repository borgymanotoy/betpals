package se.telescopesoftware.betpals.domain;

import java.util.Comparator;

public class AlternativePriorityComparator implements Comparator<Alternative> {

	public int compare(Alternative alternative1, Alternative alternative2) {
        Integer priority1 = alternative1.getPriority();
        Integer priority2 = alternative2.getPriority();

        if (priority1 == null && priority2 == null) {
            return 0;
        }
        if (priority1 == null) {
            return 1;
        }
        if (priority2 == null) {
            return -1;
        }

        return priority1.compareTo(priority2);
	}

}
