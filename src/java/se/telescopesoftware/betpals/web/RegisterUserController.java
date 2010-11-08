package se.telescopesoftware.betpals.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
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

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.UserService;


@Controller
public class RegisterUserController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private String appRoot;

    private static Logger logger = Logger.getLogger(RegisterUserController.class);

    @Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAppRoot(String appRoot) {
    	this.appRoot = appRoot;
    }

    @RequestMapping(value="/register", method = RequestMethod.GET)
    protected UserProfile formBackingObject() {
        return new UserProfile();
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    protected String onSubmit(@Valid UserProfile userProfile, BindingResult result, Model model) {
    	
    	if (result.hasErrors()) {
    		logger.debug("Error found: " + result.getErrorCount());
    		return "register";
    	}
        String username = userProfile.getEmail();
        String password = userProfile.getPassword();
        User user = new User(username);
        user.encodeAndSetPassword(password);

        Long userId = userService.registerUser(user);
        userProfile.setUserId(userId);
        userProfile.setUsername(username);
        userProfile.setRegistrationDate(new Date(System.currentTimeMillis()));
        userService.updateUserProfile(userProfile);
        user.setUserProfile(userProfile);
        model.addAttribute(user);

        String path = appRoot + "images" + File.separator + "users";
        File defaultUserImage = new File(path, "userPic.jpg");
        File userImageFile = new File(path, userId + ".jpg");
        try {
			FileUtils.copyFile(defaultUserImage, userImageFile);
		} catch (IOException ex) {
			logger.error("Could not copy user picture", ex);
		}
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

        return "userHomepageAction";
    }

}
