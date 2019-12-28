/*
 *        derpySkyblock - Derpy00001 | Derpy#5247
 *        discord.gg/bQxBB89
 *        Hi! :)
 */

package me.derpy.skyblock.exceptions;

public class UserHasIslandException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UserHasIslandException(String Message) {
		super(Message);
		this.fillInStackTrace();
	}


}
