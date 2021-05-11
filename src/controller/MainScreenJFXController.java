/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.Customers;
import utils.DBAppointments;
import utils.DBCustomers;

/**
 * FXML Controller class
 * This class is the controller class for the Main Screen. The Main Screen displays all appointment and customer information as well as include buttons for the user to go to different screens
 * to perform various tasks. These include adding/modifying customers, adding/modfying appointments, viewing reports, and exiting the application.
 * @author rebek
 */
public class MainScreenJFXController implements Initializable 
{
    @FXML private Button addAppointmentButton;
    @FXML private Button modifyAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button addCustomerButton;
    @FXML private Button modifyCustomerButton;
    @FXML private Button deleteCustomerButton;
    @FXML private Button allAppointmentsButton;
    @FXML private Button weeklyAppointmentsButton;
    @FXML private Button monthlyAppointmentsButton;
    @FXML private Button customerAppointmentsButton;
    @FXML private Button contactScheduleButton;
    @FXML private Button contactAppointmentsButton;
    @FXML private Button exitButton;
    
    @FXML private TableView <Appointments> appointmentInformationTable;
    @FXML private TableView <Customers> customerInformationTable;
    @FXML private TableColumn <Appointments, Integer> appointmentIDColumn;
    @FXML private TableColumn <Appointments, String> appointmentTitleColumn;
    @FXML private TableColumn <Appointments, String> appointmentDescriptionColumn;
    @FXML private TableColumn <Appointments, String> appointmentLocationColumn;
    @FXML private TableColumn <Appointments, Integer> appointmentContactColumn;
    @FXML private TableColumn <Appointments, String> appointmentTypeColumn;
    @FXML private TableColumn <Appointments, String> appointmentStartColumn;
    @FXML private TableColumn <Appointments, String> appointmentEndColumn;
    @FXML private TableColumn <Appointments, Integer> appointmentCustomerIDColumn;
    
    @FXML private TableColumn <Customers, Integer> customerIDColumn;
    @FXML private TableColumn <Customers, String> customerNameColumn;
    @FXML private TableColumn <Customers, String> customerAddressColumn;
    @FXML private TableColumn <Customers, Integer> customerPostalCodeColumn;
    @FXML private TableColumn <Customers, String> customerPhoneNumberColumn;
    
    @FXML private Appointments selectedAppointment;
    @FXML private Customers selectedCustomer;
    @FXML private ObservableList<Appointments> allAppointments;
    @FXML private ObservableList<Customers> allCustomers;
    
    /**
     * This method displays all appointment information in the appointment table after the user selects the All Appointments button
     * @param event if the user selects the All Appointments button, all appointments are displayed in the Table
     * @throws IOException exception is thrown if appointment data cannot be shown due to incorrect data parsing
     */
    @FXML
    public void showAllAppointments (ActionEvent event) throws IOException
    {
        appointmentInformationTable.setItems(DBAppointments.getAllAppointments());
    }

    /**
     * This method displays weekly appointment information in the appointment table after the user selects the Weekly Appointments button
     * @param event displays weekly appointments after user selects the Weekly Appointments button
     * @throws IOException exception is thrown if appointment data cannot be shown due to incorrect data parsing
     */
    @FXML
    public void showWeeklyAppointments (ActionEvent event) throws IOException
    {
        appointmentInformationTable.setItems(DBAppointments.getWeeklyAppointments());
       
    }

    /**
     * This method displays monthly appointment information in the appointment table after the user selects the Monthly Appointments button
     * @param event displays monthly appointments after the user selects the Monthly Appointments button
     * @throws IOException exception is thrown if appointment data cannot be shown due to incorrect data parsing
     */
    @FXML
    public void showMonthlyAppointments (ActionEvent event) throws IOException
    {
        appointmentInformationTable.setItems(DBAppointments.getMonthlyAppointments());
    }

