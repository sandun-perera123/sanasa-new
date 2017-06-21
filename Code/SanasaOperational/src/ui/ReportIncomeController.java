/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import report.ReportView;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class ReportIncomeController implements Initializable {


    @FXML
    private Button btnSearch;
    @FXML
    private DatePicker dtpFrom;
    @FXML
    private DatePicker dtpTo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dtpFrom.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
        dtpTo.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
    }    


    @FXML
    private void btnSearchAP(ActionEvent event) {
        
        LocalDate from = dtpFrom.getValue();
        LocalDate to = dtpTo.getValue();

        HashMap hm = new HashMap();

        if (from != null && to != null) {

            hm.put("fromdate", from.toString());
            hm.put("todate", to.toString());
            new ReportView("/report/IncomeReport.jasper", hm);

        } else {
            new ReportView("/report/IncomeReportAll.jasper");
        }
        
    }

    @FXML
    private void dtpFromAP(ActionEvent event) {
    }

    @FXML
    private void dtpToAP(ActionEvent event) {
    }
    
}
