package se.telescopesoftware.betpals.web;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


@Controller
public class RegisterUserController extends AbstractPalsController {

    private AuthenticationManager authenticationManager;
	private AccountService accountService;


	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
    @Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

    
    @RequestMapping(value="/register", method = RequestMethod.GET)
    protected UserProfile formBackingObject() {
        return new UserProfile();
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    protected ModelAndView onSubmit(@Valid UserProfile userProfile, BindingResult result, HttpServletRequest request, Locale locale, Model model) {

        String username = userProfile.getEmail();
        String password = userProfile.getPassword();

        boolean blankEmail = (username==null || username.length()==0);
        boolean blankFirstName = (userProfile.getName()==null || userProfile.getName().length()==0);
        boolean blankPassword = (password==null || password.length() == 0);
        boolean notOver18 = !userProfile.isOver18();

		if(blankEmail || blankFirstName || blankPassword || notOver18) {
			if(blankEmail) model.addAttribute("emailRequired", true);
			if(blankFirstName) model.addAttribute("firstNameRequired", true);
			if(blankPassword) model.addAttribute("passwordRequired", true);
			if (notOver18) model.addAttribute("under18", true);
			return new ModelAndView("register");
		}

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
        
        Long userId = getUserService().registerUser(user, locale);
        model.addAttribute(user);

    	accountService.createDefaultAccountForUser(userId);

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

        String redirectUrl = getRedirectUrl(request);
        return redirectUrl != null ? new ModelAndView(new RedirectView(redirectUrl)) : new ModelAndView("userHomepageAction");
    }

}
