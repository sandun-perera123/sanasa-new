/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.MemberDao;
import entity.Smember;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import report.ReportView;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class ReportMemberController implements Initializable {

    @FXML
    private ComboBox<Smember> cmbMember;
    @FXML
    private Button btnReport;
    @FXML
    private ImageView imgMemberSearch;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbMember.setItems(MemberDao.getAll());
    }

    @FXML
    private void btnReportAP(ActionEvent event) {

        Smember member = cmbMember.getSelectionModel().getSelectedItem();

        HashMap hm = new HashMap();

        if (member != null) {

            hm.put("id", member.getId());
            new ReportView("/report/MemberDetailsByID.jasper", hm);

        } else {
            new ReportView("/report/MemberDetails.jasper", hm);
        }

        Stage st = (Stage) cmbMember.getScene().getWindow();
        st.close();

    }

    @FXML
    private void imgMemberSearchMC(MouseEvent event) {

        try {

            Stage subUI = new Stage();
            MyResourceBundle rb = new MyResourceBundle();

            HashMap hm = new HashMap();
            hm.put("combo", cmbMember);
            hm.put("list", cmbMember.getItems());

            rb.setHashMap(hm);

            AnchorPane itemSearchUI = FXMLLoader.load(MemberSearchController.class.getResource("MemberSearch.fxml"), rb);
            subUI.setResizable(false);
            subUI.setScene(new Scene(itemSearchUI));
            subUI.show();

        } catch (IOException ex) {

            Logger.getLogger(RelationController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
