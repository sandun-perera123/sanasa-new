/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import com.sun.javafx.geom.CubicApproximator;
import dao.AccountDao;
import dao.AccountpaymenttypeDao;
import dao.EmployeeDao;
import entity.Account;
import entity.Accountpayment;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import static ui.Main.stage;
import util.CustomAlerts;
import static util.CustomAlerts.btnNo;
import static util.CustomAlerts.btnYes;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class AccountUpdateProcessController implements Initializable {

    @FXML
    private ProgressBar progressbar;
    @FXML
    private Button btnStart;
    @FXML
    private Label btnCount;

    private void updateAccounts(List<Account> accounts) {

        if (accounts.size() > 0) {

            int totalCount = accounts.size();
            double progressValue = 0.0;
            int completedCount = 0;

            for (Account acc : accounts) {

                BigDecimal interestRate = acc.getAccounttypeId().getInterest().divide(new BigDecimal(100.00));
                BigDecimal balance = acc.getBalance();

                BigDecimal interestedValue = balance.multiply(interestRate);
                BigDecimal monthlyinterestedValue = interestedValue.divide(new BigDecimal(12.00));

                BigDecimal newBalance = balance.add(monthlyinterestedValue);
                acc.setBalance(newBalance.setScale(2, RoundingMode.DOWN));
                acc.setLastupdate(new Date());

                Accountpayment accpayment = new Accountpayment();
                accpayment.setAccountId(acc);
                accpayment.setAccountpaymenttypeId(AccountpaymenttypeDao.getByID(3));
                accpayment.setAmount(monthlyinterestedValue.setScale(2, RoundingMode.DOWN));
//                accpayment.setBillno("පොළිය");
                accpayment.setDate(new Date());
                accpayment.setEmployeeId(EmployeeDao.getById(1));
                acc.getAccountpaymentList().add(accpayment);

                AccountDao.update(acc);
                completedCount++;
                System.out.println(acc.getBalance());
                progressValue = (completedCount / totalCount) * 100;
                progressbar.setProgress(progressValue);

            }

            btnCount.setText(String.valueOf(completedCount));

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("තහවුරු කිරීම");
            alert.setHeaderText(" ");
            alert.setContentText("ශේෂය යාවත්කාලීන කිරීමේ ක්‍රියාවලිය අවසන්. ප්‍රධාන මෙනුව වෙත යාමට අවශ්‍යද?");

            ButtonType btnYes = new ButtonType("ඔව්", ButtonBar.ButtonData.YES);
            ButtonType btnNo = new ButtonType("නැත", ButtonBar.ButtonData.NO);

            alert.getButtonTypes().setAll(btnNo, btnYes);
            Optional<ButtonType> action = alert.showAndWait();

            if (action.get() == CustomAlerts.btnYes) {

                Stage stage = (Stage) btnStart.getScene().getWindow();
                stage.close();

                loadMainWindow();

            } else {
                System.exit(0);
            }

        } else {
            System.out.println("No Accounts");
        }

    }

    private void loadMainWindow() {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("MainWindowNew.fxml"));

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
            stage.close();
            stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image("image/sanasalogo.png"));
            stage.setTitle("සී/ර සමූපකාර ණය ගණුදෙනු සමිතිය - මාදෙල්ගමුව");

            stage.setMaximized(true);
            stage.show();

        } catch (IOException ex) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("වැරදියි !");
            alert.setHeaderText("ඇතුළත් වීම අසාර්ථකයි !");
            alert.setContentText("ප්‍රධාන මෙනුව වෙත යාමට නොහැක.");

            ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(btnOk);
            alert.showAndWait();

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void btnStartAP(ActionEvent event) {

        updateAccounts(AccountDao.getAllNonUpdatedAccounts(new Date()));

    }

}
