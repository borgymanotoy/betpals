package se.telescopesoftware.betpals.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserLogEntry;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.UserService;
import se.telescopesoftware.betpals.utils.ThumbnailUtil;

/**
 * Some common functionality and helper methods for mybetpals controllers.
 * Extend this class when creating new controller.
 *
 */
@Component
public abstract class AbstractPalsController {
	
    private String appRoot;

	private UserService userService;

    protected final String IMAGE_FOLDER_COMPETITIONS = "competitions";
    protected final String IMAGE_FOLDER_COMMUNITIES = "communities";
    protected final String IMAGE_FOLDER_USERS = "users";
    protected final String TEMP_IMAGE_PREFIX = "tmp";
    protected final String DEFAULT_IMAGE_FORMAT = "jpg";
    protected final String DEFAULT_IMAGE_SUFFIX = ".jpg";
    protected final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpeg";

    protected Logger logger = Logger.getLogger(this.getClass());


    @Autowired
    public void setAppRoot(String appRoot) {
    	this.appRoot = appRoot;
    }
    
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
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
    	File imageFile = new File(path, filename + DEFAULT_IMAGE_SUFFIX);
    	if (!imageFile.exists()) {
    		imageFile = new File(getAppRoot() + "images" + File.separator + "empty" + DEFAULT_IMAGE_SUFFIX);
    	}
    	BufferedImage image;
		try {
			image = ImageIO.read(imageFile);
			response.setContentType(DEFAULT_IMAGE_CONTENT_TYPE);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, post-check=0, pre-check=0,max-age=0"); // HTTP 1.1
			response.setHeader("Pragma", "no-cache");  //HTTP 1.0
	        response.setHeader("Expires", "Wed, 01 Sep 2010 00:00:00 GMT"); 
			ImageIO.write(image, DEFAULT_IMAGE_FORMAT, response.getOutputStream());
		} catch (IOException ex) {
    		logger.error("Could not get image", ex);
		}
	}
	
	/**
	 * Saves uploaded image file to specified folder.
	 * If imageFile is empty, try to copy file from previous upload tempFile, in case
	 * of preview image before submit form.
	 * 
	 * @param imageFile
	 * @param folderName
	 * @param filename
	 * @return boolean - true if successfull, false otherwise
	 */
    protected boolean saveImage(MultipartFile imageFile, String folderName, String filename) {
    	String path = getAppRoot() + "images" + File.separator + folderName;
    	File outputFile = new File(path, filename + DEFAULT_IMAGE_SUFFIX);
    	try {
    		if (imageFile != null && !imageFile.isEmpty()) {
	        	InputStream inputStream = imageFile.getInputStream();
	        	BufferedImage image = ImageIO.read(inputStream);
	        	BufferedImage thumbnailImage = ThumbnailUtil.getScaledInstance(image, 50, 50);
	    		logger.debug("Writing uploaded picture to " + outputFile.getPath());
	    		ImageIO.write(thumbnailImage, DEFAULT_IMAGE_FORMAT, outputFile);
	        } else {
	        	Long userId = getUserId();
	        	File oldFile = new File(path, userId + DEFAULT_IMAGE_SUFFIX);
	        	File tempFile = new File(path, TEMP_IMAGE_PREFIX + userId + DEFAULT_IMAGE_SUFFIX);
	        	if(oldFile!=null) oldFile.delete();
	        	logger.debug("Moving temp picture to " + outputFile.getPath());
	        	tempFile.renameTo(outputFile);
	        }
    	} catch(Exception ex) {
    		logger.error("Could not save image", ex);
    		return false;
    	}

    	return true;
   	}

    /**
     * Sends specified message and HTTP response code.
     * 
     * @param response - HttpServletResponse object
     * @param status - HTTP response code
     * @param message
     */
    protected void sendResponseStatusAndMessage(HttpServletResponse response, int status, String message) {
		try {
			PrintWriter writer = response.getWriter();
			response.setStatus(status);
			writer.print(message);
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			logger.error("Could not send response", ex);
		}
    }
    
    /**
     * Saves supplied message to current user log.
     * 
     * @param message
     */
    protected void logUserAction(String message) {
    	UserLogEntry userLogEntry = new UserLogEntry(getUserId(), message);
    	userService.saveUserLogEntry(userLogEntry);
    }
    
    /**
     * Saves supplied message to current user log.
     * 
     * @param userId
     * @param message
     */
    protected void logUserAction(Long userId, String message) {
    	UserLogEntry userLogEntry = new UserLogEntry(userId, message);
    	userService.saveUserLogEntry(userLogEntry);
    }
    
    protected UserService getUserService() {
    	return userService;
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
