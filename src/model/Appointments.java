/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * This class is the model for the Appointments, includes getters/setters
 * @author rebek
 */


/**
 * This class is the model for the Appointments, includes getters/setters
 * @author rebek
 */
public class Appointments 
{
    private int appointmentID;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private int appointmentContactID;
    private String appointmentType;
    private String appointmentStartDate;
    private String appointmentEndDate;
    private int appointmentCustomerID;
    private int userID;
    
    /**
     *
     * @param appointmentID
     * @param appointmentTitle
     * @param appointmentDescription
     * @param appointmentLocation
     * @param appointmentContactID
     * @param appointmentType
     * @param appointmentStartDate
     * @param appointmentEndDate
     * @param appointmentCustomerID
     * @param userID
     */
    public Appointments(int appointmentID, String appointmentTitle, String appointmentDescription, String appointmentLocation, int appointmentContactID, String appointmentType, String appointmentStartDate, String appointmentEndDate, int appointmentCustomerID, int userID)
    {
        this.appointmentID = appointmentID;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentContactID = appointmentContactID;
        this.appointmentType = appointmentType;
        this.appointmentStartDate = appointmentStartDate;
        this.appointmentEndDate = appointmentEndDate;
        this.appointmentCustomerID = appointmentCustomerID;
        this.userID = userID;
    }
    
    /**
     *
     * @return
     */
    public int getAppointmentID()
    {
        return appointmentID;
    }
    
    /**
     *
     * @param appointmentID
     */
    public void setAppointmentID(int appointmentID)
    {
        this.appointmentID = appointmentID;
    }
    
    /**
     *
     * @return
     */
    public String getAppointmentTitle()
    {
        return appointmentTitle;
    }
    
    /**
     *
     * @param appointmentTitle
     */
    public void setAppointmentTitle(String appointmentTitle)
    {
        this.appointmentTitle = appointmentTitle;
    }
    
    /**
     *
     * @return
     */
    public String getAppointmentDescription()
    {
       return appointmentDescription; 
    }

    /**
     *
     * @param appointmentDescription
     */
    public void setAppointmentDescription(String appointmentDescription)
    {
        this.appointmentDescription = appointmentDescription;
    }
    //location

    /**
     *
     * @return
     */
    public String getAppointmentLocation()
    {
        return appointmentLocation;
    }

    /**
     *
     * @param appointmentLocation
     */
    public void setAppointmentLocation(String appointmentLocation)
    {
        this.appointmentLocation = appointmentLocation;
    }
    //contact id

    /**
     *
     * @return
     */
    public int getAppointmentContactID()
    {
        return appointmentContactID;
    }

    /**
     *
     * @param appointmentContactID
     */
    public void setAppointmentContactID(int appointmentContactID)
    {
        this.appointmentContactID = appointmentContactID;
    }
    //type

    /**
     *
     * @return
     */
    public String getAppointmentType()
    {
        return appointmentType;
    }

    /**
     *
     * @param appointmentType
     */
    public void setAppointmentType(String appointmentType)
    {
        this.appointmentType = appointmentType;
    }
    //start date

    /**
     *
     * @return
     */
    public String getAppointmentStartDate()
    {
        return appointmentStartDate;
    }

    /**
     *
     * @param appointmentStartDate
     */
    public void setAppointmentStartDate(String appointmentStartDate)
    {
        this.appointmentStartDate = appointmentStartDate;
    }
    //end date

    /**
     *
     * @return
     */
    public String getAppointmentEndDate()
    {
        return appointmentEndDate;
    }

    /**
     *
     * @param appointmentEndDate
     */
    public void setAppointmentEndDate(String appointmentEndDate)
    {
        this.appointmentEndDate = appointmentEndDate;
    }
    //customer id

    /**
     *
     * @return
     */
    public int getAppointmentCustomerID()
    {
        return appointmentCustomerID;
    }

    /**
     *
     * @param appointmentCustomerID
     */
    public void setAppointmentCustomerID(int appointmentCustomerID)
    {
        this.appointmentCustomerID = appointmentCustomerID;
    }
    
    public int getUserID()
    {
        return userID;
    }
    public void setUserID (int userID)
    {
        this.userID = userID;
    }
    
}
