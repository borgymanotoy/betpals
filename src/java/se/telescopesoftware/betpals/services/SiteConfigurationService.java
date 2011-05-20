package se.telescopesoftware.betpals.services;

public interface SiteConfigurationService {

	
	String getParameterValue(String parameterName);
	
	void saveParameter(String parameterName, String parameterValue);
}
