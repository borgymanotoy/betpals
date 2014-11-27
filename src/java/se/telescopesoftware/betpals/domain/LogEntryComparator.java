package se.telescopesoftware.betpals.domain;

import java.util.Comparator;
import java.util.Date;

public class LogEntryComparator implements Comparator<LogEntry> {

	public int compare(LogEntry logEntry1, LogEntry logEntry2) {
        Date date1 = logEntry1.getDate();
        Date date2 = logEntry2.getDate();

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
