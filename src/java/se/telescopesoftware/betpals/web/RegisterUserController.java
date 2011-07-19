package se.telescopesoftware.betpals.web;

import java.util.Date;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.EmailService;


@Controller
public class RegisterUserController extends AbstractPalsController {

    private AuthenticationManager authenticationManager;
	private AccountService accountService;
    private EmailService emailService;
	private MessageSource messageSource;


	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
    @Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

    @Autowired
    public void setEmailService(EmailService emailService) {
    	this.emailService = emailService;
    }
    
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
    	this.messageSource = messageSource;
    }
    
    
    @RequestMapping(value="/register", method = RequestMethod.GET)
    protected UserProfile formBackingObject() {
        return new UserProfile();
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    protected ModelAndView onSubmit(@Valid UserProfile userProfile, BindingResult result, HttpServletRequest request, Locale locale, Model model) {
    	
    	if (result.hasErrors()) {
    		model.addAttribute(result.getAllErrors());
    		return new ModelAndView("register");
    	}
    	
        String username = userProfile.getEmail();
        String password = userProfile.getPassword();
        
        User user = getUserService().getUserByEmail(username);
        if (user != null) {
        	model.addAttribute("alreadyExist", true);
    		return new ModelAndView("register");
        }
        
        user = new User(username);
        user.encodeAndSetPassword(password);

        userProfile.setUsername(username);
        userProfile.setRegistrationDate(new Date(System.currentTimeMillis()));
        userProfile.setUser(user);

        user.setUserProfile(userProfile);
        
        Long userId = getUserService().registerUser(user);
        model.addAttribute(user);

    	accountService.createDefaultAccountForUser(userId);

		String subject = messageSource.getMessage("email.register.subject", new Object[] {}, locale);
		String text = messageSource.getMessage("email.register.text", new Object[] {}, locale);

		try {
			emailService.sendEmail(userId, subject, text);
		} catch (AddressException ex) {
			logger.error("Incorrect email address", ex);
		} catch (MessagingException ex) {
			logger.error("Could not send email", ex);
		}
    	
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

        String redirectUrl = getRedirectUrl(request);
        
        return redirectUrl != null ? new ModelAndView(new RedirectView(redirectUrl)) : new ModelAndView("userHomepageAction");
    }

    protected String getRedirectUrl(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            SavedRequest savedRequest = (SavedRequest) session.getAttribute(WebAttributes.SAVED_REQUEST);
            if(savedRequest != null) {
                return savedRequest.getRedirectUrl();
            }
        }

        return null;
    }
    
}
