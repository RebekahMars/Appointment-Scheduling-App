/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author rebek
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.Customers;

/**
 * This class is the utility class for the Customers
 * @author rebek
 */
public class DBCustomers
{
            
    /**
     * This method gets all customers from the database
     * @return all customers
     */
    public static ObservableList<Customers> getAllCustomers()
    {
        ObservableList<Customers> allCustomersList = FXCollections.observableArrayList();
        try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM customers";//
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               int customerID = rs.getInt("Customer_ID");
               String customerName = rs.getString("Customer_Name");
               String customerAddress = rs.getString("Address");
               String customerPostalCode = rs.getString("Postal_Code");
               String customerPhoneNumber = rs.getString("Phone");
               int customerDivisionID = rs.getInt("Division_ID");
               allCustomersList.add(new Customers(customerID, customerName, customerAddress, customerPostalCode, customerPhoneNumber, customerDivisionID));
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return allCustomersList;
    }

    /**
     * This method gets all customer IDs from database
     * @return customer id
     */
    public static ObservableList<String> getCustomerIDs()
    {
        ObservableList<String> customerIDs = FXCollections.observableArrayList();
         try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM customers";//
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               String customerID = rs.getString("Customer_ID");
               customerIDs.add(customerID);
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return customerIDs;
    }

    /**
     * This method gets all customer country origin
     * @return customer country
     */
    public static ObservableList<String> getCustomerCountryList()
     {
          ObservableList<String> countries = FXCollections.observableArrayList();
         try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM countries";//
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               String countryName = rs.getString("Country");
               countries.add(countryName);
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return countries;
     }

    /**
     * This method gets all divisions from the database based on the country id
     * @param countryID id of country
     * @return country divisions as list
     */
    public static ObservableList<String> getCustomerCountryDivisionsList(int countryID)
     {
          ObservableList<String> countryDivisions = FXCollections.observableArrayList();
         try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = '" + countryID + "'";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               String countryName = rs.getString("Division");
               countryDivisions.add(countryName);
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return countryDivisions;
     }

    /**
     * This method gets the division id based on the division name in database
     * @param divisionName name of division
     * @return division id
     */
    public static int getDivisionID (String divisionName) 
    {
        int divisionID = 0;
        try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM first_level_divisions WHERE Division = '" + divisionName + "'";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               divisionID = rs.getInt("Division_ID");  
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return divisionID;
    }

    /**
     * This method gets the division name based on the division id from the database
     * @param divisionID id of division
     * @return division name
     */
    public static String getDivisionName (int divisionID)
    {
        String divisionName = null;
        
        try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM first_level_divisions WHERE Division_ID = '" + divisionID + "'";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               divisionName = rs.getString("Division");  
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return divisionName;
    }

    /**
     * This method gets the country ID based on the country name in the database
     * @param countryName name of country
     * @return country id
     */
    public static int getCountryID (String countryName) 
    {
        int countryID = 0;
        try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM countries WHERE Country = '" + countryName + "'";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               countryID = rs.getInt("Country_ID");  
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return countryID;
    }
 
    /**
     * This method gets the country name based on the country id in the database
     * @param countryNameID country id
     * @return country name
     */
    public static String getCountryName (int countryNameID) 
    {
        String countryName = null;
        try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM countries WHERE Country_ID = '" + countryNameID + "'";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               countryName = rs.getString("Country");  
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return countryName;
    }

    /**
     * This method gets the country id based on the division name in the database
     * @param divisionName name of division
     * @return country id
     */
    public static int getCountryIDfromDivision (String divisionName)
    {
        int countryID = 0;
        try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM first_level_divisions WHERE Division = '" + divisionName + "'";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               countryID = rs.getInt("Country_ID");  
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return countryID;
        
    }

    /**
     * This method deletes the selected customer from the database
     * @param customerID id of customer
     */
    @FXML
    public static void deleteSelectedCustomer(int customerID)
    {
        try
        {
            Connection connect = DBConnection.getConnection();  
            String deleteStatement = "DELETE FROM customers WHERE Customer_ID = '" + customerID + "'";
            PreparedStatement ps = connect.prepareStatement(deleteStatement);
            ps.execute();
            
            if(ps.getUpdateCount() > 0)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Delete Customer Successful");
                alert.setHeaderText("Customer Deleted");
                alert.setContentText("You have successfully deleted a customer.");
                alert.showAndWait();
            }
      
        }
        catch(SQLException e)
        {
            System.out.println("SQLException " + e.getMessage());
        }
    }
    
}
