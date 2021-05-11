/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * This class is the model for the Customers, includes getters/setters
 * @author rebek
 */
public class Customers 
{
    private int customerID;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhoneNumber;
    private int customerDivisionID;
    
    /**
     *
     * @param customerID
     * @param customerName
     * @param customerAddress
     * @param customerPostalCode
     * @param customerPhoneNumber
     * @param customerDivisionID
     */
    public Customers(int customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhoneNumber, int customerDivisionID)
    {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerDivisionID = customerDivisionID;
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
    
    /**
     *
     * @return
     */
    public String getCustomerName()
    {
        return customerName;
    }
    
    /**
     *
     * @param customerName
     */
    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }
    
    /**
     *
     * @return
     */
    public String getCustomerAddress()
    {
        return customerAddress;
    }

    /**
     *
     * @param customerAddress
     */
    public void setCustomerAddress(String customerAddress)
    {
        this.customerAddress = customerAddress;
    }

    /**
     *
     * @return
     */
    public String getCustomerPostalCode()
    {
        return customerPostalCode;
    }

    /**
     *
     * @param customerPostalCode
     */
    public void setCustomerPostalCode(String customerPostalCode)
    {
        this.customerPostalCode = customerPostalCode;
    }

    /**
     *
     * @return
     */
    public String getCustomerPhoneNumber()
    {
        return customerPhoneNumber;
    }

    /**
     *
     * @param customerPhoneNumber
     */
    public void setCustomerPhoneNumber(String customerPhoneNumber)
    {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    /**
     *
     * @return
     */
    public int getCustomerDivisionID()
    {
        return customerDivisionID;
    }

    /**
     *
     */
    public void setCustomerDivisionID()
    {
        this.customerDivisionID = customerDivisionID;
    }
}
