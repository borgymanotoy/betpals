package se.telescopesoftware.betpals.services;

import java.util.Map;

public interface SiteConfigurationService {

	
	String getParameterValue(String parameterName, String defaultValue);
	
	void saveParameter(String parameterName, String parameterValue);
	
	public Map<String, String[]> getAllParameters();
	
	public void saveParameters(Map<String, String> parameterMap);
}
