package se.telescopesoftware.betpals.domain;

import java.util.Comparator;
import java.util.Date;

public class LastLoggedUserComparator implements Comparator<UserProfile> {

	public int compare(UserProfile userProfile1, UserProfile userProfile2) {
        Date date1 = userProfile1.getLastLoginDate();
        Date date2 = userProfile2.getLastLoginDate();

        if (date1 == null && date2 == null) {
            return 0;
        }
        if (date1 == null) {
            return 1;
        }
        if (date2 == null) {
            return -1;
        }

        return date2.compareTo(date1);
	}

}
