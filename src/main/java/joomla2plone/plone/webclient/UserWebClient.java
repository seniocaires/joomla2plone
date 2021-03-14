package joomla2plone.plone.webclient;

import java.util.List;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import joomla2plone.joomla.entidades.User;
import joomla2plone.plone.Configuration;

public class UserWebClient {

	private static final Logger LOGGER = Logger.getLogger(UserWebClient.class.getName());

	private List<User> users;

	public UserWebClient(List<User> users) {
		this.users = users;
	}

	public void start(WebClient webClient) throws Exception {

		HtmlPage addUserPage = null;
		HtmlPage addedUserPage = null;

		for (User user : users) {

			addUserPage = webClient.getPage(Configuration.USER_URL_ADD);

			HtmlInput userId = (HtmlInput) addUserPage.getElementByName("user_id");
			userId.setValueAttribute(user.getUsername());

			HtmlInput userName = (HtmlInput) addUserPage.getElementByName("login_name");
			userName.setValueAttribute(user.getName());

			HtmlInput userPassword = (HtmlInput) addUserPage.getElementByName("password");
			userPassword.setValueAttribute(Configuration.USER_DEFAULT_PASSWORD);

			HtmlInput userPasswordConfirm = (HtmlInput) addUserPage.getElementByName("confirm");
			userPasswordConfirm.setValueAttribute(Configuration.USER_DEFAULT_PASSWORD);

			for (DomElement domElementInput : addUserPage.getElementsByTagName("input")) {
				if ("submit".equals(domElementInput.getAttribute("type"))) {
					try {
						HtmlSubmitInput submit = (HtmlSubmitInput) domElementInput;
						addedUserPage = submit.click();
						if (addedUserPage.asText().contains("User added")) {
							LOGGER.info("User added: " + user.toString());
						}
					} catch (FailingHttpStatusCodeException e) {
						if (e.getMessage().contains("503 Service Unavailable")) { // Duplicate user ID
							LOGGER.info("User already added: " + user.toString());
						}
					}
					
					break;
				}
			}
		}
	}
}
