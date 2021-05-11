/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointments;
import utils.DBAppointments;
import utils.DBConnection;
import utils.DBContacts;
import utils.DBCustomers;
import utils.DBUsers;

/**
 * FXML Controller class
 * This class is the controller for the Modify Appointment Screen, allowing for the user to modify a selected appointment
 * Information of the appointment is sent from the Main Screen/database and inserted into modifiable fields for the user to change
 * The entries are then checked to determine validity. If valid, the appointment is updated in the database and the user is taken back to the Main Screen,
 * where the modified appointment is displayed in the appointments table
 * @author rebek
 */

public class ModifyAppointmentScreenController implements Initializable 
{
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
    
    @FXML private final DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm:ss a"); //12 hour format
    @FXML private final DateTimeFormatter hours = DateTimeFormatter.ofPattern("hh");
    @FXML private final DateTimeFormatter minutes = DateTimeFormatter.ofPattern("mm");
    @FXML private final DateTimeFormatter seconds = DateTimeFormatter.ofPattern("ss");
    @FXML private final DateTimeFormatter AMPM = DateTimeFormatter.ofPattern("a");
    @FXML private final DateTimeFormatter date = DateTimeFormatter.ofPattern("mm/dd/yyyy");
    @FXML private final DateTimeFormatter datetime = DateTimeFormatter.ofPattern("M/d/u hh:mm:ss a");
    @FXML private ObservableList<String> daytime = FXCollections.observableArrayList(Arrays.asList("AM/PM", "AM", "PM"));
    
    /**
     * This method allows for the user to exit to the Main Menu screen after selecting the 'Exit' button
     * @param event exits to the Main Menu
     * @throws IOException exception thrown if unable to switch screens
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
     * This method retrieves the information of the selected appointment from the Main Menu and puts the information into its respective fields for the user to update/alter
     * 
     * @param selectedAppointment the selected appointment from the Main Menu that the user chose from the appointments table
     */
    @FXML
    public void sendAppointment(Appointments selectedAppointment)
    {      
        
        String appointmentStartTime = selectedAppointment.getAppointmentStartDate();
        String appointmentEndTime = selectedAppointment.getAppointmentEndDate();
       
        LocalDate appointmentStart = LocalDate.parse(appointmentStartTime, datetime);
       
        LocalDateTime appointmentTimeStart = LocalDateTime.parse(appointmentStartTime, datetime);
        
        
        
      
        String newStartHour = appointmentTimeStart.format(hours);
        String newStartMinute = appointmentTimeStart.format(minutes);
        String newStartSeconds = appointmentTimeStart.format(seconds);
        String newStartAMPM = appointmentTimeStart.format(AMPM);
      
        LocalDateTime appointmentTimeEnd = LocalDateTime.parse(appointmentEndTime, datetime);
        
        String newEndHour = appointmentTimeEnd.format(hours);
        String newEndMinute = appointmentTimeEnd.format(minutes);
        String newEndSeconds = appointmentTimeEnd.format(seconds);
        String newEndAMPM = appointmentTimeEnd.format(AMPM);
        
       
        
        appointmentIDEntry.setText(String.valueOf(selectedAppointment.getAppointmentID()));
        appointmentTitleEntry.setText(String.valueOf(selectedAppointment.getAppointmentTitle()));
        appointmentDescriptionEntry.setText(String.valueOf(selectedAppointment.getAppointmentDescription()));
        appointmentLocationEntry.setText(String.valueOf(selectedAppointment.getAppointmentLocation()));
        int appointmentContactID = selectedAppointment.getAppointmentContactID();
        String appointmentContactName = DBContacts.getContactName(appointmentContactID);
        appointmentContactDrop.setValue(appointmentContactName);
        userIDDrop.setValue(selectedAppointment.getUserID());
        appointmentTypeEntry.setText(String.valueOf(selectedAppointment.getAppointmentType()));
        appointmentDatePicker.setValue(appointmentStart); 
        startHour.setValue(newStartHour);
        startMinute.setValue(newStartMinute);
        startSeconds.setValue(newStartSeconds);
        startAMPM.setValue(newStartAMPM);
        endHour.setValue(newEndHour);
        endMinute.setValue(newEndMinute);
        endSeconds.setValue(newEndSeconds);
        endAMPM.setValue(newEndAMPM);
        customerIDDrop.setValue(String.valueOf(selectedAppointment.getAppointmentCustomerID()));     
    }

