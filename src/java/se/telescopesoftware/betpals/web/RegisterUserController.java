package se.telescopesoftware.betpals.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import se.telescopesoftware.betpals.services.UserService;


@Controller
public class RegisterUserController extends AbstractPalsController {

    private UserService userService;
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

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    
    @RequestMapping(value="/register", method = RequestMethod.GET)
    protected UserProfile formBackingObject() {
        return new UserProfile();
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    protected ModelAndView onSubmit(@Valid UserProfile userProfile, BindingResult result, HttpServletRequest request, Model model) {
    	
    	if (result.hasErrors()) {
    		logger.debug("Error found: " + result.getErrorCount());
    		return new ModelAndView("register");
    	}
        String username = userProfile.getEmail();
        String password = userProfile.getPassword();
        User user = new User(username);
        user.encodeAndSetPassword(password);

        userProfile.setUsername(username);
        userProfile.setRegistrationDate(new Date(System.currentTimeMillis()));
        userProfile.setUser(user);

        user.setUserProfile(userProfile);
        
        Long userId = userService.registerUser(user);
        model.addAttribute(user);

        // TODO: Remove file copy, use dynamic image loading
        String path = getAppRoot() + "images" + File.separator + "users";
        File defaultUserImage = new File(path, "userPic.jpg");
        File userImageFile = new File(path, userId + ".jpg");
        try {
			FileUtils.copyFile(defaultUserImage, userImageFile);
		} catch (IOException ex) {
			logger.error("Could not copy user picture", ex);
		}

    	accountService.createDefaultAccountForUser(userId);
		
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
