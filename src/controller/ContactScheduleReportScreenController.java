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
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.Reports;
import utils.DBContacts;
import utils.DBReports;

/**
 * FXML Controller class
 * This class is the controller for the Contact Schedule Report Screen. This class allows for the user to see appointment information for each contact by selecting a name 
 * @author rebek
 */
public class ContactScheduleReportScreenController implements Initializable {
    
    /*Intializes the variables used in the controller*/
    @FXML private ChoiceBox contactNameDrop;
    @FXML private TableView contactScheduleTable;
    @FXML private TableColumn <Reports, Integer> appointmentIDColumn;
    @FXML private TableColumn <Reports, String> appointmentTitleColumn;
    @FXML private TableColumn <Appointments, String> appointmentTypeColumn;
    @FXML private TableColumn <Appointments, String> appointmentDescriptionColumn;
    @FXML private TableColumn <Appointments, String> appointmentStartColumn;
    @FXML private TableColumn <Appointments, String> appointmentEndColumn;
    @FXML private TableColumn <Appointments, Integer> customerIDColumn;

    /**
     * This method allows for the user to go back to the Main Menu screen after selecting the 'Cancel' button
     * @param event when the 'Cancel' button is selected, the user is taken back to the Main Menu screen
     * @throws IOException throws exception when screens cannot be switched, due to incorrect data parsing
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
     * This method filters the appointments table with the appointment information for a contact name selected by the user
     * The user selects a contact name from the choiceBox, and all appointment information is displayed for that contact
     * If there are no appointments for the contact, an alert is displayed with that information
     * @param choiceBox the choiceBox that contains all the Contact Names in the database
     */
    @FXML 
    public void filterContactScheduleTable(ChoiceBox <String> choiceBox)
    {
        String selectedContact = choiceBox.getValue(); 
        contactScheduleTable.setItems(DBReports.getFilteredAppointmentsforReport(selectedContact));
        if(contactScheduleTable.getItems().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Unavailable");
            alert.setHeaderText("No Appointments to Show");
            alert.setContentText("There are no appointments for " + selectedContact);
            alert.showAndWait();
        }
    }

    /**
     * This method initializes the Contact Schedule Report screen and sets variables appropriately
     * A lambda expression is used to set the Contact Name Choice-Box/Drop-Down values. The setOnAction() method is used to determine if the user selected a contact name, and then filter the table appropriately
     * Based on the input, the lambda expression will call the DBReports().getAllAppointmentsforReport() function to display the appropriate variables
     * The lambda expression is best used here as it increases the efficiency of the code in terms of event handling
     * @param url absolute URL
     * @param rb absolute ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        contactNameDrop.setItems(DBContacts.getContactNames());
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        
        contactScheduleTable.setItems(DBReports.getAllAppointmentsforReport());
        
        contactNameDrop.setOnAction(e -> filterContactScheduleTable(contactNameDrop));
      
    }    
    
}
