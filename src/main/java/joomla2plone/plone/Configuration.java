package joomla2plone.plone;

public class Configuration {

	public static final String HOST_URL = "http://localhost:8082/";
	
	public static final String SITE_PREFIX = "portal/";
	
	public static final String LOGIN_URL = Configuration.HOST_URL + Configuration.SITE_PREFIX + "login";
	public static final String LOGIN_SUCCESS_MESSAGE = "Você agora está autenticado";
	
	public static final String ADMIN_LOGIN = "admin";
	public static final String ADMIN_PASSWORD = "admin";
	
	public static final String USER_URL_ADD = Configuration.HOST_URL + Configuration.SITE_PREFIX + "acl_users/source_users/manage_users?adding=1";
	public static final String USER_DEFAULT_PASSWORD = "123123";
	
	public static final String GROUP_URL_ADD = Configuration.HOST_URL + Configuration.SITE_PREFIX + "acl_users/source_groups/manage_groups?adding=1";
	
	public static final String ROLE_URL_ADD = Configuration.HOST_URL + Configuration.SITE_PREFIX + "acl_users/portal_role_manager/manage_roles?adding=1";
	
	public static final String ROLE_ASSIGNMENT_URL_ADD = Configuration.HOST_URL + Configuration.SITE_PREFIX + "acl_users/portal_role_manager/manage_roles?assign=1&role_id=";
	
	public static final String GROUP_ASSIGNMENT_URL_ADD = Configuration.HOST_URL + Configuration.SITE_PREFIX + "acl_users/source_groups/manage_groups?assign=1&group_id=";
	
}
