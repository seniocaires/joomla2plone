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
import joomla2plone.joomla.entidades.ViewLevel;
import joomla2plone.plone.Configuration;

public class RoleAssignmentWebClient {

	private static final Logger LOGGER = Logger.getLogger(RoleAssignmentWebClient.class.getName());

	private List<ViewLevel> viewLevels;

	public RoleAssignmentWebClient(List<ViewLevel> viewLevels) {
		this.viewLevels = viewLevels;
	}

	public void start(WebClient webClient) throws Exception {

		HtmlPage searchRoleAssignmentPage = null;
		HtmlPage searchedRoleAssignmentPage = null;
		HtmlPage addedRoleAssignmentPage = null;

		for (ViewLevel viewLevel : viewLevels) {
			for (Group group : viewLevel.getGroups()) {

				searchRoleAssignmentPage = webClient.getPage(Configuration.ROLE_ASSIGNMENT_URL_ADD + viewLevel.getTitle());

				List<DomElement> principalIds = searchRoleAssignmentPage.getElementsByName("principal_ids:list");

				HtmlSelect activeList = (HtmlSelect) principalIds.get(1);

				boolean alreadyAdded = false;
				for (HtmlOption optionActive : activeList.getOptions()) {
					if (group.getTitle().equals(optionActive.getValueAttribute())) {
						LOGGER.info("Assignment already added: " + group.toString());
						alreadyAdded = true;
					}
				}

				if (!alreadyAdded) {

					HtmlInput searchId = (HtmlInput) searchRoleAssignmentPage.getElementByName("search_id");
					searchId.setValueAttribute(group.getTitle());

					for (DomElement domElementInput : searchRoleAssignmentPage.getElementsByTagName("input")) {
						if ("submit".equals(domElementInput.getAttribute("type"))) {

							HtmlSubmitInput submit = (HtmlSubmitInput) domElementInput;
							searchedRoleAssignmentPage = submit.click();

							List<DomElement> principalIdsSearched = searchedRoleAssignmentPage.getElementsByName("principal_ids:list");

							HtmlSelect availableList = (HtmlSelect) principalIdsSearched.get(0);

							for (HtmlOption optionAvailable : availableList.getOptions()) {
								if (group.getTitle().equals(optionAvailable.getValueAttribute())) {
									availableList.setSelectedAttribute(optionAvailable, true);

									HtmlInput addButton = (HtmlInput) searchedRoleAssignmentPage.getElementByName("manage_assignRoleToPrincipals:method");
									addedRoleAssignmentPage = addButton.click();

									if (addedRoleAssignmentPage.asText().contains("Role " + viewLevel.getTitle() + " assigned to " + group.getTitle())) {
										LOGGER.info("Assignment added: " + group.toString());
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
