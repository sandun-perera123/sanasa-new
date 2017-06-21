/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.AccountTypeDao;
import dao.AccounttypecategoryDao;
import dao.AccounttypestatusDao;
import dao.DaoException;
import dao.EmployeeDao;
import entity.Accounttype;
import entity.Accounttypecategory;
import entity.Accounttypestatus;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import static ui.LoginController.privilage;
import util.CustomAlerts;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class AccountTypeController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private ComboBox<Accounttypecategory> cmbCategory;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtMinAmount;
    @FXML
    private TextField txtInterest;
    @FXML
    private ComboBox<Accounttypestatus> cmbStatus;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;
    @FXML
    private ComboBox<Accounttypecategory> cmbSearchCategory;
    @FXML
    private ComboBox<Accounttypestatus> cmbSearchStatus;
    @FXML
    private TableView<Accounttype> tblAccounttype;
    @FXML
    private TableColumn<Accounttype, String> colName;
    @FXML
    private TableColumn<Accounttype, Accounttypecategory> colCategory;
    @FXML
    private TableColumn<Accounttype, BigDecimal> colInterest;
    @FXML
    private Button btnSearchClear;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Module-Data">
    //Color Indication of Input Controls
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Accounttype accounttype;

    //Update Identification
    private Accounttype oldAccounttype;
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

        accounttype = new Accounttype();
        oldAccounttype = null;

        cmbCategory.setItems(AccounttypecategoryDao.getAll());
        cmbCategory.getSelectionModel().select(-1);
        cmbStatus.setItems(AccounttypestatusDao.getAll());
        cmbStatus.getSelectionModel().select(-1);

        txtName.setText("");
        txtMinAmount.setText("");
        txtInterest.setText("");

        dissableButtons(false, false, true, true);
        setStyle(initial);

    }

    private void loadTable() {

        cmbSearchCategory.setItems(AccounttypecategoryDao.getAll());
        cmbSearchCategory.getSelectionModel().select(-1);
        cmbSearchStatus.setItems(AccounttypestatusDao.getAll());
        cmbSearchStatus.getSelectionModel().select(-1);

        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory("accounttypecategoryId"));
        colInterest.setCellValueFactory(new PropertyValueFactory("interest"));

        fillTable(AccountTypeDao.getAll());

    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Accounttype_insert"));
        btnUpdate.setDisable(update || !privilage.get("Accounttype_update"));
        btnDelete.setDisable(delete || !privilage.get("Accounttype_delete"));

        cmbSearchCategory.setDisable(select || !privilage.get("Accounttype_select"));
        cmbSearchStatus.setDisable(select || !privilage.get("Accounttype_select"));
        btnSearchClear.setDisable(select || !privilage.get("Accounttype_select"));
    }

    private void setStyle(String style) {
        cmbCategory.setStyle(style);
        cmbStatus.setStyle(style);

        txtName.setStyle(style);
        txtInterest.setStyle(style);
        txtMinAmount.setStyle(style);

    }

    private void fillTable(ObservableList<Accounttype> accounttypes) {

        if (privilage.get("Accounttype_select") && accounttypes != null && accounttypes.size() != 0) {

            tblAccounttype.getItems().clear();
            tblAccounttype.setItems(FXCollections.observableArrayList(accounttypes));

        } else {

            tblAccounttype.getItems().clear();
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Data-Settings-Methods">
    @FXML
    private void cmbCategoryAP(ActionEvent event) {
        accounttype.setAccounttypecategoryId(cmbCategory.getSelectionModel().getSelectedItem());
        if (oldAccounttype != null && !accounttype.getAccounttypecategoryId().equals(oldAccounttype.getAccounttypecategoryId())) {
            cmbCategory.setStyle(updated);
        } else {
            cmbCategory.setStyle(valid);
        }
    }

    @FXML
    private void txtNameKR(KeyEvent event) {
        if (accounttype.setName(txtName.getText().trim())) {
            if (oldAccounttype != null && !accounttype.getName().equals(oldAccounttype.getName())) {
                txtName.setStyle(updated);
            } else {
                txtName.setStyle(valid);
            }
        } else {
            txtName.setStyle(invalid);
            accounttype.setName(null);
        }
    }

    @FXML
    private void txtMinAmountKR(KeyEvent event) {
        String min = txtMinAmount.getText().trim();

        if (min.matches("^(?!^0\\.00$)(([1-9][\\d]{0,6})|([0]))\\.[\\d]{2}$") && accounttype.setMin(BigDecimal.valueOf(Double.valueOf(min)))) {
            if (oldAccounttype != null && !accounttype.getMin().equals(oldAccounttype.getMin())) {
                txtMinAmount.setStyle(updated);
            } else {
                txtMinAmount.setStyle(valid);
            }
        } else {
            txtMinAmount.setStyle(invalid);
            accounttype.setMin(null);
        }
    }

    @FXML
    private void txtInterestKR(KeyEvent event) {
        String interest = txtInterest.getText().trim();

        if (interest.matches("^(?!^0\\.00$)(([1-9][\\d]{0,6})|([0]))\\.[\\d]{2}$") && accounttype.setInterest(BigDecimal.valueOf(Double.valueOf(interest)))) {
            if (oldAccounttype != null && !accounttype.getInterest().equals(oldAccounttype.getInterest())) {
                txtInterest.setStyle(updated);
            } else {
                txtInterest.setStyle(valid);
            }
        } else {
            txtInterest.setStyle(invalid);
            accounttype.setInterest(null);
        }
    }

    @FXML
    private void cmbStatusAP(ActionEvent event) {

        accounttype.setAccounttypestatusId(cmbStatus.getSelectionModel().getSelectedItem());
        if (oldAccounttype != null && !accounttype.getAccounttypestatusId().equals(oldAccounttype.getAccounttypestatusId())) {
            cmbStatus.setStyle(updated);
        } else {
            cmbStatus.setStyle(valid);
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Form-Operation-Methods">
    private String getErrors() {

        String errors = "";

        if (accounttype.getAccounttypecategoryId() == null) {
            errors = errors + "Category \t\tis Not Selected\n";
        }
        if (accounttype.getName() == null) {
            errors = errors + "Name \t\tis Invalid\n";
        }
        if (accounttype.getMin() == null) {
            errors = errors + "Min Amount \t\tis Invalid\n";
        }

        if (accounttype.getInterest() == null) {
            errors = errors + "Interest \t\tis Invalid\n";
        }

        if (accounttype.getAccounttypestatusId() == null) {
            errors = errors + "Status \t\tis Not Selected\n";
        }

        return errors;

    }

    private String getUpdates() {

        String updates = "";

        if (oldAccounttype != null) {

            if (accounttype.getName() != null && !accounttype.getName().equals(oldAccounttype.getName())) {
                updates = updates + oldAccounttype.getName() + " chnaged to " + accounttype.getName() + "\n";
            }

            if (!accounttype.getAccounttypecategoryId().equals(oldAccounttype.getAccounttypecategoryId())) {
                updates = updates + oldAccounttype.getAccounttypecategoryId() + " chnaged to " + accounttype.getAccounttypecategoryId() + "\n";
            }

            if (!accounttype.getMin().equals(oldAccounttype.getMin())) {
                updates = updates + oldAccounttype.getMin() + " chnaged to " + accounttype.getMin() + "\n";
            }

            if (!accounttype.getInterest().equals(oldAccounttype.getInterest())) {
                updates = updates + oldAccounttype.getInterest() + " chnaged to " + accounttype.getInterest() + "\n";
            }

            if (!accounttype.getAccounttypestatusId().equals(oldAccounttype.getAccounttypestatusId())) {
                updates = updates + oldAccounttype.getAccounttypestatusId() + " chnaged to " + accounttype.getAccounttypestatusId() + "\n";
            }

        }

        return updates;
    }

    private void fillForm() {

        if (tblAccounttype.getSelectionModel().getSelectedItem() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldAccounttype = AccountTypeDao.getById(tblAccounttype.getSelectionModel().getSelectedItem().getId());
            accounttype = AccountTypeDao.getById(tblAccounttype.getSelectionModel().getSelectedItem().getId());

            cmbCategory.getSelectionModel().select((Accounttypecategory) accounttype.getAccounttypecategoryId());
            cmbStatus.getSelectionModel().select((Accounttypestatus) accounttype.getAccounttypestatusId());

            txtName.setText(accounttype.getName());
            txtMinAmount.setText(accounttype.getMin().toString());
            txtInterest.setText(accounttype.getInterest().toString());

        }
    }

    @FXML
    private void btnAddAP(ActionEvent event) {

        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "Ara you sure you need to add this Accounttype with following details\n "
                    + "\nCategory :         \t\t" + accounttype.getAccounttypecategoryId()
                    + "\nName :       \t\t" + accounttype.getName()
                    + "\nMin Amount :  \t\t" + accounttype.getMin()
                    + "\nInterest :      \t\t" + accounttype.getInterest()
                    + "\nStatus :   \t\t" + accounttype.getAccounttypestatusId();

            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);
            
            if (action.get() == CustomAlerts.btnYes) {

                try {
                    AccountTypeDao.add(accounttype);
                    Notifications.create().title("Successs").text("Accounttype " + accounttype.getName() + " saved.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                    loadForm();
                    updateTable();
                    tblAccounttype.getSelectionModel().select(tblAccounttype.getItems().size() - 1);

                } catch (DaoException ex) {
                    Notifications.create().title("Un-Successs").text("Accounttype " + accounttype.getName() + " not saved. \n Try Again.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

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

                AccountTypeDao.delete(accounttype);
                Notifications.create().title("Successs").text("Accounttype " + accounttype.getName() + " deleted.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                loadForm();
                updateTable();

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

                    AccountTypeDao.update(accounttype);
                    Notifications.create().title("Successs").text("Accounttype " + accounttype.getName() + " updated.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

                    updateTable();
                    loadForm();

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
    private void tblAccounttypeMC(MouseEvent event) {
        fillForm();
    }
//</editor-fold>
  
//<editor-fold defaultstate="collapsed" desc="Update-Methods">
    private void updateTable() {
        
        Accounttypecategory acccategory = cmbSearchCategory.getSelectionModel().getSelectedItem();
        boolean sacccategory = cmbSearchCategory.getSelectionModel().getSelectedIndex() != -1;
        Accounttypestatus accstatus = cmbSearchStatus.getSelectionModel().getSelectedItem();
        boolean saccstatus = cmbSearchStatus.getSelectionModel().getSelectedIndex() != -1;
        
        if (!sacccategory && !saccstatus) {
            fillTable(AccountTypeDao.getAll());
        }
        if (sacccategory && !saccstatus) {
            fillTable(AccountTypeDao.getAllByCategory(acccategory));
        }
        if (!sacccategory && saccstatus) {
            fillTable(AccountTypeDao.getAllByStatus(accstatus));
        }
        
        
    }
    
    @FXML
    private void cmbSearchCategoryAP(ActionEvent event) {
        updateTable();
    }
    
    @FXML
    private void cmbSearchStatusAP(ActionEvent event) {
        updateTable();
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
