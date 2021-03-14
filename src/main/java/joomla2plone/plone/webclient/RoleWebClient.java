package joomla2plone.plone.webclient;

import java.util.List;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import joomla2plone.joomla.entidades.ViewLevel;
import joomla2plone.plone.Configuration;

public class RoleWebClient {

	private static final Logger LOGGER = Logger.getLogger(RoleWebClient.class.getName());

	private List<ViewLevel> viewLevels;

	public RoleWebClient(List<ViewLevel> viewLevels) {
		this.viewLevels = viewLevels;
	}

	public void start(WebClient webClient) throws Exception {

		HtmlPage addRolePage = null;
		HtmlPage addedRolePage = null;

		for (ViewLevel viewLevel : viewLevels) {

			addRolePage = webClient.getPage(Configuration.ROLE_URL_ADD);

			HtmlInput roleId = (HtmlInput) addRolePage.getElementByName("role_id");
			roleId.setValueAttribute(viewLevel.getTitle());

			HtmlInput roleTitle = (HtmlInput) addRolePage.getElementByName("title");
			roleTitle.setValueAttribute(viewLevel.getTitle());
			
			for (DomElement domElementInput : addRolePage.getElementsByTagName("input")) {
				if ("submit".equals(domElementInput.getAttribute("type"))) {
					try {
						HtmlSubmitInput submit = (HtmlSubmitInput) domElementInput;
						addedRolePage = submit.click();
						if (addedRolePage.asText().contains("Role added")) {
							LOGGER.info("Role added: " + viewLevel.toString());
						}
					} catch (FailingHttpStatusCodeException e) {
						if (e.getMessage().contains("503 Service Unavailable")) { // Duplicate role ID
							LOGGER.info("Role already added: " + viewLevel.toString());
						}
					}
					
					break;
				}
			}
		}
	}
}
