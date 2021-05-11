/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contacts;
import model.Customers;

/**
 * Contact utility class
 * @author rebek
 */
public class DBContacts 
{
    private Contacts contact;

    /**
     * This method gets the contact
     * @return contact
     */
    public Contacts getContact()
    {
        return contact;
    }

    /**
     * This method gets all contacts from the database
     * @return all contacts in database
     */
    public static ObservableList<Contacts> getAllContacts()
    {
        ObservableList<Contacts> allContactsList = FXCollections.observableArrayList();
        try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM contacts";//
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               int contactID = rs.getInt("Contact_ID");
               String contactName = rs.getString("Contact_Name");
               String contactEmail = rs.getString("Email");
               allContactsList.add(new Contacts(contactID, contactName, contactEmail));
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return allContactsList;
    }

    /**
     * This method gets the contact names from the database
     * @return contact names
     */
    public static ObservableList<String> getContactNames()
    {
        ObservableList<String> contactNames = FXCollections.observableArrayList();
         try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM contacts";//
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               String contactName = rs.getString("Contact_Name");
               contactNames.add(contactName);
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return contactNames;
    }

    /**
     * This method gets the contact ID based on the contact name
     * @param appointmentContact contact name
     * @return contact ID
     */
    public static int getContactID(String appointmentContact) 
    {
        int contactID = 0;
         try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM contacts WHERE Contact_Name = '" + appointmentContact + "'";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               contactID = rs.getInt("Contact_ID");  
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return contactID;
    }

    /**
     * This method gets the contact name from the database based on the contact ID
     * @param appointmentContactID contact id
     * @return contact name
     */
    public static String getContactName(int appointmentContactID) 
    {
        String contactName = null;
         try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM contacts WHERE COntact_ID = '" + appointmentContactID + "'";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               contactName = rs.getString("Contact_Name");  
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return contactName;
    }
 


}
