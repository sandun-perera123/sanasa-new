/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.DaoException;
import dao.MemberDao;
import dao.PropertygainDao;
import dao.PropertytypeDao;
import entity.Property;
import entity.Propertygain;
import entity.Propertytype;
import entity.Smember;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import static ui.LoginController.privilage;
import static ui.LoginController.user;
import util.CustomAlerts;

/**
 * FXML Controller class
 *
 * @author Sandun
 */
public class PropertyController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private ComboBox<Smember> cmbMember;
    @FXML
    private ComboBox<Propertytype> cmbType;
    @FXML
    private TextArea txtRemarks;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtValue;
    @FXML
    private Button btnPropertyAdd;
    @FXML
    private TableView<Property> tblProperty;
    @FXML
    private TableColumn<Property, String> colPropertyName;
    @FXML
    private TableColumn<Property, BigDecimal> colPropertyValue;
    @FXML
    private TableColumn<Property, Button> colPropertyDelete;
    @FXML
    private TextField txtMPropertyTotal;
    @FXML
    private TextField txtIMPropertyTotal;
    @FXML
    private TextField txtTotal;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;
    @FXML
    private ComboBox<Smember> cmbSearchMember;
    @FXML
    private Button btnSearchClear;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Propertygain> tblPropertygain;
    @FXML
    private TableColumn<Propertygain, Smember> colMember;
    @FXML
    private TableColumn<Propertygain, BigDecimal> colMTotal;
    @FXML
    private TableColumn<Propertygain, BigDecimal> colIMTotal;
    @FXML
    private TableColumn<Propertygain, BigDecimal> colTotal;
    @FXML
    private Button btnPropertyUpdate;
    @FXML
    private ImageView imgMemberSearch;
    @FXML
    private ImageView imgSearchMemberSearch;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Module-Data">
    //Color Indication of Input Controls
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Propertygain propertygain;
    private Propertygain oldPropertygain;

    private Property oldProperty;
    private Property property;

    //Table Row, Page Selected
    private int page;
    private int row;

    private boolean isListUpdate;
//</editor-fold>    

