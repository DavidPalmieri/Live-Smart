package modifydb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBInsertCategory 
{
	
	private String dbURL = "jdbc:derby:RecipeDB;create=false;";
    private  Connection conn = null;

    public DBInsertCategory()throws SQLException
    {
    	try
    	{
    		Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
    		conn = DriverManager.getConnection(dbURL);
    	}
    	catch (Exception sqlExcept)
    	{
    		sqlExcept.printStackTrace();
    	}  
    }
    
    public Integer insertCategory(String category) throws SQLException
    {
    	Integer categoryID = null;
    	
    	 PreparedStatement pstmt = conn.prepareStatement("insert into Category (Category) values (?)");
    	 pstmt.setString(1, category);
    	 pstmt.executeUpdate();
    	 
    	 pstmt = conn.prepareStatement("Select CategoryID from Category where Category = ?");
         pstmt.setString(1, category);
         ResultSet results = pstmt.executeQuery();
         
         if (results.next())
         {
         	categoryID = results.getInt(1);
         }
    	
    	return categoryID;
    }
    
    public void insertCategoryRelation(Integer recipeID, Integer categoryID) throws SQLException
    {
    	PreparedStatement pstmt = conn.prepareStatement("Select RecCatID from RecipeCategory where RecipeID = ? And CategoryID = ?");
    	pstmt.setInt(1, recipeID);
   	 	pstmt.setInt(2, categoryID);
        ResultSet results = pstmt.executeQuery();
        
        if (results.next())
        {
        	return;
        }
    	
    	pstmt = conn.prepareStatement("insert into RecipeCategory (RecipeID, CategoryID) values (?, ?)");
    	pstmt.setInt(1, recipeID);
   	 	pstmt.setInt(2, categoryID);
   	 	pstmt.executeUpdate();
    }
}
