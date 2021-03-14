package joomla2plone.joomla;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;

import joomla2plone.joomla.entidades.Configuration;
import joomla2plone.joomla.entidades.Group;
import joomla2plone.joomla.entidades.User;
import joomla2plone.joomla.entidades.ViewLevel;
import joomla2plone.plone.webclient.GroupAssignmentWebClient;
import joomla2plone.plone.webclient.GroupWebClient;
import joomla2plone.plone.webclient.LoginWebClient;
import joomla2plone.plone.webclient.RoleAssignmentWebClient;
import joomla2plone.plone.webclient.RoleWebClient;
import joomla2plone.plone.webclient.UserWebClient;

public class Run {

	private static final Logger LOGGER = Logger.getLogger(Run.class.getName());

	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JoomlaPersistenceUnit");
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query queryUsers = entityManager.createQuery("SELECT user FROM User user", User.class);
		List<User> users = queryUsers.getResultList();

		Query queryGroups = entityManager.createQuery("SELECT group FROM Group group", Group.class);
		List<Group> groups = queryGroups.getResultList();

		for (Group group : groups) {
			Query queryUsersIds = entityManager.createNativeQuery("SELECT user_id FROM " + Configuration.TABLE_PREFIX + "user_usergroup_map WHERE group_id = :groupId");
			queryUsersIds.setParameter("groupId", group.getId());
			List<Integer> usersIds = queryUsersIds.getResultList();

			for (Integer userId : usersIds) {
				User user = entityManager.find(User.class, Long.valueOf(userId));
				if (user != null) {
					group.getUsers().add(user);
				}
			}

		}

		Query queryViewLevels = entityManager.createQuery("SELECT viewLevel FROM ViewLevel viewLevel", ViewLevel.class);
		List<ViewLevel> viewLevels = queryViewLevels.getResultList();

		for (ViewLevel viewLevel : viewLevels) {
			for (Long id : viewLevel.getRulesIds()) {
				Group group = entityManager.find(Group.class, id);
				if (group != null) {
					viewLevel.getGroups().add(group);
				}
			}
		}

		try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {

			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setDoNotTrackEnabled(true);
			webClient.getOptions().setGeolocationEnabled(false);
			webClient.getOptions().setAppletEnabled(false);
			webClient.getOptions().setDownloadImages(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setPopupBlockerEnabled(true);
			webClient.getOptions().setCssEnabled(false);

			LoginWebClient.start(webClient);

			new UserWebClient(users).start(webClient);

			new GroupWebClient(groups).start(webClient);

			new RoleWebClient(viewLevels).start(webClient);

			new RoleAssignmentWebClient(viewLevels).start(webClient);
			
			new GroupAssignmentWebClient(groups).start(webClient);

		} catch (FailingHttpStatusCodeException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (MalformedURLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

		entityManager.close();
		entityManagerFactory.close();
	}

}
