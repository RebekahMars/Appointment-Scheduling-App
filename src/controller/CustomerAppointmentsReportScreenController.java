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
 * This class is the controller for the Customer Appointment Reports Screen, effectively allowing for the display of customer appointments when the user selects a month from the choiceBox
 * @author rebek
 */
public class CustomerAppointmentsReportScreenController implements Initializable {
    
    @FXML private ChoiceBox appointmentMonthDrop;
    @FXML private TableView <Reports> customerAppointmentsTable;
    @FXML private TableColumn <Reports, String> appointmentTypeColumn;
    @FXML private TableColumn <Reports, Integer> appointmentTotalColumn;
    @FXML private Button cancelButton;
    
    @FXML private ObservableList<String> months = FXCollections.observableArrayList(Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"));

    /**
     * This method allows for the user to exit to the Main Menu Screen is the user selects the 'Cancel' button
     * @param event when the user selects the 'Cancel' button, the user is taken back to the Main Menu Screen
     * @throws IOException throws exception if there is an error in switching screens due to incorrect data parsing
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
     * This method filters the table on the screen based on the user selection from the choiceBox
     * The choiceBox contains all 12 calendar months. When a month is selected, the table displays customer appointments for that selected month only
     * @param choiceBox the choiceBox containing calendar months
     */
    @FXML
    public void filterAppointmentsTable(ChoiceBox<String> choiceBox)
    {
        String selectedMonth = choiceBox.getValue(); 
        customerAppointmentsTable.setItems(DBReports.getFilteredCustomerAppointmentsReport(selectedMonth));
        if(customerAppointmentsTable.getItems().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Unavailable");
            alert.setHeaderText("No Appointments to Show");
            alert.setContentText("There are no appointments for the month of " + selectedMonth);
            alert.showAndWait();
        }
    }

    /**
     * A lambda expression is used to set the Appointment Month Choice-Box/Drop-Down values. The setOnAction() method is used to determine if the user selected a month, and then filter the table appropriately
     * Based on the input, the lambda expression will call the DBReports().getFilteredCustomerAppointmentsReport() function to display the appropriate variables based on the selected month
     * The lambda expression is best used here as it increases the efficiency of the code in terms of event handling
     * @param url absolute URL
     * @param rb absolute ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        appointmentMonthDrop.setItems(months);
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentTotalColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTotal"));
        customerAppointmentsTable.setItems(DBReports.getCustomerAppointmentsReport());
        
        
        appointmentMonthDrop.setOnAction(e -> filterAppointmentsTable(appointmentMonthDrop));
        
    }    
    
}
