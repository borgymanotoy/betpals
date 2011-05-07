package se.telescopesoftware.betpals.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import se.telescopesoftware.betpals.domain.MessageResource;
import se.telescopesoftware.betpals.domain.MessageResourceComparator;

public class MessageResourceServiceImpl implements MessageResourceService {

    private String resourceBundlePath;
    private String appRoot;
    private MessageSource messageSource;
    private Map<String, String> supportedLanguages = new HashMap<String, String>();
    
	private static Logger logger = Logger.getLogger(MessageResourceServiceImpl.class);

    
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setResourceBundlePath(String resourceBundlePath) {
        this.resourceBundlePath = resourceBundlePath;
    }

    public Map<String, String> getSupportedLanguages() {
        return supportedLanguages;
    }

    public void setSupportedLanguages(Map<String, String> supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public void setAppRoot(String appRoot) {
    	this.appRoot = appRoot;
    }
    
    public void saveMessageResource(MessageResource messageResource) {
    	Properties properties = getPropertiesForLanguage(messageResource.getLocale(), true);
    	if (properties != null) {
	    	properties.remove(messageResource.getKey());
	    	properties.setProperty(messageResource.getKey().trim(), messageResource.getValue());
	        storeProperties(properties, messageResource.getLocale());
	        clearCache();
    	}
    }

    public void deleteMessageResource(MessageResource messageResource) {
    	Properties properties = getPropertiesForLanguage(messageResource.getLocale(), false);
    	if (properties != null) {
    		properties.remove(messageResource.getKey());
	        storeProperties(properties, messageResource.getLocale());
	        clearCache();
    	}
    }

    public MessageResource getMessageResource(String language, String key) {
        MessageResource messageResource = new MessageResource(language, key);
        messageResource.setValue(messageSource.getMessage(key, new Object[]{}, new Locale(language)));

        return messageResource;
    }

    public Collection<MessageResource> getMessageResourcesByLanguage(String language) {
    	return getMessageResourcesByLanguageAndFirstLetter(language, null);
    }

    public Collection<MessageResource> getMessageResourcesByLanguageAndFirstLetter(String language, String firstLetter) {
        List<MessageResource> messageResourceList = new ArrayList<MessageResource>();
        Properties propertiesForLanguage = getPropertiesForLanguage(language, false);

        if (propertiesForLanguage != null) {
            Set<Object> set = propertiesForLanguage.keySet();
            for (Iterator<Object> iterator = set.iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                if (firstLetter != null && !key.toLowerCase().startsWith(firstLetter.toLowerCase())) {
                	continue;
                }
                MessageResource messageResource = new MessageResource(language, key);
                messageResource.setValue(propertiesForLanguage.getProperty(key));
                messageResourceList.add(messageResource);
            }
        }

        Collections.sort(messageResourceList, new MessageResourceComparator());
        return messageResourceList;
    }

    private Properties getPropertiesForLanguage(String language, boolean create) {
        try {
            Properties properties = new Properties();
            File propertiesFile = new File(appRoot + resourceBundlePath + "_" + language + ".properties");
            if (create) {
                propertiesFile.createNewFile();
            }
            properties.load(new FileInputStream(propertiesFile));
            return properties;
        } catch (IOException e) {
        	logger.error("Could not load message resource properties file: ", e);
        }
        return null;
    }
    
    private void storeProperties(Properties properties, String locale) {
        try {
            properties.store(new FileOutputStream(appRoot + resourceBundlePath + "_" + locale + ".properties"), "Comment String");
        } catch (IOException e) {
        	logger.error("Could not save message resource properties file: ", e);
        }
    }
    
    private void clearCache() {
        if (messageSource instanceof ReloadableResourceBundleMessageSource) {
            ((ReloadableResourceBundleMessageSource) messageSource).clearCache();
        }
    }

}