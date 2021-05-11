/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.TimeZone;
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
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Appointments;
import utils.DBAppointments;
import utils.DBConnection;
import utils.DBContacts;
import utils.DBCustomers;
import utils.DBUsers;

/**
 * FXML Controller class
 * 
 * This class is the controller class for the Add Appointment Screen, and allows for creation of an appointment 
 * 
 * @author Rebekah Mars
 */
public class AddAppointmentScreenController implements Initializable 
{
    /*Initialize the variables used within the controller class*/
    @FXML private TextField appointmentIDEntry;
    @FXML private TextField appointmentTitleEntry;
    @FXML private TextField appointmentDescriptionEntry;
    @FXML private TextField appointmentLocationEntry;
    @FXML private ChoiceBox appointmentContactDrop;
    @FXML private TextField appointmentTypeEntry;
    @FXML private DatePicker appointmentDatePicker;
    @FXML private ChoiceBox startHour;
    @FXML private ChoiceBox startMinute;
    @FXML private ChoiceBox startSeconds;
    @FXML private ChoiceBox startAMPM;
    @FXML private ChoiceBox endHour;
    @FXML private ChoiceBox endMinute;
    @FXML private ChoiceBox endSeconds;
    @FXML private ChoiceBox endAMPM;
    @FXML private ChoiceBox customerIDDrop;
    @FXML private ChoiceBox userIDDrop;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    
    @FXML private final DateTimeFormatter time = DateTimeFormatter.ofPattern("h:mm:ss a"); 
    @FXML private ObservableList<String> daytime = FXCollections.observableArrayList(Arrays.asList("AM/PM", "AM", "PM"));
    
    
    /**
     * This method ensures that user input is valid for an appointment to be generated and saved to the database 
     * The entries must not be blank, or contain the null value, to be considered a valid entry 
     * The appointment times must be within business hours (8am-10pm), not be scheduled on a weekend, and also must not have customer scheduling conflicts 
     * A lambda expression is used to parse through the error messages from the ObservableList, map them to a new array with the Alert messages, and then print each message in order using the forEachOrdered() method
     * The lambda expression is efficient for this use as the expression allows for effective looping through appointment data and displaying information without the need of creating a separate function 
     * @param event     This method is called when the 'Confirm' button is pressed within the screen 
     * @throws IOException  error is thrown if there is an error when parsing through data 
     */
    @FXML
    public void saveNewAppointment (ActionEvent event) throws IOException 
    {
        int appointmentID = 0;
        ZoneId userZoneID = ZoneId.of(TimeZone.getDefault().getID());
        ObservableList<String> appointmentEntryValidateErrorMessage = FXCollections.observableArrayList();
        ObservableList<String> appointmentTimeValidateErrorMessage = FXCollections.observableArrayList();
        try
        {
            Connection connect = DBConnection.getConnection();
            String insertStatement = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,CURRENT_TIMESTAMP,?,CURRENT_TIMESTAMP,?,?,?,?)";
            PreparedStatement ps = connect.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
            
            String appointmentTitle = appointmentTitleEntry.getText();
            String appointmentDescription = appointmentDescriptionEntry.getText();
            String appointmentLocation = appointmentLocationEntry.getText();
            String appointmentType = appointmentTypeEntry.getText();       
            
            String appointmentStartHour = (String) startHour.getValue();
            String appointmentStartMinute = (String) startMinute.getValue();
            String appointmentStartSecond = (String) startSeconds.getValue();
            String appointmentStartDayNight = (String) startAMPM.getValue();
            
            String appointmentEndHour = (String) endHour.getValue().toString();
            String appointmentEndMinute = (String) endMinute.getValue().toString();
            String appointmentEndSecond = (String) endSeconds.getValue().toString();
            String appointmentEndDayNight = (String) endAMPM.getValue().toString();
                       
            LocalDate appointmentDate = appointmentDatePicker.getValue();
         
            String appointmentCreator = DBUsers.getCurrentUser().getUserName(); 
            String appointmentUpdator = DBUsers.getCurrentUser().getUserName(); 
            
          
            
            //int appointmentUserID = DBUsers.getCurrentUser().getUserID(); 
            
            String appointmentContactName = (String) appointmentContactDrop.getValue(); 
            int appointmentContactID = DBContacts.getContactID(appointmentContactName);
            
            int userID = Integer.parseInt(userIDDrop.getValue().toString());
           
            
            String customerID = (String)customerIDDrop.getValue();
                     
            appointmentEntryValidateErrorMessage = validAppointmentEntry(appointmentTitle, appointmentDescription, appointmentLocation, appointmentContactName, appointmentType, 
                    appointmentDate, appointmentStartHour, appointmentStartMinute, appointmentStartSecond, appointmentStartDayNight, appointmentEndHour,
                    appointmentEndMinute, appointmentEndSecond, appointmentEndDayNight, customerID, userID);
            
            
            if(!(appointmentEntryValidateErrorMessage.isEmpty()))
            {
                appointmentEntryValidateErrorMessage.stream().map((element) -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Saving Appointment");
                    alert.setHeaderText("Invalid Appointment Entry");
                    alert.setContentText(element);
                    return alert;
                }).forEachOrdered((alert) -> {
                    alert.showAndWait();
                });
            }
            else 
            {
                String appointmentStart = appointmentStartHour + ":" + appointmentStartMinute + ":" + appointmentStartSecond + " " + appointmentStartDayNight;
                String appointmentEnd = appointmentEndHour + ":" + appointmentEndMinute + ":" + appointmentEndSecond + " " + appointmentEndDayNight;
                
                LocalDateTime appointmentStartDate = LocalDateTime.of(appointmentDate, LocalTime.parse(appointmentStart, time));             
                LocalDateTime appointmentEndDate = LocalDateTime.of(appointmentDate, LocalTime.parse(appointmentEnd, time));  
                
                ZonedDateTime appointmentStartUTC = appointmentStartDate.atZone(userZoneID).withZoneSameInstant(ZoneOffset.UTC);
                ZonedDateTime appointmentEndUTC = appointmentEndDate.atZone(userZoneID).withZoneSameInstant(ZoneOffset.UTC);
                
                Timestamp appointmentStartDateTime = Timestamp.valueOf(appointmentStartUTC.toLocalDateTime());
                Timestamp appointmentEndDateTime = Timestamp.valueOf(appointmentEndUTC.toLocalDateTime());
                
                int appointmentCustomerID = Integer.parseInt(customerIDDrop.getValue().toString());
                int appointmentUserID = Integer.parseInt(userIDDrop.getValue().toString());
             
                appointmentTimeValidateErrorMessage = validAppointmentTimes(appointmentCustomerID, appointmentStartDate, appointmentEndDate);
                
                if(!(appointmentTimeValidateErrorMessage.isEmpty()))
                {
                    appointmentTimeValidateErrorMessage.stream().map((element) -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Saving Appointment");
                    alert.setHeaderText("Invalid Appointment Entry");
                    alert.setContentText(element);
                    return alert;
                }).forEachOrdered((alert) -> {
                    alert.showAndWait();
                });
                }
                else
                {
                    ps.setString(1, appointmentTitle);
                    ps.setString(2, appointmentDescription);
                    ps.setString(3, appointmentLocation);
                    ps.setString(4, appointmentType);
            
                    ps.setString(5, appointmentStartDateTime.toString());
                    ps.setString(6, appointmentEndDateTime.toString());

                    ps.setString(7, appointmentCreator);
                    ps.setString(8, appointmentUpdator);
                    ps.setInt(9, appointmentCustomerID);
                    ps.setInt(10, appointmentUserID);
                    ps.setInt(11, appointmentContactID);
                    ResultSet rs = ps.getGeneratedKeys();
                
                    while(rs.next())
                    {
                        appointmentID = rs.getInt(1);
                    }
                
                    ps.execute();
                
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Add Appointment Successful");
                    alert.setHeaderText("New Appointment Added");
                    alert.setContentText("You have successfully created an appointment.");
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
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        
    }

