/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

/**
 * This class is the utility class for DB Queries
 * @author rebek
 */
public class DBQuery 
{
    private static Statement statement; //reference to statement variable
    private static PreparedStatement preparedStatement;
    
    //Create the Statement Object

    /**
     * This method sets the connection statement
     * @param connect connection variable
     * @throws SQLException
     */
    public static void setStatement(Connection connect) throws SQLException
    {
        statement = connect.createStatement();
    }
    
    //Return the Statement Object

    /**
     * This method gets the statement object
     * @return statement
     */
    public static Statement getStatement()
    {
        return statement;
    }

    /**
     * This method sets the prepared statement object
     * @param connect connection
     * @param sqlStatement sql string
     * @throws SQLException
     */
    public static void setPreparedStatement(Connection connect, String sqlStatement) throws SQLException
    {
        preparedStatement = connect.prepareStatement(sqlStatement);
    }

    /**
     * This method gets the prepared statement object
     * @return prepared statement object
     */
    public static PreparedStatement getPreparedStatement()
    {
        return preparedStatement;
    }
}
