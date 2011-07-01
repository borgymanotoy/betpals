package se.telescopesoftware.betpals.web.admin;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import se.telescopesoftware.betpals.services.SiteConfigurationService;
import se.telescopesoftware.betpals.web.AbstractPalsController;


@Controller
public class ConfigurationController extends AbstractPalsController {

    private SiteConfigurationService siteConfigurationService;

    @Autowired
    public void setSiteConfigurationService(SiteConfigurationService siteConfigurationService) {
        this.siteConfigurationService = siteConfigurationService;
    }

    
	@RequestMapping("/admin/viewconfiguration")
	public String viewConfiguration(Model model) {
    	Map<String, String[]> configuration = siteConfigurationService.getAllParameters();
    	model.addAttribute("configuration", configuration);
		return "configurationView";
	}

	@RequestMapping("/admin/updateconfiguration")
	public String updateConfiguration(HttpServletRequest request, Model model) {
		Map<String, String[]> requestParameterMap = request.getParameterMap();
		Map<String, String> parameterMap = new TreeMap<String, String>();
		
		for (String key : requestParameterMap.keySet()) {
			parameterMap.put(key, getValuesString(requestParameterMap.get(key)));
		}
		
		siteConfigurationService.saveParameters(parameterMap);
		
		return viewConfiguration(model);
	}
	
	private String getValuesString(String [] values) {
		StringBuffer sb = new StringBuffer();
		for (String value : values) {
			sb.append(value);
			sb.append(",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		
		return sb.toString();
	}
}