//<editor-fold defaultstate="collapsed" desc="Initializing-Methods">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadForm();
        loadTable();
        loadInnerTable();
    }

    private void loadForm() {

        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        propertygain = new Propertygain();
        oldPropertygain = null;

        List<Property> properties = new ArrayList();
        propertygain.setPropertyList(properties);

        cmbMember.setItems(MemberDao.getAllNotPropertyMembers());
        cmbMember.getSelectionModel().select(-1);

        cmbMember.setDisable(false);
        imgMemberSearch.setDisable(false);

        dissableButtons(false, false, true, true);
        setStyle(initial);

        loadInnerForm();

    }

    private void loadInnerForm() {

        property = new Property();
        oldProperty = null;

        isListUpdate = false;

        cmbType.setItems(PropertytypeDao.getAll());
        cmbType.getSelectionModel().select(-1);
        txtName.setText("");
        txtValue.setText("");
        txtRemarks.setText("");

        btnPropertyUpdate.setDisable(true);

        setInnerStyle(initial);

    }

    private void loadTable() {

        cmbSearchMember.setItems(MemberDao.getAll());
        cmbSearchMember.getSelectionModel().select(-1);

        colMember.setCellValueFactory(new PropertyValueFactory("smemberId"));
        colMTotal.setCellValueFactory(new PropertyValueFactory("movablepropertytotal"));
        colIMTotal.setCellValueFactory(new PropertyValueFactory("immovablepropertytotal"));
        colTotal.setCellValueFactory(new PropertyValueFactory("total"));

        fillTable(PropertygainDao.getAll());
        pagination.setCurrentPageIndex(0);

    }

    private void loadInnerTable() {

        Image imageDecline = new Image(getClass().getResourceAsStream("/image/inner-delete.png"));

        tblProperty.getItems().clear();

        colPropertyName.setCellValueFactory(new PropertyValueFactory("name"));
        colPropertyValue.setCellValueFactory(new PropertyValueFactory("value"));

        colPropertyDelete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Property, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Property, Button> row) {
                Button btnImageDelete = new Button();
                btnImageDelete.setGraphic(new ImageView(imageDecline));

                btnImageDelete.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        propertygain.getPropertyList().remove(row.getValue());
                        tblProperty.setItems(FXCollections.observableArrayList(propertygain.getPropertyList()));
                        updateTotal();

                    }

                });
                return new SimpleObjectProperty(btnImageDelete);
            }
        });

    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {

        btnAdd.setDisable(insert || !privilage.get("Property_insert"));
        btnUpdate.setDisable(update || !privilage.get("Property_update"));
        btnDelete.setDisable(delete || !privilage.get("Property_delete"));

        cmbSearchMember.setDisable(select || !privilage.get("Property_select"));
        btnSearchClear.setDisable(select || !privilage.get("Property_select"));

    }

    private void setStyle(String style) {
        cmbMember.setStyle(style);
    }

    private void setInnerStyle(String style) {
        cmbType.setStyle(style);
        txtName.setStyle(style);
        txtValue.setStyle(style);
        txtRemarks.setStyle(style);
    }

    private void fillTable(ObservableList<Propertygain> propertygains) {

        if (privilage.get("Property_select") && propertygains != null && propertygains.size() != 0) {

            int rowsCount = 4;
            int pageCount = ((propertygains.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? propertygains.size() : pageIndex * rowsCount + rowsCount;
                    tblPropertygain.getItems().clear();
                    tblPropertygain.setItems(FXCollections.observableArrayList(propertygains.subList(start, end)));
                    return tblPropertygain;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblPropertygain.getItems().clear();
        }

    }

    private void fillInnerTable(ObservableList<Property> properties) {

        if (properties != null && properties.size() != 0) {

            tblProperty.getItems().clear();
            tblProperty.setItems(FXCollections.observableArrayList(properties));

        } else {
            tblProperty.getItems().clear();
        }

        updateTotal();

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Data-Setting-Methods">
    @FXML
    private void cmbMemberAP(ActionEvent event) {

        propertygain.setSmemberId(cmbMember.getSelectionModel().getSelectedItem());
        if (oldPropertygain != null && !propertygain.getSmemberId().equals(oldPropertygain.getSmemberId())) {
            cmbMember.setStyle(updated);
        } else {
            cmbMember.setStyle(valid);
        }

    }

    @FXML
    private void cmbTypeAP(ActionEvent event) {
        System.out.println("1");
        property.setPropertytypeId(cmbType.getSelectionModel().getSelectedItem());
        System.out.println("2");

        if (oldProperty != null && !property.getPropertytypeId().equals(oldProperty.getPropertytypeId())) {
            System.out.println("3");
            cmbType.setStyle(updated);
            System.out.println("4");
        } else {
            cmbType.setStyle(valid);
        }

    }

    @FXML
    private void txtRemarksKR(KeyEvent event) {

        if (txtRemarks.getText() != null && txtRemarks.getText().matches("\\d+")) {
            txtRemarks.setStyle(valid);

        } else {
            txtRemarks.setStyle(invalid);
        }

    }

    @FXML
    private void txtNameKR(KeyEvent event) {

        if (property.setName(txtName.getText().trim())) {
            if (oldProperty != null && !property.getName().equals(oldProperty.getName())) {
                txtName.setStyle(updated);
            } else {
                txtName.setStyle(valid);
            }
        } else {
            txtName.setStyle(invalid);
        }

    }

    @FXML
    private void txtValueKR(KeyEvent event) {

        String value = txtValue.getText().trim();

        if (value.matches("^(?!^0\\.00$)(([1-9][\\d]{0,10})|([0]))\\.[\\d]{2}$") && property.setValue(new BigDecimal(value))) {
            if (oldProperty != null && !property.getValue().equals(oldProperty.getValue())) {
                txtValue.setStyle(updated);
            } else {
                txtValue.setStyle(valid);
            }
        } else {
            property.setValue(null);
            txtValue.setStyle(invalid);
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Form-Operation-Methods">
    private void updateTotal() {

        //Update Total Count and Total Value
        BigDecimal totalM = new BigDecimal(0.00);
        BigDecimal totalIM = new BigDecimal(0.00);
        BigDecimal total = new BigDecimal(0.00);

        for (Property p : propertygain.getPropertyList()) {

            if (p.getPropertytypeId().getId() == 1) {
                totalM = totalM.add(p.getValue()).setScale(2);
            } else {
                totalIM = totalIM.add(p.getValue()).setScale(2);
            }

        }

        total = totalM.add(totalIM).setScale(2);

        propertygain.setImmovablepropertytotal(totalIM);
        propertygain.setMovablepropertytotal(totalM);
        propertygain.setTotal(total);

        txtMPropertyTotal.setText(String.valueOf(propertygain.getMovablepropertytotal()));
        txtIMPropertyTotal.setText(String.valueOf(propertygain.getImmovablepropertytotal()));
        txtTotal.setText(String.valueOf(propertygain.getTotal()));

    }

    private String getErrors() {

        String errors = "";

        if (propertygain.getSmemberId() == null) {
            errors = errors + "සාමාජිකයා තෝරා නොමැත.\n";
        }

        if (propertygain.getPropertyList().size() == 0) {
            errors = errors + "වත්කම් ඇතුළත් කර නොමැත.\n";
        }

        return errors;

    }

    private String getInnerErrors() {

        String errors = "";

        if (propertygain.getSmemberId() == null) {
            errors = errors + "සාමාජිකයා තෝරා නොමැත.\n";
        }

        if (property.getName() == null) {
            errors = errors + "වත්කම වැරදියි ! \n";
        }

        if (property.getPropertytypeId() == null) {
            errors = errors + "වත්කම් වර්ගය තෝරා නොමැත !\n";
        }

        if (property.getValue() == null) {
            errors = errors + "වටිනාකම වැරදියි !\n";
        }

        return errors;

    }

    private String getUpdates() {

        String updates = "";

        try {

            if (oldPropertygain != null) {

                if (propertygain.getPropertyList() != null && !propertygain.getPropertyList().containsAll(oldPropertygain.getPropertyList()) || !oldPropertygain.getPropertyList().containsAll(propertygain.getPropertyList())) {
                    updates = updates + "Properties of" + oldPropertygain.getPropertyList() + " chanaged to " + propertygain.getPropertyList() + "\n";
                }

            }

            if (oldPropertygain != null && property != null && oldProperty != null) {

                if (property.getName() != null && !property.getName().equals(oldProperty.getName())) {
                    updates = updates + "වත්කම " + oldProperty.getName() + " --> " + property.getName() + "\n";
                }

                if (property.getPropertytypeId() != null && !property.getPropertytypeId().equals(oldProperty.getPropertytypeId())) {
                    updates = updates + "වර්ගය " + oldProperty.getPropertytypeId() + " --> " + property.getPropertytypeId() + "\n";
                }

                if (property.getValue() != null && !property.getValue().equals(oldProperty.getValue())) {
                    updates = updates + "වටිනාකම " + oldProperty.getValue() + " --> " + property.getValue() + "\n";
                }

                if (property.getRemarks() != null && !property.getRemarks().equals(oldProperty.getRemarks())) {
                    updates = updates + "වෙනත් කරුණු " + oldProperty.getRemarks() + " --> " + property.getRemarks() + "\n";
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

        if (tblPropertygain.getSelectionModel().getSelectedItem() != null) {

            loadInnerForm();
            dissableButtons(false, true, false, false);
            setStyle(valid);

            propertygain = PropertygainDao.getById(tblPropertygain.getSelectionModel().getSelectedItem().getId());
            oldPropertygain = PropertygainDao.getById(tblPropertygain.getSelectionModel().getSelectedItem().getId());

            /**
             * ****Disable Fields********
             */
            cmbMember.setDisable(true);
            imgMemberSearch.setDisable(true);

            /**
             * ****Disable Fields********
             */
            cmbMember.getSelectionModel().select((Smember) propertygain.getSmemberId());

            fillInnerTable(FXCollections.observableArrayList(propertygain.getPropertyList()));
            updateTotal();

            page = pagination.getCurrentPageIndex();
            row = tblPropertygain.getSelectionModel().getSelectedIndex();
        }
    }

    private void fillInnerForm() throws CloneNotSupportedException {

        if (tblProperty.getSelectionModel().getSelectedItem() != null) {

            btnPropertyUpdate.setDisable(false);

            setInnerStyle(valid);

            /**
             * ****Disable Fields********
             */
            btnPropertyAdd.setDisable(true);
            /**
             * ****Disable Fields********
             */

            int indexPropertygain = propertygain.getPropertyList().indexOf(tblProperty.getSelectionModel().getSelectedItem());
            property = propertygain.getPropertyList().get(indexPropertygain);

//            int indexOldPropertygain = oldPropertygain.getPropertyList().indexOf(tblProperty.getSelectionModel().getSelectedItem());
//            oldProperty = propertygain.getPropertyList().get(indexOldPropertygain);
            oldProperty = (Property) property.clone();

            cmbType.getSelectionModel().select(property.getPropertytypeId());
            txtName.setText(property.getName());
            txtValue.setText(property.getValue().toString());
            txtRemarks.setText(property.getRemarks());

            fillInnerTable(FXCollections.observableArrayList(propertygain.getPropertyList()));

        }

    }

    @FXML
    private void btnPropertyAddAP(ActionEvent event) {

        String errors = getInnerErrors();

        if (errors.isEmpty()) {

            Property property = new Property();

            property.setPropertygainId(propertygain);
            property.setPropertytypeId(cmbType.getSelectionModel().getSelectedItem());
            property.setName(txtName.getText());

            BigDecimal value = new BigDecimal(txtValue.getText().trim());
            property.setValue(value);

            property.setRemarks(txtRemarks.getText());

            propertygain.getPropertyList().add(property);
            tblProperty.setItems(FXCollections.observableArrayList(propertygain.getPropertyList()));

            updateTotal();

            loadInnerForm();

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("අසාර්ථකයි !");
            alert.setHeaderText(" ");
            alert.setContentText("වත්කම් තොරතුරු නිවැරදි නොවේ.");

            ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(btnOk);
            alert.showAndWait();

        }

    }

    @FXML
    private void btnAddAP(ActionEvent event) {

        propertygain.setDate(new Date());
        propertygain.setEmployeeId(user.getEmployeeId());

        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "ඔබට පහත තොරතුරු ඇතුළත් කිරීමට අවශ්‍යද?\n "
                    + "\nසාමාජිකයා:       \t\t" + propertygain.getSmemberId().getName()
                    + "\nවත්කම් :         \t\t" + propertygain.getPropertyList().toString();

            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);
            
            if (action.get() == CustomAlerts.btnYes) {

                try {

                    propertygain.setEmployeeId(user.getEmployeeId());
                    propertygain.setDate(new Date());

                    PropertygainDao.add(propertygain);
                    Notifications.create().title("සාර්ථකයි !").text("නම " + propertygain.getSmemberId().getName() + " වන සාමාජිකයාගේ වත්කම් ඇතුළත් කරන ලදී").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(10.0)).showInformation();
                    loadForm();
                    loadInnerForm();
                    fillTable(PropertygainDao.getAll());
                    fillInnerTable(FXCollections.observableArrayList(propertygain.getPropertyList()));

                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblPropertygain.getSelectionModel().select(tblPropertygain.getItems().size() - 1);

                } catch (DaoException ex) {
                    Notifications.create().title("අසාර්ථකයි !").text("නම " + propertygain.getSmemberId().getName() + " වන සාමාජිකයාගේ වත්කම් ඇතුළත් කිරීමට නොහැක. \n නැවත උත්සහා කරන්න.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

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

                PropertygainDao.delete(propertygain);
                Notifications.create().title("සාර්ථකයි !").text("සාමාජික වත්කම් තොරතුරු දත්ත ගොනුවෙන් ඉවත් කරන ලදී.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                fillTable(PropertygainDao.getAll());
                loadForm();
                loadInnerForm();
                fillTable(PropertygainDao.getAll());
                fillInnerTable(FXCollections.observableArrayList(propertygain.getPropertyList()));

                updateTable();
                pagination.setCurrentPageIndex(page);
                tblPropertygain.getSelectionModel().select(row);

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

                    PropertygainDao.update(propertygain);
                    Notifications.create().title("සාර්ථකයි !").text("වත්කම් තොරතුරු යාවත්කාලීන කරන ලදී").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

                    //updateTable();
                    fillTable(PropertygainDao.getAll());
                    loadForm();
                    loadInnerForm();
                    fillTable(PropertygainDao.getAll());
                    fillInnerTable(FXCollections.observableArrayList(propertygain.getPropertyList()));

                    pagination.setCurrentPageIndex(page);
                    tblPropertygain.getSelectionModel().select(row);

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
            loadInnerForm();
            fillTable(PropertygainDao.getAll());
            fillInnerTable(FXCollections.observableArrayList(propertygain.getPropertyList()));
        }

    }

    @FXML
    private void btnPropertyUpdateAP(ActionEvent event) {

        String errors = getInnerErrors();

        if (property != null && errors.isEmpty()) {

            property.setPropertytypeId(cmbType.getSelectionModel().getSelectedItem());
            property.setName(txtName.getText());

            BigDecimal value = new BigDecimal(txtValue.getText());
            property.setValue(value);

            property.setRemarks(txtRemarks.getText());

            Notifications.create().title("සාර්ථකයි !").text("යාවත්කාලීන කිරීම සාර්ථකයි !").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

            //fillInnerTable(FXCollections.observableArrayList(oldProperty.getPropertygainId().getPropertyList()));
            fillInnerTable(FXCollections.observableArrayList(propertygain.getPropertyList()));
        } else {

            JOptionPane.showMessageDialog(null, "වත්කම් තොරතුරු යාවත්කාලීන කිරීමට නොහැක.");

        }

    }

    @FXML
    private void tblPropertygainKP(KeyEvent event) {
        fillForm();
    }

    @FXML
    private void tblPropertygainMC(MouseEvent event) {
        fillForm();
    }

    @FXML
    private void tblPropertyMC(MouseEvent event) throws CloneNotSupportedException {
        fillInnerForm();

    }
    
    @FXML
    private void tblPropertyKR(KeyEvent event) {
        fillForm();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Search-Methods">
    @FXML
    private void cmbSearchMemberAP(ActionEvent event) {
        if (cmbSearchMember.getSelectionModel().getSelectedItem() != null) {
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
    
    private void updateTable() {
        
        Smember member = cmbSearchMember.getSelectionModel().getSelectedItem();
        boolean memberstatus = cmbSearchMember.getSelectionModel().getSelectedIndex() != -1;
        
        if (!memberstatus) {
            fillTable(PropertygainDao.getAll());
        }
        
        if (memberstatus) {
            fillTable(PropertygainDao.getAllByMember(member));
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
    
    @FXML
    private void imgSearchMemberSearchMC(MouseEvent event) {
        
        try {
            
            Stage subUI = new Stage();
            MyResourceBundle rb = new MyResourceBundle();
            
            HashMap hm = new HashMap();
            hm.put("combo", cmbSearchMember);
            hm.put("list", cmbSearchMember.getItems());
            
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

}
