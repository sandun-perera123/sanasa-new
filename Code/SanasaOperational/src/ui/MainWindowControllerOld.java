/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import static ui.LoginController.privilage;
import static ui.LoginController.user;

/**
 * FXML Controller class
 *
 * @author Suranga
 */
public class MainWindowControllerOld implements Initializable {

    public static File lastDirectory;

    @FXML
    private Menu mnuPeople;
    @FXML
    private MenuItem mtmEmployee;
    @FXML
    private SplitPane sptMain;
    @FXML
    private AnchorPane apnLeft;
    @FXML
    private SplitPane sptLeft;
    @FXML
    private AnchorPane apnLeftTop;
    @FXML
    private ImageView imgLogo;
    @FXML
    private ImageView imgUser;
    @FXML
    private Label lblUser;
    @FXML
    private Label lblTime;
    @FXML
    private AnchorPane apnLeftBottom;
    @FXML
    private AnchorPane apnRight;
    @FXML
    private Menu munAdmin;
    @FXML
    private MenuItem mtmUser;
    @FXML
    private MenuItem mtmPrivilage;
    @FXML
    private MenuItem mtmDesignation;
    @FXML
    private MenuItem mtmAccount;
    @FXML
    private MenuItem mtmLoan;
    @FXML
    private MenuItem mtmMember;
    @FXML
    private MenuItem mtmShare;
    @FXML
    private MenuItem mtmMeeting;
    @FXML
    private MenuItem mtmProperty;
    @FXML
    private MenuItem mtmRelation;
    @FXML
    private MenuItem mtmAccounttype;
    @FXML
    private MenuItem mtmLoanType;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mtmEmployee.setDisable(!(privilage.get("Employee_select")
                || privilage.get("Employee_insert")
                || privilage.get("Employee_update") || privilage.get("Employee_delete")));

        mtmPrivilage.setDisable(!(privilage.get("Privilage_select") || privilage.get("Privilage_insert")
                || privilage.get("Privilage_update") || privilage.get("Privilage_delete")));

        mtmUser.setDisable(!(privilage.get("User_select") || privilage.get("User_insert")
                || privilage.get("User_update") || privilage.get("User_delete")));
        
         mtmDesignation.setDisable(!(privilage.get("Designation_select") || privilage.get("Designation_insert")
                || privilage.get("Designation_update") || privilage.get("Designation_delete")));
         
         mtmAccount.setDisable(!(privilage.get("Account_select") || privilage.get("Account_insert")
                || privilage.get("Account_update") || privilage.get("Account_delete")));
         
         mtmLoan.setDisable(!(privilage.get("Loan_select") || privilage.get("Loan_insert")
                || privilage.get("Loan_update") || privilage.get("Loan_delete")));
         
         mtmMember.setDisable(!(privilage.get("Member_select") || privilage.get("Member_insert")
                || privilage.get("Member_update") || privilage.get("Member_delete")));
         
         mtmShare.setDisable(!(privilage.get("Share_select") || privilage.get("Share_insert")
                || privilage.get("Share_update") || privilage.get("Share_delete")));

        if (user.getEmployeeId().getImage() != null) {
            imgUser.setImage(new Image(new ByteArrayInputStream(user.getEmployeeId().getImage())));
        } else {
            imgUser.setImage((new Image("/image/user.png")));
        }

        lblUser.setText(user.getName());

    }

    @FXML
    private void mtmEmployeeAP(ActionEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Employee.fxml"));
            apnRight.getChildren().clear();
            apnRight.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void mtmUserAP(ActionEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("User.fxml"));
            apnRight.getChildren().clear();
            apnRight.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void mnuPrivilageAP(ActionEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Privilage.fxml"));
            apnRight.getChildren().clear();
            apnRight.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void mtmDesignationAP(ActionEvent event) {
         try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Designation.fxml"));
            apnRight.getChildren().clear();
            apnRight.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void mtmAccountAP(ActionEvent event) {
        
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Account.fxml"));
            apnRight.getChildren().clear();
            apnRight.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void mtmLoanAP(ActionEvent event) {
        
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Loan.fxml"));
            apnRight.getChildren().clear();
            apnRight.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void mtmMemberAP(ActionEvent event) {
        
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Member.fxml"));
            apnRight.getChildren().clear();
            apnRight.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void mtmShareAP(ActionEvent event) {
        
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Share.fxml"));
            apnRight.getChildren().clear();
            apnRight.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void mtmMeetingAP(ActionEvent event) {
        
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Meeting.fxml"));
            apnRight.getChildren().clear();
            apnRight.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void mtmPropertyAP(ActionEvent event) {
        
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Property.fxml"));
            apnRight.getChildren().clear();
            apnRight.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void mtmRelationAP(ActionEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("Relation.fxml"));
            apnRight.getChildren().clear();
            apnRight.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void mtmAccounttypeAP(ActionEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("AccountType.fxml"));
            apnRight.getChildren().clear();
            apnRight.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void mtmLoanTypeAP(ActionEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(Main.class.getResource("LoanType.fxml"));
            apnRight.getChildren().clear();
            apnRight.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowControllerOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
