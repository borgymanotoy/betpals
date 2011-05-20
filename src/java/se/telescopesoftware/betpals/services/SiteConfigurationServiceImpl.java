package se.telescopesoftware.betpals.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class SiteConfigurationServiceImpl implements SiteConfigurationService {

    private String appRoot;
    private String confugurationFilename;

    private static Logger logger = Logger.getLogger(SiteConfigurationServiceImpl.class);

    
    public void setAppRoot(String appRoot) {
    	this.appRoot = appRoot;
    }

    public void setConfigurationFilename(String filename) {
    	this.confugurationFilename = filename;
    }
    
    
	public String getParameterValue(String parameterName) {
    	Properties properties = getProperties();
    	if (properties != null) {
    		return properties.getProperty(parameterName);
    	}
		return null;
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
        try {
            Properties properties = new Properties();
            File propertiesFile = new File(appRoot + confugurationFilename);
            properties.load(new FileInputStream(propertiesFile));
            return properties;
        } catch (IOException e) {
        	logger.error("Could not load configuration properties file: ", e);
        }
        return null;
    }
    
    private void storeProperties(Properties properties) {
        try {
            properties.store(new FileOutputStream(appRoot + confugurationFilename), "Comment String");
        } catch (IOException e) {
        	logger.error("Could not save configuration properties file: ", e);
        }
    }
	
}
