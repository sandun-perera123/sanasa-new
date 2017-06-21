/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.LoanDao;
import dao.LoanStatusDao;
import dao.MeetingDao;
import dao.ShareDao;
import entity.Loan;
import entity.Propertygain;
import entity.Sharegain;
import entity.Smember;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import report.ReportView;
import static ui.LoginController.user;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class MemberLoanApprovalController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private Label lblMemberID;
    @FXML
    private Label lblName;
    @FXML
    private DatePicker dtpLoanTo;
    @FXML
    private DatePicker dtpLoanFrom;
    @FXML
    private Label lblLoanCount;
    @FXML
    private Label lblFinishLoanCount;
    @FXML
    private Label lblIsDueLoans;
    @FXML
    private DatePicker dtpSharesTo;
    @FXML
    private DatePicker dtpSharesFrom;
    @FXML
    private Label lblSharesCount;
    @FXML
    private Label lblSharesValue;
    @FXML
    private Label lblImmovableProperty;
    @FXML
    private Label lblMovableProperty;
    @FXML
    private ImageView imgMemberView;
    @FXML
    private PieChart chrtShare;
    @FXML
    private Label lblTotalMeetings;
    @FXML
    private Label lblAttendedCount;
    @FXML
    private Label lblAbsentCount;
    @FXML
    private Label lblInformedCount;
    @FXML
    private PieChart chrtAttendance;
    @FXML
    private Button btnProperty;
    @FXML
    private Button btnAttendance;
    @FXML
    private Button btnShares;
    @FXML
    private Button btnReject;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Module-Data">
    //Binding with the Form
    private Loan loan;

    ResourceBundle rb;
    @FXML
    private Button btnApprove;

