/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.AccountDao;
import dao.AccountPaymentDao;
import dao.AccountpaymenttypeDao;
import dao.MemberDao;
import entity.Account;
import entity.Accountpayment;
import entity.Accountpaymenttype;
import entity.Smember;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.eclipse.persistence.internal.jaxb.CustomAccessorAttributeAccessor;
import report.ReportView;
import static ui.LoginController.privilage;
import static ui.LoginController.user;
import util.CustomAlerts;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class AccountPaymentController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private Button btnSearchClear;
    @FXML
    private Pagination pagination;
    @FXML
    private TableColumn<Accountpayment, Integer> colMemberID;
    @FXML
    private ComboBox<Smember> cmbSearchMember;
    @FXML
    private ComboBox<Account> cmbSearchAccountNo;
    @FXML
    private ComboBox<Accountpaymenttype> cmbSearchAccountPaymentType;
    @FXML
    private TableView<Accountpayment> tblAccountPayment;
    @FXML
    private TableColumn<Accountpayment, Date> colDate;
    @FXML
    private TableColumn<Accountpayment, Account> colAccountNo;
    @FXML
    private TableColumn<Accountpayment, Accountpaymenttype> colAccountPaymentType;
    @FXML
    private TableColumn<Accountpayment, BigDecimal> colAmount;
    @FXML
    private DatePicker dtpSearchFrom;
    @FXML
    private DatePicker dtpSearchTo;
    @FXML
    private Button btnSearchLock;
    @FXML
    private TextField txtBillNo;
    @FXML
    private ComboBox<Smember> cmbMember;
    @FXML
    private ComboBox<Account> cmbAccount;
    @FXML
    private TextField txtAccountType;
    @FXML
    private TextField txtAccountNo;
    @FXML
    private TextField txtBalance;
    @FXML
    private TextField txtAvailableBalance;
    @FXML
    private ComboBox<Accountpaymenttype> cmbAccountPaymentType;
    @FXML
    private TextField txtAmount;
    @FXML
    private TextField txtNewBalance;
    @FXML
    private Button btnPay;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnClear;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Module-Data">
    //Color Indication of Input Controls
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Account account;

    //Update Identification
    private Account oldAccount;

    BigDecimal oldBalance, oldAvailableBalance;

    private Accountpayment accpayment;

    //Table Row, Page Selected
    private int page;
    private int row;

    //Search Lock
    private boolean lock;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Initializing-Methods">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadForm();
        loadTable();
    }

    private void loadForm() {

        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        account = new Account();
        oldAccount = null;
        accpayment = new Accountpayment();

        List<Accountpayment> accountpayments = new ArrayList();
        account.setAccountpaymentList(accountpayments);

        cmbMember.setItems(MemberDao.getAll());
        cmbMember.getSelectionModel().select(-1);
        cmbAccount.getItems().clear();
        cmbAccount.getSelectionModel().select(-1);
        cmbAccountPaymentType.setItems(AccountpaymenttypeDao.getAllExceptInterest());
        cmbAccountPaymentType.getSelectionModel().select(-1);

        cmbMember.setDisable(false);
        cmbAccount.setDisable(true);
        cmbAccountPaymentType.setDisable(true);
        txtAmount.setDisable(true);

        txtAmount.setText("");
        txtBillNo.setText("");
        txtAccountType.setText("");
        txtAccountNo.setText("");
        txtBalance.setText("");
        txtAvailableBalance.setText("");
        txtNewBalance.setText("");

        generateBillNo();
        dissableButtons(false, false, false);
        setStyle(initial);

    }

    private void loadTable() {
        lock = false;
        btnSearchLock.setText("අගුලු දමන්න");

        cmbSearchMember.setItems(MemberDao.getAll());
        cmbSearchMember.getSelectionModel().select(-1);
        cmbSearchAccountNo.getItems().clear();
        cmbSearchAccountNo.getSelectionModel().select(-1);
        cmbSearchAccountPaymentType.setItems(AccountpaymenttypeDao.getAllExceptInterest());
        cmbSearchAccountPaymentType.getSelectionModel().select(-1);

        colDate.setCellValueFactory(new PropertyValueFactory("date"));
        
        colMemberID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accountpayment, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Accountpayment, Integer> param) {

                return new SimpleObjectProperty(param.getValue().getAccountId().getSmemberId());
                
            }
        });
                
        colAccountNo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accountpayment, Account>, ObservableValue<Account>>() {
            @Override
            public ObservableValue<Account> call(TableColumn.CellDataFeatures<Accountpayment, Account> param) {

                return new SimpleObjectProperty(param.getValue().getAccountId().getNo());
                
            }
        });
        
        colAccountPaymentType.setCellValueFactory(new PropertyValueFactory("accountpaymenttypeId"));
        colAmount.setCellValueFactory(new PropertyValueFactory("amount"));

        dtpSearchFrom.setValue(null);
        dtpSearchTo.setValue(null);

        fillTable(AccountPaymentDao.getAllExceptInterest());
        pagination.setCurrentPageIndex(0);

    }

    private void dissableButtons(boolean select, boolean insert, boolean update) {

        btnPay.setDisable(insert || !privilage.get("AccountPayment_insert"));
        btnPrint.setDisable(update || !privilage.get("AccountPayment_update"));

        cmbSearchMember.setDisable(select || !privilage.get("AccountPayment_select"));
        cmbSearchAccountNo.setDisable(select || !privilage.get("AccountPayment_select"));
        cmbSearchAccountPaymentType.setDisable(select || !privilage.get("AccountPayment_select"));
        dtpSearchFrom.setDisable(select || !privilage.get("AccountPayment_select"));
        dtpSearchTo.setDisable(select || !privilage.get("AccountPayment_select"));
        btnSearchLock.setDisable(select || !privilage.get("AccountPayment_select"));
        btnSearchClear.setDisable(select || !privilage.get("AccountPayment_select"));

    }

    private void setStyle(String style) {

        cmbMember.setStyle(style);
        cmbAccount.setStyle(style);
        cmbAccountPaymentType.setStyle(style);

        txtBillNo.setStyle(style);
        txtAccountType.setStyle(style);
        txtAccountNo.setStyle(style);
        txtAmount.setStyle(style);
        txtBalance.setStyle(style);
        txtAvailableBalance.setStyle(style);
        txtNewBalance.setStyle(style);
    }

    private void fillTable(ObservableList<Accountpayment> accountspayments) {

        if (privilage.get("AccountPayment_select") && accountspayments != null && accountspayments.size() != 0) {

            int rowsCount = 4;
            int pageCount = ((accountspayments.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? accountspayments.size() : pageIndex * rowsCount + rowsCount;
                    tblAccountPayment.getItems().clear();
                    tblAccountPayment.setItems(FXCollections.observableArrayList(accountspayments.subList(start, end)));
                    return tblAccountPayment;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblAccountPayment.getItems().clear();
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Data-Settings-Methods">
    private void generateBillNo() {

        Date today = java.sql.Date.valueOf(LocalDate.now());
        Long count = AccountPaymentDao.getCountByDate(today);

        String year = String.valueOf(today.getYear() + 1900);
        String month = String.valueOf(today.getMonth() + 1);
        String date = String.valueOf(today.getDate());

        String code = year + month + date + "-" + (++count);

//        accpayment.setBillno(code);
//        txtBillNo.setText(accpayment.getBillno());

    }

    private boolean generateNewBalance(String amount) {

        if (!amount.isEmpty() && amount.matches("^(?!^0\\.00$)(([1-9][\\d]{0,9})|([0]))\\.[\\d]{2}$") && cmbAccountPaymentType.getSelectionModel().getSelectedItem() != null) {

            BigDecimal availableBalance = account.getAvailablebalance();
            Accountpaymenttype accountpaymenttype = cmbAccountPaymentType.getSelectionModel().getSelectedItem();

            if (accountpaymenttype.getId() == 1) {

                BigDecimal amountDecimal = new BigDecimal(amount);
                accpayment.setAmount(amountDecimal);

                account.setBalance(account.getBalance().add(amountDecimal));
                account.setAvailablebalance(account.getAvailablebalance().add(amountDecimal));

                txtNewBalance.setText(account.getBalance().toString());
                txtAvailableBalance.setText(account.getAvailablebalance().toString());

                return true;

            } else if (accountpaymenttype.getId() == 2) {

                BigDecimal amountDecimal = new BigDecimal(amount);
                accpayment.setAmount(amountDecimal);

                if (amountDecimal.compareTo(oldAvailableBalance) == -1 || amountDecimal.compareTo(oldAvailableBalance) == 0) {

                    account.setBalance(account.getBalance().subtract(amountDecimal));
                    account.setAvailablebalance(account.getAvailablebalance().subtract(amountDecimal));

                    txtNewBalance.setText(account.getBalance().toString());
                    txtAvailableBalance.setText(account.getAvailablebalance().toString());

                    return true;

                } else {

                    account.setBalance(oldBalance);
                    account.setAvailablebalance(oldAvailableBalance);

                    txtNewBalance.setText(account.getBalance().toString());
                    txtAvailableBalance.setText(account.getAvailablebalance().toString());

                    return false;

                }

            }

        } else {

            account.setBalance(oldBalance);
            account.setAvailablebalance(oldAvailableBalance);

            txtNewBalance.setText(account.getBalance().toString());
            txtAvailableBalance.setText(account.getAvailablebalance().toString());

            return false;

        }

        return false;

    }

    @FXML
    private void cmbMemberAP(ActionEvent event) {

        if (cmbMember.getSelectionModel().getSelectedItem() != null) {

            Smember member = (Smember) cmbMember.getSelectionModel().getSelectedItem();
            cmbAccount.setItems(FXCollections.observableArrayList(member.getAccountList()));

            cmbAccountPaymentType.getSelectionModel().select(-1);
            cmbMember.setStyle(valid);
            cmbMember.setDisable(true);
            cmbAccount.setDisable(false);

        } else {
            cmbAccount.getItems().clear();
        }

    }

    @FXML
    private void cmbAccountAP(ActionEvent event) {

        if (cmbAccount.getSelectionModel().getSelectedItem() != null) {

            account = cmbAccount.getSelectionModel().getSelectedItem();
            accpayment.setAccountId(cmbAccount.getSelectionModel().getSelectedItem());

            oldBalance = account.getBalance() == null ? new BigDecimal("0.00") : account.getBalance();
            oldAvailableBalance = account.getAvailablebalance() == null ? new BigDecimal("0.00") : account.getAvailablebalance();

            txtAccountType.setText(account.getAccounttypeId().getName());
            txtAccountNo.setText(account.getNo().toString());
               
            txtBalance.setText(account.getBalance()== null ? "0.00" : account.getBalance().toString());
            txtBalance.setText(account.getAvailablebalance()== null ? "0.00" : account.getAvailablebalance().toString());

            cmbAccount.setDisable(true);
            cmbAccountPaymentType.setDisable(false);
            cmbAccount.setStyle(valid);

            generateNewBalance("");
            txtAmount.setText("");

        }

    }

    @FXML
    private void cmbAccountPaymentTypeAP(ActionEvent event) {

        if (cmbAccountPaymentType.getSelectionModel().getSelectedItem() != null) {

            accpayment.setAccountpaymenttypeId(cmbAccountPaymentType.getSelectionModel().getSelectedItem());
//            account.getAccountpaymentList().clear();
//            account.getAccountpaymentList().add(accountpayment);
//            account.getAccountpaymentList().get(0)

            cmbAccountPaymentType.setStyle(valid);
            cmbAccountPaymentType.setDisable(true);
            txtAmount.setDisable(false);
            generateNewBalance("");
            txtAmount.setText("");
        }

    }

    @FXML
    private void txtAmountKR(KeyEvent event) {

        String amount = txtAmount.getText().trim();

        if (generateNewBalance(amount)) {
            txtAmount.setStyle(valid);
        } else {
            txtAmount.setStyle(invalid);
        }

    }
//</editor-fold>

    private String getErrors() {
        String errors = "";

        if (accpayment.getAccountId() == null) {
            errors = errors + "ගිණුම තෝරා නොමැත\n";
        }
        if (accpayment.getAccountpaymenttypeId() == null) {
            errors = errors + "ගැනීම් / තැන්පතු තෝරා නොමැත\n";
        }
        if (accpayment.getAmount() == null) {
            errors = errors + "මුදල ඇතුළත් කර නොමැත\n";
        }

        return errors;

    }

    private void generateReport(Accountpayment accountpayment) {

        HashMap hm = new HashMap();
        hm.put("accountpaymentid", accountpayment.getId());

        new ReportView("/report/AccountPaymentById.jasper",hm);

    }

    @FXML
    private void btnPayAP(ActionEvent event) {

        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "පහත තොරතුරු ඇතුළත් කිරීමට අවශ්‍යද?\n "
                    + "\nගිණුම් අංකය :         \t\t" + accpayment.getAccountId().getNo()
                    + "\nගිණුම් වර්ගය :       \t\t" + account.getAccounttypeId().getName()
                    + "\nගැනීම් / තැන්පතු :  \t\t" + accpayment.getAccountpaymenttypeId().getName()
                    + "\nමුදල :      \t\t" + accpayment.getAmount()
                    + "\nනව ශේෂය :   \t\t" + account.getBalance()
                    + "\nලබාගත හැකි ශේෂය :       \t\t" + account.getAvailablebalance();


            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);
            
            if (action.get() == CustomAlerts.btnYes) {

                accpayment.setEmployeeId(user.getEmployeeId());
                accpayment.setDate(new Date());
                account.getAccountpaymentList().add(accpayment);

                AccountDao.update(account);
                
                generateReport(accpayment);
                
                Notifications.create().title("සාර්ථකයි").text("ගිණුම් තොරතුරු යාවත්කාලීන කරන ලදී").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                loadForm();
                pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                tblAccountPayment.getSelectionModel().select(tblAccountPayment.getItems().size() - 1);

            }

        } else {

            CustomAlerts.showAddError(errors);

        }

    }

    @FXML
    private void btnPrintAP(ActionEvent event) {
        
        

    }

    @FXML
    private void tblAccountPaymentMC(MouseEvent event) {
    }

    @FXML
    private void btnSearchClearAP(ActionEvent event) {

    }

    @FXML
    private void cmbSearchMemberAP(ActionEvent event) {
    }

    @FXML
    private void cmbSearchAccountNoAP(ActionEvent event) {
    }

    @FXML
    private void cmbSearchAccountPaymentTypeAP(ActionEvent event) {
    }

    @FXML
    private void dtpSearchFromAP(ActionEvent event) {
    }

    @FXML
    private void dtpSearchToAP(ActionEvent event) {
    }

    @FXML
    private void btnSearchLockAP(ActionEvent event) {
    }

    @FXML
    private void btnClearAP(ActionEvent event) {
    }

}
