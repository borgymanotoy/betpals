package se.telescopesoftware.betpals.web;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import se.telescopesoftware.betpals.domain.Country;
import se.telescopesoftware.betpals.domain.PasswordRecoveryRequest;
import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.EmailService;
import se.telescopesoftware.betpals.services.SiteConfigurationService;


@Controller
public class EditUserProfileController extends AbstractPalsController {

    private EmailService emailService;
    private SiteConfigurationService siteConfigurationService;
    private AuthenticationManager authenticationManager;
	private MessageSource messageSource;


    @Autowired
    public void setEmailService(EmailService emailService) {
    	this.emailService = emailService;
    }
    
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
    	this.messageSource = messageSource;
    }
    
    @Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
    
    @Autowired
    public void setSiteConfigurationService(SiteConfigurationService siteConfigurationService) {
    	this.siteConfigurationService = siteConfigurationService;
    }
    
    
    @RequestMapping(value="/editprofile", method=RequestMethod.GET)
    public String formBackingObject(ModelMap model) {
    	model.addAttribute("userProfile", getUserProfile());
    	model.addAttribute("countryList", Country.values());
        return "editProfileView";
    }

    @RequestMapping(value="/editprofile", method=RequestMethod.POST)
    public String onSubmit(@Valid UserProfile updatedUserProfile, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		model.addAttribute(result.getAllErrors());
    		updatedUserProfile.setUser(getUser());
    		return "editProfileView";
    	}

    	UserProfile userProfile = getUserProfile();

    	if (!userProfile.getEmail().equalsIgnoreCase(updatedUserProfile.getEmail())) {
	        User user = getUserService().getUserByEmail(updatedUserProfile.getEmail());
	        if (user != null) {
	        	model.addAttribute("alreadyExist", true);
	    		updatedUserProfile.setUser(getUser());
	    		return "editProfileView";
	        }
	        
	        user = getUser();
	        if (user.checkPassword(updatedUserProfile.getPassword())) {
	        	userProfile.setEmail(updatedUserProfile.getEmail());
	        	user.setUsername(updatedUserProfile.getEmail());
	        	user.encodeAndSetPassword(updatedUserProfile.getPassword());
	        	getUserService().updateUser(user);
	        } else {
	        	model.addAttribute("wrongPassword", true);
	    		updatedUserProfile.setUser(getUser());
	    		return "editProfileView";
	        }
    	}

    	userProfile.setName(updatedUserProfile.getName());
    	userProfile.setSurname(updatedUserProfile.getSurname());
    	userProfile.setAddress(updatedUserProfile.getAddress());
    	userProfile.setBio(updatedUserProfile.getBio());
    	userProfile.setCity(updatedUserProfile.getCity());
    	userProfile.setCountry(updatedUserProfile.getCountry());
    	userProfile.setPostalCode(updatedUserProfile.getPostalCode());
    	
    	getUserService().updateUserProfile(userProfile);
        logUserAction("Update user profile");
        
        saveImage(updatedUserProfile.getUserImageFile(), IMAGE_FOLDER_USERS, getUserId().toString());

        return "userHomepageAction";
    }

    @RequestMapping(value="/forgotpasswordview")
    public String forgotPasswordView(@RequestParam(value="email", required = false) String email, ModelMap model) {
    	model.addAttribute("email", email);
        return "forgotPasswordView";
    }
    
    @RequestMapping(value="/forgotpassword", method=RequestMethod.POST)
    public String forgotPassword(@RequestParam("email") String email, Locale locale, ModelMap model) {
    	
    	User user = getUserService().getUserByEmail(email);
    	if (user != null) {
    		PasswordRecoveryRequest passwordRecoveryRequest = new PasswordRecoveryRequest(user);
    		getUserService().registerPasswordRecoveryRequest(passwordRecoveryRequest);
    		logUserAction(user.getId(), "Send " + passwordRecoveryRequest);
    		String link = siteConfigurationService.getParameterValue("site.url", "http://www.mybetpals.com") + "/forgotpassword/" + passwordRecoveryRequest.getRequestHash();
			String subject = messageSource.getMessage("email.forgot.password.subject", new Object[] {}, locale);
			String text = messageSource.getMessage("email.forgot.password.text", new Object[] {link}, locale);

    		try {
				emailService.sendEmail(user.getId(), subject, text);
			} catch (AddressException ex) {
				logger.error("Incorrect email address", ex);
	    		//TODO: change to Spring validation error
	    		model.addAttribute("error", messageSource.getMessage("error.could.not.send.email", new Object[] {}, locale));
	        	return "forgotPasswordView";
			} catch (MessagingException ex) {
				logger.error("Could not send email", ex);
	    		//TODO: change to Spring validation error
	    		model.addAttribute("error", messageSource.getMessage("error.could.not.send.email", new Object[] {}, locale));
	        	return "forgotPasswordView";
			}
			model.addAttribute("success", messageSource.getMessage("forgot.password.email.sent", new Object[] {}, locale));
    	} else {
    		//TODO: change to Spring validation error
    		model.addAttribute("error", messageSource.getMessage("error.no.user.with.email", new Object[] {}, locale));
    	}
    	
    	model.addAttribute("email", email);
    	return "forgotPasswordView";
    }
    
    @RequestMapping(value="/forgotpassword/{requestHash}")
    public String forgotPasswordProcessLink(@PathVariable("requestHash") String requestHash, Locale locale, ModelMap model) {
    	PasswordRecoveryRequest passwordRecoveryRequest = getUserService().findPasswordRecoveryRequest(requestHash);
    	if (passwordRecoveryRequest != null) {
    		model.addAttribute(passwordRecoveryRequest);
    	} else {
    		model.addAttribute("error", messageSource.getMessage("forgot.password.wrong.request.hash", new Object[] {}, locale));
    	}
    	
        return "forgotPasswordView";
    }
    
    
	@RequestMapping(value="/changepassword")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Model model) {
		User user = getUser();
		if (user.checkPassword(oldPassword)) {
			user.encodeAndSetPassword(newPassword);
			getUserService().updateUser(user);
			logUserAction("Changed password");
		} else {
	    	model.addAttribute("userProfile", getUserProfile());
	    	model.addAttribute("countryList", Country.values());
	    	model.addAttribute("wrongOldPassword", true);
	        return "editProfileView";
		}
		return "userHomepageAction";
	}

	@RequestMapping(value="/changeforgottenpassword")
	public String changeForgottenPassword(@RequestParam("requestHash") String requestHash, @RequestParam("newPassword") String newPassword, Model model) {
    	PasswordRecoveryRequest passwordRecoveryRequest = getUserService().findPasswordRecoveryRequest(requestHash);
    	if (passwordRecoveryRequest != null) {
    		User user = getUserService().getUserByUserId(passwordRecoveryRequest.getUserId());
    		user.encodeAndSetPassword(newPassword);
    		getUserService().updateUser(user);
    		logUserAction(user.getId(), "Changed forgotten password");
    		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), newPassword);
    		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));
    	}

		return "userHomepageAction";
	}
	
    
	@RequestMapping(value="/savetempuserimage")	
	public void saveTempImage(@RequestParam("imageFile") MultipartFile imageFile, HttpServletResponse response) {
		String filename = "tmp" + getUserId();
		boolean success = saveImage(imageFile, IMAGE_FOLDER_USERS, filename);
		if (success) {
			String message = "{\"success\":\"true\", \"filename\":\"" + filename + "\"}";
			sendResponseStatusAndMessage(response, HttpServletResponse.SC_OK, message);
		} else {
			String message = "{\"success\":\"false\", \"filename\":\"" + filename + "\"}";
			sendResponseStatusAndMessage(response, HttpServletResponse.SC_OK, message);
		}
	}

	@RequestMapping(value="/users/images/{userId}")	
	public void getImage(@PathVariable String userId, HttpServletResponse response) {
    	sendJPEGImage(IMAGE_FOLDER_USERS, userId, response);
	}
    
}
