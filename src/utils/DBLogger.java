/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;

/**
 * Utility class for Logger
 * @author rebek
 */
public class DBLogger 
{
    private static final String fileName = "login_activity.text";
    
    /**
     * This method logs the user log-in activity to the login text file
     * @param user username
     * @param logInAttempt success or fail
     */
    public static void logActivity(String user, String logInAttempt)
    {
        try
        {
            FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);          
            PrintWriter pw = new PrintWriter(bw);
            
            pw.write(ZonedDateTime.now() + " " + user + " " + logInAttempt + "\n");  
           
            pw.close();
            
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }
}