    /**
     * This method takes the user to the Add Appointment screen if the Add button is selected under the appointment tale
     * @param event switches the screen to the Add Appointment screen if the Add button is selected
     * @throws IOException exception is thrown if unable to switch to the Add Appointment screen due to incorrect data parsing
     */
    @FXML
    public void addAppointmentScreen (ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddAppointmentScreen.fxml"));
        Scene addAppointmentScene = new Scene(root);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addAppointmentScene);
        window.setTitle("Scheduling Application: Add Appointment");
        window.show();
    }

    /**
     * This method allows the user to modify an existing appointment after selecting an appointment from the table and clicking the 'Modify' button under the appointment table
     * The user can only modify one appointment at a time and must select an appointment before clicking the 'Modify' button, or an alert will be displayed
     * @param event the Modify Appointment screen is shown if the user first selects and appointment and clicks the 'Modify' button under the Appointments table
     * @throws IOException exception is thrown if unable to switch to the Modify Appointment screen due to incorrect data parsing
     */
    @FXML
    public void modifyAppointmentScreen (ActionEvent event) throws IOException
    {
        selectedAppointment = appointmentInformationTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyAppointmentScreen.fxml"));
        Parent parent = loader.load();
        Scene modifyAppointmentScreen = new Scene(parent);
        ModifyAppointmentScreenController controller = loader.getController();
      
        
        if(selectedAppointment == null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Modifying Appointment");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("You cannot modify an Appointment without\n selecting one from the table first.");
            alert.showAndWait();
        }
        else
        {
            controller.sendAppointment(selectedAppointment);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(modifyAppointmentScreen);
            window.setTitle("Scheduling Application: Modify Existing Appointment");
            window.show();
        }
    }

    /**
     * This method deletes a selected appointment from the appointments table. An appointment must be selected before the delete button is clicked, or an error will show
     * The appointment is then removed from the database, and the table updated to reflect the action
     * @param event deletes appointment if appointment is selected and then the 'delete' button is clicked
     * @throws IOException exception is thrown if unable to delete appointment due to incorrect data parsing
     */
    @FXML
    public void deleteAppointment (ActionEvent event) throws IOException
    {
        selectedAppointment = appointmentInformationTable.getSelectionModel().getSelectedItem();
           
        if(selectedAppointment == null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Deleting Appointment");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("You cannot delete an Appointment without\n selecting one from the table first.");
            alert.showAndWait(); 
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete of Appointment");
            alert.setHeaderText("Confirm Delete");
            alert.setContentText("Are you sure you want to delete this appointment?");
            Optional<ButtonType> deleteAppointmentResult = alert.showAndWait(); 
            
            if(deleteAppointmentResult.get() == ButtonType.OK)
            {
                int appointmentID = appointmentInformationTable.getSelectionModel().getSelectedItem().getAppointmentID();
                String appointmentType = appointmentInformationTable.getSelectionModel().getSelectedItem().getAppointmentType();
                DBAppointments.deleteSelectedAppointment(appointmentID, appointmentType);
                appointmentInformationTable.setItems(DBAppointments.getAllAppointments());
            }

        }
    }

    /**
     * This method deletes a customer from the database. A customer must be selected from the customers table and must not have any existing appointments in order to be deleted,
     * otherwise an error is thrown. 
     * A lambda expression is used to filter the user selection of deleting an appointment, calling the DBCustomers.deleteSelectedCustomer based on the customer ID to match the ID to 
     * the correct appointment in the database and delete it. The lambda expression then resets the customer table to display the updated customers information. 
     * The lambda expression is best used here as it increases the efficiency of the code in terms of event handling as well as reduce the lines of code written. 
     * @param event deletes customer if user selects the 'Delete' button under the customers table, as long as a customer is selected and the customer does not have any appointments
     * @throws IOException
     */
    @FXML
    public void deleteCustomer (ActionEvent event) throws IOException
    {
        selectedCustomer = customerInformationTable.getSelectionModel().getSelectedItem();
        int customerID = selectedCustomer.getCustomerID();
        
        ObservableList<Appointments> filteredAppointments = DBAppointments.customerAppointmentChecker(customerID);
           
        if(selectedCustomer == null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Deleting Customer");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("You cannot delete a Customer without\n selecting one from the table first.");
            alert.showAndWait(); 
        }
        if(!(filteredAppointments.isEmpty()))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Deleting Customer");
            alert.setHeaderText("Customer Appointments Schedule");
            alert.setContentText("You cannot delete a Customer without\n deleting their scheduled appointments first.");
            alert.showAndWait(); 
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete of Customer");
            alert.setHeaderText("Confirm Delete");
            alert.setContentText("Are you sure you want to delete this Customer?");
            alert.showAndWait()
                    .filter(deleteCustomerResult -> deleteCustomerResult == ButtonType.OK)
                    .ifPresent(deleteCustomerResult -> DBCustomers.deleteSelectedCustomer(customerID));
            customerInformationTable.setItems(DBCustomers.getAllCustomers());

        }
    }

    /**
     * This method allows for the user to switch to the Add Customer screen by selecting the 'Add' button below the customer information table
     * @param event brings the user to the Add Customer screen if the user selects the Add button under the customers table
     * @throws IOException exception is thrown is unable to switch screens 
     */
    @FXML
    public void addCustomerScreen (ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddCustomerScreen.fxml"));
        Scene addAppointmentScene = new Scene(root);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addAppointmentScene);
        window.setTitle("Scheduling Application: Add Customer");
        window.show();
    }

    /**
     * This method allows for the user to switch to the Modify Customer screen by first selecting a customer from the customer table and then clicking the 'Modify' button below the customer
     * information table. An alert is thrown if the user clicks the button without first selecting a customer. 
     * @param event switches to the Modify Customer screen if the user selects the 'Modify' button
     * @throws IOException exception thrown if unable to switch screens due to incorrect data parsing
     */
    @FXML
    public void modifyCustomerScreen (ActionEvent event) throws IOException
    {
        selectedCustomer = customerInformationTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyCustomerScreen.fxml"));
        Parent parent = loader.load();
        Scene modifyCustomerScreen = new Scene(parent);
        ModifyCustomerScreenController controller = loader.getController();
      
        
        if(selectedCustomer == null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Modifying Customer");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("You cannot modify a Customer without\n selecting one from the table first.");
            alert.showAndWait();
        }
        else
        {
            controller.sendCustomer(selectedCustomer);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(modifyCustomerScreen);
            window.setTitle("Scheduling Application: Modify Existing Customer");
            window.show();
        }
    }
    
    /**
     * This method switches to the Contact Appointments Report screen after the user selects the Contact Appointments button
     * @param event switches to the Contact Appointment screen after selecting the Contact Appointments button
     * @throws IOException exception thrown if unable to switch screens
     */
    @FXML
    public void contactAppointmentsScreen (ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ContactAppointmentsReportScreen.fxml"));
        Scene addAppointmentScene = new Scene(root);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addAppointmentScene);
        window.setTitle("Scheduling Application: View Contact Appointments");
        window.show();
    }

    /**
     * This method switches to the Customer Appointments Report screen after the user selects the Customer Appointments button
     * @param event switches to the Customer Appointments screen after selecting the Customer Appointments button
     * @throws IOException exception thrown if unable to switch screens
     */
    @FXML
    public void customerAppointmentsScreen (ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerAppointmentsReportScreen.fxml"));
        Scene addAppointmentScene = new Scene(root);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addAppointmentScene);
        window.setTitle("Scheduling Application: View Customer Appointments");
        window.show();
    }

    /**
     * This method switches to the Contact Schedule Report screen after the user selects the Contact Schedule button
     * @param event switches to the Contact Schedule Appointments screen
     * @throws IOException
     */
    @FXML
    public void contactScheduleScreen (ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ContactScheduleReportScreen.fxml"));
        Scene addAppointmentScene = new Scene(root);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addAppointmentScene);
        window.setTitle("Scheduling Application: View Contact Schedule");
        window.show();
    }

    /**
     * This method allows for the user to exit the application after selecting the 'Exit' button
     * @param event exits the application
     * @throws IOException exception thrown if unable to exit
     */
    @FXML
    public void exitApplication (ActionEvent event) throws IOException
    {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }

    /**
     * This method initializes the Main Screen and sets parameters appropriately
     * @param url absolute URL
     * @param rb absolute ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        /*Initialize the Appointments table*/
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));;
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));;
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));;
        appointmentContactColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentContactID"));;
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));;
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStartDate"));;
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEndDate"));;
        appointmentCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerID"));;
        
        allAppointments = DBAppointments.getAllAppointments();   
        appointmentInformationTable.setItems(allAppointments);
        
        /*Initialize the Customers table*/
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        customerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
        
        allCustomers = DBCustomers.getAllCustomers();
        customerInformationTable.setItems(allCustomers);
        
    }    
    
}
