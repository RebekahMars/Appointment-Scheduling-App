/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * This class is the model for the User, includes getters/setters
 * @author rebek
 */
public class User 
{
    private int userID;
    private String username;
    private String password;
    
    /**
     *
     * @param userID
     * @param username
     * @param password
     */
    public User(int userID, String username, String password)
    {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }
    
    /**
     *
     * @return
     */
    public int getUserID()
    {
        return userID;
    }

    /**
     *
     * @return
     */
    public String getUserName()
    {
        return username;
    }

    /**
     *
     * @return
     */
    public String getPassword()
    {
        return password;
    }

    /**
     *
     * @param userID
     */
    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    /**
     *
     * @param username
     */
    public void setUserName(String username)
    {
        this.username = username;
    }

    /**
     *
     * @param password
     */
    public void setPassword (String password)
    {
        this.password = password;
    }
    
    
}
