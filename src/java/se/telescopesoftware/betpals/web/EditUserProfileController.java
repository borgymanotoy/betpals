package se.telescopesoftware.betpals.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import se.telescopesoftware.betpals.domain.Country;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.UserService;
import se.telescopesoftware.betpals.utils.ThumbnailUtil;


@Controller
public class EditUserProfileController extends AbstractPalsController {

    private UserService userService;
    private String appRoot;
    
    private static Logger logger = Logger.getLogger(EditUserProfileController.class);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAppRoot(String appRoot) {
    	this.appRoot = appRoot;
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
        
        saveImage(updatedUserProfile.getUserImageFile());

        return "userHomepageAction";
    }
    
    private void saveImage(MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
        	try {
	        	InputStream inputStream = imageFile.getInputStream();
	        	BufferedImage image = ImageIO.read(inputStream);
	        	BufferedImage thumbnailImage = ThumbnailUtil.getScaledInstance(image, 50, 50);
	
	        	String path = appRoot + "images" + File.separator + "users";
	        	File outputFile = new File(path, getUserId() + ".jpg");
	        	ImageIO.write(thumbnailImage, "jpg", outputFile);
	        	logger.debug("Writing user picture to " + outputFile.getPath());

        	} catch(Exception ex) {
        		logger.error("Could not save image", ex);
        	}
        }
    }

}
