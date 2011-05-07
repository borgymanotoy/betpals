package se.telescopesoftware.betpals.domain;

public class MessageResource {

    public static final String ALL_LOCALES = "all";
    private String locale;
    private String key;
    private String value;

    
    public MessageResource() {
    }
    
    public MessageResource(String locale, String key) {
    	this.locale = locale;
    	this.key = key;
    }
    
    public MessageResource(String locale, String key, String value) {
    	this.locale = locale;
    	this.key = key;
    	this.value = value;
    }
    
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
	
}
