package se.telescopesoftware.betpals.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
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

    private TimeZone timeZone = TimeZone.getDefault();
    private long serverGMT;

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

    /** Returns TimeZone object.
     *
     *  @return the timeZone object
     */
    public TimeZone getTimeZone() {
        return timeZone;
    }

    /** Set TimeZone object.
     *
     *  @param timeZone the timeZone to set
     */
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    /**
	 * @return the serverGMT
	 */
	public long getServerGMT() {
		return serverGMT;
	}

	/**
	 * @param serverGMT the serverGMT to set
	 */
	public void setServerGMT(long serverGMT) {
		this.serverGMT = serverGMT;
	}

	/** Returns the converted date object from a string date.
     *
     *  @param strDate String Date to convert into a Date object.
     *  @return Converted Date object.
     */
    public Date stringToDate(String strDate) {
    	return stringToDate(strDate, null);
    }

    /** Returns the converted date object from a string date.
    *
    *  @param strDate String Date to convert into a Date object.
    *  @param pattern String pattern used for converting the string date into Date object.
    *  @return Converted Date object.
    */
    public Date stringToDate(String strDate, String pattern) {
        Date date = null;
        if (strDate != null && strDate.length() > 0) {
            pattern = (pattern != null) ? pattern : "MMMM dd, yyyy hh:mm:ssa";
            DateFormat df = new SimpleDateFormat(pattern);
            try {
                date = df.parse(strDate);
            } catch (ParseException pe) {
                pe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /** Returns the UTC/GMT Date object from another date object.
    *
    *  @param date Date object where the UTC Date will be extracted.
    *  @return UTC Date object.
    */
    protected Date getUTCDateObject(Date date) {
        return getUTCDateObject(date, null);
    }

    /** Returns the UTC/GMT Date object from another date object.
    *
    *  @param date Date object where the UTC Date will be extracted.
    *  @param pattern String pattern used for converting the GTM string date into Date object.
    *  @return UTC Date object.
    */
    protected Date getUTCDateObject(Date date, String pattern) {
        pattern = pattern != null ? pattern : "dd MMM yyyy kk:mm:ss";
        return stringToDate(date.toGMTString(), pattern);
    }

    /** Returns the UTC/GMT time difference (long format) between the date input .
    *
    *  @param date Date object where the UTC Date will be extracted.
    *  @return UTC time difference in long format.
    */
    protected long getGMTDifference(Date date) {
    	return getGMTDifference(date, null);
    }

    /** Returns the UTC/GMT time difference (long format) between the date input .
    *
    *  @param date Date object where the UTC Date will be extracted.
    *  @param pattern String pattern used for converting the GTM string date into Date object.
    *  @return UTC time difference in long format.
    */
    protected long getGMTDifference(Date date, String pattern) {
        pattern = pattern != null ? pattern : "dd MMM yyyy kk:mm:ss";
        int msInMin = 60000;
        int minInHr = 60;
        return Math.round( (( (double)(date.getTime() - getUTCDateObject(date, pattern).getTime()) / msInMin)/ minInHr) );
    }

    /** Returns the UTC/GMT time difference (double format) between the date input .
    *
    *  @param date Date object where the UTC Date will be extracted.
    *  @param pattern String pattern used for converting the GTM string date into Date object.
    *  @param isRounded flag to return the rounded GMT offset in hours or the un-rounded GMT offset.
    *  @return UTC time difference in double format.
    */
    protected double getGMTDifference(Date date, String pattern, boolean isRounded) {
        pattern = pattern != null ? pattern : "dd MMM yyyy kk:mm:ss";
        int msInMin = 60000;
        int minInHr = 60;
        
        double gmt = (( (double)(date.getTime() - getUTCDateObject(date, pattern).getTime()) / msInMin)/ minInHr);
        if (isRounded) {
        	return Math.round(gmt);
        } else {
        	return gmt;
        }
    }
    
    protected Date getAdjustedClientDateTime(Date date, double offsetDifference) {
    	DateTime dt = new DateTime(date);
    	int offsetDifferenceMinutes = (int) offsetDifference * 60;
        return dt.plusMinutes(offsetDifferenceMinutes).toDate();
    }
}
