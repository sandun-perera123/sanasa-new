/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.AccountDao;
import dao.AccountPaymentDao;
import dao.AccountpaymenttypeDao;
import dao.DaoException;
import dao.LoanDao;
import dao.LoanpaymentDao;
import dao.MemberDao;
import dao.SharegainDao;
import dao.SharetypeDao;
import entity.Account;
import entity.Accountpayment;
import entity.Accounttype;
import entity.Loan;
import entity.Loanpayment;
import entity.Loantype;
import entity.Share;
import entity.Sharegain;
import entity.Smember;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import static ui.LoginController.user;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class IncomeController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private TextField txtShares;
    @FXML
    private TextField txtLoanAmount;
    @FXML
    private TextField txtInterest;
    @FXML
    private TextField txtAccountAmount;
    @FXML
    private Button btnLoanAdd;
    @FXML
    private Button btnAccountAdd;
    @FXML
    private TableView<Loanpayment> tblLoan;
    @FXML
    private TableColumn<Loanpayment, Loantype> colLoanType;
    @FXML
    private TableColumn<Loanpayment, BigDecimal> colLoanAmount;
    @FXML
    private TableColumn<Loanpayment, BigDecimal> colInterest;
    @FXML
    private TableView<Accountpayment> tblAccount;
    @FXML
    private TableColumn<Accountpayment, Accounttype> colAccounttype;
    @FXML
    private TableColumn<Accountpayment, Account> colAccount;
    @FXML
    private TableColumn<Accountpayment, BigDecimal> colAccountAmount;
    @FXML
    private ComboBox<Smember> cmbMember;
    @FXML
    private TextField txtMemberID;
    @FXML
    private ComboBox<Loan> cmbLoan;
    @FXML
    private ComboBox<Account> cmbAccount;
    @FXML
    private DatePicker dtpDate;
    @FXML
    private Pane pneBasicForm;
    @FXML
    private Pane pneLoanForm;
    @FXML
    private Pane pneAccountPane;
    @FXML
    private Button btnConfirm;
    @FXML
    private TextField txtVoucherNo;
    @FXML
    private ImageView imgMemberSearch;
    @FXML
    private Button btnSave;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Module-Data">
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Sharegain sharegain;
    private Loanpayment loanpayment;
    private Accountpayment accountpayment;

    private Smember globleMember;
    private Date globleDate;
    private String globleVoucherNo;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Initializing-Methods">
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        loadForm();

        //Initialize Loan Table
        colLoanType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Loanpayment, Loantype>, ObservableValue<Loantype>>() {
            @Override
            public ObservableValue<Loantype> call(TableColumn.CellDataFeatures<Loanpayment, Loantype> param) {
                return new SimpleObjectProperty(param.getValue().getLoanId().getLoantypeId());
            }
        });

        colLoanAmount.setCellValueFactory(new PropertyValueFactory("installment"));
        colInterest.setCellValueFactory(new PropertyValueFactory("interest"));

        //Initialize Account Table
        colAccounttype.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accountpayment, Accounttype>, ObservableValue<Accounttype>>() {
            @Override
            public ObservableValue<Accounttype> call(TableColumn.CellDataFeatures<Accountpayment, Accounttype> param) {

                return new SimpleObjectProperty(param.getValue().getAccountId().getAccounttypeId());

            }
        });
        colAccount.setCellValueFactory(new PropertyValueFactory("accountId"));
        colAccountAmount.setCellValueFactory(new PropertyValueFactory("amount"));

    }

    private void loadForm() {

        pneBasicForm.setDisable(false);

        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        tblLoan.getItems().clear();
        tblAccount.getItems().clear();

        loadBasicForm();
        loadLoanForm();
        loadAccountForm();

        pneAccountPane.setDisable(true);
        pneLoanForm.setDisable(true);

        // setStyle(initial);
    }

    private void loadBasicForm() {

        cmbMember.setItems(MemberDao.getAll());
        cmbMember.getSelectionModel().select(-1);
        cmbMember.getEditor().setStyle(initial);

        dtpDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
        globleDate = java.sql.Date.valueOf(dtpDate.getValue());
        dtpDate.getEditor().setStyle(valid);

        txtMemberID.setText("");
        txtShares.setText("");
        txtVoucherNo.setText("");

        txtMemberID.setStyle(initial);
        txtShares.setStyle(initial);
        txtVoucherNo.setStyle(initial);

    }

    private void loadLoanForm() {

        loanpayment = new Loanpayment();

        cmbLoan.getSelectionModel().select(-1);

        loanpayment.setDate(globleDate);

        txtLoanAmount.setText("");
        txtInterest.setText("");

        cmbLoan.getEditor().setStyle(initial);
        txtLoanAmount.setStyle(initial);
        txtInterest.setStyle(initial);

    }

    private void loadAccountForm() {

        accountpayment = new Accountpayment();

        cmbAccount.getSelectionModel().select(-1);

        accountpayment.setDate(globleDate);
        accountpayment.setEmployeeId(user.getEmployeeId());

        txtAccountAmount.setText("");
        
        cmbAccount.getEditor().setStyle(initial);
        txtAccountAmount.setStyle(initial);

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Data-Setting-Methods">
    @FXML
    private void cmbMemberAP(ActionEvent event) {

        Smember member = cmbMember.getSelectionModel().getSelectedItem();

        if (member != null) {
            cmbMember.setStyle(valid);
            globleMember = member;
            txtMemberID.setText(member.getMemberid());
        } else {
            cmbMember.setStyle(initial);
            txtMemberID.setText("");
            globleMember = null;
        }

    }

    @FXML
    private void txtSharesKR(KeyEvent event) {

        String sharevalue = txtShares.getText().trim();

        if (sharevalue != null && sharevalue.matches("^(?!^0\\.00$)(([1-9][\\d]{0,10})|([0]))\\.[\\d]{2}$")) {

            BigDecimal sharevaluebdecimal = new BigDecimal(sharevalue);
            BigDecimal count = sharevaluebdecimal.divide(new BigDecimal("100.0"));

            sharegain = new Sharegain();
            List<Share> shares = new ArrayList();
            sharegain.setShareList(shares);

            Share share = new Share();
            share.setCount(count.intValue());
            share.setLinetotal(sharevaluebdecimal);
            share.setSharegainId(sharegain);
            share.setSharetypeId(SharetypeDao.getByID(1));

            sharegain.setDate(globleDate);
            sharegain.setEmployeeId(user.getEmployeeId());
            sharegain.setSmemberId(globleMember);
            sharegain.setTotalcount(share.getCount());
            sharegain.setTotalvalue(share.getLinetotal());
            sharegain.setVoucherNo(globleVoucherNo);
            sharegain.getShareList().add(share);

            txtShares.setStyle(valid);

        } else {
            txtShares.setStyle(invalid);
            sharegain = null;
        }

    }

    @FXML
    private void dtpDateAP(ActionEvent event) {

        LocalDate date = dtpDate.getValue();

        if (date != null) {
            dtpDate.getEditor().setStyle(valid);
            globleDate = java.sql.Date.valueOf(date);
            dtpDate.setValue(date);
        } else {
            dtpDate.getEditor().setStyle(initial);
            dtpDate.setValue(null);
            globleDate = null;
        }

    }

    @FXML
    private void txtVoucherNoKR(KeyEvent event) {

        String voucherNo = txtVoucherNo.getText().trim();

        if (voucherNo != null) {
            txtVoucherNo.setStyle(valid);
            globleVoucherNo = voucherNo;
        } else {
            txtVoucherNo.setStyle(initial);
            globleVoucherNo = voucherNo;
        }

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

    //For Loan Form
    @FXML
    private void cmbLoanAP(ActionEvent event) {

        if (cmbLoan.getSelectionModel().getSelectedItem() != null) {
            Loan loan = cmbLoan.getSelectionModel().getSelectedItem();
            loanpayment.setLoanId(loan);
            cmbLoan.setStyle(valid);
        }

    }

    @FXML
    private void txtLoanAmountKR(KeyEvent event) {

        String loanamount = txtLoanAmount.getText().trim();

        if (loanamount != null && loanamount.matches("^(?!^0\\.00$)(([1-9][\\d]{0,10})|([0]))\\.[\\d]{2}$")) {

            BigDecimal loanamountbigdecimal = new BigDecimal(loanamount);
            loanpayment.setInstallment(loanamountbigdecimal);
            txtLoanAmount.setStyle(valid);
        } else {
            txtLoanAmount.setStyle(invalid);
            loanpayment.setInstallment(null);
        }

    }

    @FXML
    private void txtInterestKR(KeyEvent event) {

        String interest = txtInterest.getText().trim();

        if (interest != null && interest.matches("^(?!^0\\.00$)(([1-9][\\d]{0,10})|([0]))\\.[\\d]{2}$")) {

            BigDecimal interestbigdecimal = new BigDecimal(interest);
            loanpayment.setInterest(interestbigdecimal);
            txtInterest.setStyle(valid);
        } else {
            loanpayment.setInterest(null);
            txtInterest.setStyle(invalid);
        }

    }
    
    @FXML
    private void txtAccountAmountKR(KeyEvent event) {
        
        String amount = txtAccountAmount.getText().trim();

        if (amount != null && amount.matches("^(?!^0\\.00$)(([1-9][\\d]{0,10})|([0]))\\.[\\d]{2}$")) {

            BigDecimal amountbigdecimal = new BigDecimal(amount);
            accountpayment.setAmount(amountbigdecimal);
            txtAccountAmount.setStyle(valid);
        } else {
            txtAccountAmount.setStyle(invalid);
            accountpayment.setAmount(null);
        }
        
    }

    @FXML
    private void btnAccountAddAP(ActionEvent event) {
        
        if (accountpayment != null) {

            if (accountpayment.getAccountId()== null || accountpayment.getAmount()== null || accountpayment.getDate()== null) {
                JOptionPane.showMessageDialog(null, "Some fields are not filled !");
            } else {
                accountpayment.setVoucherNo(globleVoucherNo);
                accountpayment.setAccountpaymenttypeId(AccountpaymenttypeDao.getByID(1));
                tblAccount.getItems().add(accountpayment);
                loadAccountForm();
            }

        }
        
    }

    @FXML
    private void cmbAccountAP(ActionEvent event) {
        
        if (cmbAccount.getSelectionModel().getSelectedItem() != null) {
            Account account = cmbAccount.getSelectionModel().getSelectedItem();
            accountpayment.setAccountId(account);
            cmbAccount.setStyle(valid);
        }
        
    }

//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Form-Operation-Methods">
    @FXML
    private void btnConfirmAP(ActionEvent event) throws DaoException {

        boolean isMemberSelected = cmbMember.getSelectionModel().getSelectedItem() != null;
        boolean isDateSelected = dtpDate.getValue() != null;
        boolean isVoucherNoEntered = !txtVoucherNo.getText().isEmpty();

        if (isMemberSelected && isDateSelected && isVoucherNoEntered) {

            System.out.println("****************");
            System.out.println(globleMember.getName());
            System.out.println(globleDate);
            System.out.println(globleVoucherNo);
            System.out.println("****************");

            pneBasicForm.setDisable(true);
            pneLoanForm.setDisable(false);
            pneAccountPane.setDisable(false);

            ObservableList<Loan> loanlist = LoanDao.getAllByMember(globleMember);
            cmbLoan.setItems(loanlist);
            
            ObservableList<Account> accountlist = AccountDao.getAllByMember(globleMember);
            cmbAccount.setItems(accountlist);

        } else {

            cmbLoan.getItems().clear();

            btnConfirm.setDisable(false);
            pneLoanForm.setDisable(true);
            pneAccountPane.setDisable(true);

            JOptionPane.showMessageDialog(null, "Some fields are not entered !");
        }

    }

    @FXML
    private void btnLoanAddAP(ActionEvent event) {
        if (loanpayment != null) {

            if (loanpayment.getLoanId() == null || loanpayment.getInstallment() == null || loanpayment.getInterest() == null) {
                JOptionPane.showMessageDialog(null, "Some fields are not filled !");
            } else {
                loanpayment.setVoucherNo(globleVoucherNo);
                tblLoan.getItems().add(loanpayment);
                loadLoanForm();
            }

        }
    }
    
    @FXML
    private void btnSaveAP(ActionEvent event) throws DaoException {

        if (sharegain != null || loanpayment != null || accountpayment != null) {

            saveSharegain();
            saveLoanpayment();
            saveAccountpayment();
            
            loadForm();

        }else{
            JOptionPane.showMessageDialog(null, "Can't save. Some details not filled !");
        }

    }
    
    
    private void saveSharegain() throws DaoException {

        if (sharegain != null && sharegain.getShareList().size() > 0) {
            SharegainDao.add(sharegain);
            System.out.println("Sharegain saved !");
        }

    }

    private void saveLoanpayment() throws DaoException {

        if (tblLoan.getItems().size() > 0) {
            for (Loanpayment lp : tblLoan.getItems()) {
                LoanpaymentDao.add(lp);
            }

            System.out.println("loanpayments saved !");
        }

    }
    
    private void saveAccountpayment() throws DaoException{
        if (tblAccount.getItems().size() > 0) {
            for (Accountpayment ap : tblAccount.getItems()) {
                AccountPaymentDao.add(ap);
            }

            System.out.println("accountpayments saved !");
        }
    }

//</editor-fold>
   

}
