/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * This class is the model for the Contacts, includes getters/setters
 * @author rebek
 */
public class Contacts 
{
    private int contactID;
    private String contactName;
    private String contactEmail;
    
    /**
     *
     * @param contactID
     * @param contactName
     * @param contactEmail
     */
    public Contacts (int contactID, String contactName, String contactEmail)
    {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     *
     * @return
     */
    public int getContactID()
    {
        return contactID;
    }

    /**
     *
     * @param contactID
     */
    public void setContactID(int contactID)
    {
        this.contactID = contactID;
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
    public String getContactEmail()
    {
        return contactEmail;
    }

    /**
     *
     * @param contactEmail
     */
    public void setContactEmail(String contactEmail)
    {
        this.contactEmail = contactEmail;
    }
}
