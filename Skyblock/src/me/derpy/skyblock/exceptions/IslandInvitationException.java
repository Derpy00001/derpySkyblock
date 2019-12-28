/*
 *        derpySkyblock - Derpy00001 | Derpy#5247
 *        discord.gg/bQxBB89
 *        Hi! :)
 */

package me.derpy.skyblock.exceptions;

public class IslandInvitationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public IslandInvitationException(String Message) {
		super(Message);
		this.fillInStackTrace();
	}


}
