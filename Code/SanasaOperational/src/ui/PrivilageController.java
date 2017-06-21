/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.DaoException;
import dao.ModuleDao;
import dao.PrivilageDao;
import dao.RoleDao;
import entity.Module;
import entity.Privilage;
import entity.Role;
import java.net.URL;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import static ui.LoginController.privilage;
import util.CustomAlerts;

/**
 * FXML Controller class
 *
 * @author SahaN
 */
public class PrivilageController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="Module-Data">
    //Color Indication of Input Controls
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Privilage privilege;

    //Update Identification
    private Privilage oldPrivilege;

    //Table Row, Page Selected
    private int page;
    private int row;

//</editor-fold> 
    //<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private ComboBox<Role> cmbRole;
    @FXML
    private ComboBox<Module> cmbModule;
    @FXML
    private CheckBox cbxSelect;
    @FXML
    private CheckBox cbxInsert;
    @FXML
    private CheckBox cbxUpdate;
    @FXML
    private CheckBox cbxDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSearchClear;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Privilage> tblPrivilage;
    @FXML
    private TableColumn<Privilage, Role> colRole;
    @FXML
    private TableColumn<Privilage, Module> colModule;
    @FXML
    private TableColumn<Privilage, Integer> colSelect;
    @FXML
    private TableColumn<Privilage, Integer> colInsert;
    @FXML
    private TableColumn<Privilage, Integer> colUpdate;
    @FXML
    private TableColumn<Privilage, Integer> colDelete;
    @FXML
    private ComboBox<Role> cmbSearchRole;
    @FXML
    private ComboBox<Module> cmbSearchModule;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Initialize-Method">
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

        privilege = new Privilage();
        privilege.setIns(0);
        privilege.setSel(0);
        privilege.setUpd(0);
        privilege.setDel(0);

        oldPrivilege = null;

        cmbModule.setItems(ModuleDao.getAll());
        cmbModule.getSelectionModel().select(-1);
        cmbRole.setItems(RoleDao.getAll());
        cmbRole.getSelectionModel().select(-1);

        cbxSelect.setSelected(false);
        cbxInsert.setSelected(false);
        cbxUpdate.setSelected(false);
        cbxDelete.setSelected(false);

        dissableButtons(false, false, true, true);
        setStyle(initial);

    }

    private void loadTable() {

        cmbSearchModule.setItems(ModuleDao.getAll());
        cmbSearchModule.getSelectionModel().select(-1);
        cmbSearchRole.setItems(RoleDao.getAll());
        cmbSearchRole.getSelectionModel().select(-1);

        colRole.setCellValueFactory(new PropertyValueFactory("roleId"));
        colModule.setCellValueFactory(new PropertyValueFactory("moduleId"));
        colSelect.setCellValueFactory(new PropertyValueFactory("sel"));
        colInsert.setCellValueFactory(new PropertyValueFactory("ins"));
        colUpdate.setCellValueFactory(new PropertyValueFactory("upd"));
        colDelete.setCellValueFactory(new PropertyValueFactory("del"));

        fillTable(PrivilageDao.getAll());
        pagination.setCurrentPageIndex(0);

    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Privilage_insert"));
        btnUpdate.setDisable(update || !privilage.get("Privilage_update"));
        btnDelete.setDisable(delete || !privilage.get("Privilage_delete"));

        cmbSearchModule.setDisable(select || !privilage.get("Privilage_select"));
        cmbSearchRole.setDisable(select || !privilage.get("Privilage_select"));
    }

    private void setStyle(String style) {
        cmbModule.setStyle(style);
        cmbRole.setStyle(style);

    }

    private void fillTable(ObservableList<Privilage> privilages) {

        if (privilage.get("Privilage_select") && privilages != null && privilages.size() != 0) {

            int rowsCount = 4;
            int pageCount = ((privilages.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? privilages.size() : pageIndex * rowsCount + rowsCount;
                    tblPrivilage.getItems().clear();
                    tblPrivilage.setItems(FXCollections.observableArrayList(privilages.subList(start, end)));
                    return tblPrivilage;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblPrivilage.getItems().clear();
        }

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Data-Setting-Methods">
    @FXML
    private void cmbRoleAP(ActionEvent event) {
        privilege.setRoleId(cmbRole.getSelectionModel().getSelectedItem());
        if (oldPrivilege != null && !privilege.getRoleId().equals(oldPrivilege.getRoleId())) {
            cmbRole.setStyle(updated);
        } else {
            cmbRole.setStyle(valid);
        }
        if (oldPrivilege == null && cmbRole.getSelectionModel().getSelectedItem() != null) {
            cmbModule.setItems(ModuleDao.getUnassignedModuleByRole(cmbRole.getSelectionModel().getSelectedItem()));
        }

    }

    @FXML
    private void cmbModuleAP(ActionEvent event) {
        privilege.setModuleId(cmbModule.getSelectionModel().getSelectedItem());
        if (oldPrivilege != null && !privilege.getModuleId().equals(oldPrivilege.getModuleId())) {
            cmbModule.setStyle(updated);
        } else {
            cmbModule.setStyle(valid);
        }
    }

    @FXML
    private void cbxSelectAP(ActionEvent event) {
        privilege.setSel(cbxSelect.isSelected() ? 1 : 0);
    }

    @FXML
    private void cbxInsertAP(ActionEvent event) {
        privilege.setIns(cbxInsert.isSelected() ? 1 : 0);
    }

    @FXML
    private void cbxUpdateAP(ActionEvent event) {
        privilege.setUpd(cbxUpdate.isSelected() ? 1 : 0);
    }

    @FXML
    private void cbxDeleteAP(ActionEvent event) {
        privilege.setDel(cbxDelete.isSelected() ? 1 : 0);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Form-Operation">
    private String getErrors() {

        String errors = "";
        try {

            if (privilege.getRoleId() == null) {
                errors = errors + "Role      \t\tis Not Selected\n";
            }
            if (privilege.getModuleId() == null) {
                errors = errors + "Module    \tis Not Selected\n";
            }
            if (!(cbxSelect.isSelected() || cbxInsert.isSelected() || cbxUpdate.isSelected() || cbxDelete.isSelected())) {
                errors = errors + "Privilage \t\tis Not Selected\n";
            }

        } catch (Exception e) {
            System.out.println("\n\n----------------------Error Checking Error---------------------------------------------------\n");
            System.out.println(e.getClass());
            System.out.println("\n-------------------------------------------------------------------------\n\n");
            JOptionPane.showMessageDialog(null, e.getClass(), "Error checking Error", JOptionPane.ERROR_MESSAGE);
        }
        return errors;

    }

    private String getUpdates() {

        String updates = "";

        try {

            if (oldPrivilege != null) {

                if (privilege.getSel() != null && !privilege.getSel().equals(oldPrivilege.getSel())) {
                    updates = updates + "Select Changed " + oldPrivilege.getSel() + " to " + privilege.getSel() + "\n";
                }

                if (privilege.getIns() != null && !privilege.getIns().equals(oldPrivilege.getIns())) {
                    updates = updates + "Insert Changed " + oldPrivilege.getIns() + " to " + privilege.getIns() + "\n";
                }

                if (privilege.getUpd() != null && !privilege.getUpd().equals(oldPrivilege.getUpd())) {
                    updates = updates + "Update Changed " + oldPrivilege.getUpd() + " to " + privilege.getUpd() + "\n";
                }
                if (privilege.getDel() != null && !privilege.getDel().equals(oldPrivilege.getDel())) {
                    updates = updates + "Delete Changed " + oldPrivilege.getDel() + " to " + privilege.getDel() + "\n";
                }
            }
        } catch (Exception e) {
            System.out.println("\n\n----------------------Update Checking Error---------------------------------------------------\n");
            System.out.println(e.getClass());
            System.out.println("\n-------------------------------------------------------------------------\n\n");
            JOptionPane.showMessageDialog(null, e.getClass(), "Update checking Error", JOptionPane.ERROR_MESSAGE);
        }

        return updates;
    }

    private void fillForm() {
        if (tblPrivilage.getSelectionModel().getSelectedItem() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldPrivilege = PrivilageDao.getById(tblPrivilage.getSelectionModel().getSelectedItem().getId());
            privilege = PrivilageDao.getById(tblPrivilage.getSelectionModel().getSelectedItem().getId());

            cmbRole.getItems().clear();
            cmbRole.getSelectionModel().select(privilege.getRoleId());

            cmbModule.getItems().clear();
            cmbModule.getSelectionModel().select(privilege.getModuleId());

            cbxSelect.setSelected(privilege.getSel() == 1);
            cbxInsert.setSelected(privilege.getIns() == 1);
            cbxUpdate.setSelected(privilege.getUpd() == 1);
            cbxDelete.setSelected(privilege.getDel() == 1);

            page = pagination.getCurrentPageIndex();
            row = tblPrivilage.getSelectionModel().getSelectedIndex();

        }
    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {

        String errors = getErrors();

        if (errors.isEmpty()) {

            String updates = getUpdates();

            if (!updates.isEmpty()) {


                Optional<ButtonType> action = CustomAlerts.showUpdateConfirmation(updates);
                
                if (action.get()== CustomAlerts.btnYes) {

                    PrivilageDao.update(privilege);
                    Notifications.create().title("Successs").text("Privilege " + privilege.toString() + " updated.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

                    fillTable(PrivilageDao.getAll());
                    loadForm();
                    pagination.setCurrentPageIndex(page);
                    tblPrivilage.getSelectionModel().select(row);

                }
            } else {

                CustomAlerts.showNoUpdatesError();
                
            }
        } else {

            CustomAlerts.showUpdatesError(errors);
        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        if (getUpdates().isEmpty() && getErrors().isEmpty()) {


            Optional<ButtonType> action = CustomAlerts.showDeleteConfirmation();
            
            if (action.get()== CustomAlerts.btnYes) {

                PrivilageDao.delete(privilege);
                Notifications.create().title("Successs").text("Employee " + privilege.toString() + " deleted.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                loadForm();
                loadTable();
                pagination.setCurrentPageIndex(page);
                tblPrivilage.getSelectionModel().select(row);

            }
        } else {

            CustomAlerts.showDeleteError();
            
        }
    }

    @FXML
    private void btnClearAP(ActionEvent event) {

        Optional<ButtonType> action = CustomAlerts.showClearFormConfirmation();
        
        if (action.get()== CustomAlerts.btnYes) {
            loadForm();
        }
    }

    @FXML
    private void btnAddAP(ActionEvent event) {

        privilege.setSel(cbxSelect.isSelected() ? 1 : 0);
        privilege.setIns(cbxInsert.isSelected() ? 1 : 0);
        privilege.setUpd(cbxUpdate.isSelected() ? 1 : 0);
        privilege.setDel(cbxDelete.isSelected() ? 1 : 0);

        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "Ara you sure you need to add this Privileges with following details\n "
                    + "\nRole :         \t\t" + privilege.getRoleId().getName()
                    + "\nModule :       \t\t" + privilege.getModuleId().getName()
                    + "\nSelect :       \t\t" + privilege.getSel()
                    + "\nInsert :       \t\t" + privilege.getIns()
                    + "\nUpdate :       \t\t" + privilege.getUpd()
                    + "\nDelete :       \t\t" + privilege.getDel();
            
            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);
            
            if (action.get()== CustomAlerts.btnYes) {

                try {

                    PrivilageDao.add(privilege);
                    Notifications.create().title("Successs").text("Privilege " + privilege.toString() + " saved.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                    loadForm();
                    fillTable(PrivilageDao.getAll());
                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblPrivilage.getSelectionModel().select(tblPrivilage.getItems().size() - 1);

                } catch (DaoException ex) {
                    Notifications.create().title("Un-Successs").text("Privilege " + privilege.toString() + " not saved. \n Try Again.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

                }

            }

        } else {

                CustomAlerts.showAddError(errors);
        }
    }

    @FXML
    private void tblPrivilageMC(MouseEvent event) {
        fillForm();
    }

    @FXML
    private void tblPrivilageKR(KeyEvent event) {
        fillForm();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Search-Methods">
    private void updateTable() {

        Role role = cmbSearchRole.getSelectionModel().getSelectedItem();
        boolean srole = cmbSearchRole.getSelectionModel().getSelectedIndex() != -1;
        Module module = cmbSearchModule.getSelectionModel().getSelectedItem();
        boolean smodule = cmbSearchModule.getSelectionModel().getSelectedIndex() != -1;

        if (!smodule) {

            if (!srole) {
                
                  fillTable(PrivilageDao.getAll());

            } else {
                  fillTable(PrivilageDao.getAllByRole(role));
            }

        } else {
            if (!srole) {
                
                  fillTable(PrivilageDao.getAllByModule(module));

            } else {
                  fillTable(PrivilageDao.getAllByRoleModule(role,module));
            }
        }

      

    }

    @FXML
    private void cmbSearchRoleAP(ActionEvent event) {
        updateTable();

    }

    @FXML
    private void cmbSearchModuleAP(ActionEvent event) {
         updateTable();

    }

    @FXML
    private void btnSearchClearAP(ActionEvent event) {

        Optional<ButtonType> action = CustomAlerts.showSearchClearConfirmation();

        if (action.get()== CustomAlerts.btnYes) {
            loadTable();
        }
    }
//</editor-fold>

}