//</editor-fold>
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        loan = (Loan) rb.getObject("Loan");
        loadForm();
    }

    private void loadForm() {

        loadMemberDetail(loan.getSmemberId());
        loadPropertyDetails(loan.getSmemberId());
        loadSharesDetails(loan.getSmemberId(), null, null);
        loadAttendanceDetails(loan.getSmemberId());
    }

    private void loadMemberDetail(Smember member) {

        lblName.setText(member.getName());
        lblMemberID.setText(member.getMemberid());

        if (member.getPhoto() != null) {
            imgMemberView.setImage(new Image(new ByteArrayInputStream(member.getPhoto())));
        } else {
            imgMemberView.setImage(new Image("/image/user.png"));
        }

    }

    private void loadPropertyDetails(Smember member) {

        if (member.getPropertygainList().size() > 0) {

            Propertygain property = member.getPropertygainList().get(0);
            lblMovableProperty.setText("රු. " + property.getMovablepropertytotal().toString());
            lblImmovableProperty.setText("රු. " + property.getImmovablepropertytotal().toString());

        } else {
            lblMovableProperty.setText("-");
            lblImmovableProperty.setText("-");
        }

    }

    private void loadSharesDetails(Smember member, Date fdate, Date tdate) {

        if (member.getSharegainList().size() > 0) {

            Integer count = 0;
            BigDecimal total = new BigDecimal(0.00);

            List<Sharegain> sharegains = null;

            if (fdate == null | tdate == null) {

                sharegains = member.getSharegainList();

            } else {

                sharegains = member.getSharegainList().stream()
                        .filter(a -> a.getDate().after(fdate) && a.getDate().before(tdate))
                        .collect(Collectors.toList());

            }

            for (Sharegain sharegain : sharegains) {

                count += sharegain.getTotalcount();
                total = total.add(sharegain.getTotalvalue());

            }

            lblSharesCount.setText(count.toString());
            lblSharesValue.setText(total.toString());

            ObservableList<PieChart.Data> pieChartData
                    = FXCollections.observableArrayList(
                            new PieChart.Data("", ShareDao.getTotalCount() - count),
                            new PieChart.Data("හිමි", count)
                    );

            chrtShare.setData(pieChartData);

        } else {
            lblSharesCount.setText("-");
            lblSharesValue.setText("-");
        }

    }

    private void loadAttendanceDetails(Smember member) {

        Integer totalMeetingCount = MeetingDao.getAll().size();

        Long attendedCount = member.getAttendanceList().stream()
                .filter(a -> a.getAttendancetypeId().getId() == 1)
                .count();

        Long notAttendedCount = member.getAttendanceList().stream()
                .filter(a -> a.getAttendancetypeId().getId() == 2)
                .count();

        Long informedCount = member.getAttendanceList().stream()
                .filter(a -> a.getAttendancetypeId().getId() == 3)
                .count();

        lblTotalMeetings.setText(totalMeetingCount.toString());
        lblAttendedCount.setText(attendedCount.toString());
        lblAbsentCount.setText(notAttendedCount.toString());
        lblInformedCount.setText(informedCount.toString());

        //Update the Chart
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("පැමිණි", attendedCount),
                        new PieChart.Data("නොපැමිණි", notAttendedCount),
                        new PieChart.Data("දැනුම් දුන්", informedCount)
                );

        chrtAttendance.setData(pieChartData);

    }

    @FXML
    private void btnPropertyAP(ActionEvent event) {

        if (!loan.getSmemberId().getPropertygainList().isEmpty()) {

            HashMap hm = new HashMap();
            hm.put("propertygainid", loan.getSmemberId().getPropertygainList().get(0).getId());
            new ReportView("/report/PropertyByPropertygainID.jasper", hm);

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("අසාර්ථකයි !");
            alert.setHeaderText(" ");
            alert.setContentText("වත්කම් තොරතුරු ඇතුළත් කර නොමැත.");

            ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(btnOk);
            alert.showAndWait();

        }

    }

    @FXML
    private void btnAttendanceAP(ActionEvent event) {

        if (!loan.getSmemberId().getAttendanceList().isEmpty()) {

            HashMap hm = new HashMap();
            hm.put("memberid", loan.getSmemberId().getId());
            new ReportView("/report/AttendanceByMember.jasper", hm);

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("අසාර්ථකයි !");
            alert.setHeaderText(" ");
            alert.setContentText("පැමිණීම් තොරතුරු ඇතුළත් කර නොමැත.");

            ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(btnOk);
            alert.showAndWait();

        }

    }

    @FXML
    private void btnSharesAP(ActionEvent event) {

        if (!loan.getSmemberId().getSharegainList().isEmpty()) {

            HashMap hm = new HashMap();

            if (dtpSharesFrom.getValue() == null && dtpSharesTo.getValue() == null) {

                hm.put("memberid", loan.getSmemberId().getId());
                new ReportView("/report/SharesByMember.jasper", hm);

            } else {

                Date fdate = java.sql.Date.valueOf(dtpSharesFrom.getValue());
                Date tdate = java.sql.Date.valueOf(dtpSharesTo.getValue());

                hm.put("memberid", loan.getSmemberId().getId());
                hm.put("fdate", fdate);
                hm.put("tdate", tdate);
                new ReportView("/report/SharesByMember.jasper", hm);

            }

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("අසාර්ථකයි !");
            alert.setHeaderText(" ");
            alert.setContentText("කොටස් මිළදී ගැනීම් තොරතුරු ඇතුළත් කර නොමැත.");

            ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(btnOk);
            alert.showAndWait();

        }

    }

    @FXML
    private void btnRejectAP(ActionEvent event) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("සාර්ථකයි !");
        alert.setHeaderText("ණය අවලංගු කිරීම");
        alert.setContentText("ණය ඉල්ලීම අවලංගු කිරීමට අවශ්‍යද?");

        ButtonType btnYes = new ButtonType("ඔව්", ButtonData.YES);
        ButtonType btnNo = new ButtonType("නැත", ButtonData.NO);

        alert.getButtonTypes().setAll(btnNo, btnYes);

        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == btnYes) {

            loan.setLoanstatusId(LoanStatusDao.getById(3));
            loan.setDoapproved(new Date());
            loan.setEmployeeApprovedId(user.getEmployeeId());

            LoanDao.update(loan);
            Notifications.create().title("සාර්ථකයි !").text("ණය ඉල්ලීම අවලංගු කරන ලදී.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

            Stage win = (Stage) btnReject.getScene().getWindow();
            win.close();

        }

    }

    @FXML
    private void btnApproveAP(ActionEvent event) {

        try {
            Stage subUI = new Stage();
            MyResourceBundle rb = new MyResourceBundle();

            HashMap hm = new HashMap();
            hm.put("loan", loan);

            rb.setHashMap(hm);

            AnchorPane ui = FXMLLoader.load(MemberSearchController.class.getResource("LoanAprroval.fxml"), rb);
            subUI.setResizable(false);
            
            Scene scene = new Scene(ui);
            scene.getStylesheets().add("/style/Styles.css");
            subUI.setScene(scene);
            subUI.showAndWait();

            if (loan.getLoanstatusId().getId() == 1) {
                btnApprove.setDisable(true);
            }

        } catch (IOException ex) {
            Logger.getLogger(MemberLoanApprovalController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void dtpLoanToAP(ActionEvent event) {
    }

    @FXML
    private void dtpLoanFromAP(ActionEvent event) {
    }

    private void searchSharesByDateRange() {

        if (dtpSharesFrom.getValue() != null && dtpSharesTo.getValue() != null) {

            Date from = java.sql.Date.valueOf(dtpSharesFrom.getValue());
            Date to = java.sql.Date.valueOf(dtpSharesTo.getValue());

            loadSharesDetails(loan.getSmemberId(), from, to);

        } else {

            loadSharesDetails(loan.getSmemberId(), null, null);

        }

    }

    @FXML
    private void dtpSharesToAP(ActionEvent event) {
        searchSharesByDateRange();
    }

    @FXML
    private void dtpSharesFromAP(ActionEvent event) {
        searchSharesByDateRange();
    }

}
