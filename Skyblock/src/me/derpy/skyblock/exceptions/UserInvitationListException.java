/*
 *        derpySkyblock - Derpy00001 | Derpy#5247
 *        discord.gg/bQxBB89
 *        Hi! :)
 */

package me.derpy.skyblock.exceptions;

public class UserInvitationListException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UserInvitationListException(String Message) {
		super(Message);
		this.fillInStackTrace();
	}


}
