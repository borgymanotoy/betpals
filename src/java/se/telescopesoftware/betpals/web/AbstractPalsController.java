package se.telescopesoftware.betpals.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.utils.ThumbnailUtil;

/**
 * Some common functionality and helper methods for mybetpals controllers.
 * Extend this class when creating new controller.
 *
 */
public abstract class AbstractPalsController {

    private String appRoot;
    
    protected final String IMAGE_FOLDER_COMPETITIONS = "competitions";
    protected final String IMAGE_FOLDER_COMMUNITIES = "communities";
    protected final String IMAGE_FOLDER_USERS = "users";

    protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public void setAppRoot(String appRoot) {
    	this.appRoot = appRoot;
    }
    
    /**
     * Return system path to application root folder.
     * @return Absolute path to webapp root folder
     */
	protected String getAppRoot() {
		return appRoot;
	}

	/**
	 * Get logged on user.
	 * @return current authenticated User.
	 */
	protected User getUser() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	return (User) authentication.getPrincipal();
	}
	
	/**
	 * Get logged on user id.
	 * @return current authenticated User id.
	 */
	protected Long getUserId() {
		return getUser().getId();
	}
	
	/**
	 * Get UserProfile for logged on user.
	 * @return UserProfile of current authenticated User.
	 */
	protected UserProfile getUserProfile() {
		return getUser().getUserProfile();
	}

	/**
	 * Get specified picture and write it to response output stream.
	 * Return default empty picture, if requested file does not exist. 
	 * 
	 * @param folderName - image folder name
	 * @param filename - filename without extension
	 * @param response - HttpServletResponse 
	 */
	protected void sendJPEGImage(String folderName, String filename, HttpServletResponse response) {
    	String path = getAppRoot() + "images" + File.separator + folderName;
    	File imageFile = new File(path, filename + ".jpg");
    	logger.debug("Request for image: " + imageFile.getPath());

    	if (!imageFile.exists()) {
    		imageFile = new File(getAppRoot() + "images" + File.separator + "empty.jpg");
    	} 
    	
    	BufferedImage image;
		try {
			image = ImageIO.read(imageFile);
			response.setContentType("image/jpeg");
			ImageIO.write(image, "jpg", response.getOutputStream());
		} catch (IOException ex) {
    		logger.error("Could not get image", ex);
		}
	}
	
	/**
	 * Saves uploaded image file to specified folder.
	 * 
	 * @param imageFile
	 * @param folderName
	 * @param filename
	 */
    protected void saveImage(MultipartFile imageFile, String folderName, String filename) {
        if (imageFile != null && !imageFile.isEmpty()) {
        	try {
	        	InputStream inputStream = imageFile.getInputStream();
	        	BufferedImage image = ImageIO.read(inputStream);
	        	BufferedImage thumbnailImage = ThumbnailUtil.getScaledInstance(image, 50, 50);
	
	        	String path = getAppRoot() + "images" + File.separator + folderName;
	        	File outputFile = new File(path, filename + ".jpg");
	        	ImageIO.write(thumbnailImage, "jpg", outputFile);
	        	logger.debug("Writing uploaded picture to " + outputFile.getPath());

        	} catch(Exception ex) {
        		logger.error("Could not save image", ex);
        	}
        }
    }

}
