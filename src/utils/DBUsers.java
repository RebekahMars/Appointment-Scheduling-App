/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 * This class is the utility for Users
 * @author rebek
 */
import model.User;
import utils.DBConnection;
import utils.DBQuery;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Utility class for users
 * @author rebek
 */
public class DBUsers 
{
    private static User currentUser;
    
    /**
     *
     * @return current user
     */
    public static User getCurrentUser()
    {
        return currentUser;
    }
    
    /**
     * This method determines if a username and password combination is valid in the database
     * @param username username
     * @param password password
     * @return
     */
    public static boolean applicationLoginTest (String username, String password) //throws SQLException
    {
        String userDB = null;
        String passwordDB = null;
        
        try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM users WHERE User_Name = '" + username + "' AND Password = '" + password + "'";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
                userDB = rs.getString("User_Name");
                passwordDB = rs.getString("Password");
            }
            
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        
        if(username.equals(userDB))
        {
            if(password.equals(passwordDB))
            {
               int userID = DBUsers.getUserID(username);
               DBUsers.currentUser = new User(userID, username, password);
               currentUser.setUserID(userID);
               currentUser.setUserName(username);
               currentUser.setPassword(password);
               
               String logInAttempt = "SUCCESS";
               DBLogger.logActivity(username, logInAttempt);
               return true;
            }
        }
        else
        {
            String logInAttempt = "FAILURE";
            DBLogger.logActivity(username, logInAttempt);
            return false;
        }
       return false;
    }

    /**
     * This method gets the user id from the database based on the username
     * @param username username
     * @return userID
     */
    public static int getUserID(String username)
    {
        int userID = 0;
        
        try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM users WHERE User_Name = '" + username + "'";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
                userID = rs.getInt("User_ID");               
            }
            
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        
        return userID;
        
    }
    public static ObservableList<String> getAllUserIDs()
    {
        ObservableList userIDs = FXCollections.observableArrayList();
        try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM users";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
                int userID = rs.getInt("User_ID");   
                userIDs.add(userID);
            }
            
            
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        
        return userIDs;
    }
}
