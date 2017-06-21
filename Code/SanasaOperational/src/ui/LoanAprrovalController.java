/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.LoanDao;
import dao.LoanStatusDao;
import entity.Loan;
import entity.Loanpayment;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import report.ReportView;
import static ui.LoginController.user;
import static ui.Main.stage;
import static ui.MainWindowNewController.date;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class LoanAprrovalController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private Button btnApprove;
    @FXML
    private TextField txtAmount;
    @FXML
    private TextField txtRequestedAmount;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Module-Data">
    private Loan loan;

    ResourceBundle rb;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Initializing-Methods">
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.rb = rb;
        loan = (Loan) rb.getObject("loan");

        txtRequestedAmount.setText(loan.getAmount().toString());

        List<Loanpayment> payments = new ArrayList();
        loan.setLoanpaymentList(payments);

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Form-Operations">
    private void generateReport(Loan loan) {

        HashMap hm = new HashMap();
        hm.put("loanid", loan.getId());

        new ReportView("/report/LoanPaymentsByLoanID.jasper", hm);

    }

    private void generateInstallments() {

        BigDecimal xx = loan.getInterest();
        BigDecimal yy = new BigDecimal("100.00");
        BigDecimal z = new BigDecimal("365.00");

        BigDecimal r = xx.divide(yy);
        BigDecimal q = r.divide(z,MathContext.DECIMAL128).multiply(new BigDecimal("30.00"));

        System.out.println(r);
        System.out.println(q);

        //BigDecimal monthlyinstallment = loan.getInterest().divide(new BigDecimal("100.00"),2, RoundingMode.DOWN).divide(new BigDecimal("12.00"),2, RoundingMode.DOWN);
        BigDecimal monthlyinstallment = q;
        BigDecimal total = loan.getApprovedamount();
        BigDecimal monthscount = new BigDecimal(loan.getDuration());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, loan.getDopay());

        BigDecimal x = total.divide(monthscount,RoundingMode.DOWN);

        for (int y = 0; y < loan.getDuration(); y++) {

            Loanpayment payment = new Loanpayment();

            payment.setLoanId(loan);
            payment.setInsno(y + 1);

            payment.setDopayment(cal.getTime());
            cal.add(Calendar.MONTH, 1);

            BigDecimal ints = (total.subtract(x.multiply(new BigDecimal(y)))).multiply(monthlyinstallment);
            payment.setInterest(ints.setScale(2,RoundingMode.DOWN));

            payment.setInstallment(x.setScale(2,RoundingMode.DOWN));

            BigDecimal installment = x.add(ints);
            payment.setTotalAmount(installment.setScale(2,RoundingMode.DOWN));

            loan.getLoanpaymentList().add(payment);

            System.out.println("******************");
            System.out.println("Installment No : " + payment.getInsno());
            System.out.println("Payment Date : " + payment.getDopayment());
            System.out.println("Installment : " + payment.getInstallment());
            System.out.println("Interest : " + payment.getInterest());
            System.out.println("Total : " + payment.getTotalAmount());

        }

    }

    @FXML
    private void btnApproveAP(ActionEvent event) {

        if (txtAmount.getStyle().contains(Style.valid)) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ණය අනුමත කිරීම");
            alert.setHeaderText(" ");
            alert.setContentText("ණය ඉල්ලීම අනුමත කිරීමට අවශ්‍යද?");

            ButtonType btnYes = new ButtonType("ඔව්", ButtonBar.ButtonData.YES);
            ButtonType btnNo = new ButtonType("නැත", ButtonBar.ButtonData.NO);

            alert.getButtonTypes().setAll(btnNo, btnYes);

            Optional<ButtonType> action = alert.showAndWait();

            if (action.get() == btnYes) {

                System.out.println("today " + date);
                loan.setDoapproved(date);

                loan.setLoanstatusId(LoanStatusDao.getById(1));
                loan.setApprovedamount(new BigDecimal(txtAmount.getText().trim()));
                loan.setEmployeeApprovedId(user.getEmployeeId());

                //generateInstallments();

                double sum = loan.getLoanpaymentList().stream()
                        .mapToDouble(o -> o.getTotalAmount().doubleValue())
                        .sum();
                loan.setBalance(BigDecimal.valueOf(sum));

                LoanDao.update(loan);

                //generateReport(loan);

                Notifications.create().owner(stage).title("සාර්ථකයි !").text("ණය අනුමත කරන ලදී.")
                        .position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(2.0)).showInformation();

                Stage stg = (Stage) btnApprove.getScene().getWindow();
                stg.close();

            }
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("අසාර්ථකයි !");
            alert.setHeaderText(" ");
            alert.setContentText("ණය අනුමත ප්‍රමාණය වැරදියි !");

            ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);

            alert.getButtonTypes().setAll(btnOk);
            alert.show();

        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Data-Setting-Methods">
    @FXML
    private void txtAmountKR(KeyEvent event) {

        String amount = txtAmount.getText().trim();

        if (!amount.isEmpty() && amount.matches("^(?!^0\\.00$)(([1-9][\\d]{0,6})|([0]))\\.[\\d]{2}$")) {

            boolean validity = false;
            BigDecimal amt = new BigDecimal(amount);

            boolean a = amt.compareTo(new BigDecimal("0.00")) == 1;
            boolean b = amt.compareTo(loan.getAmount()) == 0 || amt.compareTo(loan.getAmount()) == -1;

            validity = a & b;

            if (validity) {
                txtAmount.setStyle(Style.valid);
            } else {
                txtAmount.setStyle(Style.invalid);
            }

        } else {
            txtAmount.setStyle(Style.invalid);
        }

    }
//</editor-fold>

}
