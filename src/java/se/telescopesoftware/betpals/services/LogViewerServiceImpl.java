package se.telescopesoftware.betpals.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import se.telescopesoftware.betpals.domain.LogEntry;
import se.telescopesoftware.betpals.domain.LogEntryComparator;

public class LogViewerServiceImpl implements LogViewerService {

    private String logPath;
    private String appRoot;
	

    public void setAppRoot(String appRoot) {
    	this.appRoot = appRoot;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }


    //TODO: clean up
    public Collection<LogEntry> getLatestLogEntries(Integer fileId, String level) {
    	Pattern pattern = Pattern.compile("(^[^A-Z]+) ([A-Z]+) \\[(.+)\\] - (.*)");
    	DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS");
    	List<LogEntry> result = new ArrayList<LogEntry>();
    	File logFile = fileId != null ? new File(appRoot + logPath + "." + fileId) : new File(appRoot + logPath);
    	if (!logFile.exists()) {
    		logFile = new File(appRoot + logPath);
    	}
    	try {
			BufferedReader logReader = new BufferedReader(new FileReader(logFile));
			LogEntry previousEntry = null;
			StringBuffer additionalInfo = new StringBuffer();
			while (true) {
				String line = logReader.readLine();
			    if (line == null) {
					if (previousEntry != null) {
						previousEntry.setAdditionalInfo(additionalInfo.toString());
						result.add(previousEntry);
					}
			    	break;
			    }
				Matcher matcher = pattern.matcher(line);
				if (matcher.matches()) {
					LogEntry logEntry = new LogEntry();
					DateTime dateTime = dateTimeFormatter.parseDateTime(matcher.group(1));
					logEntry.setDate(dateTime.toDate());
					logEntry.setLevel(matcher.group(2));
					logEntry.setSource(matcher.group(3));
					logEntry.setMessage(matcher.group(4));
					if (previousEntry != null) {
						previousEntry.setAdditionalInfo(additionalInfo.toString());
						result.add(previousEntry);
					}
					previousEntry = logEntry;
					additionalInfo = new StringBuffer();
				} else {
					additionalInfo.append(line);
				}
				
	        }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		result = filterByLevel(result, level);
		Collections.sort(result, new LogEntryComparator());
		return result;
	}
    
    private List<LogEntry> filterByLevel(List<LogEntry> logEntries, String level) {
    	if (level.equalsIgnoreCase("DEBUG")) {
    		return logEntries;
    	}
    	List<LogEntry> result = new ArrayList<LogEntry>();
    	
    	for (LogEntry logEntry : logEntries) {
    		if (applicableForLevel(logEntry, level)) {
    			result.add(logEntry);
    		}
    	}
    	
    	return result;
    }

    private boolean applicableForLevel(LogEntry logEntry, String level) {
    	String logEntryLevel = logEntry.getLevel();
    	if (level.equalsIgnoreCase("ERROR")) {
			return logEntryLevel.equalsIgnoreCase("ERROR") || logEntryLevel.equalsIgnoreCase("FATAL"); 
		} else if (level.equalsIgnoreCase("WARN")) {
			return logEntryLevel.equalsIgnoreCase("ERROR") || 
				logEntryLevel.equalsIgnoreCase("WARN") || 
				logEntryLevel.equalsIgnoreCase("FATAL"); 
		} else if (level.equalsIgnoreCase("INFO")) {
			return logEntryLevel.equalsIgnoreCase("ERROR") || 
			logEntryLevel.equalsIgnoreCase("INFO") || 
			logEntryLevel.equalsIgnoreCase("WARN") || 
			logEntryLevel.equalsIgnoreCase("FATAL"); 
		}
    	return false;
    }
}