    /**
     * This method is activated when the user selects the 'Confirm' button the Modify Appointment Screen
     * It gets the data values in each field and updates the appropriate appointment with the new information
     * Like the Add Appointment, the Modify Appointment also checks to make sure the data entries are valid
     * If valid, the appointment is updated and the user is sent back to the Main Menu where the updated appointment can be viewed in the table
     * @param event method is called when the Confirm button is selected by the user
     * @throws IOException exception thrown if unable to modify appointment and switch screens due to incorrect data parsing
     */
    @FXML
    public void modifyAppointment (ActionEvent event) throws IOException
    {   
        ObservableList<String> appointmentEntryValidateErrorMessage = FXCollections.observableArrayList();
        ObservableList<String> appointmentTimeValidateErrorMessage = FXCollections.observableArrayList();
        ZoneId userZoneID = ZoneId.of(TimeZone.getDefault().getID());
        String appointmentID = appointmentIDEntry.getText();
       
        try
        {
            Connection connect = DBConnection.getConnection();
            String modifyStatement = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Last_Update=CURRENT_TIMESTAMP, Last_Updated_By=?, Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID = '" + appointmentID + "'";
            PreparedStatement ps = connect.prepareStatement(modifyStatement);
            
            String appointmentTitle = appointmentTitleEntry.getText();
            String appointmentDescription = appointmentDescriptionEntry.getText();
            String appointmentLocation = appointmentLocationEntry.getText();
            String appointmentType = appointmentTypeEntry.getText();       
            
            String appointmentStartHour = startHour.getValue().toString();
            String appointmentStartMinute = startMinute.getValue().toString();
            String appointmentStartSecond = startSeconds.getValue().toString();
            String appointmentStartDayNight = startAMPM.getValue().toString();
            
            String appointmentEndHour = endHour.getValue().toString();
            String appointmentEndMinute = endMinute.getValue().toString();
            String appointmentEndSecond = endSeconds.getValue().toString();
            String appointmentEndDayNight = endAMPM.getValue().toString();
            
            LocalDate appointmentDate = appointmentDatePicker.getValue();
            
            String appointmentUpdator = DBUsers.getCurrentUser().getUserName(); 
            
            
            String appointmentContactName = (String) appointmentContactDrop.getValue();
            int appointmentContactID = DBContacts.getContactID(appointmentContactName);
            
            String customerID = (String) customerIDDrop.getValue();
            int user = Integer.parseInt(userIDDrop.getValue().toString());
            
            appointmentEntryValidateErrorMessage = validAppointmentEntry(appointmentTitle, appointmentDescription, appointmentLocation, appointmentContactName, appointmentType, 
                    appointmentDate, appointmentStartHour, appointmentStartMinute, appointmentStartSecond, appointmentStartDayNight, appointmentEndHour,
                    appointmentEndMinute, appointmentEndSecond, appointmentEndDayNight, customerID, user);
            
            
            if(!(appointmentEntryValidateErrorMessage.isEmpty()))
            {
                appointmentEntryValidateErrorMessage.stream().map((element) -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Modifying Appointment");
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
            
                   ps.setString(7, appointmentUpdator);
                   ps.setInt(8, appointmentCustomerID);
                   ps.setInt(9, appointmentUserID);
                   ps.setInt(10, appointmentContactID);
            
                   ps.executeUpdate();
                }   
                   if(ps.getUpdateCount() > 0)
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Modify Appointment Successful");
                        alert.setHeaderText("Appointment Modified");
                        alert.setContentText("You have successfully modified an appointment.");
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
     * This method validates the modified appointment times by determining if the modified times are within business hours, not on the weekends, and in chronological order
     * If not valid, an error message is generated based on that specific error
     * @param customerID appointment customer ID
     * @param start appointment start time in localdatetime format
     * @param end appointment end time in localdatetime format
     * @return the error message for an invalid data field (if any)
     */
    @FXML public ObservableList<String> validAppointmentTimes(int customerID, LocalDateTime start, LocalDateTime end)
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
        if(startDay == 6 || startDay == 7 || endDay == 6 || endDay == 7)
        {
            errorMessage.add("The appointment start and end dates must not\n be scheduled on the weekend.");
        }
        if(startTime.equals(endTime))
        {
            errorMessage.add("The appointment start time cannot be at\n the same time as the appointment end time.");
        }
        if(startTime.isAfter(endTime) || endTime.isBefore(startTime))
        {
            errorMessage.add("The appointment start and end times must be\n in chronological order.");
        }
        if(!(customerAppointments.isEmpty()))
        {
           errorMessage.add("Please select an appointment time that \ndoes not conflict with another appointment");         
        }
        
        return errorMessage;
    }

    /**
     * This method validates the appointment fields by determining if each field contains a value
     * If the field is empty or has the null value, an error message is created based on the missing field
     * @param title appointment Title
     * @param description appointment description
     * @param location appointment location
     * @param contactName appointment contact name
     * @param type appointment type
     * @param appointmentDate appointment date
     * @param hourStart appointment starting hour
     * @param minuteStart appointment starting minute(s)
     * @param secondsStart appointment starting second(s)
     * @param dayStart appointment starting time of day
     * @param hourEnd appointment ending hour
     * @param minuteEnd appointment ending minutes
     * @param secondsEnd appointment ending seconds
     * @param dayEnd appointment ending time of day
     * @param customer appointment customer id
     * @param user user id
     * @return the error message for the invalid data field (if any)
     */
    @FXML
    public ObservableList<String> validAppointmentEntry (String title, String description, String location, String contactName, String type, 
            LocalDate appointmentDate, String hourStart, String minuteStart, String secondsStart, String dayStart, String hourEnd, String minuteEnd,
            String secondsEnd, String dayEnd, String customer, int user)
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
        if(user == 0)
        {
            errorMessage.add("Please select an appointment user ID");
        }
        return errorMessage;
    }

    /**
     * This method gets all hours from 01-12
     * @return hours 01-12
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
     * This method gets all minutes from 01-60
     * @return minutes 01-60
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
     * This method gets all seconds from 01-60
     * @return seconds 01-60
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
     * This method initializes the Modify Appointment Screen and sets parameters accordingly
     * @param url absolute URL
     * @param rb absolute ResourceBundle
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
