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
import dao.MemberDao;
import dao.MemberstatusDao;
import entity.Employee;
import entity.Gender;
import entity.Memberstatus;
import entity.Smember;
import groovy.util.GroovyTestCase;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
import static ui.LoginController.user;
import static ui.Main.stage;
import static ui.MainWindowControllerOld.lastDirectory;
import util.CustomAlerts;

/**
 * FXML Controller class
 *
 * @author Sandun
 */
public class MemberController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="Module-Data">
//Color Indication of Input Controls
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Smember member;

    //Update Identification
    private Smember oldMember;

    //Table Row, Page Selected
    private int page;
    private int row;

    //Photo Selection
    private boolean photoSelected;

    private boolean isOk = true;

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtFullname;
    @FXML
    private ComboBox<Gender> cmbGender;
    @FXML
    private TextArea txaAddress;
    @FXML
    private DatePicker dtpDOB;
    @FXML
    private TextField txtTel;
    @FXML
    private TextField txtNIC;
    @FXML
    private TextField txtEducation;
    @FXML
    private TextField txtOccupation;
    @FXML
    private DatePicker dtpMembershipDate;
    @FXML
    private ImageView imgPhoto;
    @FXML
    private Button btnPhotoSelect;
    @FXML
    private Button btnPhotoClear;
    @FXML
    private ComboBox<Memberstatus> cmbStatus;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;
    @FXML
    private TextField txtSearchMemberID;
    @FXML
    private TextField txtSearchName;
    @FXML
    private TextField txtSearchNIC;
    @FXML
    private ComboBox<Memberstatus> cmbSearchStatus;
    @FXML
    private TableView<Smember> tblMember;
    @FXML
    private TableColumn<Smember, Integer> colMemberID;
    @FXML
    private TableColumn<Smember, String> colName;
    @FXML
    private TableColumn<Smember, String> colAddress;
    @FXML
    private TableColumn<Smember, String> colTel;
    @FXML
    private TextField txtMemberID;
    @FXML
    private Button btnSearchClear;
    @FXML
    private Pagination pagination;
    @FXML
    private Button btnWebcam;
    

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

        member = new Smember();
        oldMember = null;

        member.setDomembership(new Date());

        cmbGender.setItems(GenderDao.getAll());
        cmbGender.getSelectionModel().select(-1);

        cmbStatus.setItems(MemberstatusDao.getAll());
        cmbStatus.getSelectionModel().select(0);
        member.setMemberstatusId(MemberstatusDao.getById(1));

        txtMemberID.setText("");
        txtName.setText("");
        txtFullname.setText("");
        txaAddress.setText("");
        txtNIC.setText("");
        txtTel.setText("");
        txtOccupation.setText("");
        txtEducation.setText("");

        dtpDOB.setValue(null);
        dtpMembershipDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

        imgPhoto.setImage(new Image("/image/user.png"));
        photoSelected = false;
        dissableButtons(false, false, true, true);
        setStyle(initial);

        //generateMemberID();

    }

    //Enable and disabling buttons and search fields by checkin privilages.
    private void dissableButtons(boolean select, boolean insert, boolean update,
            boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Member_insert"));
        btnUpdate.setDisable(update || !privilage.get("Member_update"));
        btnDelete.setDisable(delete || !privilage.get("Member_delete"));

        txtSearchMemberID.setDisable(select || !privilage.get("Member_select"));
        txtSearchName.setDisable(select || !privilage.get("Member_select"));
        txtSearchNIC.setDisable(select || !privilage.get("Member_select"));
        cmbSearchStatus.setDisable(select || !privilage.get("Member_select"));

        btnSearchClear.setDisable(select || !privilage.get("Member_select"));
    }

    //Set color for all the controls dynamically.
    private void setStyle(String style) {
        cmbGender.setStyle(style);
        cmbStatus.setStyle(valid);

        txtMemberID.setStyle(style);
        txtName.setStyle(style);
        txtFullname.setStyle(style);
        txtNIC.setStyle(style);
        txtTel.setStyle(style);
        txtOccupation.setStyle(style);
        txtEducation.setStyle(style);

        if (!txaAddress.getChildrenUnmodifiable().isEmpty()) {
            ((ScrollPane) txaAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(style);
        }

        dtpDOB.getEditor().setStyle(style);
        dtpMembershipDate.getEditor().setStyle(valid);

    }

    //Define the main Table component.
    //Bind columns.
    private void loadTable() {

        cmbSearchStatus.setItems(MemberstatusDao.getAll());
        cmbSearchStatus.getSelectionModel().select(-1);
        txtSearchMemberID.setText("");
        txtSearchName.setText("");
        txtSearchNIC.setText("");

        colMemberID.setCellValueFactory(new PropertyValueFactory("memberid"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colTel.setCellValueFactory(new PropertyValueFactory("tel"));
//        colStatus.setCellValueFactory(new PropertyValueFactory("memberstatusId"));

        fillTable(MemberDao.getAll());
        pagination.setCurrentPageIndex(0);

    }

    //Fill data to the table Component.
    private void fillTable(ObservableList<Smember> members) {

        if (privilage.get("Member_select") && members != null && members.size() != 0) {

            int rowsCount = 12;
            int pageCount = ((members.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? members.size() : pageIndex * rowsCount + rowsCount;
                    tblMember.getItems().clear();
                    tblMember.setItems(FXCollections.observableArrayList(members.subList(start, end)));
                    return tblMember;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblMember.getItems().clear();
        }

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Data-Settings-Methods">
    //This method is used to generate a new member ID.
    //New ID will generate by adding "1" to last ID.
    private void generateMemberID() {

        Integer newID = Integer.valueOf(MemberDao.getLastMemberID()) + 1;
        String formattedID = String.format("%04d", newID);
        member.setMemberid(formattedID);
        txtMemberID.setText(member.getMemberid());

    }

    //Check whether NIC is already exsists
    private boolean isUniqueNIC(String nic) {

        if (!nic.isEmpty() && ((nic.length() == 10) || (nic.length() == 12))) {
            Integer s = MemberDao.getAllByNic(nic).size();
            if (s > 0) {
                return false;
            } else {
                return true;
            }
        }
        return false;

    }

    //Check whether NIC is match with Birth Year and Gender
    private boolean isMatchedNIC(String nic, LocalDate date, Gender gender) {

        if (!nic.isEmpty() && date != null && gender != null) {

            Integer nyear = 0;
            Integer ngender = 0;

            Date ldate = java.sql.Date.valueOf(date);

            Integer dyear = Integer.valueOf(ldate.getYear());

            if (nic.length() == 10) {

                nyear = Integer.valueOf(nic.substring(0, 2));
                ngender = Integer.valueOf(nic.substring(2, 5));

            } else if (nic.length() == 12) {

                nyear = Integer.valueOf(nic.substring(0, 4));
                ngender = Integer.valueOf(nic.substring(4, 7));

            }

            if (nyear == dyear) {

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

    //Key release for Member ID. Set value to the relavant field in the object.
    @FXML
    private void txtMemberIDKR(KeyEvent event) {

        String memberid = txtMemberID.getText().trim();

        if (memberid.matches("\\d+") && member.setMemberid(memberid)) {
            if (oldMember != null && !(member.getMemberid() == oldMember.getMemberid())) {
                txtMemberID.setStyle(updated);
            } else {
                txtMemberID.setStyle(valid);
            }
        } else {
            txtMemberID.setStyle(invalid);
            member.setMemberid(null);
        }

    }

    //Key release for Name. Set value to the relavant field in the object.
    @FXML
    private void txtNameKR(KeyEvent event) {

        if (member.setName(txtName.getText().trim())) {
            if (oldMember != null && !member.getName().equals(oldMember.getName())) {
                txtName.setStyle(updated);
            } else {
                txtName.setStyle(valid);
            }
        } else {
            txtName.setStyle(invalid);
        }

    }

    //Key release for FullName. Set value to the relavant field in the object.
    @FXML
    private void txtFullnameKR(KeyEvent event) {

        if (member.setFullname(txtFullname.getText().trim())) {
            if (oldMember != null && !member.getFullname().equals(oldMember.getFullname())) {
                txtFullname.setStyle(updated);
            } else {
                txtFullname.setStyle(valid);
            }
        } else {
            txtFullname.setStyle(invalid);
        }

    }

    //Key release for Gender. Set value to the relavant field in the object.
    @FXML
    private void cmbGenderAP(ActionEvent event) {

        member.setGenderId(cmbGender.getSelectionModel().getSelectedItem());
        if (oldMember != null && !member.getGenderId().equals(oldMember.getGenderId())) {
            cmbGender.setStyle(updated);
            revalidateNIC();
        } else {
            cmbGender.setStyle(valid);
            revalidateNIC();
        }

    }

    //Key release for Address. Set value to the relavant field in the object.
    @FXML
    private void txaAddressKR(KeyEvent event) {

        if (member.setAddress(txaAddress.getText().trim())) {
            if (oldMember != null && !member.getAddress().equals(oldMember.getAddress())) {
                ((ScrollPane) txaAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(updated);
            } else {
                ((ScrollPane) txaAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(valid);
            }
        } else {
            ((ScrollPane) txaAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(invalid);
        }

    }

    //Key release for DOB. Set value to the relavant field in the object.
    @FXML
    private void dtpDOBAP(ActionEvent event) {

        if (dtpDOB.getValue() != null) {
            Date today = new Date();
            today.setYear(today.getYear() - 18);
            Date dob = java.sql.Date.valueOf(dtpDOB.getValue());
            if (dob.before(today)) {
                member.setDob(dob);
                if (oldMember != null && !member.getDob().equals(oldMember.getDob())) {
                    dtpDOB.getEditor().setStyle(updated);
                    revalidateNIC();
                } else {
                    dtpDOB.getEditor().setStyle(valid);
                    revalidateNIC();
                }
            } else {
                dtpDOB.getEditor().setStyle(invalid);
                member.setDob(null);
                revalidateNIC();
            }
        }

    }

    //Key release for Telephone. Set value to the relavant field in the object.
    //Optional Field.
    @FXML
    private void txtTelKR(KeyEvent event) {

        if (member.setTel(txtTel.getText().trim())) {
            if (oldMember != null && !member.getTel().equals(oldMember.getTel())) {
                txtTel.setStyle(updated);
            } else {
                txtTel.setStyle(valid);
            }
        } else {
            txtTel.setStyle(invalid);
        }

    }

    //Key release for NIC. Set value to the relavant field in the object.
    //Optional Field.
    @FXML
    private void txtNICKR(KeyEvent event) {

        if (member.setNic(txtNIC.getText())) {
            if (oldMember != null && oldMember.getNic() != null && member.getNic() != null && oldMember.getNic().equals(member.getNic())) {
                txtNIC.setStyle(valid);
            } else if (oldMember != null && oldMember.getNic() != member.getNic()) {
                txtNIC.setStyle(updated);
            } else {
                txtNIC.setStyle(valid);
            }
        } else {
            txtNIC.setStyle(invalid);
        }

    }

    //Key release for Education. Set value to the relavant field in the object.
    //Optional Field.
    @FXML
    private void txtEducationKR(KeyEvent event) {

        if (member.setEducation(txtEducation.getText().trim())) {
            if (oldMember != null && !member.getEducation().equals(oldMember.getEducation())) {
                txtEducation.setStyle(updated);
            } else {
                txtEducation.setStyle(valid);
            }
        } else {
            txtEducation.setStyle(invalid);
        }

    }

    //Key release for Occupation. Set value to the relavant field in the object.
    //Optional Field.
    @FXML
    private void txtOccupationKR(KeyEvent event) {

        if (member.setOccupation(txtOccupation.getText().trim())) {
            if (oldMember != null && !member.getOccupation().equals(oldMember.getOccupation())) {
                txtOccupation.setStyle(updated);
            } else {
                txtOccupation.setStyle(valid);
            }
        } else {
            txtOccupation.setStyle(invalid);
        }

    }

    //Key release for Membership Date. Set value to the relavant field in the object.
    @FXML
    private void dtpMembershipDateAP(ActionEvent event) {

        if (dtpMembershipDate.getValue() != null) {
            Date today = new Date();
            Date domembership = java.sql.Date.valueOf(dtpMembershipDate.getValue());

            if (domembership.before(today)) {
                member.setDomembership(domembership);
                if (oldMember != null && !member.getDomembership().equals(oldMember.getDomembership())) {
                    dtpMembershipDate.getEditor().setStyle(updated);
                } else {
                    dtpMembershipDate.getEditor().setStyle(valid);
                }
            } else {
                dtpMembershipDate.getEditor().setStyle(invalid);
                member.setDomembership(null);
            }
        }

    }

    //Action Perform for Photo Select. Set value to the relavant field in the object.
    @FXML
    private void btnPhotoSelectAP(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        if (lastDirectory != null) {
            fileChooser.setInitialDirectory(lastDirectory);
        }
        fileChooser.setTitle("ගොනුව තෝරන්න");
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

                    if (img.getIconHeight() <= 200 && img.getIconWidth() <= 200) {
                        Image photo = new Image(fis);
                        imgPhoto.setImage(photo);
                        member.setPhoto(image);
                        photoSelected = true;

                    } else {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("වැරදියි !");
                        alert.setHeaderText("ඡායාරූපය තේරීම");
                        alert.setContentText("ඡායාරූපය 200x200 වඩා කුඩා විය යුතුය.");

                        ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
                        alert.getButtonTypes().setAll(btnOk);
                        alert.showAndWait();

                        photoSelected = false;
                    }
                } else {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("වැරදියි !");
                    alert.setHeaderText("ඡායාරූපය තේරීම");
                    alert.setContentText("ඡායාරූපය JPG, JPEG, GIF හෝ  PNG විය යුතුය");

                    ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(btnOk);
                    alert.showAndWait();

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

    //Action Perform for Photo Clear. Set value to the relavant field in the object.
    @FXML
    private void btnPhotoClearAP(ActionEvent event) {

        if (member.getPhoto() != null) {

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
                if (oldMember != null && oldMember.getPhoto() != null) {
                    photoSelected = true;
                } else {
                    photoSelected = false;
                }
                member.setPhoto(null);
            }
        }

    }

    //Action Perform for Status. Set value to the relavant field in the object.
    @FXML
    private void cmbStatusAP(ActionEvent event) {

        member.setMemberstatusId(cmbStatus.getSelectionModel().getSelectedItem());
        if (oldMember != null && !member.getMemberstatusId().equals(oldMember.getMemberstatusId())) {
            cmbStatus.setStyle(updated);
        } else {
            cmbStatus.setStyle(valid);
        }

    }

    //Action Perform for showing WebCam Controller.
    @FXML
    private void btnWebcamAP(ActionEvent event) {

        try {

            MyResourceBundle rb = new MyResourceBundle();

            HashMap hm = new HashMap();
            hm.put("member", member);

            rb.setHashMap(hm);

            AnchorPane itemSearchUI = FXMLLoader.load(MemberController.class.getResource("Webcam.fxml"), rb);

            DialogPane dialogPane = new DialogPane();
            dialogPane.setContent(itemSearchUI);
            dialogPane.setHeaderText("Web Cam");

            Dialog subUI = new Dialog();
            subUI.setDialogPane(dialogPane);
            subUI.showAndWait();

            if (member.getPhoto() != null) {
                photoSelected = true;
                imgPhoto.setImage(new Image(new ByteArrayInputStream(member.getPhoto())));
            } else {
                photoSelected = false;
                imgPhoto.setImage(new Image("/image/user.png"));
            }

        } catch (IOException ex) {

            System.out.println(ex.getMessage());

        }

    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Form-Operation-Methods">
    //Get errors by checking null values in the object.
    private String getErrors() {
        String errors = "";

        if (member.getMemberid() == null) {
            errors = errors + "සාමාජික අංකය වැරදි\n";
        }
        if (member.getName() == null) {
            errors = errors + "නම වැරදි\n";
        }
        if (member.getFullname() == null) {
            errors = errors + "සම්පූර්ණ නම වැරදි\n";
        }
        if (member.getGenderId() == null) {
            errors = errors + "ස්ත්‍රී/පුරුෂ බව තෝරා නොමැත\n";
        }
        if (member.getAddress() == null) {
            errors = errors + "ලිපිනය වැරදි\n";
        }
        if (member.getDob() == null) {
            errors = errors + "උපන්දිනය වැරදි\n";
        }
        if (txtNIC.getText() != null && !member.setNic(txtNIC.getText().trim())) {
            errors = errors + "ජා.හැ. අංකය වැරදි\n";
        }
        if (txtTel.getText() != null && !member.setTel(txtTel.getText().trim())) {
            errors = errors + "දුරකතන අංකය වැරදි\n";
        }
        if (txtOccupation.getText() != null && !member.setOccupation(txtOccupation.getText().trim())) {
            errors = errors + "රැකියාව වැරදි\n";
        }
        if (txtEducation.getText() != null && !member.setEducation(txtEducation.getText().trim())) {
            errors = errors + "අධ්‍යාපනික තත්තවය වැරදි\n";
        }
        if (member.getDomembership() == null) {
            errors = errors + "සාමාජිකත්වය ලැබූ දිනය වැරදී\n";
        }
        if (member.getMemberstatusId() == null) {
            errors = errors + "සාමාජික තත්වය තෝරා නොමැත\n";
        }

        return errors;

    }

    //Get updates by comparing old object and new object.
    private String getUpdates() {

        String updates = "";

        if (oldMember != null) {

            if (!member.getMemberid().equals(oldMember.getMemberid())) {
                updates = updates + "සාමාජික අංකය : " + oldMember.getMemberid() + " ---> " + member.getMemberid() + "\n";
            }
            if (!member.getName().equals(oldMember.getName())) {
                updates = updates + "නම : " + oldMember.getName() + " ---> " + member.getName() + "\n";
            }
            if (!member.getFullname().equals(oldMember.getFullname())) {
                updates = updates + "සම්පූර්ණ නම : " + oldMember.getFullname() + " ---> " + member.getFullname() + "\n";
            }
            if (!member.getGenderId().equals(oldMember.getGenderId())) {
                updates = updates + "ස්ත්‍රී/පුරුෂ බව : " + oldMember.getGenderId() + " ---> " + member.getGenderId() + "\n";
            }
            if (!member.getAddress().equals(oldMember.getAddress())) {
                updates = updates + "ලිපිනය : " + oldMember.getAddress() + " ---> " + member.getAddress() + "\n";
            }
            if (!member.getDob().equals(oldMember.getDob())) {
                updates = updates + "උපන්දිනය : " + oldMember.getDob() + " ---> " + member.getDob() + "\n";
            }
//            if (!member.getNic().equals(oldMember.getNic())) {
//                updates = updates + "ජා.හැ. අංකය : " + oldMember.getNic() + " ---> " + member.getNic() + "\n";
//            }

            if (!(oldMember.getNic() != null
                    && member.getNic() != null
                    && oldMember.getNic().equals(member.getNic()))) {
                if (oldMember.getNic() != member.getNic()) {
                    updates = updates + oldMember.getNic()
                            + " ---> " + member.getNic() + "\n";
                }
            }

            if (!(oldMember.getTel() != null && member.getTel() != null && oldMember.getTel().equals(member.getTel()))) {
                if (oldMember.getTel() != member.getTel()) {
                    updates = updates + oldMember.getTel() + " ---> " + member.getTel() + "\n";
                }
            }

            if (!(oldMember.getOccupation() != null && member.getOccupation() != null && oldMember.getOccupation().equals(member.getOccupation()))) {
                if (oldMember.getOccupation() != member.getOccupation()) {
                    updates = updates + oldMember.getOccupation() + " ---> " + member.getOccupation() + "\n";
                }
            }

            if (!(oldMember.getEducation() != null && member.getEducation() != null && oldMember.getEducation().equals(member.getEducation()))) {
                if (oldMember.getEducation() != member.getEducation()) {
                    updates = updates + oldMember.getEducation() + " ---> " + member.getEducation() + "\n";
                }
            }

            if (!member.getDomembership().equals(oldMember.getDomembership())) {
                updates = updates + "සාමාජිකත්වය ලැබූ දිනය : " + oldMember.getDomembership() + " ---> " + member.getDomembership() + "\n";
            }
            if (photoSelected) {
                updates = updates + "ඡායාරූපය වෙනස් විය\n";
            }
            if (!member.getMemberstatusId().equals(oldMember.getMemberstatusId())) {
                updates = updates + "සාමාජික තත්වය : " + oldMember.getMemberstatusId() + " ---> " + member.getMemberstatusId() + "\n";
            }

        }

        return updates;
    }

    //Fill data to the form when click on the table.
    private void fillForm() {
        if (tblMember.getSelectionModel().getSelectedItem() != null) {

            isOk = false;

            dissableButtons(false, true, false, false);

            oldMember = MemberDao.getById(tblMember.getSelectionModel().getSelectedItem().getId());
            member = MemberDao.getById(tblMember.getSelectionModel().getSelectedItem().getId());

            cmbGender.getSelectionModel().select((Gender) member.getGenderId());
            cmbStatus.getSelectionModel().select((Memberstatus) member.getMemberstatusId());

            txtMemberID.setText(String.valueOf(member.getMemberid()));
            txtName.setText(member.getName());
            txtFullname.setText(member.getFullname());
            txaAddress.setText(member.getAddress());

            /*Optional Fields*/
            txtNIC.setText(member.getNic());
            txtTel.setText(member.getTel() == null ? "" : member.getTel());
            txtOccupation.setText(member.getOccupation() == null ? "" : member.getOccupation());
            txtEducation.setText(member.getEducation() == null ? "" : member.getEducation());
            /*Optional Fields*/

            dtpDOB.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(member.getDob())));
            dtpMembershipDate.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(member.getDomembership()));

            if (member.getPhoto() != null) {
                imgPhoto.setImage(new Image(new ByteArrayInputStream(member.getPhoto())));
            } else {
                imgPhoto.setImage(new Image("/image/user.png"));
            }

            setStyle(valid);

            page = pagination.getCurrentPageIndex();
            row = tblMember.getSelectionModel().getSelectedIndex();

            isOk = true;
        }
    }

    
    //Action Perform for Add button
    @FXML
    private void btnAddAP(ActionEvent event) {

        String errors = getErrors();

        if (errors.isEmpty()) {

            //Setting confirmation message.
            String confermation = "පහත සාමාජික තොරතුරු ඇතුළත් කිරීමට අවශ්‍යද ?\n "
                    + "\nසාමාජික අංකය :--> " + member.getMemberid()
                    + "\nනම :--> " + member.getName()
                    + "\nසම්පූර්ණ නම :--> " + member.getFullname()
                    + "\nස්ත්‍රී/පුරුෂ බව :--> " + member.getGenderId().getName()
                    + "\nලිපිනය :--> " + member.getAddress()
                    + "\nඋපන්දිනය :--> " + member.getDob()
                    + "\nජා.හැ. අංකය :--> " + member.getNic()
                    + "\nදු.ක අංකය :--> " + (member.getTel() == null ? "" : member.getTel())
                    + "\nරැකියාව :--> " + member.getOccupation()
                    + "\nඅධ්‍යාපනික තත්වය :--> " + (member.getEducation() == null ? "" : member.getEducation())
                    + "\nඡායාරූපය :--> " + (member.getPhoto() == null ? "තෝරා නොමැත" : "තෝරා ඇත")
                    + "\nසාමාජික තත්වය :--> " + member.getMemberstatusId().getName();

            //Asking for the confirmation.
            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);

            if (action.get() == CustomAlerts.btnYes) {

                try {

                    //Set user to the member object.
                    member.setEmployeeId(user.getEmployeeId());

                    //Pass the member object to the MemberDao
                    MemberDao.add(member);
                    
                     //Show the notification
                    Notifications.create().title("සාර්ථකයි !").text("සාමාජික අංක " + member.getMemberid() + " වන සාමාජිකයා ඇතුළත් කරන ලදී.")
                            .position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(10.0)).showInformation();
                    loadForm(); // Make the form to default status and values.
                    updateTable(); // Refresh the Table.
                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblMember.getSelectionModel().select(tblMember.getItems().size() - 1);

                } catch (DaoException ex) {
                    Notifications.create().title("අසාර්ථකයි !").text("සාමාජික අංක " + member.getName() + " වන සාමාජිකයා ඇතුළත් කිරීමට නොහැක. \n නැවත උත්සහා කරන්න.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

                }

            }

        } else {

            CustomAlerts.showAddError(errors);

        }
    }

    //Action Perform for Delete button
    @FXML
    private void btnDeleteAP(ActionEvent event) {

        //Check for exsistance of Updates and Errors
        if (getUpdates().isEmpty() && getErrors().isEmpty()) {

            //Confirmation Message
            Optional<ButtonType> action = CustomAlerts.showDeleteConfirmation();

            if (action.get() == CustomAlerts.btnYes) {

                //Passing member object to MemberDao
                MemberDao.delete(member);
                Notifications.create().title("සාර්ථකයි !").text("සාමාජික අංක " + member.getMemberid() + " වන සාමාජිකයා ඉවත් කරන ලදී.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                //Reload the form
                loadForm();
                updateTable();
                pagination.setCurrentPageIndex(page);
                tblMember.getSelectionModel().select(row);

            }
        } else {

            CustomAlerts.showDeleteError();

        }

    }

    //Action Perform for Update button
    @FXML
    private void btnUpdateAP(ActionEvent event) {

        String errors = getErrors();

        if (errors.isEmpty()) {

            String updates = getUpdates();

            if (!updates.isEmpty()) {

                Optional<ButtonType> action = CustomAlerts.showUpdateConfirmation(updates);

                if (action.get() == CustomAlerts.btnYes) {

                    MemberDao.update(member);
                    Notifications.create().title("සාර්ථකයි !").text(member.getName() + ": සාමාජික තොරතුරු යාවත්කාලීන කරන ලදී.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

                    updateTable();
                    loadForm();
                    pagination.setCurrentPageIndex(page);
                    tblMember.getSelectionModel().select(row);

                }
            } else {

                CustomAlerts.showNoUpdatesError();

            }
        } else {
            CustomAlerts.showUpdatesError(getErrors());
        }

    }

    //Action Perform for Clear button
    @FXML
    private void btnClearAP(ActionEvent event) {

        Optional<ButtonType> action = CustomAlerts.showClearFormConfirmation();

        if (action.get() == CustomAlerts.btnYes) {
            loadForm();
        }
    }

    //Mouse Click AP for Table.
    //Trigger when click on the table row.
    @FXML
    private void tblMemberMC(MouseEvent event) {
        fillForm();
    }

    //Key Press AP for Table.
    //Trigger when select and press a key on the table.
    @FXML
    private void tblMemberKR(KeyEvent event) {
        fillForm();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Search-Methods">
    //Update the content of the Table.
    private void updateTable() {

        String memberid = txtSearchMemberID.getText().trim();
        boolean smemberid = !memberid.isEmpty();

        String name = txtSearchName.getText().trim();
        boolean sname = !name.isEmpty();

        String nic = txtSearchNIC.getText().trim();
        boolean snic = !nic.isEmpty();

        Memberstatus status = cmbSearchStatus.getSelectionModel().getSelectedItem();
        boolean sstatus = cmbSearchStatus.getSelectionModel().getSelectedIndex() != -1;

        if (!sname && !sstatus && !snic && !smemberid) {
            fillTable(MemberDao.getAll());
        }
        if (!sname && sstatus && !snic && !smemberid) {
            fillTable(MemberDao.getAllByStatus(status));
        }
        if (!sname && !sstatus && snic && !smemberid) {
            fillTable(MemberDao.getAllByNic(nic));
        }
        if (sname && !sstatus && !snic && !smemberid) {
            fillTable(MemberDao.getAllByName(name));
        }
        if (!sname && !sstatus && !snic && smemberid) {
            fillTable(MemberDao.getAllByMemberid(memberid));
        }

    }

    //Search by Member ID
    @FXML
    private void txtSearchMemberIDKR(KeyEvent event) {

        cmbSearchStatus.getSelectionModel().select(-1);
        txtSearchName.setText("");
        txtSearchNIC.setText("");
        updateTable();

    }

    //Search By Name
    @FXML
    private void txtSearchNameKR(KeyEvent event) {

        cmbSearchStatus.getSelectionModel().select(-1);
        txtSearchMemberID.setText("");
        txtSearchNIC.setText("");
        updateTable();

    }

    //Search By NIC
    @FXML
    private void txtSearchNICKR(KeyEvent event) {

        cmbSearchStatus.getSelectionModel().select(-1);
        txtSearchMemberID.setText("");
        txtSearchName.setText("");
        updateTable();

    }

    //Search By Status
    @FXML
    private void cmbSearchStatusAP(ActionEvent event) {

        txtSearchNIC.setText("");
        txtSearchMemberID.setText("");
        txtSearchName.setText("");
        updateTable();

    }

    //AP for Search Clear button.
    @FXML
    private void btnSearchClearAP(ActionEvent event) {

        Optional<ButtonType> action = CustomAlerts.showSearchClearConfirmation();

        if (action.get() == CustomAlerts.btnYes) {
            loadTable();
        }

    }

    @FXML
    private void cmbSearchStatusKR(KeyEvent event) {

    }

//</editor-fold>
}
