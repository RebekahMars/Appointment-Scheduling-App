/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Reports;
import utils.DBReports;

/**
 * FXML Controller class
 * This class is the controller class for the Contact Appointments Report and allows for the display of appointments for contacts by month
 * The user selects the month from a Choice-Box at the top of the screen. The month selection filters the appointments by type and contact and displays the information in a Table
 * @author rebek
 */
public class ContactAppointmentsReportScreenController implements Initializable {
    
    @FXML private ObservableList<String> months = FXCollections.observableArrayList(Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"));
    @FXML private ChoiceBox appointmentMonthDrop;
    @FXML private TableView <Reports> contactAppointmentsTable;
    @FXML private TableColumn <Reports, String> contactNameColumn;
    @FXML private TableColumn <Reports, Integer> appointmentTotalColumn;
    @FXML private Button exitButton;
    
    /**
     * This method allows for the user to go back to the Main Menu screen after selecting the 'Cancel' button
     * @param event when the 'Cancel' button is selected, the method will bring the user back to the Main Menu screen
     * @throws IOException exception is thrown is unable to switch screens, due to incorrect data parsing
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
     * This method allows for the user to select a month from a Choice Box, which then filters the Table to display appointments by type and their assigned contacts for the selected month
     * @param choiceBox the Choice Box containing all 12 months in a calendar year 
     */
    @FXML
    public void filterAppointmentsTable(ChoiceBox<String> choiceBox)
    {
        String selectedMonth = choiceBox.getValue(); 
        contactAppointmentsTable.setItems(DBReports.getFilteredContactAppointmentsReport(selectedMonth));
        if(contactAppointmentsTable.getItems().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Unavailable");
            alert.setHeaderText("No Appointments to Show");
            alert.setContentText("There are no appointments for the month of " + selectedMonth);
            alert.showAndWait();
        }
    }

    /**
     * This method initializes the Contact Appointments Report Screen and sets variables accordingly.
     * A lambda expression is used to set the Contact Name Choice-Box/Drop-Down values. The setOnAction() method is used to determine if the user selected a month, and then filters the table appropriately
     * Based on the input, the lambda expression will call the DBReports.getContactAppointmentsReport() function to display the appropriate variables based on the month selected
     * The lambda expression is best used here as it increases the efficiency of the code in terms of event handling
     * @param url absolute URL
     * @param rb absolute ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
       contactNameColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
       appointmentTotalColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTotal"));
       appointmentMonthDrop.setItems(months);
       contactAppointmentsTable.setItems(DBReports.getContactAppointmentsReport());
       
       appointmentMonthDrop.setOnAction(e -> filterAppointmentsTable(appointmentMonthDrop));
    }    
    
}
