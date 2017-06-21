/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.DaoException;
import dao.GenderDao;
import dao.MemberDao;
import dao.RelationsDao;
import entity.Gender;
import entity.Relations;
import entity.Smember;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import static ui.LoginController.privilage;
import static ui.LoginController.user;
import util.CustomAlerts;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class RelationController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtRelationship;
    @FXML
    private TextField txtNIC;
    @FXML
    private TextField txtTel;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private TextArea txaAddress;
    @FXML
    private ComboBox<Smember> cmbMember;
    @FXML
    private ComboBox<Gender> cmbGender;
    @FXML
    private ComboBox<Smember> cmbSearchMember;
    @FXML
    private Button btnSearchClear;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Relations> tblRelation;
    @FXML
    private TableColumn<Relations, Smember> colMember;
    @FXML
    private TableColumn<Relations, String> colRelation;
    @FXML
    private TableColumn<Relations, String> colRelationship;
    @FXML
    private TableColumn<Relations, String> colTel;
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
    private Relations relation;

    //Update Identification
    private Relations oldRelation;

    //Table Row, Page Selected
    private int page;
    private int row;

    private boolean isOk = true;
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

        relation = new Relations();
        oldRelation = null;

        cmbMember.setDisable(false);
        cmbSearchMember.setDisable(false);

        cmbMember.setItems(MemberDao.getAllByRelationCount());
        cmbMember.getSelectionModel().select(-1);
        cmbGender.setItems(GenderDao.getAll());
        cmbGender.getSelectionModel().select(-1);

        txtName.setText("");
        txtRelationship.setText("");
        txtNIC.setText("");
        txtTel.setText("");
        txaAddress.setText("");

        dissableButtons(false, false, true, true);
        setStyle(initial);

    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Relation_insert"));
        btnUpdate.setDisable(update || !privilage.get("Relation_update"));
        btnDelete.setDisable(delete || !privilage.get("Relation_delete"));

        cmbSearchMember.setDisable(select || !privilage.get("Relation_select"));
        btnSearchClear.setDisable(select || !privilage.get("Relation_select"));

    }

    private void setStyle(String style) {
        cmbMember.setStyle(style);
        cmbGender.setStyle(style);

        txtName.setStyle(style);
        txtRelationship.setStyle(style);
        txtNIC.setStyle(style);
        txtTel.setStyle(style);

        if (!txaAddress.getChildrenUnmodifiable().isEmpty()) {
            ((ScrollPane) txaAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(style);
        }

    }

    private void loadTable() {

        cmbSearchMember.setItems(MemberDao.getAll());
        cmbSearchMember.getSelectionModel().select(-1);

        colMember.setCellValueFactory(new PropertyValueFactory("smemberId"));
        colRelation.setCellValueFactory(new PropertyValueFactory("name"));
        colRelationship.setCellValueFactory(new PropertyValueFactory("relationship"));
        colTel.setCellValueFactory(new PropertyValueFactory("tel"));

        fillTable(RelationsDao.getAll());
        pagination.setCurrentPageIndex(0);

    }

    private void fillTable(ObservableList<Relations> relationss) {

        if (privilage.get("Relation_select") && relationss != null && relationss.size() != 0) {

            int rowsCount = 8;
            int pageCount = ((relationss.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? relationss.size() : pageIndex * rowsCount + rowsCount;
                    tblRelation.getItems().clear();
                    tblRelation.setItems(FXCollections.observableArrayList(relationss.subList(start, end)));
                    return tblRelation;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblRelation.getItems().clear();
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Data-Setting-Methods">
    @FXML
    private void txtNameKR(KeyEvent event) {
        if (relation.setName(txtName.getText().trim())) {
            if (oldRelation != null && !relation.getName().equals(oldRelation.getName())) {
                txtName.setStyle(updated);
            } else {
                txtName.setStyle(valid);
            }
        } else {
            txtName.setStyle(invalid);
        }
    }

    @FXML
    private void txtRelationshipKR(KeyEvent event) {

        if (relation.setRelationship(txtRelationship.getText().trim())) {
            if (oldRelation != null && !relation.getRelationship().equals(oldRelation.getRelationship())) {
                txtRelationship.setStyle(updated);
            } else {
                txtRelationship.setStyle(valid);
            }
        } else {
            txtRelationship.setStyle(invalid);
        }

    }

    //Check whether NIC is already exsists
    private boolean isUniqueNIC(String nic) {

        if (!nic.isEmpty() && ((nic.length() == 10) || (nic.length() == 12))) {
            Integer s = RelationsDao.getAllByNic(nic).size();
            if (s > 0) {
                return false;
            } else {
                return true;
            }
        }
        return false;

    }

    //Check whether NIC is match with Birth Year and Gender
    private boolean isMatchedNIC(String nic, Gender gender) {

        if (!nic.isEmpty() && gender != null) {

            Integer nyear = 0;
            Integer ngender = 0;

            if (nic.length() == 10) {

                nyear = Integer.valueOf(nic.substring(0, 2));
                ngender = Integer.valueOf(nic.substring(2, 5));

            } else if (nic.length() == 12) {

                nyear = Integer.valueOf(nic.substring(0, 4));
                ngender = Integer.valueOf(nic.substring(4, 7));

            }

            if (nyear != 0 && ngender != 0) {

                if (gender.getId() == 1) {
                    return ngender <= 500;
                } else {
                    return ngender > 500;
                }

            } else {
                return false;
            }

        }

        return true;

    }

    private void revalidateNIC() {

        if (isOk) {

            txtNIC.requestFocus();

            try {

                Robot robot = new Robot();
                robot.keyRelease(KeyCode.UP.impl_getCode());

            } catch (AWTException e) {
                e.printStackTrace();
            }

        }

    }

    @FXML
    private void txtNICKR(KeyEvent event) {

        if (/*isUniqueNIC(txtNIC.getText()) && isMatchedNIC(txtNIC.getText(), cmbGender.getSelectionModel().getSelectedItem()) && */relation.setNic(txtNIC.getText().trim())) {
            if (oldRelation != null && !relation.getNic().equals(oldRelation.getNic())) {
                txtNIC.setStyle(updated);
            } else {
                txtNIC.setStyle(valid);
            }
        } else {
            txtNIC.setStyle(invalid);
        }

    }

    @FXML
    private void txtTelKR(KeyEvent event) {
        if (relation.setTel(txtTel.getText().trim())) {
            if (oldRelation != null && !relation.getTel().equals(oldRelation.getTel())) {
                txtTel.setStyle(updated);
            } else {
                txtTel.setStyle(valid);
            }
        } else {
            txtTel.setStyle(invalid);
        }
    }

    @FXML
    private void txaAddressKR(KeyEvent event) {

        if (relation.setAddress(txaAddress.getText().trim())) {
            if (oldRelation != null && !relation.getAddress().equals(oldRelation.getAddress())) {
                ((ScrollPane) txaAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(updated);
            } else {
                ((ScrollPane) txaAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(valid);
            }
        } else {
            ((ScrollPane) txaAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(invalid);
        }

    }

    @FXML
    private void cmbMemberAP(ActionEvent event) {
        relation.setSmemberId(cmbMember.getSelectionModel().getSelectedItem());
        if (oldRelation != null && !relation.getSmemberId().equals(oldRelation.getSmemberId())) {
            cmbMember.setStyle(updated);
        } else {
            cmbMember.setStyle(valid);
        }
    }

    @FXML
    private void cmbGenderAP(ActionEvent event) {

        relation.setGenderId(cmbGender.getSelectionModel().getSelectedItem());
        if (oldRelation != null && !relation.getGenderId().equals(oldRelation.getGenderId())) {
            cmbGender.setStyle(updated);
            revalidateNIC();
        } else {
            cmbGender.setStyle(valid);
            revalidateNIC();
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Form-Operations">
    private String getErrors() {

        String errors = "";

        if (relation.getSmemberId() == null) {
            errors = errors + "සාමාජිකයා තෝරා නොමැත\n";
        }
        if (relation.getName() == null) {
            errors = errors + "නම වැරදියි\n";
        }
        if (relation.getGenderId() == null) {
            errors = errors + "ස්ත්‍රී / පුරුෂ බව තෝරා නොමැත\n";
        }
        if (relation.getRelationship() == null) {
            errors = errors + "නෑදෑකම වැරදි\n";
        }
        if (txtTel.getText() != null && !relation.setTel(txtTel.getText().trim())) {
            errors = errors + "දුරකතන අංකය වැරදියි\n";
        }
        if (txtNIC.getText() != null && !relation.setNic(txtNIC.getText().trim())) {
            errors = errors + "ජා.හැ. අංකය වැරදියි\n";
        }
        if (txaAddress.getText() != null && !relation.setAddress(txaAddress.getText().trim())) {
            errors = errors + "ලිපිනය වැරදියි\n";
        }

        return errors;

    }

    private String getUpdates() {

        String updates = "";

        if (oldRelation != null) {

            if (relation.getName() != null && !relation.getName().equals(oldRelation.getName())) {
                updates = updates + "නම : " + oldRelation.getName() + " --> " + relation.getName() + "\n";
            }
            if (!relation.getGenderId().equals(oldRelation.getGenderId())) {
                updates = updates + "ස්ත්‍රී / පුරුෂ බව : " + oldRelation.getGenderId() + " --> " + relation.getGenderId() + "\n";
            }

            if (!relation.getRelationship().equals(oldRelation.getRelationship())) {
                updates = updates + "නෑදෑකම : " + oldRelation.getRelationship() + " --> " + relation.getRelationship() + "\n";
            }
                    
            if (!(oldRelation.getNic() != null
                    && relation.getNic() != null
                    && oldRelation.getNic().equals(relation.getNic()))) {
                if (oldRelation.getNic() != relation.getNic()) {
                    updates = updates + oldRelation.getNic()
                            + " ---> " + relation.getNic() + "\n";
                }
            }
            
            if (!(oldRelation.getTel() != null && relation.getTel() != null && oldRelation.getTel().equals(relation.getTel()))) {
                if (oldRelation.getTel() != relation.getTel()) {
                    updates = updates + oldRelation.getTel() + " ---> " + relation.getTel() + "\n";
                }
            }
            
            if (!(oldRelation.getAddress() != null && relation.getAddress() != null && oldRelation.getAddress().equals(relation.getAddress()))) {
                if (oldRelation.getAddress() != relation.getAddress()) {
                    updates = updates + oldRelation.getAddress() + " ---> " + relation.getAddress() + "\n";
                }
            }
            

        }

        return updates;
    }

    private void fillForm() {

        if (tblRelation.getSelectionModel().getSelectedItem() != null) {

            isOk = false;

            dissableButtons(false, true, false, false);
            cmbMember.setDisable(true);
            cmbSearchMember.setDisable(true);

            oldRelation = RelationsDao.getById(tblRelation.getSelectionModel().getSelectedItem().getId());
            relation = RelationsDao.getById(tblRelation.getSelectionModel().getSelectedItem().getId());

            cmbMember.getSelectionModel().select((Smember) relation.getSmemberId());
            cmbGender.getSelectionModel().select((Gender) relation.getGenderId());

            txtName.setText(relation.getName());
            txtNIC.setText(relation.getNic());
            txtRelationship.setText(relation.getRelationship());
            txtTel.setText(relation.getTel());
            txaAddress.setText(relation.getAddress());

            page = pagination.getCurrentPageIndex();
            row = tblRelation.getSelectionModel().getSelectedIndex();

            setStyle(valid);
            isOk = true;
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

                    RelationsDao.update(relation);
                    Notifications.create().title("සාර්ථකයි !").text("තොරතුරු යාවත්කාලීන කරන ලදී").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

                    updateTable();
                    loadForm();
                    pagination.setCurrentPageIndex(page);
                    tblRelation.getSelectionModel().select(row);

                }
            } else {
                    CustomAlerts.showNoUpdatesError();
            }
        } else {

            CustomAlerts.showUpdatesError(errors);
        }

    }

    @FXML
    private void btnAddAP(ActionEvent event) {

        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "ඔබට පහත තොරතුරු ඇතුළත් කිරීමට අවශ්‍යද?\n "
                    + "\nසාමාජිකයා --> " + relation.getSmemberId().getName()
                    + "\nනම --> " + relation.getName()
                    + "\nස්ත්‍රී / පුරුෂ බව --> " + relation.getGenderId().getName()
                    + "\nනෑදෑකම --> " + relation.getRelationship()
                    + "\nජා.හැ. අංකය --> " + relation.getNic()
                    + "\nදුරකතන අංකය --> " + relation.getTel()
                    + "\nලිපිනය --> " + relation.getAddress();

            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);
            if (action.get() == CustomAlerts.btnYes) {
                try {

                    relation.setEmployeeId(user.getEmployeeId());
                    relation.setDate(new Date());

                    RelationsDao.add(relation);
                    Notifications.create().title("සාර්ථකයි !").text("සාමාජික නම් කිරීම් තොරතුරු ඇතුළත් කරන ලදී").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                    loadForm();
                    updateTable();
                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblRelation.getSelectionModel().select(tblRelation.getItems().size() - 1);

                } catch (DaoException ex) {
                    Notifications.create().title("අසාර්ථකයි !").text("තොරතුරු ඇතුළත් කිරීම අසාර්ථකයි ! නැවත උත්සහා කරන්න").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

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

                RelationsDao.delete(relation);
                Notifications.create().title("සාර්ථකයි !").text("සාමාජික නම් කිරීම් තොරතුරු දත්ත ගොනුවෙන් ඉවත් කරන ලදී.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                loadForm();
                updateTable();
                pagination.setCurrentPageIndex(page);
                tblRelation.getSelectionModel().select(row);

            }
        } else {

            CustomAlerts.showDeleteError();
            
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

//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Search-Methods">
    private void updateTable() {

        Smember member = cmbSearchMember.getSelectionModel().getSelectedItem();
        boolean smember = cmbSearchMember.getSelectionModel().getSelectedIndex() != -1;

        if (!smember) {
            fillTable(RelationsDao.getAll());
        }
        if (smember) {
            fillTable(RelationsDao.getAllByMember(member));
        }

    }

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

    @FXML
    private void tblRelationMC(MouseEvent event) {
        fillForm();
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
