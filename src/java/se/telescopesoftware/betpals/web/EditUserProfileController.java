package se.telescopesoftware.betpals.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.telescopesoftware.betpals.domain.Country;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.UserService;


@Controller
public class EditUserProfileController extends AbstractPalsController {

    private UserService userService;
    

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value="/editprofile", method=RequestMethod.GET)
    protected String formBackingObject(ModelMap model) {
    	model.addAttribute("userProfile", getUserProfile());
    	model.addAttribute("countryList", Country.values());
        return "editProfileView";
    }

    @RequestMapping(value="/editprofile", method=RequestMethod.POST)
    protected String onSubmit(@Valid UserProfile updatedUserProfile, BindingResult result, Model model) {
    	
    	if (result.hasErrors()) {
    		logger.debug("Error found: " + result.getErrorCount());
    		return "editProfileView";
    	}

    	UserProfile userProfile = getUserProfile();
    	
    	userProfile.setName(updatedUserProfile.getName());
    	userProfile.setSurname(updatedUserProfile.getSurname());
    	userProfile.setAddress(updatedUserProfile.getAddress());
    	userProfile.setBio(updatedUserProfile.getBio());
    	userProfile.setCity(updatedUserProfile.getCity());
    	userProfile.setCountry(updatedUserProfile.getCountry());
    	userProfile.setPostalCode(updatedUserProfile.getPostalCode());
    	
        userService.updateUserProfile(userProfile);
        
        saveImage(updatedUserProfile.getUserImageFile(), IMAGE_FOLDER_USERS, getUserId().toString());

        return "userHomepageAction";
    }
    
}
