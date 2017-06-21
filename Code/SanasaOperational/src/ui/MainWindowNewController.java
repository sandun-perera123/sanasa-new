/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import report.ReportView;
import static ui.LoginController.privilage;
import static ui.LoginController.user;
import static ui.Main.stage;
import util.HibernateUtil;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class MainWindowNewController implements Initializable {

    @FXML
    private Label lblUser;
    @FXML
    private Button btnMember;
    @FXML
    private AnchorPane pneCenter;
    @FXML
    private ImageView imgUser;
    @FXML
    private Button btnRelation;
    @FXML
    private Button btnShares;
    @FXML
    private Button btnProperty;
    @FXML
    private Button btnLoan;
    @FXML
    private Button btnAccount;
    @FXML
    private Button btnApproveLoan;
    @FXML
    private Button btnAttendance;
    @FXML
    private Button btnAccountPayment;
    @FXML
    private Button btnMeeting;
    @FXML
    private ImageView btnHome;
    @FXML
    private ImageView btnLogout;
    @FXML
    private Button btnPrivilege;
    @FXML
    private Button btnUser;
    @FXML
    private Label lblDate;

    public static Date date;
    @FXML
    private Button btnCommonAccount;
    @FXML
    private Button btnReportMember;
    @FXML
    private Button btnCommonAccountTransaction;
    @FXML
    private Button btnIncome;
    @FXML
    private Button btnReportCommonaccountTransaction;
    @FXML
    private Button btnReportCommonaccountBalance;
    @FXML
    private Button btnReportIncome;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnMember.setDisable(!(privilage.get("Member_select") || privilage.get("Member_insert")
                || privilage.get("Member_update") || privilage.get("Member_delete")));

        if (user.getEmployeeId().getImage() != null) {
            imgUser.setImage(new Image(new ByteArrayInputStream(user.getEmployeeId().getImage())));
        } else {
            imgUser.setImage((new Image("/image/user.png")));
        }

        loadDashBoard();

        if (date != null) {

            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String sdate = df.format(date);

            lblDate.setText(sdate);
            
        }else{
            
            date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String sdate = df.format(date);

            lblDate.setText(sdate);
            
        }

        lblUser.setText(user.getName());
    }

    @FXML
    private void btnMemberAP(ActionEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Member.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnRelationAP(ActionEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Relation.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnSharesAP(ActionEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Share.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnPropertyAP(ActionEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Property.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnLoanAP(ActionEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Loan.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnAccountAP(ActionEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Account.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnApproveLoanAP(ActionEvent event) {

        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("LoanApproval.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void btnAttendanceAP(ActionEvent event) {

        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Attendance.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void btnAccountPaymentAP(ActionEvent event) {

        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("AccountPayment.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void btnMeetingAP(ActionEvent event) {

        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Meeting.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void btnHomeAP(MouseEvent event) {

        loadDashBoard();

    }

    private void loadDashBoard() {

        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("DashBoard.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void btnLogoutMC(MouseEvent event) {

        stage.close();
        user = null;

        try {

            Parent root = FXMLLoader.load(Main.class.getResource("Login.fxml"));
            Scene scene = new Scene(root);

            Stage primaryStage = new Stage();
            primaryStage.setTitle("සී/ර සමූපකාර ණය ගණුදෙනු සමිතිය - මාදෙල්ගමුව");

            primaryStage.setScene(scene);

            primaryStage.show();

            stage = primaryStage;
            stage.getIcons().add(new Image("image/sanasalogo.png"));

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void btnPrivilegeAP(ActionEvent event) {

        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Privilage.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void btnUserAP(ActionEvent event) {

        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("User.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void btnCommonAccountAP(ActionEvent event) {
        
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("CommonAccount.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void btnReportMemberAP(ActionEvent event) {
        
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("ReportMember.fxml"));

            DialogPane dialogPane = new DialogPane();
            dialogPane.setContent(root);

            Dialog subUI = new Dialog();
            subUI.setDialogPane(dialogPane);

            subUI.show();

        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void btnCommonAccountTransactionAP(ActionEvent event) {
        
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("CommonAccountTransaction.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void btnIncomeAP(ActionEvent event) {
        
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("IncomeNew.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void btnReportCommonaccountTransactionAP(ActionEvent event) {
        System.out.println("1");
        new ReportView("/report/CommontransactionByDate.jasper");
        System.out.println("2");
        
    }

    @FXML
    private void btnReportCommonaccountBalanceAP(ActionEvent event) {
        
        new ReportView("/report/Commonaccount.jasper");
    }

    @FXML
    private void btnReportIncomeAP(ActionEvent event) {
        
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("ReportIncome.fxml"));
            pneCenter.getChildren().clear();
            pneCenter.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
