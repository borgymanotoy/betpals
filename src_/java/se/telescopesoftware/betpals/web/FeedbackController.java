package se.telescopesoftware.betpals.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.UserFeedbackMessage;
import se.telescopesoftware.betpals.services.EmailService;
import se.telescopesoftware.betpals.services.SiteConfigurationService;

@Controller
public class FeedbackController extends AbstractPalsController {

	private EmailService emailService;
	private MessageSource messageSource;
	private VelocityEngine velocityEngine;
	private SiteConfigurationService siteConfigurationService;

	
	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
    	this.messageSource = messageSource;
    }

    @Autowired
    public void setVelocityEngine(VelocityEngine velocityEngine) {
    	this.velocityEngine = velocityEngine;
    }
	
    @Autowired
    public void setSiteConfigurationService(SiteConfigurationService siteConfigurationService) {
    	this.siteConfigurationService = siteConfigurationService;
    }
    
	
    @RequestMapping(value="/feedback")
    protected String processUserFeedback(@RequestParam("pageUrl") String pageUrl, @RequestParam("comment") String comment, Locale locale) {

		try {
			String supportEmail = siteConfigurationService.getParameterValue("feedback.email", "bugs@mybetpals.com");
			String language = locale != null ? locale.getLanguage() : "en";
			String template = "userFeedback_" + language + ".vm";
			String acknowledgeTemplate = "userFeedbackAcknowledge_" + language + ".vm";
			Map<String, Object> model = new HashMap<String, Object>();
			UserFeedbackMessage feedbackMessage = prepareUserFeedbackMessage(pageUrl, comment);
            model.put("feedback", feedbackMessage);

            String message = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, model);
            String acknowledgeMessage = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, acknowledgeTemplate, model);
			String subject = messageSource.getMessage("email.feedback.subject", new Object[] {feedbackMessage.getReportId(), feedbackMessage.getUserName()}, locale);
			String acknowledgeSubject = messageSource.getMessage("email.feedback.acknowledge.subject", new Object[] {feedbackMessage.getReportId()}, locale);
			emailService.sendEmail(feedbackMessage.getUserEmail(), supportEmail, subject, message);
			emailService.sendEmail(getUserId(), acknowledgeSubject, acknowledgeMessage);
		} catch (Exception e) {
			logger.error("Could not send email: ", e);
		}

    	
    	return "feedbackAcknowledgeView";
    }
    

    private UserFeedbackMessage prepareUserFeedbackMessage(String pageUrl, String comment) {
    	UserFeedbackMessage feedbackMessage = new UserFeedbackMessage(getUserProfile());
    	feedbackMessage.setPageUrl(pageUrl);
    	feedbackMessage.setComment(comment);
    	
    	return feedbackMessage;
    }
}
