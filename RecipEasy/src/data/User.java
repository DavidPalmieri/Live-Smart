 ////////////////////////////////////////////////////////////////////////////////////////////////////////+
//																										//
//	User: User information class																		//	
//	Author: Chris Costa																					//
//																										//
//	<== Constructors ==>																				//
//																										//	
//		User(int userID, String username)																//
//			Creates a new user object, setting the userID and username to the values of the parameters	//
//																										//
//	<== Accessors ==>																					//
//																										//
//		getUserID(): int																				//
//			returns the value of the userID variable of the user										//
//																										//
//		getUsername(): String																			//
//			returns the value of the username variable of the user										//
//																										//
/////////////////////////////////////////////////////////////////////////////////////////////////////////

package data;

public class User 
{
	//ID of the user
	private int userID;
	
	//name of the user
	private String username;
	
	//Create a new User object, setting the userID and username
	public User(int userID, String username)
	{
		this.userID = userID;
		this.username = username;
	}
	
	//get the userID of the user
	public int getUserID()
	{
		return userID;
	}
	
	//get the username of the user
	public String getUsername()
	{
		return username;
	}
}
