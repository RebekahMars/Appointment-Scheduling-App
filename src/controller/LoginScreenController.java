/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointments;
import utils.DBAppointments;
import utils.DBUsers;

/**
 * FXML Controller Class
 * This class is the controller for the Login Screen
 * @author rebek
 */
public class LoginScreenController implements Initializable 
{
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private TextField zoneIDTextField;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label zoneIDLabel;
    @FXML private Label loginScreenTitleLabel;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    @FXML private String errorTitleText;
    @FXML private String errorHeaderText;
    @FXML private String errorContentText;
    @FXML private String loginTitleText;
    @FXML private String loginHeaderText;
    @FXML private String loginContentText;
            
    /**
     * This method allows for the user to exit the application entirely by selecting the 'Exit' button
     * @param event when the user selects 'Exit', the application closes
     * @throws IOException throws exception if unable to exit the application properly due to incorrect data parsing
     */
    @FXML
    public void exitApplication (ActionEvent event) throws IOException
    {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }

    /**
     * This method allows for the user to log into the application after a correct username/password is entered
     * The Log-in screen displays the ZoneID of the user in a disabled TextField, and allows for the user to enter a username and password combination
     * If the username and password combination match what is stored in the database, the user is able to access the application.
     * Appointments are also checked to determine whether any are occurring within 15 minutes. If so, that information is displayed. 
     * @param event when user selects 'CONFIRM', the method occurs
     * @throws IOException throws exception if data is parsed incorrectly 
     */
    @FXML
    public void logintoApplication (ActionEvent event) throws IOException
    {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        ObservableList <Appointments> appointmentin15 = DBAppointments.getAppointmentsin15Minutes();
        
      
        boolean validUser = DBUsers.applicationLoginTest(username, password);
        
        
        if(validUser)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(loginTitleText);
            alert.setHeaderText(loginHeaderText);
            alert.setContentText(loginContentText + username);
            alert.showAndWait();
            
            if(appointmentin15.isEmpty())
            {
                Alert noAppointmentAlert = new Alert(Alert.AlertType.INFORMATION);
                noAppointmentAlert.setTitle("Appointment in 15 Minutes");
                noAppointmentAlert.setHeaderText("No Appointments to Show");
                noAppointmentAlert.setContentText("There are no appointments scheduled in the next 15 minutes.");
                noAppointmentAlert.showAndWait();
            }
            else
            {
                int appointmentID = appointmentin15.get(0).getAppointmentID();
                String appointmentDateTime = appointmentin15.get(0).getAppointmentStartDate();
                
                Alert appointmentAlert = new Alert(Alert.AlertType.INFORMATION);
                appointmentAlert.setTitle("Appointment Scheduled in 15 Minutes");
                appointmentAlert.setHeaderText("Showing Appointments Scheduled in 15 Minutes");
                appointmentAlert.setContentText("Appointment ID: " + appointmentID + "\nDate & Time: " + appointmentDateTime); 
                appointmentAlert.showAndWait();   
            }
            
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreenJFX.fxml"));
            Scene mainMenu = new Scene(root);      
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(mainMenu);
            window.setTitle("Scheduling Application: Main Menu");
            window.show();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(errorTitleText);
            alert.setHeaderText(errorHeaderText);
            alert.setContentText(errorContentText);
            alert.showAndWait();
        }
        
    
    }

    /**
     * This method sets the user ZoneID
     */
    @FXML
    public void setUserZoneID()
    {
        ZoneId userZoneID = ZoneId.of(TimeZone.getDefault().getID());
        zoneIDTextField.setText(userZoneID.toString());
        
    }

    /**
     * This method determines if the user language is English or French based on Locale and sets the text to the appropriate language
     */
    @FXML
    public void determineUserLanguage()
    {
        Locale currentLocale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("languages/languages", currentLocale);
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        zoneIDLabel.setText(rb.getString("zone"));
        loginScreenTitleLabel.setText(rb.getString("screen"));
        confirmButton.setText(rb.getString("confirm"));
        cancelButton.setText(rb.getString("cancel"));
        errorTitleText = rb.getString("errorTitle");
        errorHeaderText = rb.getString("errorHeader");
        errorContentText = rb.getString("errorContent");
        loginTitleText = rb.getString("loginTitle");
        loginHeaderText = rb.getString("loginHeader");
        loginContentText = rb.getString("loginContent");
        
    }

    /**
     * This method initializes the Login Screen and sets parameters accordingly
     * @param url absolute URL
     * @param rb absolute ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        zoneIDTextField.setDisable(true);
        determineUserLanguage();
        setUserZoneID();
    }    
    
}
