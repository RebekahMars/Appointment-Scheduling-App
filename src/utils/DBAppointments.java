/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 * Appointments utility class
 * @author rebek
 */
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Appointments utility class
 * @author rebek
 */

public class DBAppointments 
{
    private Appointments appointments;
    
    /**
     *
     * @return appointments
     */
    public Appointments getAppointments()
    {
        return appointments;
    }
    
    /**
     * This method gets all appointments from database
     * @return appointments
     */
    public static ObservableList<Appointments> getAllAppointments()
    {
        ZoneId userZoneID = ZoneId.of(TimeZone.getDefault().getID());
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");   
        
        ObservableList<Appointments> allAppointmentsList = FXCollections.observableArrayList();
        try
        {
            Connection connect = DBConnection.getConnection();
            DBQuery.setStatement(connect);
            Statement statement = DBQuery.getStatement();
            
            String selectStatement = "SELECT * FROM appointments";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            
            while(rs.next())
            {
               
               int appointmentID = rs.getInt("Appointment_ID");
               String title = rs.getString("Title");
               String description = rs.getString("Description");
               String location = rs.getString("Location");
               int contactID = rs.getInt("Contact_ID");
               String type = rs.getString("Type");
               
               Timestamp startTime = rs.getTimestamp("Start");
               ZonedDateTime zoneStartTime = startTime.toInstant().atZone(userZoneID);
               LocalDateTime userStartTime = zoneStartTime.toLocalDateTime();    
               String appointmentStartDate = datetime.format(userStartTime);
               
               Timestamp endTime = rs.getTimestamp("End");
               ZonedDateTime zoneEndTime = endTime.toInstant().atZone(userZoneID);
               LocalDateTime userEndTime = zoneEndTime.toLocalDateTime();  
               String appointmentEndDate = datetime.format(userEndTime);
                         
               int customerID = rs.getInt("Customer_ID");
               int userID = rs.getInt("User_ID");
               
               allAppointmentsList.add(new Appointments(appointmentID, title, description, location, contactID, type, appointmentStartDate, appointmentEndDate, customerID, userID));
                
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return allAppointmentsList;
    }

    /**
     * This method returns all weekly appointments from database
     * @return weekly appointments
     */
    public static ObservableList<Appointments> getWeeklyAppointments()
    {
        ZoneId userZoneID = ZoneId.of(TimeZone.getDefault().getID());
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"); 
        
        ObservableList<Appointments> weeklyAppointments = FXCollections.observableArrayList();
        
        try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM appointments WHERE Start BETWEEN CURRENT_DATE AND ADDDATE(CURRENT_DATE, INTERVAL 1 WEEK)";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               int appointmentID = rs.getInt("Appointment_ID");
               String title = rs.getString("Title");
               String description = rs.getString("Description");
               String location = rs.getString("Location");
               int contactID = rs.getInt("Contact_ID");
               String type = rs.getString("Type");
               
               
               Timestamp startTime = rs.getTimestamp("Start");
               ZonedDateTime zoneStartTime = startTime.toInstant().atZone(userZoneID);
               LocalDateTime userStartTime = zoneStartTime.toLocalDateTime();            
               String appointmentStartDate = datetime.format(userStartTime);
               
               Timestamp endTime = rs.getTimestamp("End");
               ZonedDateTime zoneEndTime = endTime.toInstant().atZone(userZoneID);
               LocalDateTime userEndTime = zoneEndTime.toLocalDateTime();           
               String appointmentEndDate = datetime.format(userEndTime);
               
               
               int customerID = rs.getInt("Customer_ID");
               int userID = rs.getInt("User_ID");
               
               weeklyAppointments.add(new Appointments(appointmentID, title, description, location, contactID, type, appointmentStartDate, appointmentEndDate, customerID, userID));    
               
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        if(weeklyAppointments.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Unavailable");
            alert.setHeaderText("No Weekly Appointments to Show");
            alert.setContentText("There are no appointments this upcoming week.");
            alert.showAndWait();
        }
        return weeklyAppointments;
    }

    /**
     * This method returns all monthly appointments from database
     * @return monthly appointments
     */
    public static ObservableList<Appointments> getMonthlyAppointments()
    {
        ZoneId userZoneID = ZoneId.of(TimeZone.getDefault().getID());
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");   
       
        ObservableList<Appointments> monthlyAppointments = FXCollections.observableArrayList();
        
        try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM appointments WHERE Start BETWEEN CURRENT_DATE AND ADDDATE(CURRENT_DATE, INTERVAL 1 MONTH)";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               int appointmentID = rs.getInt("Appointment_ID");
               String title = rs.getString("Title");
               String description = rs.getString("Description");
               String location = rs.getString("Location");
               int contactID = rs.getInt("Contact_ID");
               String type = rs.getString("Type");
               
               
               Timestamp startTime = rs.getTimestamp("Start");
               ZonedDateTime zoneStartTime = startTime.toInstant().atZone(userZoneID);
               LocalDateTime userStartTime = zoneStartTime.toLocalDateTime(); 
               String appointmentStartDate = datetime.format(userStartTime);
               
               
               
               Timestamp endTime = rs.getTimestamp("End");
               ZonedDateTime zoneEndTime = endTime.toInstant().atZone(userZoneID);
               LocalDateTime userEndTime = zoneEndTime.toLocalDateTime();           
               String appointmentEndDate = datetime.format(userEndTime);
               int customerID = rs.getInt("Customer_ID");
               int userID = rs.getInt("User_ID");
               
               monthlyAppointments.add(new Appointments(appointmentID, title, description, location, contactID, type, appointmentStartDate, appointmentEndDate, customerID, userID));    
               
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        if(monthlyAppointments.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Unavailable");
            alert.setHeaderText("No Monthly Appointments to Show");
            alert.setContentText("There are no appointments scheduled in the upcoming month.");
            alert.showAndWait();
        }
        return monthlyAppointments;
    }

    /**
     * This appointment deletes selected appointment from database
     * @param appointmentID ID of selected appointment
     * @param appointmentType type of selected appointment
     */
    @FXML
    public static void deleteSelectedAppointment(int appointmentID, String appointmentType)
    {
        try
        {
            Connection connect = DBConnection.getConnection();  
            String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID = '" + appointmentID + "'";
            PreparedStatement ps = connect.prepareStatement(deleteStatement);
            //ps.setInt(1, appointmentID);
            ps.execute();
            
            if(ps.getUpdateCount() > 0)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Delete Appointment Successful");
                alert.setHeaderText("Appointment Deleted");
                alert.setContentText("You have successfully deleted the appointment with\n Appointment ID: " + appointmentID + " and Appointment Title: " + appointmentType);
                alert.showAndWait();
            }
      
        }
        catch(SQLException e)
        {
            System.out.println("SQLException " + e.getMessage());
        }
    }
   
    /**
     * This method returns the appointments within 15 minutes of log-in
     * @return appointments in 15 minutes from log-in
     */
    @FXML
    public static ObservableList<Appointments> getAppointmentsin15Minutes()
    {
        ZoneId userZoneID = ZoneId.of(TimeZone.getDefault().getID());
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");   
       
        ObservableList<Appointments> minute15Appointments = FXCollections.observableArrayList();
        
        
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime currentDateTimePlus15 = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(15);
        System.out.println(currentDateTimePlus15);
        
        try
        {
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM appointments WHERE Start BETWEEN '" + currentDateTime + "' AND '" + currentDateTimePlus15 + "'"; //BETWEEN CURRENT_TIMESTAMP AND ADDDATE(CURRENT_DATE, INTERVAL 15 MINUTE)"; //BETWEEN CURRENT_TIME AND ADDDATE(CURRENT_TIME, INTERVAL 15 MINUTE)";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            while(rs.next())
            {
               int appointmentID = rs.getInt("Appointment_ID");
               String title = rs.getString("Title");
               String description = rs.getString("Description");
               String location = rs.getString("Location");
               int contactID = rs.getInt("Contact_ID");
               String type = rs.getString("Type");
               
               
               Timestamp startTime = rs.getTimestamp("Start");
               ZonedDateTime zoneStartTime = startTime.toInstant().atZone(userZoneID);
               LocalDateTime userStartTime = zoneStartTime.toLocalDateTime();            
               String appointmentStartDate = datetime.format(userStartTime);  
               
               
               
               Timestamp endTime = rs.getTimestamp("End");
               ZonedDateTime zoneEndTime = endTime.toInstant().atZone(userZoneID);
               LocalDateTime userEndTime = zoneEndTime.toLocalDateTime();           
               String appointmentEndDate = datetime.format(userEndTime);
               
               
               int customerID = rs.getInt("Customer_ID");
               int userID = rs.getInt("User_ID");
              
               minute15Appointments.add(new Appointments(appointmentID, title, description, location, contactID, type, appointmentStartDate, appointmentEndDate, customerID, userID));    
  
               
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
      
        return minute15Appointments;
    }

    /**
     * This method returns all appointments from a specific customer id
     * @param customerID customer id for appointment search
     * @return appointments of customer
     */
    @FXML
    public static ObservableList<Appointments> customerAppointmentChecker(int customerID)
    {
        ZoneId userZoneID = ZoneId.of(TimeZone.getDefault().getID());
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");   
        
        ObservableList<Appointments> customerAppointmentsList = FXCollections.observableArrayList();
        try
        {
            Connection connect = DBConnection.getConnection();
            DBQuery.setStatement(connect);
            Statement statement = DBQuery.getStatement();
            
            String selectStatement = "SELECT * FROM appointments WHERE Customer_ID = '" + customerID + "'";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            
            while(rs.next())
            {
               
               int appointmentID = rs.getInt("Appointment_ID");
               String title = rs.getString("Title");
               String description = rs.getString("Description");
               String location = rs.getString("Location");
               int contactID = rs.getInt("Contact_ID");
               String type = rs.getString("Type");
               
               Timestamp startTime = rs.getTimestamp("Start");
               ZonedDateTime zoneStartTime = startTime.toInstant().atZone(userZoneID);
               LocalDateTime userStartTime = zoneStartTime.toLocalDateTime();            
               String appointmentStartDate = datetime.format(userStartTime);
               
               Timestamp endTime = rs.getTimestamp("End");
               ZonedDateTime zoneEndTime = endTime.toInstant().atZone(userZoneID);
               LocalDateTime userEndTime = zoneEndTime.toLocalDateTime();           
               String appointmentEndDate = datetime.format(userEndTime);
               int userID = rs.getInt("User_ID");
               
               customerAppointmentsList.add(new Appointments(appointmentID, title, description, location, contactID, type, appointmentStartDate, appointmentEndDate, customerID, userID));
                
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return customerAppointmentsList;
    }

    /**
     * This method returns all appointments from customer within a specific timeframe to check for apppointment conflicts
     * @param customerID id of customer
     * @param start appointment start time in localdatetime format
     * @param end appointment end time in localdatetime format
     * @return appointments within a certain time frame
     */
    public static ObservableList<Appointments> getAllAppointmentsFromCustomerWithStartandEndTimes(int customerID, LocalDateTime start, LocalDateTime end)
    {
        ZoneId userZoneID = ZoneId.of(TimeZone.getDefault().getID());
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");   
        
        ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();
        try
        {
            Connection connect = DBConnection.getConnection();
            DBQuery.setStatement(connect);
            Statement statement = DBQuery.getStatement();
            
            String selectStatement = "SELECT * FROM appointments WHERE Customer_ID = '" + customerID + "'";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            
            while(rs.next())
            {              
               int appointmentID = rs.getInt("Appointment_ID");
               String title = rs.getString("Title");
               String description = rs.getString("Description");
               String location = rs.getString("Location");
               int contactID = rs.getInt("Contact_ID");
               String type = rs.getString("Type");
               int userID = rs.getInt("User_ID");
               
               Timestamp startTime = rs.getTimestamp("Start");
               ZonedDateTime zoneStartTime = startTime.toInstant().atZone(userZoneID);
               LocalDateTime userStartTime = zoneStartTime.toLocalDateTime();            
               String appointmentStartDate = datetime.format(userStartTime);
               
               Timestamp endTime = rs.getTimestamp("End");
               ZonedDateTime zoneEndTime = endTime.toInstant().atZone(userZoneID);
               LocalDateTime userEndTime = zoneEndTime.toLocalDateTime();           
               String appointmentEndDate = datetime.format(userEndTime);

               if((start.isEqual(userStartTime) || end.isEqual(userEndTime)) || (start.isAfter(userStartTime) && start.isBefore(userEndTime)) || (end.isAfter(userStartTime) && end.isBefore(userEndTime)))
               {
                   appointmentsList.add(new Appointments(appointmentID, title, description, location, contactID, type, appointmentStartDate, appointmentEndDate, customerID, userID));
               }
               
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return appointmentsList;
    }
   
}
