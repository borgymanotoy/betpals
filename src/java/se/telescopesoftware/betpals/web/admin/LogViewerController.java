package se.telescopesoftware.betpals.web.admin;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.services.LogViewerService;
import se.telescopesoftware.betpals.web.AbstractPalsController;

@Controller
public class LogViewerController extends AbstractPalsController {

    private LogViewerService logViewerService;

    @Autowired
    public void setLogViewerService(LogViewerService logViewerService) {
        this.logViewerService = logViewerService;
    }
	
    @RequestMapping(value="/admin/viewlatestlog")
    public String viewLog(@RequestParam(value="file", required=false) Integer fileId, @RequestParam(value="level", required=false) String level, HttpSession session, ModelMap model) {
        String logLevelFromSession = (String) session.getAttribute("logLevel");
        if (level != null) {
        	session.setAttribute("logLevel", level);
        } else {
        	level = logLevelFromSession != null ? logLevelFromSession : "DEBUG";
        }
    	model.addAttribute(logViewerService.getLatestLogEntries(fileId, level));
        return "logView";
    }
    
    
}
