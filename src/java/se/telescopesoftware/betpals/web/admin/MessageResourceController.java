package se.telescopesoftware.betpals.web.admin;

import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.MessageResource;
import se.telescopesoftware.betpals.services.MessageResourceService;
import se.telescopesoftware.betpals.web.AbstractPalsController;

@Controller
public class MessageResourceController extends AbstractPalsController {

    private MessageResourceService messageResourceService;

    @Autowired
    public void setMessageResourceService(MessageResourceService messageResourceService) {
        this.messageResourceService = messageResourceService;
    }
	
    @RequestMapping(value="/admin/listmessageresources")
    public String listMessageResource(@RequestParam("lang") String language, @RequestParam(value="letter", required=false) String letter, ModelMap model) {
        model.addAttribute(messageResourceService.getMessageResourcesByLanguageAndFirstLetter(language, letter));
        model.addAttribute("listLanguage", language);
        return "listMessageResourcesView";
    }
    
    @RequestMapping(value="/admin/searchmessageresources")
    public String searchMessageResources(@RequestParam("lang") String language, @RequestParam(value="query") String query, ModelMap model) {
    	model.addAttribute(messageResourceService.findMessageResources(language, query));
    	model.addAttribute("listLanguage", language);
    	return "listMessageResourcesView";
    }
    
    @RequestMapping(value="/admin/editmessageresource", method=RequestMethod.GET)
    public String formBackingObject(@RequestParam("lang") String language, @RequestParam(value="key", required=false) String key, Model model) {
        if (key == null) {
            model.addAttribute(new MessageResource(language));
        } else {
        	model.addAttribute(messageResourceService.getMessageResource(language, key));
        }

        model.addAttribute("supportedLanguages", messageResourceService.getSupportedLanguages().values());
        return "editMessageResourceView";
    }

    @RequestMapping(value="/admin/editmessageresource", method=RequestMethod.POST)
    public String onSubmit(@Valid MessageResource messageResource, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return "editMessageResourceView";
    	}
    	
        String locale = messageResource.getLocale();
        if (MessageResource.ALL_LOCALES.equalsIgnoreCase(locale)) {
            Map<String, String> supportedLanguages = messageResourceService.getSupportedLanguages();
            for (String language : supportedLanguages.values()) {
                messageResourceService.saveMessageResource(new MessageResource(language, messageResource.getKey(), messageResource.getValue()));
            }
            Locale siteLocale = LocaleContextHolder.getLocale();
            model.addAttribute("lang", siteLocale.getLanguage());
        } else {
            messageResourceService.saveMessageResource(messageResource);
            model.addAttribute("lang", messageResource.getLocale());
        }
        
        model.addAttribute("letter", messageResource.getKey().substring(0, 1));
        return "listMessageResourcesAction";
    }

    @RequestMapping(value="/admin/deletemessageresource")
    public String deleteMessageResource(@RequestParam("lang") String language, @RequestParam("key") String key, Model model) {
        MessageResource messageResource = new MessageResource(language, key);
        messageResourceService.deleteMessageResource(messageResource);

        if (key != null && !key.isEmpty()) {
        	model.addAttribute("letter", key.substring(0, 1));
        } 
        model.addAttribute("lang", language);

        return "listMessageResourcesAction";
    }
    
}
