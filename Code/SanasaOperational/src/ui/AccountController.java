/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.AccountDao;
import dao.AccountStatusDao;
import dao.AccountTypeDao;
import dao.DaoException;
import dao.MemberDao;
import entity.Account;
import entity.Accountstatus;
import entity.Accounttype;
import entity.Smember;
import java.io.IOException;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import static ui.LoginController.privilage;
import util.CustomAlerts;

/**
 * FXML Controller class
 *
 * @author Sandun
 */
public class AccountController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private ComboBox<Smember> cmbMember;
    @FXML
    private ComboBox<Accounttype> cmbAccountType;
    @FXML
    private TextField txtAccountNo;
    @FXML
    private ComboBox<Accountstatus> cmbAccountStatus;
    @FXML
    private DatePicker dtpCreatedDate;
    @FXML
    private DatePicker dtpClosingDate;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;
    @FXML
    private TextField txtSearchAccountNo;
    @FXML
    private ComboBox<Accountstatus> cmbSearchAccountStatus;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Account> tblAccount;
    @FXML
    private TableColumn<Account, String> colName;
    @FXML
    private TableColumn<Account, Integer> colAccountName;
    @FXML
    private TableColumn<Account, Accountstatus> colStatus;
    @FXML
    private Button btnSearchClear;
    @FXML
    private ComboBox<Smember> cmbSearchMember;
    @FXML
    private ImageView imgMemberSearch;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Module-Data">
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Account account;

    //Update Identification
    private Account oldAccount;

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

        account = new Account();
        oldAccount = null;

        cmbMember.setItems(MemberDao.getAll());
        cmbMember.getSelectionModel().select(-1);
        cmbAccountType.setItems(AccountTypeDao.getAll());
        cmbAccountType.getSelectionModel().select(-1);
        cmbAccountStatus.setItems(AccountStatusDao.getAll());
        cmbAccountStatus.getSelectionModel().select(-1);

        dtpClosingDate.setValue(null);
        dtpCreatedDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

        txtAccountNo.setText("");

        dissableButtons(false, false, true, true);
        setStyle(initial);

    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Account_insert"));
        btnUpdate.setDisable(update || !privilage.get("Account_update"));
        btnDelete.setDisable(delete || !privilage.get("Account_delete"));

        txtSearchAccountNo.setDisable(select || !privilage.get("Account_select"));
        cmbSearchMember.setDisable(select || !privilage.get("Account_select"));
        cmbSearchAccountStatus.setDisable(select || !privilage.get("Account_select"));
        btnSearchClear.setDisable(select || !privilage.get("Account_select"));

    }

    private void setStyle(String style) {
        cmbMember.setStyle(style);
        cmbAccountType.setStyle(style);
        cmbAccountStatus.setStyle(style);

        txtAccountNo.setStyle(style);

        dtpCreatedDate.getEditor().setStyle(style);
        dtpClosingDate.getEditor().setStyle(style);

    }

    private void loadTable() {

        cmbSearchAccountStatus.setItems(AccountStatusDao.getAll());
        cmbSearchAccountStatus.getSelectionModel().select(-1);
        cmbSearchMember.setItems(MemberDao.getAll());
        cmbSearchMember.getSelectionModel().select(-1);
        txtSearchAccountNo.setText("");

//        colName.setCellValueFactory(
//            new Callback<TableColumn.CellDataFeatures<Account, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Account, String> row) {
//                return new SimpleStringProperty(row.getValue().getSmemberId().getName()); 
//            }
//        }
//        
//        );
//new PropertyValueFactory("memberId"));
        colName.setCellValueFactory(new PropertyValueFactory("smemberId"));
        colStatus.setCellValueFactory(new PropertyValueFactory("accountstatusId"));
        colAccountName.setCellValueFactory(new PropertyValueFactory("no"));

        fillTable(AccountDao.getAll());
        pagination.setCurrentPageIndex(0);

    }

    private void fillTable(ObservableList<Account> accounts) {

        if (privilage.get("Account_select") && accounts != null && accounts.size() != 0) {

            int rowsCount = 4;
            int pageCount = ((accounts.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? accounts.size() : pageIndex * rowsCount + rowsCount;
                    tblAccount.getItems().clear();
                    tblAccount.setItems(FXCollections.observableArrayList(accounts.subList(start, end)));
                    return tblAccount;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblAccount.getItems().clear();
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Data-Setting-Methods">
    @FXML
    private void cmbMemberAP(ActionEvent event) {

        account.setSmemberId(cmbMember.getSelectionModel().getSelectedItem());
        if (oldAccount != null && !account.getSmemberId().equals(oldAccount.getSmemberId())) {
            cmbMember.setStyle(updated);
        } else {
            cmbMember.setStyle(valid);
        }

    }

    @FXML
    private void cmbAccountTypeAP(ActionEvent event) {

        account.setAccounttypeId(cmbAccountType.getSelectionModel().getSelectedItem());
        if (oldAccount != null && !account.getAccounttypeId().equals(oldAccount.getAccounttypeId())) {
            cmbAccountType.setStyle(updated);
        } else {
            cmbAccountType.setStyle(valid);
        }

    }

    @FXML
    private void txtAccountNoKR(KeyEvent event) {

        String accNo = txtAccountNo.getText().trim();

        if (accNo.matches("\\d{4}") && account.setNo(Integer.valueOf(accNo))) {
            if (oldAccount != null && !account.getNo().equals(oldAccount.getNo())) {
                txtAccountNo.setStyle(updated);
            } else {
                txtAccountNo.setStyle(valid);
            }
        } else {
            txtAccountNo.setStyle(invalid);
        }

    }

    @FXML
    private void cmbAccountStatusAP(ActionEvent event) {

        account.setAccountstatusId(cmbAccountStatus.getSelectionModel().getSelectedItem());
        if (oldAccount != null && !account.getAccountstatusId().equals(oldAccount.getAccountstatusId())) {
            cmbAccountStatus.setStyle(updated);
        } else {
            cmbAccountStatus.setStyle(valid);
        }

    }

    @FXML
    private void dtpCreatedDateAP(ActionEvent event) {

        if (dtpCreatedDate.getValue() != null) {
            Date today = new Date();
            Date createdDate = java.sql.Date.valueOf(dtpCreatedDate.getValue());

            if (createdDate.before(today)) {
                account.setDocreate(createdDate);
                if (oldAccount != null && !account.getDocreate().equals(oldAccount.getDocreate())) {
                    dtpCreatedDate.getEditor().setStyle(updated);
                } else {
                    dtpCreatedDate.getEditor().setStyle(valid);
                }
            } else {
                dtpCreatedDate.getEditor().setStyle(invalid);
                account.setDocreate(null);
            }
        }

    }

    @FXML
    private void dtpClosingDateAP(ActionEvent event) {

        if (dtpClosingDate.getValue() != null) {
            Date today = new Date();
            Date closingDate = java.sql.Date.valueOf(dtpClosingDate.getValue());

            if (closingDate.before(today) && closingDate.after(java.sql.Date.valueOf(dtpCreatedDate.getValue()))) {
                account.setDoclosing(closingDate);
                if (oldAccount != null && !account.getDoclosing().equals(oldAccount.getDoclosing())) {
                    dtpClosingDate.getEditor().setStyle(updated);
                } else {
                    dtpClosingDate.getEditor().setStyle(valid);
                }
            } else {
                dtpClosingDate.getEditor().setStyle(invalid);
                account.setDoclosing(null);
            }
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Form-Operation-Methods">
    private String getErrors() {

        String errors = "";

        if (account.getSmemberId() == null) {
            errors = errors + "Member Is Invalid !\n";
        }
        if (account.getAccounttypeId() == null) {
            errors = errors + "Account Type Is Invalid !\n";
        }
        if (account.getNo() == null) {
            errors = errors + "Account No Is Invalid !\n";
        }

        if (account.getAccountstatusId() == null) {
            errors = errors + "Account Status Is Invalid !\n";
        }

        if (account.getDocreate() == null) {
            errors = errors + "Created Date Is Invalid !\n";
        }

//        if (account.getDoclosing() == null) {
//            errors = errors + "Closing Date Is Invalid !\n";
//        }
        return errors;

    }

    private String getUpdates() {

        String updates = "";

        if (oldAccount != null) {

            if (!account.getSmemberId().equals(oldAccount.getSmemberId())) {
                updates = updates + oldAccount.getSmemberId() + " chnaged to " + account.getSmemberId() + "\n";
            }

            if (!account.getAccounttypeId().equals(oldAccount.getAccounttypeId())) {
                updates = updates + oldAccount.getAccounttypeId() + " chnaged to " + account.getAccounttypeId() + "\n";
            }

            if (!account.getNo().equals(oldAccount.getNo())) {
                updates = updates + oldAccount.getNo() + " chnaged to " + account.getNo() + "\n";
            }

            if (!account.getAccountstatusId().equals(oldAccount.getAccountstatusId())) {
                updates = updates + oldAccount.getAccountstatusId() + " chnaged to " + account.getAccountstatusId() + "\n";
            }

            if (!account.getDocreate().equals(oldAccount.getDocreate())) {
                updates = updates + oldAccount.getDocreate() + " chnaged to " + account.getDocreate() + "\n";
            }

            if (account.getDoclosing() != null && !account.getDoclosing().equals(oldAccount.getDoclosing())) {
                updates = updates + oldAccount.getDoclosing() + " chnaged to " + account.getDoclosing() + "\n";
            }

        }

        return updates;
    }

    private void fillForm() {

        if (tblAccount.getSelectionModel().getSelectedItem() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldAccount = AccountDao.getById(tblAccount.getSelectionModel().getSelectedItem().getId());
            account = AccountDao.getById(tblAccount.getSelectionModel().getSelectedItem().getId());

            cmbMember.getSelectionModel().select((Smember) account.getSmemberId());
            cmbAccountType.getSelectionModel().select((Accounttype) account.getAccounttypeId());
            cmbAccountStatus.getSelectionModel().select((Accountstatus) account.getAccountstatusId());

            txtAccountNo.setText(String.valueOf(account.getNo()));

            dtpCreatedDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(account.getDocreate())));
            dtpCreatedDate.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(account.getDocreate()));

            if (account.getDoclosing() != null) {

                dtpClosingDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(account.getDoclosing())));
                dtpClosingDate.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(account.getDoclosing()));

            } else {
                dtpClosingDate.setValue(null);
            }

            page = pagination.getCurrentPageIndex();
            row = tblAccount.getSelectionModel().getSelectedIndex();
        }

    }

    @FXML
    private void btnAddAP(ActionEvent event) {

        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "Ara you sure you need to add this Account with following details\n "
                    + "\nMember Name :         \t\t" + account.getSmemberId().getName()
                    + "\nAccount Type :       \t\t" + account.getAccounttypeId()
                    + "\nAccount No :  \t\t" + account.getNo()
                    + "\nAccount Status :      \t\t" + account.getAccountstatusId()
                    + "\nCreated Date :   \t\t" + account.getDocreate();
//                    + "\nClosing Date :       \t\t" + account.getDoclosing();
            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);

            if (action.get() == CustomAlerts.btnYes) {

                try {
                    AccountDao.add(account);
                    Notifications.create().title("Successs").text("Account " + account.getNo() + " saved.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                    loadForm();
                    updateTable();
                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblAccount.getSelectionModel().select(tblAccount.getItems().size() - 1);

                } catch (DaoException ex) {
                    Notifications.create().title("Un-Successs").text("Account " + account.getNo() + " not saved. \n Try Again.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

                }

            }

        } else {

            CustomAlerts.showAddError(errors);

        }

    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {

        if (getUpdates().isEmpty() && getErrors().isEmpty()) {

            Optional<ButtonType> action = CustomAlerts.showDeleteConfirmation();

            if (action.get() == CustomAlerts.btnYes) {

                AccountDao.delete(account);
                Notifications.create().title("Successs").text("Account " + account.getNo() + " deleted.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                loadForm();
                updateTable();
                pagination.setCurrentPageIndex(page);
                tblAccount.getSelectionModel().select(row);

            }
        } else {

            CustomAlerts.showDeleteError();
            
        }

    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {

        String errors = getErrors();

        if (errors.isEmpty()) {

            String updates = getUpdates();

            if (!updates.isEmpty()) {

                Optional<ButtonType> action = CustomAlerts.showUpdateConfirmation(updates);

                if (action.get() == CustomAlerts.btnYes) {

                    AccountDao.update(account);
                    Notifications.create().title("Successs").text("Account " + account.getNo() + " updated.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

                    updateTable();
                    loadForm();
                    pagination.setCurrentPageIndex(page);
                    tblAccount.getSelectionModel().select(row);

                }
            } else {

                CustomAlerts.showNoUpdatesError();
                
            }
        } else {

            CustomAlerts.showUpdatesError(errors);

        }

    }

    @FXML
    private void btnClearAP(ActionEvent event) {

        Optional<ButtonType> action = CustomAlerts.showClearFormConfirmation();

        if (action.get() == CustomAlerts.btnYes) {
            loadForm();
        }

    }

    @FXML
    private void tblAccountMC(MouseEvent event) {
        fillForm();
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
    
    
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Search-Methods">
    private void updateTable() {

        Smember member = cmbSearchMember.getSelectionModel().getSelectedItem();
        boolean smember = !(member == null);
        String accno = txtSearchAccountNo.getText().trim();
        boolean saccno = !accno.isEmpty();
        Accountstatus status = cmbSearchAccountStatus.getSelectionModel().getSelectedItem();
        boolean sstatus = cmbSearchAccountStatus.getSelectionModel().getSelectedIndex() != -1;

        if (!smember && !saccno && !sstatus) {
            fillTable(AccountDao.getAll());
        }
        if (smember && !saccno && !sstatus) {
            fillTable(AccountDao.getAllByMember(member));
        }
        if (!smember && saccno && !sstatus) {
            fillTable(AccountDao.getAllByAccno(Integer.valueOf(accno)));
        }
        if (!smember && !saccno && sstatus) {
            fillTable(AccountDao.getAllByAccstatus(status));
        }

    }

    @FXML
    private void cmbSearchMemberAP(ActionEvent event) {

        if (cmbSearchMember.getSelectionModel().getSelectedItem() != null) {
            txtSearchAccountNo.setText("");
            cmbSearchAccountStatus.getSelectionModel().select(-1);
            updateTable();
        }

    }

    @FXML
    private void txtSearchAccountNoKR(KeyEvent event) {

        cmbSearchAccountStatus.getSelectionModel().select(-1);
        cmbSearchMember.getSelectionModel().select(-1);

        updateTable();

    }

    @FXML
    private void cmbSearchAccountStatusAP(ActionEvent event) {

        if (cmbSearchAccountStatus.getSelectionModel().getSelectedItem() != null) {
            txtSearchAccountNo.setText("");
            cmbSearchMember.getSelectionModel().select(-1);
            updateTable();
        }

    }

    @FXML
    private void btnSearchClearAP(ActionEvent event) {

        Optional<ButtonType> action = CustomAlerts.showSearchClearConfirmation();

        if (action.get() == CustomAlerts.btnYes) {
            loadTable();
        }

    }
//</editor-fold>

    

}
