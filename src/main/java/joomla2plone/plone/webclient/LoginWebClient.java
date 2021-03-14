package joomla2plone.plone.webclient;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import joomla2plone.plone.Configuration;

public class LoginWebClient {

	public static void start(WebClient webClient) throws Exception {

		final HtmlPage loginPage = webClient.getPage(Configuration.LOGIN_URL);
		HtmlInput acName = (HtmlInput) loginPage.getElementById("__ac_name");
		acName.setValueAttribute(Configuration.ADMIN_LOGIN);

		HtmlInput acPassword = (HtmlInput) loginPage.getElementById("__ac_password");
		acPassword.setValueAttribute(Configuration.ADMIN_PASSWORD);

		HtmlSubmitInput submit = (HtmlSubmitInput) loginPage.getElementByName("submit");

		HtmlPage loginPageOK = submit.click();

		if (!loginPageOK.asText().contains(Configuration.LOGIN_SUCCESS_MESSAGE)) {
			throw new Exception("Login error!");
		}
	}
}
