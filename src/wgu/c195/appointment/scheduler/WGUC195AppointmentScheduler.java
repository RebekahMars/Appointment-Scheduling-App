/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgu.c195.appointment.scheduler;

import java.sql.Connection;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DBQuery;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;

/**
 * This class is the main class where the application is ran
 * @author rebek
 */
public class WGUC195AppointmentScheduler extends Application
{

    /**
     * This launches the database connection and then closes it
     * @param args the command line arguments
     */
    public static void main(String[] args) //throws SQLException
    {
        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
        
    }

    /**
     * This starts the application at the log-in page
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception 
    {
      Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.setTitle("Login Screen");
      stage.show();
    }
    
}
