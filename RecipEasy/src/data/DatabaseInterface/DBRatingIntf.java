//////////////////////////////////////////////////////////////////////////////////////
//																					//
//	DBRatingIntf - Database Interface for Rating Table								//
//	Author - Chris Costa															//
//																					//
//	Description: This class provides methods for interacting with information in 	//
//	the Rating table of the database.												//
//																					//
//																					//
//	Instance Variables:																//
//																					//
//		conn: A private object of type Connection (java.sql) used to direct access 	//
//			to the database.  Initialized in the constructor.						//
//																					//
//																					//
//	Constructors																	//
//																					//
//		Constructor: Creates a connection to the embedded database that all 		//
//			subsequent methods use.													//
//			Parameters: none.														//
//																					//
//																					//
//	Public Methods																	//
//																					//
//		getAllByUser: Uses the database connection to create a SQL query that 		//
//			returns all ratings that a user has made (Table: Rating, columns: 		//
//			RecipeID, Liked, Ease, Cost) using their userID.  The results are used 	//
//			to create Rating objects to be added to the user's User object.			//
//			Parameters: userID - int used to query the database for all related		//
//				records.															//
//			Returns: ratings - ArrayList of Rating obects used to store the details //
//				of each rating made by the user.									//
//																					//
//		getAllByRecipe: Uses the database connection to create a SQL query that 	//
//			returns all ratings that users have made on a specific recipe (Table: 	//
//			Rating, columns: Liked, Ease, Cost) using the recipeID.  The results 	//
//			are used to create Rating objects to be used as statistics in another 	//
//			class.																	//
//			Parameters: recipeID - int used to query the database for all related	//
//				records.															//
//			Returns: ratings - ArrayList of Rating obects used to store the details //
//				of each rating pertaining to the specified recipe					//
//																					//
//		createRating: Uses the database connection to query the Rating table for 	//
//			existing ratings pertaining to a specific userID and recipeID (table:	//
//			Rating, column: RatingID). If a record exists, it's ID is returned. If	//
//			it does not, a new record is created for the relationship, and the 		//
//			method is called recursively to return the newly created RatingID		//
//			Parameters: userID and recipeID - used to form a relationship for the 	//
//				Rating record.														//
//			Returns: ratingID - the specific record id  of the reltaionship between //
//				the recipe and user.												//
//																					//
//		updateRating: Uses the database connection to update the Rating table for 	//
//			a specific ratingID (table:	Rating, columns: Liked, Ease, Cost).  This  //
//			method should be called each time the user changes their ratings of a	//
//			recipe.																	//
//			Parameters: ratingID - the specific record id of the relationship 	 	//
//				between the recipe and user.										//
//				liked, ease, cost - the individual user specified ratings of the 	//
//				recipe.																//
//			Returns: nothing.														//
//																					//
//		close: closes the connection to the database, if it is open.				//
//			Parameters: none.														//
//			Returns: nothing.														//
//																					//
//////////////////////////////////////////////////////////////////////////////////////


package data.DatabaseInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import data.Users.Rating;

public class DBRatingIntf //Database Interface for Rating Table.
{
	//Database connection used throughout this class.
	private Connection conn;
	
	//Attempt to connect to the database. Always call the close method when finished with the DBUsersIntf object.
	public DBRatingIntf()
	{
		try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection("jdbc:derby:RecipeDB;create=false;");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
	
	//Attempt to get the ratings of each recipe that the specified user has rated.
	public ArrayList<Rating> getAllByUser(int userID)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<Rating> ratings = new ArrayList<Rating>(); //Returns an empty ArrayList if no ratings have been made.
		
		try //Attempt to query the Rating table for records relating to the Username.
		{
			pstmt = conn.prepareStatement("Select recipeID, liked, ease, cost from Rating where UserID = ?");
			pstmt.setInt(1, userID);
			res = pstmt.executeQuery();
            while(res.next()) //If records are found, use the fields to create a Rating object for each, and add it to the ArrayList.
            {
            	Rating rating = new Rating(res.getInt(1));
            	rating.setRatings(res.getInt(2), res.getInt(3), res.getInt(4));
            	ratings.add(rating);
            }
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		return ratings;
	}
	
	//Attempt to get every rating relating to a specific recipe
	public ArrayList<Rating> getAllByRecipe(int recipeID)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<Rating> ratings = new ArrayList<Rating>(); //Returns an empty ArrayList if no ratings have been made.
		
		try //Attempt to query the Rating table for records relating to the Recipe.
		{
			pstmt = conn.prepareStatement("Select liked, ease, cost from Rating where recipeID = ?");
			pstmt.setInt(1, recipeID);
			res = pstmt.executeQuery();
            while(res.next()) //If records are found, use the fields to create a Rating object for each, and add it to the ArrayList.
            {
            	Rating rating = new Rating(recipeID);
            	rating.setRatings(res.getInt(2), res.getInt(3), res.getInt(4));
            	ratings.add(rating);
            }
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		return ratings;
	}
	
	//Attempt to insert a record into the Rating table consisting of the UserID and RecipeID. Returns the newly created RatingID.
	public int createRating(int userID, int recipeID)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		int ratingID = -1; //If not RatingID is created, returns -1 for error handling.
		
		try //Attempt to query the Rating table for an existing RatingID for the relationship between the given UserID and RecipeID. 
		{
			pstmt = conn.prepareStatement("Select RatingID from Rating where recipeID = ? and userID = ?");
			pstmt.setInt(1, recipeID);
			pstmt.setInt(2, userID);
			res = pstmt.executeQuery();
			 try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); }; //Close the database object for reuse.
            
			 if(res.next()) //If the RatingID exists, return it for future use.
            {
            	return res.getInt(1);
            }
            else //If the RatingID does not exist, insert a new record into the table to generate a new one.
    		{
    			pstmt = conn.prepareStatement("Insert into Rating (UserID, RatingID) values (?, ?)");
    			pstmt.setInt(1, recipeID);
    			pstmt.setInt(2, userID);
    			pstmt.executeUpdate();
    			createRating(userID, recipeID); //Recursively call the function to return the newly generated RatingID.
    		}
		}
		
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		return ratingID;
	}
	
	//Attempt to update the Rating table record specified (RatingID) with new ratings (Liked, Ease, Cost).
	public void updateRating(int ratingID, int liked, int ease, int cost)
	{
		PreparedStatement pstmt = null;
		
		try //Attempt to update the specified record with new values.
		{
			pstmt = conn.prepareStatement("Update Rating Set Liked = ?, Ease = ?, Cost = ?  where RatingID = ?");
			pstmt.setInt(1, liked);
			pstmt.setInt(2, ease);
			pstmt.setInt(3, cost);
			pstmt.setInt(4, ratingID);
			pstmt.executeQuery();

		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
	}
	
	//Attempt to close the database connection.  This method should always be called when finished with the object.
	public void close()
	{
		try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); };
	}
}
