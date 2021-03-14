package joomla2plone.plone.webclient;

import java.util.List;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import joomla2plone.joomla.entidades.Group;
import joomla2plone.plone.Configuration;

public class GroupWebClient {

	private static final Logger LOGGER = Logger.getLogger(GroupWebClient.class.getName());

	private List<Group> groups;

	public GroupWebClient(List<Group> groups) {
		this.groups = groups;
	}

	public void start(WebClient webClient) throws Exception {

		HtmlPage addGroupPage = null;
		HtmlPage addedGroupPage = null;

		for (Group group : groups) {

			addGroupPage = webClient.getPage(Configuration.GROUP_URL_ADD);

			HtmlInput groupId = (HtmlInput) addGroupPage.getElementByName("group_id");
			groupId.setValueAttribute(group.getTitle());

			HtmlInput groupTitle = (HtmlInput) addGroupPage.getElementByName("title");
			groupTitle.setValueAttribute(group.getTitle());
			
			for (DomElement domElementInput : addGroupPage.getElementsByTagName("input")) {
				if ("submit".equals(domElementInput.getAttribute("type"))) {
					try {
						HtmlSubmitInput submit = (HtmlSubmitInput) domElementInput;
						addedGroupPage = submit.click();
						if (addedGroupPage.asText().contains("Group added")) {
							LOGGER.info("Group added: " + group.toString());
						}
					} catch (FailingHttpStatusCodeException e) {
						if (e.getMessage().contains("503 Service Unavailable")) { // Duplicate group ID
							LOGGER.info("Group already added: " + group.toString());
						}
					}
					
					break;
				}
			}
		}
	}
}
