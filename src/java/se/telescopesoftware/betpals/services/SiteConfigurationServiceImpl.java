package se.telescopesoftware.betpals.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class SiteConfigurationServiceImpl implements SiteConfigurationService {

    private String appRoot;
    private String confugurationFilename;

    private Properties cachedProperties;
    
    private static Logger logger = Logger.getLogger(SiteConfigurationServiceImpl.class);

    
    public void setAppRoot(String appRoot) {
    	this.appRoot = appRoot;
    }

    public void setConfigurationFilename(String filename) {
    	this.confugurationFilename = filename;
    }
    
    
	public String getParameterValue(String parameterName, String defaultValue) {
    	Properties properties = getProperties();
    	if (properties != null) {
    		return properties.getProperty(parameterName);
    	}
		return defaultValue;
	}

	public void saveParameter(String parameterName, String parameterValue) {
    	Properties properties = getProperties();
    	if (properties != null) {
	    	properties.remove(parameterName);
	    	properties.setProperty(parameterName, parameterValue);
	        storeProperties(properties);
	        logger.info("Set configuration parameter " + parameterName + " value to " + parameterValue);
    	}
	}

    private Properties getProperties() {
    	if (cachedProperties != null) {
    		return cachedProperties;
    	}
        try {
            Properties properties = new Properties();
            File propertiesFile = new File(appRoot + confugurationFilename);
            properties.load(new FileInputStream(propertiesFile));
            cachedProperties = properties;
            return properties;
        } catch (IOException e) {
        	logger.error("Could not load configuration properties file: ", e);
        }
        return null;
    }
    
    private void storeProperties(Properties properties) {
    	cachedProperties = properties;
        try {
            properties.store(new FileOutputStream(appRoot + confugurationFilename), "System configuration parameters");
        } catch (IOException e) {
        	logger.error("Could not save configuration properties file: ", e);
        }
    }

	public Map<String, String[]> getAllParameters() {
		return new TreeMap<String, String[]>((Map)getProperties());
	}

	public void saveParameters(Map<String, String> parameters) {
		Properties properties = new Properties();
		properties.putAll(parameters);
		storeProperties(properties);
	}
	
}