    /**
     * This method returns an error message if invalid input is entered based on the customer ID, and appointment Start/End date-time 
     * The method uses the customer ID selected from the drop-down by the user, the date selected by the user, and the start/end times selected by the user 
     * If the appointment times are not within business hours, overlap with another customer appointment, or are not in chronological order, an error message will generate 
     * @param customerID the specific ID of the customer, derived from the database 
     * @param start the starting time of the appointment, derived from user input and converted to a LocalDateTime Object 
     * @param end the ending time of the appointment, derived from user input and converted to a LocalDateTime Object 
     * @return the specific error message for each invalid user input 
     */
    @FXML 
    public ObservableList<String> validAppointmentTimes(int customerID, LocalDateTime start, LocalDateTime end)
    {
        ObservableList<String> errorMessage = FXCollections.observableArrayList();
        ObservableList<Appointments> customerAppointments = DBAppointments.getAllAppointmentsFromCustomerWithStartandEndTimes(customerID, start, end);
               
        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();
        
        LocalTime time8AM = LocalTime.MIDNIGHT.plusHours(8);
        LocalTime time10PM = LocalTime.NOON.plusHours(10); 
        
        int startDay = start.getDayOfWeek().getValue(); //Saturday is 6, Sunday is 7
        int endDay = end.getDayOfWeek().getValue(); //Saturday is 6, Sunday is 7
        
        if(startTime.isBefore(time8AM) || startTime.isAfter(time10PM))
        {
            errorMessage.add("Please select an appointment start time \nwithin business hours of 8am-10pm.");
        }
        if(endTime.isBefore(time8AM) || endTime.isAfter(time10PM))
        {
            errorMessage.add("Please select an appointment end time \nwithin business hours of 8am-10pm.");
        }
        if(startTime.equals(endTime))
        {
            errorMessage.add("The appointment start time cannot be at\n the same time as the appointment end time.");
        }
        if(startTime.isAfter(endTime) || endTime.isBefore(startTime))
        {
            errorMessage.add("The appointment start and end times must be\n in chronological order.");
        }
        if(startDay == 6 || startDay == 7 || endDay == 6 || endDay == 7)
        {
            errorMessage.add("The appointment start and end dates must not\n be scheduled on the weekend.");
        }
        if(!(customerAppointments.isEmpty()))
        {
           errorMessage.add("Please select an appointment time that \ndoes not conflict with another appointment");         
        }
        
        return errorMessage;
    }

