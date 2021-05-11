/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * This class is the model for the Main Screen, includes getters/setters
 * @author rebek
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is the model for the Main Screen, includes getters/setters
 * @author rebek
 */
public class MainScreen 
{

    /**
     *
     */
    public static ObservableList<Appointments> appointmentTable = FXCollections.observableArrayList(); 

    /**
     *
     */
    public static ObservableList<Customers> customerTable = FXCollections.observableArrayList();

    /**
     *
     */
    public static Appointments selectedAppointment;

    /**
     *
     */
    public static Customers selectedCustomer;
    
    /**
     *
     * @param newAppointment
     */
    public static void addAppointment (Appointments newAppointment)
    {
        appointmentTable.add(newAppointment);
    }
    
    /**
     *
     * @param selectedAppointment
     * @param index
     */
    public static void modifyAppointment (Appointments selectedAppointment, int index)
    {
        appointmentTable.set(index, selectedAppointment);
    }
    
    /**
     *
     * @param selectedAppointment
     * @return
     */
    public static boolean deleteAppointment (Appointments selectedAppointment)
    {
        appointmentTable.remove(selectedAppointment);
        return true;
    }

    /**
     *
     * @return
     */
    public static ObservableList<Appointments> getAllAppointments()
    {
        return appointmentTable;
    }
    
    /**
     *
     * @param newCustomer
     */
    public static void addCustomer (Customers newCustomer)
    {
        customerTable.add(newCustomer);
    }
    
    /**
     *
     * @param selectedCustomer
     * @param index
     */
    public static void modifyCustomer (Customers selectedCustomer, int index)
    {
        customerTable.set(index, selectedCustomer);
    }
    
    /**
     *
     * @param selectedCustomer
     * @return
     */
    public static boolean deleteCustomer (Customers selectedCustomer)
    {
        customerTable.remove(selectedCustomer);
        return true;
    }

    /**
     *
     * @return
     */
    public static ObservableList<Customers> getAllCustomers()
    {
        return customerTable;
    }
}
