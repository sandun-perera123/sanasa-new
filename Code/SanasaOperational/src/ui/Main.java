/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;
import static ui.MainWindowNewController.date;

/**
 *
 * @author Suranga
 */
public class Main extends Application {

    public static Stage stage;

    @Override
    public void start(Stage primaryStage){

        try {

           // getInternationalDate();
            
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("සණස සංවර්ධන බැංකුව");

            primaryStage.setScene(scene);

            primaryStage.show();

            stage = primaryStage;
            stage.getIcons().add(new Image("image/sanasalogo.png"));

        } catch (IOException ex) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    private boolean isInternetActivate(){

        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping www.google.com");
            
            if (p1.waitFor() == 0) {
                return true;
            }
            
            if (p1.waitFor() == 1) {
                return false;
            }
        } catch (IOException iOException) {
            System.out.println(iOException.getMessage());
        } catch (InterruptedException interruptedException) {
            System.out.println(interruptedException.getMessage());
        }

        return false;

    }

    private void getInternationalDate(){

        if (isInternetActivate()) {

            try {
                
                String TIME_SERVER = "time-a.nist.gov";
                
                NTPUDPClient timeClient = new NTPUDPClient();
                InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
                TimeInfo timeInfo = timeClient.getTime(inetAddress);
                NtpV3Packet message = timeInfo.getMessage();
                long serverTime = message.getTransmitTimeStamp().getTime();
                Date time = new Date(serverTime);
                System.out.println("Local :" + new Date());
                System.out.println("Online " + TIME_SERVER + ": " + time);
                
                date = time;
                
            } catch (IOException iOException) {
                System.out.println(iOException.getMessage());
            }

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("අසාර්ථකයි !");
            alert.setHeaderText("");
            alert.setContentText("අන්තර්ජාල සබදතාවය ක්‍රියාත්මක නොමැති බැවින් ඇතුළත් විය නොහැක.");

            ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(btnOk);
            alert.showAndWait();

            System.exit(0);

        }

    }

    @Override
    public void stop() {
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(args);
    }

}
