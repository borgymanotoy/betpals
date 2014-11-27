package se.telescopesoftware.betpals.services;

import java.util.Collection;

import se.telescopesoftware.betpals.domain.LogEntry;

public interface LogViewerService  {


    Collection<LogEntry> getLatestLogEntries(Integer fileId, String level);

}