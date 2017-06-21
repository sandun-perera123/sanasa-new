/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.DaoException;
import dao.DesignationDao;
import dao.EmployeeDao;
import entity.Designation;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import javafx.scene.control.ListView;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
public class DesignationController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="Module-Data">
    Designation designation;

    Designation oldDesignation;

    private String valid;
    private String invalid;
    private String updated;
    private String initial;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private TextField txtDesignation;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private ListView<Designation> lstDesignation;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Initialize-Methods">
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        loadForm();

    }

    private void loadForm() {

        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        designation = new Designation();
        oldDesignation = null;

        lstDesignation.getItems().clear();
        lstDesignation.setItems(DesignationDao.getAll());

        txtDesignation.setText("");

        dissableButtons(false, false, true, true);
        setStyle(initial);

    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Designation_insert"));
        btnUpdate.setDisable(update || !privilage.get("Designation_update"));
        btnDelete.setDisable(delete || !privilage.get("Designation_delete"));

    }

    private void setStyle(String style) {

        txtDesignation.setStyle(style);

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Data-Setting-Methods">
    @FXML
    private void txtDesignationKR(KeyEvent event) {

        if (designation.setName(txtDesignation.getText().trim())) {
            if (oldDesignation != null && !designation.getName().equals(oldDesignation.getName())) {
                txtDesignation.setStyle(updated);
            } else {
                txtDesignation.setStyle(valid);
            }
        } else {
            txtDesignation.setStyle(invalid);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Form Operation-Methods">
    private String getErrors() {

        String errors = "";

        try {

            if (designation.getName() == null) {
                errors = errors + "Designation \t\tis Invalid\n";

            }
        } catch (Exception e) {

            System.out.println("\n\n----------Error Cheking Error---------------------------------------------------------------\n");
            System.out.println(e.getClass());
            System.out.println("\n-------------------------------------------------------------------------\n\n");
            JOptionPane.showMessageDialog(null, e.getClass(), "Error Cheking Error", JOptionPane.ERROR_MESSAGE);

        }

        return errors;

    }

    private String getUpdates() {

        String updates = "";

        try {

            if (oldDesignation != null) {

                if (designation.getName() != null && !designation.getName().equals(oldDesignation.getName())) {
                    updates = updates + oldDesignation.getName() + " chnaged to " + designation.getName() + "\n";
                }

            }
        } catch (Exception e) {

            System.out.println("\n\n----------Update Cheking Error---------------------------------------------------------------\n");
            System.out.println(e.getClass());
            System.out.println("\n-------------------------------------------------------------------------\n\n");
            JOptionPane.showMessageDialog(null, e.getClass(), "Update Cheking Error", JOptionPane.ERROR_MESSAGE);

        }

        return updates;
    }

    private void fillForm() {
        if (lstDesignation.getItems() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldDesignation = DesignationDao.getById(lstDesignation.getSelectionModel().getSelectedItem().getId());
            designation = DesignationDao.getById(lstDesignation.getSelectionModel().getSelectedItem().getId());

            txtDesignation.setText(designation.getName());

        }
    }

    @FXML
    private void btnAddAP(ActionEvent event) throws DaoException {

        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "Ara you sure you need to add this Designation \n "
                    + "\nDesignation :         \t\t" + designation.getName();


            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);
            
            if (action.get() == CustomAlerts.btnYes) {
                Notifications.create().title("Successs").text("Designation " + designation.getName() + " Saved.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                DesignationDao.add(designation);
                loadForm();
            }
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
    private void btnUpdateAP(ActionEvent event) {
        String errors = getErrors();

        if (errors.isEmpty()) {

            String updates = getUpdates();

            if (!updates.isEmpty()) {

                Optional<ButtonType> action = CustomAlerts.showUpdateConfirmation(updates);
                
                if (action.get() == CustomAlerts.btnYes) {
                    Notifications.create().title("Successs").text("Designation " + designation.getName() + " updated.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                    DesignationDao.update(designation);
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
    private void btnDeleteAP(ActionEvent event) throws DaoException {

        Optional<ButtonType> action = CustomAlerts.showDeleteConfirmation();

        if (action.get() == CustomAlerts.btnYes) {

            if (EmployeeDao.getAllByDesignation(designation)==null) {
                DesignationDao.delete(designation);
                loadForm();
                Notifications.create().title("Successs").text("Designation " + designation.getName() + " deleted.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
            } else {

                CustomAlerts.showDeleteAssignedDataError();
                
            }

        } else {
            CustomAlerts.showDeleteError();

        }
    }

    @FXML
    private void lstDesignationMC(MouseEvent event) {
        fillForm();
    }

//</editor-fold>
}
