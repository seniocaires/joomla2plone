package joomla2plone.plone.webclient;

import java.util.List;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import joomla2plone.joomla.entidades.Group;
import joomla2plone.joomla.entidades.User;
import joomla2plone.plone.Configuration;

public class GroupAssignmentWebClient {

	private static final Logger LOGGER = Logger.getLogger(GroupAssignmentWebClient.class.getName());

	private List<Group> groups;

	public GroupAssignmentWebClient(List<Group> groups) {
		this.groups = groups;
	}

	public void start(WebClient webClient) throws Exception {

		HtmlPage searchGroupAssignmentPage = null;
		HtmlPage searchedGroupAssignmentPage = null;
		HtmlPage addedGroupAssignmentPage = null;

		for (Group group : groups) {
			for (User user : group.getUsers()) {

				searchGroupAssignmentPage = webClient.getPage(Configuration.GROUP_ASSIGNMENT_URL_ADD + group.getTitle());

				List<DomElement> principalIds = searchGroupAssignmentPage.getElementsByName("principal_ids:list");

				HtmlSelect activeList = (HtmlSelect) principalIds.get(1);

				boolean alreadyAdded = false;
				for (HtmlOption optionActive : activeList.getOptions()) {
					if (user.getName().equals(optionActive.getValueAttribute())) {
						LOGGER.info("Assignment already added: " + user.toString());
						alreadyAdded = true;
					}
				}

				if (!alreadyAdded) {

					HtmlInput searchId = (HtmlInput) searchGroupAssignmentPage.getElementByName("search_id");
					searchId.setValueAttribute(user.getUsername());

					for (DomElement domElementInput : searchGroupAssignmentPage.getElementsByTagName("input")) {
						if ("submit".equals(domElementInput.getAttribute("type"))) {

							HtmlSubmitInput submit = (HtmlSubmitInput) domElementInput;
							searchedGroupAssignmentPage = submit.click();

							List<DomElement> principalIdsSearched = searchedGroupAssignmentPage.getElementsByName("principal_ids:list");

							HtmlSelect availableList = (HtmlSelect) principalIdsSearched.get(0);

							for (HtmlOption optionAvailable : availableList.getOptions()) {
								if (user.getUsername().equals(optionAvailable.getValueAttribute())) {
									availableList.setSelectedAttribute(optionAvailable, true);

									HtmlInput addButton = (HtmlInput) searchedGroupAssignmentPage.getElementByName("manage_addPrincipalsToGroup:method");
									addedGroupAssignmentPage = addButton.click();

									if (addedGroupAssignmentPage.asText().contains("Group " + group.getTitle() + " assigned to " + user.getName())) {
										LOGGER.info("Assignment added: " + user.toString());
									}
									break;
								}
							}

							break;
						}
					}
				}
			}
		}
	}
}
