/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.CommonaccountDao;
import dao.CommonaccountcategoryDao;
import dao.CommonaccountsubcategoryDao;
import dao.DaoException;
import entity.Commonaccount;
import entity.Commonaccountcategory;
import entity.Commonaccountsubcategory;
import java.math.BigDecimal;
import java.net.URL;
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
import static ui.LoginController.privilage;
import static ui.LoginController.user;
import util.CustomAlerts;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class CommonAccountController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private ComboBox<Commonaccountcategory> cmbCategory;
    @FXML
    private ComboBox<Commonaccountsubcategory> cmbSubcategory;
    @FXML
    private TextField txtName;
    @FXML
    private Button btnAdd;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Commonaccount> tblAccount;
    @FXML
    private TableColumn<Commonaccount, Commonaccountcategory> colMainCategory;
    @FXML
    private TableColumn<Commonaccount, Commonaccountsubcategory> colSubCategory;
    @FXML
    private TableColumn<Commonaccount, String> colName;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
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
    private Commonaccount commonaccount;

    //Update Identification
    private Commonaccount oldCommonaccount;

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

    //Load initial data to the form.
    //And set default values for the controls.
    private void loadForm() {

        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        commonaccount = new Commonaccount();
        oldCommonaccount = null;

        cmbCategory.setItems(CommonaccountcategoryDao.getAll());
        cmbCategory.getSelectionModel().select(-1);

        cmbSubcategory.setItems(CommonaccountsubcategoryDao.getAll());
        cmbSubcategory.getSelectionModel().select(-1);

        txtName.setText("");

        dissableButtons(false, false, true, true);
        setStyle(initial);

    }

    //Enable and disabling buttons and search fields by checkin privilages.
    private void dissableButtons(boolean select, boolean insert, boolean update,
            boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Member_insert"));
        btnUpdate.setDisable(update || !privilage.get("Member_update"));
        btnDelete.setDisable(delete || !privilage.get("Member_delete"));
    }

    //Set color for all the controls dynamically.
    private void setStyle(String style) {
        cmbCategory.setStyle(style);
        cmbSubcategory.setStyle(style);
        txtName.setStyle(style);

    }

    //Define the main Table component.
    //Bind columns.
    private void loadTable() {

        colMainCategory.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Commonaccount, Commonaccountcategory>, ObservableValue<Commonaccountcategory>>() {
            @Override
            public ObservableValue<Commonaccountcategory> call(TableColumn.CellDataFeatures<Commonaccount, Commonaccountcategory> param) {
                return new SimpleObjectProperty<>(param.getValue().getCommonaccountsubcategoryId().getCommonaccountcategoryId());
            }
        });
        colSubCategory.setCellValueFactory(new PropertyValueFactory("commonaccountsubcategoryId"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));

        fillTable(CommonaccountDao.getAll());
        pagination.setCurrentPageIndex(0);

    }

    //Fill data to the table Component.
    private void fillTable(ObservableList<Commonaccount> accounts) {

        if (privilage.get("Commonaccount_select") && accounts != null && accounts.size() != 0) {

            int rowsCount = 12;
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

//<editor-fold defaultstate="collapsed" desc="Data-Settings-Methods">
    @FXML
    private void cmbCategoryAP(ActionEvent event) {

        if (cmbCategory.getSelectionModel().getSelectedItem() != null) {

            Commonaccountcategory category = cmbCategory.getSelectionModel().getSelectedItem();
            cmbSubcategory.setItems(CommonaccountsubcategoryDao.getAllByCategory(category));
            cmbCategory.setStyle(valid);
        }

    }

    @FXML
    private void cmbSubcategoryAP(ActionEvent event) {

        commonaccount.setCommonaccountsubcategoryId(cmbSubcategory.getSelectionModel().getSelectedItem());
        if (oldCommonaccount != null && !commonaccount.getCommonaccountsubcategoryId().equals(oldCommonaccount.getCommonaccountsubcategoryId())) {
            cmbSubcategory.setStyle(updated);
        } else {
            cmbSubcategory.setStyle(valid);
        }

    }

    @FXML
    private void txtNameKR(KeyEvent event) {

        if (commonaccount.setName(txtName.getText().trim())) {
            if (oldCommonaccount != null && !commonaccount.getName().equals(oldCommonaccount.getName())) {
                txtName.setStyle(updated);
            } else {
                txtName.setStyle(valid);
            }
        } else {
            txtName.setStyle(invalid);
        }

    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Form-Operation-Methods">
    //Get errors by checking null values in the object.
    private String getErrors() {
        String errors = "";
        
        if (commonaccount.getCommonaccountsubcategoryId() == null) {
            errors = errors + "ප්‍රධාන වර්ගය තෝරා නොමැත. \n";
        }else{
            
            if (commonaccount.getCommonaccountsubcategoryId().getCommonaccountcategoryId() == null) {
                errors = errors + "උප වර්ගය තෝරා නොමැත.\n";
            }
            
        }
        
        if (commonaccount.getName() == null) {
            errors = errors + "ගිණුමේ නම ඇතුළත් කර නොමැත. \n";
        }
        
        return errors;
        
    }
    
    //Get updates by comparing old object and new object.
    private String getUpdates() {
        
        String updates = "";
        
        if (oldCommonaccount != null) {
            
            if (!commonaccount.getCommonaccountsubcategoryId().equals(oldCommonaccount.getCommonaccountsubcategoryId())) {
                updates = updates + "ප්‍රධාන වර්ගය : " + oldCommonaccount.getCommonaccountsubcategoryId() + " ---> " + commonaccount.getCommonaccountsubcategoryId() + "\n";
            }
            if (!commonaccount.getName().equals(oldCommonaccount.getName())) {
                updates = updates + "උප වර්ගය : " + oldCommonaccount.getName() + " ---> " + commonaccount.getName() + "\n";
            }
            if (!commonaccount.getCommonaccountsubcategoryId().getCommonaccountcategoryId().equals(oldCommonaccount.getCommonaccountsubcategoryId().getCommonaccountcategoryId())) {
                updates = updates + "ගිණුමේ නම : " + oldCommonaccount.getCommonaccountsubcategoryId().getCommonaccountcategoryId() + " ---> " + commonaccount.getCommonaccountsubcategoryId().getCommonaccountcategoryId() + "\n";
            }
            
        }
        
        return updates;
    }
    
    //Fill data to the form when click on the table.
    private void fillForm() {
        
        if (tblAccount.getSelectionModel().getSelectedItem() != null) {
            
            dissableButtons(false, true, false, false);
            
            oldCommonaccount = CommonaccountDao.getById(tblAccount.getSelectionModel().getSelectedItem().getId());
            commonaccount = CommonaccountDao.getById(tblAccount.getSelectionModel().getSelectedItem().getId());
            
            cmbCategory.getSelectionModel().select((Commonaccountcategory) commonaccount.getCommonaccountsubcategoryId().getCommonaccountcategoryId());
            cmbSubcategory.getSelectionModel().select((Commonaccountsubcategory) commonaccount.getCommonaccountsubcategoryId());
            
            txtName.setText(commonaccount.getName());
            
            setStyle(valid);
            
            page = pagination.getCurrentPageIndex();
            row = tblAccount.getSelectionModel().getSelectedIndex();
        }
        
    }
    
    @FXML
    private void btnAddAP(ActionEvent event) {
        
        String errors = getErrors();
        
        if (errors.isEmpty()) {
            
            //Setting confirmation message.
            String confermation = "පහත සාමාජික තොරතුරු ඇතුළත් කිරීමට අවශ්‍යද ?\n "
                    + "\nසාමාජික අංකය :--> " + commonaccount.getCommonaccountsubcategoryId().getCommonaccountcategoryId().getName()
                    + "\nනම :--> " + commonaccount.getCommonaccountsubcategoryId().getName()
                    + "\nසම්පූර්ණ නම :--> " + commonaccount.getName();
            
            //Asking for the confirmation.
            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);
            
            if (action.get() == CustomAlerts.btnYes) {
                
                try {
                    
                    commonaccount.setBalance(new BigDecimal("0.00"));
                    
                    //Pass the commonaccount object to the CommonaccountDao
                    CommonaccountDao.add(commonaccount);
                    
                    //Show the notification
                    Notifications.create().title("සාර්ථකයි !").text(commonaccount.getName() + " ඇතුළත් කරන ලදී.")
                            .position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(10.0)).showInformation();
                    loadForm(); // Make the form to default status and values.
                    //updateTable(); // Refresh the Table.
                    fillTable(CommonaccountDao.getAll());
                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblAccount.getSelectionModel().select(tblAccount.getItems().size() - 1);
                    
                } catch (DaoException ex) {
                    Notifications.create().title("අසාර්ථකයි !").text(commonaccount.getName() + " ඇතුළත් කිරීමට නොහැක. \n නැවත උත්සහා කරන්න.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                    
                }
                
            }
            
        } else {
            
            CustomAlerts.showAddError(errors);
            
        }
        
    }
    
    @FXML
    private void tblAccountMC(MouseEvent event) {
        fillForm();
    }
    
    @FXML
    private void btnDeleteAP(ActionEvent event) {
        
        if (getUpdates().isEmpty() && getErrors().isEmpty()) {
            
            Optional<ButtonType> action = CustomAlerts.showDeleteConfirmation();
            
            if (action.get() == CustomAlerts.btnYes) {
                
                CommonaccountDao.delete(commonaccount);
                Notifications.create().title("සාර්ථකයි !").text(commonaccount.getName()+ " දත්ත ගොනුවෙන් ඉවත් කරන ලදී.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                loadForm();
                //updateTable();
                fillTable(CommonaccountDao.getAll());
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
                    
                    CommonaccountDao.update(commonaccount);
                    Notifications.create().title("සාර්ථකයි !").text(commonaccount.getName()+ " යාවත්කාලීන කරන ලදී.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                    //updateTable();
                    fillTable(CommonaccountDao.getAll());
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
//</editor-fold>

}
