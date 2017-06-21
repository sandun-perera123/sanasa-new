/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.CivilstatusDao;
import dao.DaoException;
import dao.DesignationDao;
import dao.EmployeeDao;
import dao.EmployeestatusDao;
import dao.GenderDao;
import entity.Civilstatus;
import entity.Designation;
import entity.Employee;
import entity.Employeestatus;
import entity.Gender;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.swing.ImageIcon;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import static ui.LoginController.privilage;
import static ui.Main.stage;
import static ui.MainWindowControllerOld.lastDirectory;
import util.CustomAlerts;

/**
 * FXML Controller class
 *
 * @author Suranga
 */
public class EmployeeController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="Module-Data">
    //Color Indication of Input Controls
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Employee employee;

    //Update Identification
    private Employee oldEmployee;

    //Table Row, Page Selected
    private int page;
    private int row;

    //Photo Selection
    private boolean photoSelected;

    //Search Lock
    private boolean lock;

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private TextField txtName;
    @FXML
    private ComboBox<Civilstatus> cmbCivilstatus;
    @FXML
    private DatePicker dtpDob;
    @FXML
    private TextField txtNic;
    @FXML
    private ComboBox<Gender> cmbGender;
    @FXML
    private TextArea txtAddress;
    @FXML
    private TextField txtMobile;
    @FXML
    private TextField txtLand;
    @FXML
    private TextField txtEmail;
    @FXML
    private ImageView imgPhoto;
    @FXML
    private ComboBox<Designation> cmbDesignation;
    @FXML
    private DatePicker dtpAssign;
    @FXML
    private TextField txtSearchName;
    @FXML
    private ComboBox<Designation> cmbSearchDesignation;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnPhotoSelect;
    @FXML
    private Button btnPhotoClear;
    @FXML
    private Button btnSearchClear;
    @FXML
    private Button btnSearchLock;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Employee> tblEmployee;
    @FXML
    private TableColumn<Employee, String> colName;

    @FXML
    private TableColumn<Employee, Designation> colDesignation;
    @FXML
    private TableColumn<Employee, String> colMobile;
    @FXML
    private TableColumn<Employee, String> colEmail;
    @FXML
    private TableColumn<Employee, Employeestatus> colStatus;
    @FXML
    private ComboBox<Employeestatus> cmbSearchEmployeestatus;

    @FXML
    private ComboBox<Employeestatus> cmbEmployeestatus;
    @FXML
    private Label lblNewDesignation;

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

        employee = new Employee();
        oldEmployee = null;

        cmbGender.setItems(GenderDao.getAll());
        cmbGender.getSelectionModel().select(-1);
        cmbCivilstatus.setItems(CivilstatusDao.getAll());
        cmbCivilstatus.getSelectionModel().select(-1);
        cmbDesignation.setItems(DesignationDao.getAll());
        cmbDesignation.getSelectionModel().select(-1);
        cmbEmployeestatus.setItems(EmployeestatusDao.getAll());
        cmbEmployeestatus.getSelectionModel().select(-1);

        txtName.setText("");
        txtAddress.setText("");
        txtNic.setText("");
        txtMobile.setText("");
        txtLand.setText("");
        txtEmail.setText("");
        dtpDob.setValue(null);
        dtpAssign.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

        imgPhoto.setImage(new Image("/image/user.png"));
        photoSelected = false;
        dissableButtons(false, false, true, true);
        setStyle(initial);

    }

    private void loadTable() {
        lock = false;
        btnSearchLock.setText("Lock");

        cmbSearchEmployeestatus.setItems(EmployeestatusDao.getAll());
        cmbSearchEmployeestatus.getSelectionModel().select(-1);
        cmbSearchDesignation.setItems(DesignationDao.getAll());
        cmbSearchDesignation.getSelectionModel().select(-1);
        txtSearchName.setText("");

        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colStatus.setCellValueFactory(new PropertyValueFactory("employeestatusId"));
        colMobile.setCellValueFactory(new PropertyValueFactory("mobile"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colDesignation.setCellValueFactory(new PropertyValueFactory("designationId"));

        fillTable(EmployeeDao.getAll());
        pagination.setCurrentPageIndex(0);

    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Employee_insert"));
        btnUpdate.setDisable(update || !privilage.get("Employee_update"));
        btnDelete.setDisable(delete || !privilage.get("Employee_delete"));

        txtSearchName.setDisable(select || !privilage.get("Employee_select"));
        cmbSearchDesignation.setDisable(select || !privilage.get("Employee_select"));
        cmbSearchEmployeestatus.setDisable(select || !privilage.get("Employee_select"));
        btnSearchLock.setDisable(select || !privilage.get("Employee_select"));
        btnSearchClear.setDisable(select || !privilage.get("Employee_select"));

        lblNewDesignation.setDisable(select || !privilage.get("Designation_select"));
    }

    private void setStyle(String style) {
        cmbGender.setStyle(style);
        cmbCivilstatus.setStyle(style);
        cmbDesignation.setStyle(style);
        cmbEmployeestatus.setStyle(style);

        txtName.setStyle(style);
        txtNic.setStyle(style);
        txtMobile.setStyle(style);
        txtLand.setStyle(style);
        txtEmail.setStyle(style);

        if (!txtAddress.getChildrenUnmodifiable().isEmpty()) {
            ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(style);
        }

        dtpDob.getEditor().setStyle(style);
        dtpAssign.getEditor().setStyle(style);

    }

    private void fillTable(ObservableList<Employee> employees) {

        if (privilage.get("Employee_select") && employees != null && employees.size() != 0) {

            int rowsCount = 4;
            int pageCount = ((employees.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? employees.size() : pageIndex * rowsCount + rowsCount;
                    tblEmployee.getItems().clear();
                    tblEmployee.setItems(FXCollections.observableArrayList(employees.subList(start, end)));
                    return tblEmployee;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblEmployee.getItems().clear();
        }

    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Data-Setting-Methods">
    @FXML
    private void txtNameKR(KeyEvent event) {

        if (employee.setName(txtName.getText().trim())) {
            if (oldEmployee != null && !employee.getName().equals(oldEmployee.getName())) {
                txtName.setStyle(updated);
            } else {
                txtName.setStyle(valid);
            }
        } else {
            txtName.setStyle(invalid);
        }

    }

    @FXML
    private void cmbCivilstatusAP(ActionEvent event) {
        employee.setCivilstatusId(cmbCivilstatus.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getCivilstatusId().equals(oldEmployee.getCivilstatusId())) {
            cmbCivilstatus.setStyle(updated);
        } else {
            cmbCivilstatus.setStyle(valid);
        }
    }

    @FXML
    private void dtpDobAP(ActionEvent event) {
        if (dtpDob.getValue() != null) {
            Date today = new Date();
            today.setYear(today.getYear() - 18);
            Date dob = java.sql.Date.valueOf(dtpDob.getValue());
            if (dob.before(today)) {
                employee.setDob(dob);
                if (oldEmployee != null && !employee.getDob().equals(oldEmployee.getDob())) {
                    dtpDob.getEditor().setStyle(updated);
                } else {
                    dtpDob.getEditor().setStyle(valid);
                }
            } else {
                dtpDob.getEditor().setStyle(invalid);
            }
        }
    }

    @FXML
    private void txtNicKR(KeyEvent event) {
        if (employee.setNic(txtNic.getText().trim())) {
            if (oldEmployee != null && !employee.getNic().equals(oldEmployee.getNic())) {
                txtNic.setStyle(updated);
            } else {
                txtNic.setStyle(valid);
            }
        } else {
            txtNic.setStyle(invalid);
        }
    }

    @FXML
    private void cmbGenderAP(ActionEvent event) {
        employee.setGenderId(cmbGender.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getGenderId().equals(oldEmployee.getGenderId())) {
            cmbGender.setStyle(updated);
        } else {
            cmbGender.setStyle(valid);
        }
    }

    @FXML
    private void txtAddressKR(KeyEvent event) {
        if (employee.setAddress(txtAddress.getText().trim())) {
            if (oldEmployee != null && !employee.getAddress().equals(oldEmployee.getAddress())) {
                ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(updated);
            } else {
                ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(valid);
            }
        } else {
            ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(invalid);
        }
    }

    @FXML
    private void txtMobileKR(KeyEvent event) {

        if (employee.setMobile(txtMobile.getText().trim())) {
            if (oldEmployee != null && !employee.getMobile().equals(oldEmployee.getMobile())) {
                txtMobile.setStyle(updated);
            } else {
                txtMobile.setStyle(valid);
            }
        } else {
            txtMobile.setStyle(invalid);
        }
    }

    @FXML
    private void txtLandKR(KeyEvent event) {

        if (employee.setLand(txtLand.getText())) {
            if (oldEmployee != null && oldEmployee.getLand() != null && employee.getLand() != null && oldEmployee.getLand().equals(employee.getLand())) {
                txtLand.setStyle(valid);
            } else if (oldEmployee != null && oldEmployee.getLand() != employee.getLand()) {
                txtLand.setStyle(updated);
            } else {
                txtLand.setStyle(valid);
            }
        } else {
            txtLand.setStyle(invalid);
        }
    }

    @FXML
    private void txtEmailKR(KeyEvent event) {

        if (employee.setEmail(txtEmail.getText().trim())) {
            if (oldEmployee != null && oldEmployee.getEmail() != null && employee.getEmail() != null && oldEmployee.getEmail().equals(employee.getEmail())) {
                txtEmail.setStyle(valid);
            } else if (oldEmployee != null && oldEmployee.getEmail() != employee.getEmail()) {
                txtEmail.setStyle(updated);
            } else {
                txtEmail.setStyle(valid);
            }

        } else {
            txtEmail.setStyle(invalid);
        }

    }

    @FXML
    private void cmbDesignationAP(ActionEvent event) {
        employee.setDesignationId(cmbDesignation.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getDesignationId().equals(oldEmployee.getDesignationId())) {
            cmbDesignation.setStyle(updated);
        } else {
            cmbDesignation.setStyle(valid);
        }
    }

    @FXML
    private void dtpAssignAP(ActionEvent event) {
        if (dtpDob.getValue() != null) {
            Date today = new Date();
            Date assign = java.sql.Date.valueOf(dtpAssign.getValue());
            if (assign.before(today)) {
                employee.setAssigned(assign);
                if (oldEmployee != null && !employee.getAssigned().equals(oldEmployee.getAssigned())) {
                    dtpAssign.getEditor().setStyle(updated);
                } else {
                    dtpAssign.getEditor().setStyle(valid);
                }
            } else {
                dtpAssign.getEditor().setStyle(invalid);
            }
        }
    }

    @FXML
    private void btnPhotoSelectAP(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        if (lastDirectory != null) {
            fileChooser.setInitialDirectory(lastDirectory);
        }
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            lastDirectory = file.getParentFile();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                byte[] image = new byte[(int) file.length()];
                DataInputStream dataIs = new DataInputStream(new FileInputStream(file));
                dataIs.readFully(image);

                ImageIcon img = new ImageIcon(image);
                String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('.'));
                if (extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".gif")) {

                    if (img.getIconHeight() <= 100 && img.getIconWidth() <= 100) {
                        Image photo = new Image(fis);
                        imgPhoto.setImage(photo);
                        employee.setImage(image);
                        photoSelected = true;

                    } else {

                        //Dialogs.create().title("Error Message").masthead("Photo Select").message("The Image Size should smaller than 100x100 Pixel").showError();
                        photoSelected = false;
                    }
                } else {
                    //Dialogs.create().title("Error Message").masthead("Photo Select").message("The File should be JPG, JPEG, GIF or PNG").showError();
                    photoSelected = false;
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            photoSelected = false;
        }
    }

    @FXML
    private void btnPhotoClearAP(ActionEvent event) {

        if (employee.getImage() != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("තහවුරු කරන්න");
            alert.setHeaderText("ඡායාරූපය ඉවත් කිරීම");
            alert.setContentText("ඡායාරූපය ඉවත් කිරීමට අවශ්‍යද?");

            ButtonType btnYes = new ButtonType("ඔව්", ButtonBar.ButtonData.YES);
            ButtonType btnNo = new ButtonType("නැත", ButtonBar.ButtonData.NO);

            alert.getButtonTypes().setAll(btnNo, btnYes);
            Optional<ButtonType> action = alert.showAndWait();

            if (action.get() == btnYes) {
                imgPhoto.setImage(new Image("/image/user.png"));
                if (oldEmployee != null && oldEmployee.getImage() != null) {
                    photoSelected = true;
                } else {
                    photoSelected = false;
                }
                employee.setImage(null);
            }
        }
    }

    @FXML
    private void cmbEmployeestatusAP(ActionEvent event) {

        employee.setEmployeestatusId(cmbEmployeestatus.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getEmployeestatusId().equals(oldEmployee.getEmployeestatusId())) {
            cmbEmployeestatus.setStyle(updated);
        } else {
            cmbEmployeestatus.setStyle(valid);
        }
    }

    @FXML
    private void lblNewDesignationMC(MouseEvent event) {
        try {

            AnchorPane itemSearchUI = FXMLLoader.load(Main.class.getResource("Designation.fxml"));

            DialogPane dialogPane = new DialogPane();
            dialogPane.setContent(itemSearchUI);
            dialogPane.setHeaderText("Designation");

            Dialog subUI = new Dialog();
            subUI.setDialogPane(dialogPane);

            subUI.show();

            cmbDesignation.getItems().clear();
            cmbDesignation.setItems(DesignationDao.getAll());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Form-Operation-Methods">
    private String getErrors() {
        String errors = "";

        if (employee.getName() == null) {
            errors = errors + "Name \t\tis Invalid\n";
        }
        if (employee.getGenderId() == null) {
            errors = errors + "Gender \t\tis Not Selected\n";
        }
        if (employee.getCivilstatusId() == null) {
            errors = errors + "Civilstatus \tis Not Selected\n";
        }

        if (employee.getAddress() == null) {
            errors = errors + "Address \t\tis Invalid\n";
        }

        if (employee.getDob() == null) {
            errors = errors + "Birth Date \tis Invalid\n";
        }
        if (employee.getNic() == null) {
            errors = errors + "NIC No. \t\tis Invalid\n";
        }
        if (employee.getMobile() == null) {
            errors = errors + "Mobile No. \tis Invalid\n";
        }

        if (txtLand.getText() != null && !employee.setLand(txtLand.getText().trim())) {
            errors = errors + "Land No. \t\tis Invalid\n";
        }
        if (txtEmail.getText() != null && !employee.setEmail(txtEmail.getText().trim())) {
            errors = errors + "Email \t\tis Invalid\n";
        }

        if (employee.getDesignationId() == null) {
            errors = errors + "Designation \tis Not Selected\n";
        }

        if (employee.getAssigned() == null) {
            errors = errors + "Assign Date \tis Invalid\n";
        }

        if (employee.getEmployeestatusId() == null) {
            errors = errors + "Status \t\tis Not Selected\n";
        }

        return errors;

    }

    private String getUpdates() {

        String updates = "";

        if (oldEmployee != null) {

            if (employee.getName() != null && !employee.getName().equals(oldEmployee.getName())) {
                updates = updates + oldEmployee.getName() + " chnaged to " + employee.getName() + "\n";
            }

            if (!employee.getAddress().equals(oldEmployee.getAddress())) {
                updates = updates + oldEmployee.getAddress() + " chnaged to " + employee.getAddress() + "\n";
            }

            if (!employee.getNic().equals(oldEmployee.getNic())) {
                updates = updates + oldEmployee.getNic() + " chnaged to " + employee.getNic() + "\n";
            }

            if (!(oldEmployee.getLand() != null
                    && employee.getLand() != null
                    && oldEmployee.getLand().equals(employee.getLand()))) {
                if (oldEmployee.getLand() != employee.getLand()) {
                    updates = updates + oldEmployee.getLand()
                            + " chnaged to " + employee.getLand() + "\n";
                }
            }

            if (!(oldEmployee.getEmail() != null && employee.getEmail() != null && oldEmployee.getEmail().equals(employee.getEmail()))) {
                if (oldEmployee.getEmail() != employee.getEmail()) {
                    updates = updates + oldEmployee.getEmail() + " chnaged to " + employee.getEmail() + "\n";
                }
            }

            if (!employee.getMobile().equals(oldEmployee.getMobile())) {
                updates = updates + oldEmployee.getMobile() + " chnaged to " + employee.getMobile() + "\n";
            }

            if (!employee.getGenderId().equals(oldEmployee.getGenderId())) {
                updates = updates + oldEmployee.getGenderId() + " chnaged to " + employee.getGenderId() + "\n";
            }

            if (!employee.getCivilstatusId().equals(oldEmployee.getCivilstatusId())) {
                updates = updates + oldEmployee.getCivilstatusId() + " chnaged to " + employee.getCivilstatusId() + "\n";
            }

            if (!employee.getDesignationId().equals(oldEmployee.getDesignationId())) {
                updates = updates + oldEmployee.getDesignationId() + " chnaged to " + employee.getDesignationId() + "\n";
            }

            if (!employee.getDob().equals(oldEmployee.getDob())) {
                updates = updates + oldEmployee.getDob().toString() + "(dob)" + " chnaged to " + employee.getDob().toString() + "\n";
            }

            if (!employee.getAssigned().equals(oldEmployee.getAssigned())) {
                updates = updates + oldEmployee.getAssigned().toString() + " chnaged to " + employee.getAssigned().toString() + "\n";
            }

            if (photoSelected) {
                updates = updates + "Photo Changed\n";
            }

            if (!employee.getEmployeestatusId().equals(oldEmployee.getEmployeestatusId())) {
                updates = updates + oldEmployee.getEmployeestatusId() + " chnaged to " + employee.getEmployeestatusId() + "\n";
            }

        }

        return updates;
    }

    private void fillForm() {
        if (tblEmployee.getSelectionModel().getSelectedItem() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldEmployee = EmployeeDao.getById(tblEmployee.getSelectionModel().getSelectedItem().getId());
            employee = EmployeeDao.getById(tblEmployee.getSelectionModel().getSelectedItem().getId());

            cmbGender.getSelectionModel().select((Gender) employee.getGenderId());
            cmbCivilstatus.getSelectionModel().select((Civilstatus) employee.getCivilstatusId());
            cmbDesignation.getSelectionModel().select((Designation) employee.getDesignationId());
            cmbEmployeestatus.getSelectionModel().select((Employeestatus) employee.getEmployeestatusId());

            txtName.setText(employee.getName());
            txtAddress.setText(employee.getAddress());
            txtNic.setText(employee.getNic());
            txtMobile.setText(employee.getMobile());
            txtLand.setText(employee.getLand());
            txtEmail.setText(employee.getEmail());

            dtpDob.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDob())));
            dtpDob.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDob()));
            dtpAssign.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(employee.getAssigned())));
            dtpAssign.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(employee.getAssigned()));

            if (employee.getImage() != null) {
                imgPhoto.setImage(new Image(new ByteArrayInputStream(employee.getImage())));
            } else {
                imgPhoto.setImage(new Image("/image/user.png"));
            }

            page = pagination.getCurrentPageIndex();
            row = tblEmployee.getSelectionModel().getSelectedIndex();
        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {

        if (getUpdates().isEmpty() && getErrors().isEmpty()) {

            Optional<ButtonType> action = CustomAlerts.showDeleteConfirmation();

            if (action.get() == CustomAlerts.btnYes) {

                EmployeeDao.delete(employee);
                Notifications.create().title("Successs").text("Employee " + employee.getName() + " deleted.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                loadForm();
                updateTable();
                pagination.setCurrentPageIndex(page);
                tblEmployee.getSelectionModel().select(row);

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

                    EmployeeDao.update(employee);
                    Notifications.create().title("Successs").text("Employee " + employee.getName() + " updated.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

                    updateTable();
                    loadForm();
                    pagination.setCurrentPageIndex(page);
                    tblEmployee.getSelectionModel().select(row);

                }
            } else {

                CustomAlerts.showNoUpdatesError();

            }
        } else {

            CustomAlerts.showAddError(errors);

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
    private void btnAddAP(ActionEvent event) {
        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "Ara you sure you need to add this Employee with following details\n "
                    + "\nName :         \t\t" + employee.getName()
                    + "\nGender :       \t\t" + employee.getGenderId().getName()
                    + "\nCivilstatus :  \t\t" + employee.getCivilstatusId().getName()
                    + "\nAddress :      \t\t" + employee.getAddress()
                    + "\nBirth Date :   \t\t" + employee.getDob().toString()
                    + "\nNIC No :       \t\t" + employee.getNic()
                    + "\nMobile No :    \t\t" + employee.getMobile()
                    + "\nLand :         \t\t" + employee.getLand()
                    + "\nEmail :        \t\t" + employee.getEmail()
                    + "\nDesignation :  \t\t" + employee.getDesignationId().getName()
                    + "\nAssigned Date :  \t" + employee.getAssigned().toString()
                    + "\nPhoto :  \t\t\t" + (employee.getImage() == null ? "Not Selected" : "Selected")
                    + "\nStatus :  \t\t" + employee.getEmployeestatusId().getName();

            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);

            if (action.get() == CustomAlerts.btnYes) {

                try {
                    EmployeeDao.add(employee);
                    Notifications.create().title("Successs").text("Employee " + employee.getName() + " saved.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                    loadForm();
                    updateTable();
                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblEmployee.getSelectionModel().select(tblEmployee.getItems().size() - 1);

                } catch (DaoException ex) {
                    Notifications.create().title("Un-Successs").text("Employee " + employee.getName() + " not saved. \n Try Again.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

                }

            }

        } else {

            CustomAlerts.showAddError(errors);
        }
    }

    @FXML
    private void tblEmployeeMC(MouseEvent event) {
        fillForm();
    }

    @FXML
    private void tblEmployeeKR(KeyEvent event) {
        fillForm();
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Search-Methods">
    private void updateTable() {

        String name = txtSearchName.getText().trim();
        boolean sname = !name.isEmpty();
        Employeestatus status = cmbSearchEmployeestatus.getSelectionModel().getSelectedItem();
        boolean sstatus = cmbSearchEmployeestatus.getSelectionModel().getSelectedIndex() != -1;
        Designation designation = cmbSearchDesignation.getSelectionModel().getSelectedItem();
        boolean sdesignation = cmbSearchDesignation.getSelectionModel().getSelectedIndex() != -1;

        if (!sname && !sstatus && !sdesignation) {
            fillTable(EmployeeDao.getAll());
        }
        if (sname && !sstatus && !sdesignation) {
            fillTable(EmployeeDao.getAllByName(name));
        }
        if (!sname && !sstatus && sdesignation) {
            fillTable(EmployeeDao.getAllByDesignation(designation));
        }
        if (!sname && sstatus && !sdesignation) {
            fillTable(EmployeeDao.getAllByStatus(status));
        }
        if (sname && sstatus && !sdesignation) {
            fillTable(EmployeeDao.getAllByNameStatus(name, status));
        }
        if (sname && !sstatus && sdesignation) {
            fillTable(EmployeeDao.getAllByNameDesignation(name, designation));
        }
        if (!sname && sstatus && sdesignation) {
            fillTable(EmployeeDao.getAllByStatusDesignation(status, designation));
        }
        if (sname && sstatus && sdesignation) {
            fillTable(EmployeeDao.getAllByNameStatusDesignation(name, status, designation));
        }

    }

    @FXML
    private void cmbSearchEmployeestatusAP(ActionEvent event) {

        if (cmbSearchEmployeestatus.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                txtSearchName.setText("");
                cmbSearchDesignation.getSelectionModel().select(-1);
            }
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

    @FXML
    private void btnSearchLockAP(ActionEvent event) {

        if (lock) {
            btnSearchLock.setText("Lock");
            lock = false;
            fillTable(EmployeeDao.getAll());
        } else {
            btnSearchLock.setText("Unlock");
            lock = true;
            updateTable();
        }
    }

    @FXML
    private void txtSearchNameKR(KeyEvent event) {

        if (!lock) {
            cmbSearchEmployeestatus.getSelectionModel().select(-1);
            cmbSearchDesignation.getSelectionModel().select(-1);

        }
        updateTable();
    }

    @FXML
    private void cmbSearchDesignationAP(ActionEvent event) {

        if (cmbSearchDesignation.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                txtSearchName.setText("");
                cmbSearchEmployeestatus.getSelectionModel().select(-1);

            }
            updateTable();
        }

    }

//</editor-fold>
}