    /**
     * This method returns an error message if the user input for the appointment entries (title, description, location, contact name, type, 
     * date, start time, end time, and contact ID) are invalid 
     * This method specifically checks if any of the user inputs are either empty, have the null value, or default value 
     * @param title the appointment title 
     * @param description the appointment description 
     * @param location the appointment location 
     * @param contactName the appointment contact name selected 
     * @param type the appointment type 
     * @param appointmentDate the appointment date selected 
     * @param hourStart the appointment starting hour selected 
     * @param minuteStart the appointment starting minute(s) selected 
     * @param secondsStart the appointment starting second(s) selected 
     * @param dayStart the appointment start time of day selected 
     * @param hourEnd the appointment ending hour selected 
     * @param minuteEnd the appointment ending minute(s) selected 
     * @param secondsEnd the appointment ending second(s) selected 
     * @param dayEnd the appointment end time of day selected 
     * @param customer the appointment customer ID selected 
     * @param userID the user ID
     * @return an error message if the user input is invalid 
     */
    @FXML
    public ObservableList<String> validAppointmentEntry (String title, String description, String location, String contactName, String type, 
            LocalDate appointmentDate, String hourStart, String minuteStart, String secondsStart, String dayStart, String hourEnd, String minuteEnd,
            String secondsEnd, String dayEnd, String customer, int userID)
    {
        ObservableList<String> errorMessage = FXCollections.observableArrayList();
        
        if(title.isEmpty())
        {
           errorMessage.add("Please enter an appointment title.");
        }
        if(description.isEmpty())
        {
            errorMessage.add("Please enter an appointment description.");
        }
        if(location.isEmpty())
        {
            errorMessage.add("Please enter an appointment location.");
        }
        if(contactName == null)
        {
           errorMessage.add("Please select an appointment contact."); 
        }
        if(type.isEmpty())
        {
            errorMessage.add("Please enter an appointment type.");
        }
        if(appointmentDate == null)
        {
            errorMessage.add("Please select an appointment date.");
        }
        if(hourStart == "HOUR")
        {
            errorMessage.add("Please select the hours for an appointment start time.");
        }
        if(minuteStart == "MINUTE")
        {
            errorMessage.add("Please select the minutes for an appointment start time.");
        }
        if(secondsStart == "SECOND")
        {
            errorMessage.add("Please select the seconds for an appointment start time.");
        }
        if(dayStart == "AM/PM")
        {
            errorMessage.add("Please select the time of day (AM or PM) \nfor an appointment start time.");
        }
         if(hourEnd == "HOUR")
        {
            errorMessage.add("Please select the hours for an appointment end time.");
        }
        if(minuteEnd == "MINUTE")
        {
            errorMessage.add("Please select the minutes for an appointment end time.");
        }
        if(secondsEnd == "SECOND")
        {
            errorMessage.add("Please select the seconds for an appointment end time.");
        }
        if(dayEnd == "AM/PM")
        {
            errorMessage.add("Please select the time of day (AM or PM) \nfor an appointment end time.");
        }
        if(customer == null)
        {
            errorMessage.add("Please select an appointment customer ID");
        }
        if(userID == 0)
        {
            errorMessage.add("Please select an appointment user ID");
        }
        return errorMessage;
    }

