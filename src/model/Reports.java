/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * This class is the model for the Reports, includes getters/setters
 * @author rebek
 */
public class Reports 
{
    private String appointmentMonth;
    private String appointmentType;
    private int appointmentTotal;
    private String contactName;
    private int appointmentID;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentStart;
    private String appointmentEnd;
    private int customerID;
    
    /**
     *
     * @param appointmentID
     * @param appointmentTitle
     * @param contactName
     * @param appointmentType
     * @param appointmentDescription
     * @param appointmentStart
     * @param appointmentEnd
     * @param customerID
     */
    public Reports(int appointmentID, String appointmentTitle, String contactName, String appointmentType, String appointmentDescription, String appointmentStart, String appointmentEnd, int customerID)
    {
        this.appointmentID = appointmentID;
        this.appointmentTitle = appointmentTitle;
        this.contactName = contactName;
        this.appointmentType = appointmentType;
        this.appointmentDescription = appointmentDescription;
        this.appointmentStart = appointmentStart;
        this.appointmentEnd = appointmentEnd;
        this.customerID = customerID;
    }
    
    /**
     *
     * @param contactName
     * @param appointmentTotal
     * @param appointmentMonth
     */
    public Reports(String contactName, int appointmentTotal, String appointmentMonth)
    {
        this.contactName = contactName;
        this.appointmentTotal = appointmentTotal;
        this.appointmentMonth = appointmentMonth;
    }
    
    /**
     *
     * @param appointmentMonth
     * @param appointmentType
     * @param appointmentTotal
     */
    public Reports(String appointmentMonth, String appointmentType, int appointmentTotal)
    {
        this.appointmentMonth = appointmentMonth;
        this.appointmentType = appointmentType;
        this.appointmentTotal = appointmentTotal;
    }
    
    /**
     *
     * @return
     */
    public String getAppointmentMonth()
    {
        return appointmentMonth;
    }

    /**
     *
     * @param appointmentMonth
     */
    public void setAppointmentMonth(String appointmentMonth)
    {
        this.appointmentMonth = appointmentMonth;
    }

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

    /**
     *
     * @return
     */
    public int getAppointmentTotal()
    {
        return appointmentTotal;
    }

    /**
     *
     * @param appointmentTotal
     */
    public void setAppointmentTotal(int appointmentTotal)
    {
        this.appointmentTotal = appointmentTotal;
    }

    /**
     *
     * @return
     */
    public String getContactName()
    {
        return contactName;
    }

    /**
     *
     * @param contactName
     */
    public void setContactName(String contactName)
    {
        this.contactName = contactName;
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

    /**
     *
     * @return
     */
    public String getAppointmentStart()
    {
        return appointmentStart;
    }

    /**
     *
     * @param appointmentStart
     */
    public void setAppointmentStart(String appointmentStart)
    {
        this.appointmentStart = appointmentStart;
    }

    /**
     *
     * @return
     */
    public String getAppointmentEnd()
    {
        return appointmentEnd;
    }

    /**
     *
     * @param appointmentEnd
     */
    public void setAppointmentEnd(String appointmentEnd)
    {
        this.appointmentEnd = appointmentEnd;
    }

    /**
     *
     * @return
     */
    public int getCustomerID()
    {
        return customerID;
    }

    /**
     *
     * @param customerID
     */
    public void setCustomerID(int customerID)
    {
        this.customerID = customerID;
    }
}
