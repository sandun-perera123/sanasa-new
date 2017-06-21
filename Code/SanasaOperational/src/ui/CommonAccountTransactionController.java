/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.CommonaccountDao;
import dao.CommonaccountcategoryDao;
import dao.CommonaccountsubcategoryDao;
import dao.CommonaccounttransactionDao;
import dao.CommonaccounttransactiontypeDao;
import entity.Commonaccount;
import entity.Commonaccountcategory;
import entity.Commonaccountsubcategory;
import entity.Commonaccounttransaction;
import entity.Commonaccounttransactiontype;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import static ui.LoginController.privilage;
import static ui.LoginController.user;
import util.CustomAlerts;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class CommonAccountTransactionController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private ComboBox<Commonaccountcategory> cmbMainCategory;
    @FXML
    private ComboBox<Commonaccountsubcategory> cmbSubCategory;
    @FXML
    private ComboBox<Commonaccount> cmbAccount;
    @FXML
    private ComboBox<Commonaccounttransactiontype> cmbTransactionType;
    @FXML
    private TextField txtBalance;
    @FXML
    private DatePicker dtpDate;
    @FXML
    private Pagination pagination;
    @FXML
    private TextField txtAmount;
    @FXML
    private TextField txtNewBalance;
    @FXML
    private TextArea txaRemarks;
    @FXML
    private TableView<Commonaccounttransaction> tblTransaction;
    @FXML
    private TableColumn<Commonaccounttransaction, Commonaccount> colAccount;
    @FXML
    private TableColumn<Commonaccounttransaction, Date> colDate;
    @FXML
    private TableColumn<Commonaccounttransaction, Commonaccounttransactiontype> colType;
    @FXML
    private TableColumn<Commonaccounttransaction, BigDecimal> colAmount;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
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
    private Commonaccounttransaction transaction;

    //Update Identification
    private Commonaccounttransaction oldTransaction;

    BigDecimal oldBalance;

    private Commonaccount account;

    //Table Row, Page Selected
    private int page;
    private int row;
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

        account = new Commonaccount();
        transaction = new Commonaccounttransaction();
        oldTransaction = null;

        List<Commonaccounttransaction> transactions = new ArrayList();
        account.setCommonaccounttransactionList(transactions);

        cmbMainCategory.setItems(CommonaccountcategoryDao.getAll());
        cmbMainCategory.getSelectionModel().select(-1);
        cmbSubCategory.setItems(CommonaccountsubcategoryDao.getAll());
        cmbSubCategory.getSelectionModel().select(-1);
        cmbTransactionType.setItems(CommonaccounttransactiontypeDao.getAll());
        cmbTransactionType.getSelectionModel().select(-1);

        txtBalance.setText("");
        txtAmount.setText("");
        txtNewBalance.setText("");
        txaRemarks.setText("");

        dtpDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

        dissableButtons(false, false, true, true);
        setStyle(initial);

    }

    private void loadTable() {

        colAccount.setCellValueFactory(new PropertyValueFactory("commonaccountId"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));
        colType.setCellValueFactory(new PropertyValueFactory("commonaccounttransactiontypeId"));
        colAmount.setCellValueFactory(new PropertyValueFactory("amount"));

        fillTable(CommonaccounttransactionDao.getAll());
        pagination.setCurrentPageIndex(0);

    }

    private void fillTable(ObservableList<Commonaccounttransaction> transactions) {

        if (privilage.get("Commonaccounttransaction_select") && transactions != null && transactions.size() != 0) {

            int rowsCount = 15;
            int pageCount = ((transactions.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? transactions.size() : pageIndex * rowsCount + rowsCount;
                    tblTransaction.getItems().clear();
                    tblTransaction.setItems(FXCollections.observableArrayList(transactions.subList(start, end)));
                    return tblTransaction;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblTransaction.getItems().clear();
        }

    }

    private void setStyle(String style) {

        cmbMainCategory.setStyle(style);
        cmbSubCategory.setStyle(style);
        cmbAccount.setStyle(style);
        cmbTransactionType.setStyle(style);

        txtBalance.setStyle(style);
        txtAmount.setStyle(style);
        txtNewBalance.setStyle(style);

        if (!txaRemarks.getChildrenUnmodifiable().isEmpty()) {
            ((ScrollPane) txaRemarks.getChildrenUnmodifiable().get(0)).getContent().setStyle(style);
        }

        dtpDate.getEditor().setStyle(style);

    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {

        btnAdd.setDisable(insert || !privilage.get("Commonaccounttransaction_insert"));
        btnDelete.setDisable(delete || !privilage.get("Commonaccounttransaction_delete"));

    }
//</editor-fold>
 
//<editor-fold defaultstate="collapsed" desc="Data-Setting-Methods">
    
    //get the the Account object by Main Category and Sub Category
    private void getAccount() {
        
        if (cmbMainCategory.getSelectionModel().getSelectedItem() != null && cmbSubCategory.getSelectionModel().getSelectedItem() != null) {
            
            Commonaccountcategory main = cmbMainCategory.getSelectionModel().getSelectedItem();
            Commonaccountsubcategory sub = cmbSubCategory.getSelectionModel().getSelectedItem();
            
            cmbAccount.setItems(CommonaccountDao.getAllByMainSub(main, sub));
            
        }
        
    }
    
    private String getErrors() {
        
        String errors = "";
        
        if (account == null) {
            errors = errors + "ගිණුම තෝරා නොමැත.\n";
        }
        
        if (transaction.getDate() == null) {
            errors = errors + "දිනය තෝරා නොමැත.\n";
        }
        
        if (transaction.getCommonaccounttransactiontypeId() == null) {
            errors = errors + "හර / බැර තෝරා නොමැත\n";
        }
        
        if (transaction.getAmount() == null) {
            errors = errors + "මුදල ඇතුළත් කර නොමැත.\n";
        }
        
        return errors;
        
    }
    
    //AP for Main Category
    @FXML
    private void cmbMainCategoryAP(ActionEvent event) {
        
        if (cmbMainCategory.getSelectionModel().getSelectedItem() != null) {
            ObservableList<Commonaccountsubcategory> observableArrayList = FXCollections.observableArrayList(cmbMainCategory.getSelectionModel().getSelectedItem().getCommonaccountsubcategoryList());
            cmbSubCategory.setItems(observableArrayList);
            getAccount();
            cmbMainCategory.setStyle(valid);
        }
        
    }
    
    @FXML
    private void cmbSubCategoryAP(ActionEvent event) {
        
        if (cmbSubCategory.getSelectionModel().getSelectedItem() != null) {
            getAccount();
            cmbSubCategory.setStyle(valid);
        }
        
    }
    
    @FXML
    private void cmbAccountAP(ActionEvent event) {
        
        if (cmbAccount.getSelectionModel().getSelectedItem() != null) {
            
            account = cmbAccount.getSelectionModel().getSelectedItem();
            transaction.setCommonaccountId(account);
            if (oldTransaction != null && !account.equals(oldTransaction.getCommonaccountId())) {
                cmbAccount.setStyle(updated);
                oldBalance = account.getBalance() == null ? new BigDecimal("0.00") : account.getBalance();
            } else {
                cmbAccount.setStyle(valid);
                oldBalance = account.getBalance() == null ? new BigDecimal("0.00") : account.getBalance();
            }
            
            txtBalance.setText(account.getBalance().toString());
            
            generateNewBalance("");
            txtAmount.setText("");
            
        }
        
    }
    
    @FXML
    private void cmbTransactionTypeAP(ActionEvent event) {
        
        transaction.setCommonaccounttransactiontypeId(cmbTransactionType.getSelectionModel().getSelectedItem());
        if (oldTransaction != null && !transaction.getCommonaccounttransactiontypeId().equals(oldTransaction.getCommonaccounttransactiontypeId())) {
            cmbTransactionType.setStyle(updated);
        } else {
            cmbTransactionType.setStyle(valid);
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
    
    private boolean generateNewBalance(String amount) {
        
        if (!amount.isEmpty() && amount.matches("^(?!^0\\.00$)(([1-9][\\d]{0,9})|([0]))\\.[\\d]{2}$") && cmbTransactionType.getSelectionModel().getSelectedItem() != null) {
            
            BigDecimal availableBalance = account.getBalance();
            Commonaccounttransactiontype accountpaymenttype = cmbTransactionType.getSelectionModel().getSelectedItem();
            Commonaccountcategory commonaccountcategoryId = account.getCommonaccountsubcategoryId().getCommonaccountcategoryId();
            
            if (accountpaymenttype.getId() == 1) {
                
                if(commonaccountcategoryId.getId() == 2 | commonaccountcategoryId.getId() == 3 | commonaccountcategoryId.getId() == 4){
                    return increase(amount);
                }else{
                    return decrease(amount);
                }
                
            } else if (accountpaymenttype.getId() == 2) {
                
                if(commonaccountcategoryId.getId() == 1 | commonaccountcategoryId.getId() == 5){
                    return increase(amount);
                }else{
                    return decrease(amount);
                }
            }
        } else {
            
            account.setBalance(oldBalance);
            txtNewBalance.setText(account.getBalance().toString());
            return false;
        }
        
        return false;
    }
    
    //Supportive method for generateNewBalance()
    //This will decrese the curent balance
    private boolean decrease(String amount) {
        
        BigDecimal amountDecimal = new BigDecimal(amount);
        transaction.setAmount(amountDecimal);
        
        if (amountDecimal.compareTo(oldBalance) == -1 || amountDecimal.compareTo(oldBalance) == 0) {
            
            account.setBalance(account.getBalance().subtract(amountDecimal));
            
            txtNewBalance.setText(account.getBalance().toString());
            
            return true;
            
        } else {
            
            account.setBalance(oldBalance);
            txtNewBalance.setText(account.getBalance().toString());
            
            return false;
            
        }
    }
    
    //Supportive method for generateNewBalance()
    //This will increase the curent balance
    private boolean increase(String amount) {
        BigDecimal amountDecimal = new BigDecimal(amount);
        transaction.setAmount(amountDecimal);
        
        account.setBalance(account.getBalance().add(amountDecimal));
        
        txtNewBalance.setText(account.getBalance().toString());
        
        return true;
    }
    
    @FXML
    private void dtpDateAP(ActionEvent event) {
        
        if (dtpDate.getValue() != null) {
            Date assign = java.sql.Date.valueOf(dtpDate.getValue());
            transaction.setDate(assign.toString());
            if (oldTransaction != null && !transaction.getDate().equals(oldTransaction.getDate())) {
                dtpDate.getEditor().setStyle(updated);
            } else {
                dtpDate.getEditor().setStyle(valid);
            }
            
        } else {
            dtpDate.getEditor().setStyle(invalid);
        }
        
    }
    
    @FXML
    private void txaRemarksKR(KeyEvent event) {
        
        if (transaction.setRemarks(txaRemarks.getText())) {
            if (oldTransaction != null && oldTransaction.getRemarks() != null && transaction.getRemarks() != null && oldTransaction.getRemarks().equals(transaction.getRemarks())) {
                txaRemarks.setStyle(valid);
            } else if (oldTransaction != null && oldTransaction.getRemarks() != transaction.getRemarks()) {
                txaRemarks.setStyle(updated);
            } else {
                txaRemarks.setStyle(valid);
            }
        } else {
            txaRemarks.setStyle(invalid);
        }
        
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Form-Operation-Methods">
    @FXML
    private void btnAddAP(ActionEvent event) {
        
        String errors = getErrors();
        
        if (errors.isEmpty()) {
            
            String confermation = "පහත තොරතුරු ඇතුළත් කිරීමට අවශ්‍යද?\n "
                    + "\nගිණුම :         \t\t" + account.getName()
                    + "\nදිනය :       \t\t" + transaction.getDate()
                    + "\nහර / බැර :  \t\t" + transaction.getCommonaccounttransactiontypeId().getName()
                    + "\nමුදල :      \t\t" + transaction.getAmount();
            
            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);
            
            if (action.get() == CustomAlerts.btnYes) {
                
                account.getCommonaccounttransactionList().add(transaction);
                transaction.setEmployeeId(user.getEmployeeId());
                
                CommonaccountDao.update(account);
                Notifications.create().title("Successs").text("Transaction " + account.getName() + " saved.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                loadForm();
                fillTable(CommonaccounttransactionDao.getAll());
                pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                tblTransaction.getSelectionModel().select(tblTransaction.getItems().size() - 1);
                
            }
            
        } else {
            
            CustomAlerts.showAddError(errors);
        }
        
    }
    
    @FXML
    private void btnDeleteAP(ActionEvent event) {
    }
    
    @FXML
    private void btnClearAP(ActionEvent event) {
    }
//</editor-fold>

}
