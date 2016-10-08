package com.blackbox.ids.user.connectivity;

/**
 * This class connect to the user authentication based server and checking the User existence  from that server.
 * 
 * @author BlackBox
 * @version 1.0
 * 
 */

public interface UserConnectivity {

	/**
	 * API check the user exist in the LDAP.
	 * 
	 * @param userName
	 *            {@link String}
	 */
	boolean checkUserExistence(String userName);
	
	boolean authenticate(String userName, String password);
	
	boolean checkConnection();

}
