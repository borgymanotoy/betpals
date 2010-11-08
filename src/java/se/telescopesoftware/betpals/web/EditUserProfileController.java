package se.telescopesoftware.betpals.web;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.UserService;


@Controller
public class EditUserProfileController {

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

    @RequestMapping(value="/editprofile", method = RequestMethod.GET)
    protected String formBackingObject(ModelMap model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	User user = (User) authentication.getPrincipal();
    	UserProfile userProfile = user.getUserProfile();

    	model.addAttribute("userProfile", userProfile);
        return "editProfileView";
    }

    @RequestMapping(value="/editprofile", method = RequestMethod.POST)
    protected String onSubmit(@Valid UserProfile updatedUserProfile, BindingResult result, Model model) {
    	
    	if (result.hasErrors()) {
    		logger.debug("Error found: " + result.getErrorCount());
    		return "editProfileView";
    	}

    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	User user = (User) authentication.getPrincipal();
    	UserProfile userProfile = user.getUserProfile();
    	
    	userProfile.setName(updatedUserProfile.getName());
    	userProfile.setSurname(updatedUserProfile.getSurname());
    	
        userService.updateUserProfile(userProfile);
        
        MultipartFile imageFile = updatedUserProfile.getUserImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
        	try {
	        	InputStream inputStream = imageFile.getInputStream();
	        	BufferedImage image = ImageIO.read(inputStream);
	        	BufferedImage thumbnailImage = getScaledInstance(image, 50, 50);
	
	        	String path = appRoot + "images" + File.separator + "users";
	        	File outputFile = new File(path, userProfile.getUserId() + ".jpg");
	        	ImageIO.write(thumbnailImage, "jpg", outputFile);
	        	logger.debug("Writing user picture to " + outputFile.getPath());

        	} catch(Exception ex) {
        		logger.error("Could not save image", ex);
        	}
        	
        }

        return "userHomepageAction";
    }

    /**
     * Convenience method that returns a scaled instance of the
     * provided {@code BufferedImage}.
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @return a scaled version of the original {@code BufferedImage}
     */
    public BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight) {
        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage)img;

        int w = img.getWidth();
        int h = img.getHeight();
        
        do {
            if (w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }

}
