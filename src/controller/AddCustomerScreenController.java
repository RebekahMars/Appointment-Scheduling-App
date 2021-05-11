/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DBCustomers;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import model.Customers;
import utils.DBUsers;

/**
 * FXML Controller class 
 * This class is the controller class for the Add Customer Screen, and allows for creation of a customer 
 * @author rebek
 */
public class AddCustomerScreenController implements Initializable 
{
    /*Initialize the variables used within the controller*/
    @FXML private TextField customerIDEntry; 
    @FXML private TextField customerNameEntry;
    @FXML private ChoiceBox customerCountryDrop;
    @FXML private TextField customerAddress1;
    @FXML private ChoiceBox customerProvinceDrop;
    @FXML private TextField customerPostalCodeEntry;
    @FXML private TextField customerPhoneNumberEntry;
    @FXML private Label customerLocation;
    
    /**
     * This method is called when the 'Cancel' button is selected in the screen, which takes the user to the Main Menu screen without saving their data entries for customer creation
     * @param event when the 'Cancel' button is selected 
     * @throws IOException exception is thrown if screens cannot change, due to data parsing 
     */
    @FXML 
    public void exitMainMenu (ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreenJFX.fxml"));
        Scene addAppointmentScene = new Scene(root);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addAppointmentScene);
        window.show();
    }

    /**
     * This method is called when the user selects a value from the Country Name drop-down
     * This method sets the values of the Customer Province drop-down to correspond to the values listed in the database for the specific Country Name selected
     * If U.S. happens to be selected, the text is changed to Customer State. If the U.S. is deselected, the text reverts back to Customer Division
     * @param choiceBox the Country Name choice-box listed on the screen
     */
    @FXML 
    public void setDivisions (ChoiceBox<String> choiceBox)
    {
        String countryName = choiceBox.getValue();
        int countryID = DBCustomers.getCountryID(countryName);
        if(countryID == 1)
        {
            customerLocation.setText("CUSTOMER STATE");
        }
        else
        {
            customerLocation.setText("CUSTOMER DIVISION");
        }
        customerProvinceDrop.setItems(DBCustomers.getCustomerCountryDivisionsList(countryID));
        
    }

    /**
     * This method is used to save a new customer to the database after validating user entries (value is not empty or contains the null value)
     * Once the entries have been proven to be valid (the error list is empty), the new customer is saved to the database and the user is taken back to the Main Menu screen
     * A lambda expression is used to parse through the error messages from the ObservableList, map them to a new array with the Alert messages, and then print each message in order using the forEachOrdered() method
     * The lambda expression is efficient for this use as the expression allows for effective looping through customer data and displaying information without the need of creating a separate function 
     * @param event method is called when the 'Confirm' button is clicked on the screen
     * @throws IOException exception thrown if there is an error parsing through data
     */
    @FXML
    public void saveNewCustomer (ActionEvent event) throws IOException
    {
        int customerID = 0;
        ObservableList<String> customerEntryValidation = FXCollections.observableArrayList();
        try
        {
            Connection connect = DBConnection.getConnection();
            String insertStatement = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?,?)";
            PreparedStatement ps = connect.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
            
            String customerName = customerNameEntry.getText();
            String customerCountry = (String) customerCountryDrop.getValue();
            String customerAddress = customerAddress1.getText();
            String customerDivision = (String) customerProvinceDrop.getValue();
            String customerPostalCode = customerPostalCodeEntry.getText();
            String customerPhoneNumber = customerPhoneNumberEntry.getText();
            String customerCreator = DBUsers.getCurrentUser().getUserName();
            String customerUpdator = DBUsers.getCurrentUser().getUserName();
            
            
            customerEntryValidation = validCustomerEntry(customerName, customerCountry, customerAddress, customerDivision, customerPostalCode, customerPhoneNumber);
            
            if(!(customerEntryValidation.isEmpty()))
            {
                customerEntryValidation.stream().map((element) -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Saving Customer");
                alert.setHeaderText("Invalid Customer Entry");
                alert.setContentText(element);
                return alert;
            }).forEachOrdered((alert) -> {
                    alert.showAndWait();
            });
            }
            else
            {
                int customerDivisionID = DBCustomers.getDivisionID(customerDivision);
                
                ps.setString(1, customerName);
                ps.setString(2, customerAddress);
                ps.setString(3, customerPostalCode);
                ps.setString(4, customerPhoneNumber);
                ps.setString(5, customerCreator);
                ps.setString(6, customerUpdator);
                ps.setInt(7, customerDivisionID);
                ResultSet rs = ps.getGeneratedKeys();
            
                while(rs.next())
                {
                    customerID = rs.getInt(1);
                }
                ps.execute();
                if(ps.getUpdateCount() > 0)
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Add Customer Successful");
                    alert.setHeaderText("New Customer Added");
                    alert.setContentText("You have successfully created a customer.");
                    alert.showAndWait();
                
                    Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreenJFX.fxml"));
                    Scene mainMenu = new Scene(root);      
                    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                    window.setScene(mainMenu);
                    window.setTitle("Scheduling Application: Main Menu");
                    window.show();           
                }
            }
                  
        }
        catch(SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    /**
     * This method is used to validate each user entry on the Add Customer Screen
     * If the entry is empty or has the null value, the appropriate error message is added to a message list to be displayed to the user
     * @param name the name of the customer
     * @param country the country of the customer
     * @param address the address of the customer
     * @param division the division of the customer (or state if in US)
     * @param postalCode the postal code of the customer
     * @param phoneNumber the phone number of the customer
     * @return an error message if any variable is empty or contains the null value
     */
    @FXML
    public ObservableList<String> validCustomerEntry(String name, String country, String address, String division, String postalCode, String phoneNumber)
    {
        ObservableList<String> errorMessage = FXCollections.observableArrayList();
        
        if(name.isEmpty())
        {
            errorMessage.add("Please enter a name.");
        }
        if(country == null)
        {
            errorMessage.add("Please select a country.");
        }
        if(address.isEmpty() || address.length() <= 0)
        {
            errorMessage.add("Please enter an address.");
        }
        if(division == null)
        {
            errorMessage.add("Please select a division/state.");
        }
        if(postalCode.isEmpty())
        {
            errorMessage.add("Please enter a postal code.");
        }
        if(phoneNumber.isEmpty())
        {
            errorMessage.add("Please enter a phone number.");
        }
        
        return errorMessage;
    }

    /**
     * This method initializes the Add Customer Screen and sets parameters appropriately
     * A lambda expression is used to set the Customer Country Choice-Box/Drop-Down values. The setOnAction() method is used to determine if the user selected an input
     * Based on the input, the lambda expression will call the setDivisions() function to display the appropriate variables
     * The lambda expression is best used here as it increases the efficiency of the code in terms of event handling
     * @param url absolute URL
     * @param rb absolute ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        customerIDEntry.setDisable(true);
        customerCountryDrop.setItems(DBCustomers.getCustomerCountryList());
        customerCountryDrop.setOnAction(e -> setDivisions(customerCountryDrop));
    }    
    
}
