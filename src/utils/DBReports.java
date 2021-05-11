/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import model.Reports;

/**
 * This class is the utility model for Reports
 * @author rebek
 */
public class DBReports 
{
    private Reports customerAppointmentReport;
    private Reports contactAppointmentReport;
    private Reports contactScheduleReport;
    
    /**
     * This method gets customer appointment reports
     * @return customer appointment report
     */
    public Reports getCustomerAppointmentReport()
    {
        return customerAppointmentReport;
    }
    
    /**
     * This method gets contact appointment reports
     * @return contact appointment report
     */
    public Reports getContactAppointmentReport()
    {
        return contactAppointmentReport;
    }
    
    /**
     * This method gets the contact schedule report
     * @return contact schedule report
     */
    public Reports getContactScheduleReport()
    {
        return contactScheduleReport;
    }
    
    /**
     * This method gets all customer appointments from database and groups them by type and month
     * @return customer appointment report
     */
    public static ObservableList<Reports> getCustomerAppointmentsReport()
    {
        ObservableList<Reports> customerAppointmentsReport = FXCollections.observableArrayList();
        
        try
        {
            Connection connect = DBConnection.getConnection();
            DBQuery.setStatement(connect);
            Statement statement = DBQuery.getStatement();
            
            String selectStatement = "SELECT Type, MONTH(Start) AS 'Month', COUNT(*) AS 'Total' FROM appointments GROUP BY Type, MONTH(Start)";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            
             while(rs.next())
             {
                 int appointmentMonthID = rs.getInt("Month");
                 String appointmentMonth = getAppointmentMonth(appointmentMonthID);
                 String appointmentType = rs.getString("Type");
                 int appointmentTotal = rs.getInt("Total");
                 
                 customerAppointmentsReport.add(new Reports(appointmentMonth, appointmentType, appointmentTotal));
              
                 
             }
            
            
        }
        catch(SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        
        return customerAppointmentsReport;
    }

    /**
     * This method gets all filtered customer appointment reports based on month
     * @param month calendar month
     * @return customer appointments report
     */
    public static ObservableList<Reports> getFilteredCustomerAppointmentsReport(String month)
    {
        ObservableList<Reports> filteredCustomerAppointmentsReport = FXCollections.observableArrayList();
        
        try
        {
            Connection connect = DBConnection.getConnection();
            DBQuery.setStatement(connect);
            Statement statement = DBQuery.getStatement();
            
            String selectStatement = "SELECT Type, MONTH(Start) AS 'Month', COUNT(*) AS 'Total' FROM appointments GROUP BY Type, MONTH(Start)";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            
             while(rs.next())
             {
                 int appointmentMonthID = rs.getInt("Month");
                 String appointmentMonth = getAppointmentMonth(appointmentMonthID);
                 String appointmentType = rs.getString("Type");
                 int appointmentTotal = rs.getInt("Total");
                 
                 if(appointmentMonth.equals(month))
                 {
                    filteredCustomerAppointmentsReport.add(new Reports(appointmentMonth, appointmentType, appointmentTotal));   
                 }
                              
             }
            
            
        }
        catch(SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        
        return filteredCustomerAppointmentsReport;
       
    }
    
    /**
     * This method gets all appointments for a report
     * @return all appointments
     */
    public static ObservableList<Reports> getAllAppointmentsforReport()
    {
        ZoneId userZoneID = ZoneId.of(TimeZone.getDefault().getID());
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");   
        
        ObservableList<Reports> allAppointmentsList = FXCollections.observableArrayList();
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
               int contactID = rs.getInt("Contact_ID");
               String contactName = DBContacts.getContactName(contactID);
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
               
               allAppointmentsList.add(new Reports(appointmentID, title, contactName, type, description, appointmentStartDate, appointmentEndDate, customerID));
                
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return allAppointmentsList;
    }

    /**
     * This method returns all appointments for selected contact
     * @param selectedContact selected contact
     * @return appointments for selected contact
     */
    public static ObservableList<Reports> getFilteredAppointmentsforReport(String selectedContact)
    {
        ZoneId userZoneID = ZoneId.of(TimeZone.getDefault().getID());
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");   
        
        ObservableList<Reports> filteredAppointmentsList = FXCollections.observableArrayList();
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
               int contactID = rs.getInt("Contact_ID");
               String contactName = DBContacts.getContactName(contactID);
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
               
               if(contactName.equals(selectedContact))
               {
                 filteredAppointmentsList.add(new Reports(appointmentID, title, contactName, type, description, appointmentStartDate, appointmentEndDate, customerID));  
               }
                
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return filteredAppointmentsList;
    }

    /**
     * This method returns all contact appointments and groups them by contact_ID and month
     * @return contact appointments
     */
    public static ObservableList<Reports> getContactAppointmentsReport()
    {
        ObservableList<Reports> contactAppointmentsReport = FXCollections.observableArrayList();
        
        try
        {
            Connection connect = DBConnection.getConnection();
            DBQuery.setStatement(connect);
            Statement statement = DBQuery.getStatement();
            
            String selectStatement = "SELECT Contact_ID, MONTH(Start) AS 'Month', COUNT(*) AS 'Total' FROM appointments GROUP BY Contact_ID, MONTH(Start)";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            
             while(rs.next())
             {
                 int appointmentMonthID = rs.getInt("Month");
                 String appointmentMonth = getAppointmentMonth(appointmentMonthID);
                 int appointmentContact = rs.getInt("Contact_ID");
                 String contactName = DBContacts.getContactName(appointmentContact);
                 int appointmentTotal = rs.getInt("Total");
                 
                 contactAppointmentsReport.add(new Reports(contactName, appointmentTotal, appointmentMonth));
              
                 
             }
            
            
        }
        catch(SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        
        return contactAppointmentsReport;
    }

    /**
     * This method gets all appointments for contacts based on month and groups them by contact_ID
     * @param month calendar month
     * @return filtered contact appointments
     */
    public static ObservableList<Reports> getFilteredContactAppointmentsReport(String month)
    {
        ObservableList<Reports> filteredContactAppointmentsReport = FXCollections.observableArrayList();
        
        try
        {
            Connection connect = DBConnection.getConnection();
            DBQuery.setStatement(connect);
            Statement statement = DBQuery.getStatement();
            
            String selectStatement = "SELECT Contact_ID, MONTH(Start) AS 'Month', COUNT(*) AS 'Total' FROM appointments GROUP BY Contact_ID, MONTH(Start)";
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            
             while(rs.next())
             {
                 int appointmentMonthID = rs.getInt("Month");
                 String appointmentMonth = getAppointmentMonth(appointmentMonthID);
                 int appointmentContact = rs.getInt("Contact_ID");
                 String contactName = DBContacts.getContactName(appointmentContact);
                 int appointmentTotal = rs.getInt("Total");
                 
                 if(appointmentMonth.equals(month))
                 {
                      filteredContactAppointmentsReport.add(new Reports(contactName, appointmentTotal, appointmentMonth)); 
                 }
              
                 
             }
            
            
        }
        catch(SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        
        return filteredContactAppointmentsReport;
    }

    /**
     * This method returns a list of calendar months
     * @param monthID
     * @return month
     */
    public static String getAppointmentMonth(int monthID)
    {
        String month = null;
        
        switch(monthID)
        {
            case 1:
                month = "January";
                break;
            case 2:
                month = "February";
                break;   
            case 3:
                month = "March";
                break;
            case 4:
                month = "April";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "June";
                break;
            case 7:
                month = "July";
                break;
            case 8:
                month = "August";
                break;
            case 9:
                month = "September";
                break;
            case 10:
                month = "October";
                break;
            case 11:
                month = "November";
                break;
            case 12:
                month = "December";
                break;
                
        }
        
        return month;
    }
    
}
