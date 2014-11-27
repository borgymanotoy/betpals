package se.telescopesoftware.betpals.services;

import java.util.Collection;
import java.util.Map;

import se.telescopesoftware.betpals.domain.MessageResource;

public interface MessageResourceService  {

    void saveMessageResource(MessageResource messageResource);

    void deleteMessageResource(MessageResource messageResource);

    Collection<MessageResource> getMessageResourcesByLanguage(String aLocale);

    Collection<MessageResource> findMessageResources(String aLocale, String query);

    Collection<MessageResource> getMessageResourcesByLanguageAndFirstLetter(String language, String firstLetter);

    MessageResource getMessageResource(String language, String key);

    Map<String, String> getSupportedLanguages();
    
}