package se.telescopesoftware.betpals.domain;

import java.util.Comparator;

public class MessageResourceComparator implements Comparator<MessageResource> {

    public int compare(MessageResource resource1, MessageResource resource2) {

        String key1 = resource1.getKey();
        String key2 = resource2.getKey();

        if (key1 == null && key2 == null) {
            return 0;
        }
        if (key1 == null) {
            return 1;
        }
        if (key2 == null) {
            return -1;
        }

        return key1.compareTo(key2);
    }

}