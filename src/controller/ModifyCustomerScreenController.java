/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import model.Customers;
import utils.DBConnection;
import utils.DBCustomers;
import utils.DBUsers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 * This class is the controller for the Modify Customer Screen
 * Allows the user to modify a customer if data fields are modified correctly per validity checkers
 * If properly modified, the customer is modified and the user is taken back to the Main Menu
 * @author rebek
 */
public class ModifyCustomerScreenController implements Initializable {
    @FXML private TextField customerIDEntry; 
    @FXML private TextField customerNameEntry;
    @FXML private ChoiceBox customerCountryDrop;
    @FXML private TextField customerAddress1;
    @FXML private ChoiceBox customerProvinceDrop;
    @FXML private Label customerLocation;
    @FXML private TextField customerPostalCodeEntry;
    @FXML private TextField customerPhoneNumberEntry;

    /**
     *
     * @param event
     * @throws IOException
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
     *
     * @param choiceBox
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
     * This method brings the selected Customer from the Main Menu to the Modify Customer Screen and sets the data fields appropriately
     * @param selectedCustomer the user selected Customer
     */
    @FXML
    public void sendCustomer(Customers selectedCustomer)
    {
        int customerDivisionID = selectedCustomer.getCustomerDivisionID();
        String customerDivisionName = DBCustomers.getDivisionName(customerDivisionID);
        
        int customerCountryNameID = DBCustomers.getCountryIDfromDivision(customerDivisionName);
        String customerCountryName = DBCustomers.getCountryName(customerCountryNameID);
        
        customerIDEntry.setText(String.valueOf(selectedCustomer.getCustomerID()));
        customerNameEntry.setText(String.valueOf(selectedCustomer.getCustomerName()));
        customerCountryDrop.setValue(customerCountryName);
        customerAddress1.setText(String.valueOf(selectedCustomer.getCustomerAddress()));
        customerProvinceDrop.setValue(customerDivisionName);
        customerPostalCodeEntry.setText(String.valueOf(selectedCustomer.getCustomerPostalCode()));
        customerPhoneNumberEntry.setText(String.valueOf(selectedCustomer.getCustomerPhoneNumber()));
             
    }

    /**
     * This method allows the user to modify a selected customer attributes
     * If the changes are valid, the selected customer is updated and the user is sent back to the Main menu
     * @param event method is called when the user selects the Confirm button
     * @throws IOException exception thrown if unable to modify customer and switch to the Main Menu
     */
    @FXML
    public void modifyCustomer(ActionEvent event) throws IOException
    {
        ObservableList<String> customerEntryValidation = FXCollections.observableArrayList();
        String customerID = customerIDEntry.getText();
        
         
         try
         {
            Connection connect = DBConnection.getConnection();
            String modifyStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = CURRENT_TIMESTAMP, Last_Update = CURRENT_TIMESTAMP, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = '" + customerID + "'";
            PreparedStatement ps = connect.prepareStatement(modifyStatement);
            
            String customerName = customerNameEntry.getText();
            String customerCountry = (String) customerCountryDrop.getValue();
            String customerAddress = customerAddress1.getText();
            String customerDivision = (String) customerProvinceDrop.getValue();
            String customerPostalCode = customerPostalCodeEntry.getText();
            String customerPhoneNumber = customerPhoneNumberEntry.getText();
            String customerUpdator = DBUsers.getCurrentUser().getUserName();
            
            customerEntryValidation = validCustomerEntry(customerName, customerCountry, customerAddress, customerDivision, customerPostalCode, customerPhoneNumber);
            
             if(!(customerEntryValidation.isEmpty()))
            {
                customerEntryValidation.stream().map((element) -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Modifying Customer");
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
                ps.setString(5, customerUpdator);
                ps.setInt(6, customerDivisionID);
            
                ps.executeUpdate();
            
                if(ps.getUpdateCount() > 0)
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Modify Customer Successful");
                    alert.setHeaderText("Customer Modified");
                    alert.setContentText("You have successfully modified a customer.");
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
     * This method determines if the data fields are valid, or if the entries are not null or empty
     * If null or empty, an error message is generated based on the data field and returned
     * @param name customer name
     * @param country customer country
     * @param address customer address
     * @param division customer division or state
     * @param postalCode customer postal code
     * @param phoneNumber customer phone number
     * @return error message if data entries are invalid (if any)
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
     * This method initializes the Modify Customer Screen, and sets parameters appropriately 
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