    /**
     * This method allows for the user to return to the Main Menu after selecting the 'Cancel' button 
     * This method exits the Add Appointment screen, and no information entered is saved 
     * @param event if the exit button is pressed, the user returns to the Main Menu screen 
     * @throws IOException if there is an issue switching screens due to data parsing 
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
     * This method generates a list of hours from 01-12 
     * @return the hours from 01-12 
     */
    @FXML
    public ObservableList<String> getHours()
    {
        ObservableList<String> hours = FXCollections.observableArrayList();
        hours.add("HOUR");
        DecimalFormat twoDigits = new DecimalFormat("00");
        for(int i = 1; i <=12; i++)
        {
          hours.add(twoDigits.format(i));
        }
        return hours;
    }

    /**
     *This method generates a list of minutes from 00-60 
     * @return the minutes from 00-60 
     */
    @FXML
     public ObservableList<String> getMinutes()
    {
        ObservableList<String> minutes = FXCollections.observableArrayList();
        minutes.add("MINUTE");
        DecimalFormat twoDigits = new DecimalFormat("00");
        for(int i = 0; i <=60; i++)
        {
          minutes.add(twoDigits.format(i));
        }
        return minutes;
    }

    /**
     * This method generates a list of seconds from 00-60 
     * @return the seconds from 00-60 
     */
    @FXML
     public ObservableList<String> getSeconds()
    {
        ObservableList<String> seconds = FXCollections.observableArrayList();
        seconds.add("SECOND");
        DecimalFormat twoDigits = new DecimalFormat("00");
        for(int i = 0; i <=60; i++)
        {
          seconds.add(twoDigits.format(i));
        }
        return seconds;
    }

    /**
     * This method initializes the Add Appointment Screen and sets parameters accordingly 
     * @param url an absolute URL 
     * @param rb an absolute ResourceBundle 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        appointmentIDEntry.setDisable(true);
        appointmentContactDrop.setItems(DBContacts.getContactNames());
        customerIDDrop.setItems(DBCustomers.getCustomerIDs());
        userIDDrop.setItems(DBUsers.getAllUserIDs());
        
        ObservableList<String> hours = getHours();
        ObservableList<String> minutes = getMinutes();
        ObservableList<String> seconds = getSeconds();
        startHour.setItems(hours);  
        startHour.setValue("HOUR");
        startMinute.setItems(minutes); 
        startMinute.setValue("MINUTE");
        startSeconds.setItems(seconds);
        startSeconds.setValue("SECOND");
        startAMPM.setItems(daytime);
        startAMPM.setValue("AM/PM");
        
        endHour.setItems(hours);  
        endHour.setValue("HOUR");
        endMinute.setItems(minutes); 
        endMinute.setValue("MINUTE");
        endSeconds.setItems(seconds);
        endSeconds.setValue("SECOND");
        endAMPM.setItems(daytime);
        endAMPM.setValue("AM/PM");
    }    
    
}
