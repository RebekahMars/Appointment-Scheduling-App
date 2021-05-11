/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is the utility class for the DB Connection
 * @author rebek
 */
public class DBConnection 
{
    //Java Database URL components
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ07NIR";
    
    //Java Database URL
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName + "?connectionTimeZone=SERVER";
    
    //Reference to Driver and Connection Interface
    private static final String MYSQLJBCDriver = "com.mysql.jdbc.Driver";
    
    //Username and Password
    private static final String username = "U07NIR";
    private static final String password = "53689075372";
    
    private static Connection connect = null;
    
    /**
     * This method starts the connection to the database
     * @return
     */
    public static Connection startConnection()
    {
        try
        {
            Class.forName(MYSQLJBCDriver);
            connect = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection successful!");
        }
        catch(SQLException e)
        {
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }
        
        return connect;
    }
    
    /**
     * This method is a getter for the connection
     * @return connection
     */
    public static Connection getConnection()
    {
        return connect;
    }

    /**
     * This method closes the connection to the database
     */
    public static void closeConnection()
    {
        try
        {
            connect.close();
            System.out.println("You have disconnected from the MySQL Database.");
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }   
}
