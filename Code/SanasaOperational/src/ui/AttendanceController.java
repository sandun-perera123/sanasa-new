/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.AttendancetypeDao;
import dao.DaoException;
import dao.MeetingDao;
import dao.MeetingstatusDao;
import dao.MeetingtypeDao;
import dao.MemberDao;
import entity.Attendance;
import entity.Attendancetype;
import entity.Meeting;
import entity.Meetingstatus;
import entity.Meetingtype;
import entity.Smember;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
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
 * @author Sandun-PC
 */
public class AttendanceController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private ComboBox<Meetingtype> cmbMeetingType;
    @FXML
    private ComboBox<Meeting> cmbMeeting;
    @FXML
    private TextField txtMeetingDate;
    @FXML
    private TextField txtExpectedCount;
    @FXML
    private TextField txtCount;
    @FXML
    private DatePicker dtpFrom;
    @FXML
    private DatePicker dtpTo;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Attendance> tblAttendance;
    @FXML
    private TableColumn<Attendance, Integer> colMemberID;
    @FXML
    private TableColumn<Attendance, Smember> colMemberName;
    @FXML
    private TableColumn<Attendance, ComboBox> colAttendanceType;
    @FXML
    private TableColumn<Attendance, TextField> colReason;
    @FXML
    private TextField txtSearchMemberID;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnAdd;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Module-Data">
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Meeting meeting;

    //Update Identification
    private Meeting oldMeeting;

    //Table Row, Page Selected
    private int page;
    private int row;

    List<Attendance> atds;
    
    ResourceBundle rb;

//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Initializing-Methods">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        loadForm();
        loadTable();
        setData();

    }
    
    private void setData(){
     
        if(rb != null){
            
            meeting = (Meeting) rb.getObject("meeting");
            
            dtpFrom.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(meeting.getDomeeting())));
            dtpTo.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(meeting.getDomeeting())));
            
            cmbMeeting.getSelectionModel().select(meeting);
            cmbMeetingType.getSelectionModel().select(meeting.getMeetingtypeId());
            
            txtExpectedCount.setText(meeting.getExpectedcount().toString());
            txtMeetingDate.setText(meeting.getDomeeting().toString());
            
            txtSearchMemberID.setDisable(false);
            
            cmbMeetingType.setDisable(true);
            dtpFrom.setDisable(true);
            dtpTo.setDisable(true);
            cmbMeeting.setDisable(true);

            fillTable(FXCollections.observableArrayList(meeting.getAttendanceList()));
            
            updateCount();

        }
        
    }

    private void loadForm() {

        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        meeting = null;
        oldMeeting = null;
        atds = null;
        tblAttendance.getItems().clear();
        
        txtSearchMemberID.setDisable(true);

        cmbMeetingType.setItems(MeetingtypeDao.getAll());
        cmbMeetingType.getSelectionModel().select(-1);

        cmbMeeting.getItems().clear();
        cmbMeeting.getSelectionModel().select(-1);

        dtpFrom.setValue(null);
        dtpTo.setValue(null);

        txtMeetingDate.setText("");
        txtExpectedCount.setText("");
        txtCount.setText("");

        dissableButtons(false, false, true, true);
        setStyle(initial);

    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Attendance_insert"));
    }

    private void setStyle(String style) {
        cmbMeetingType.setStyle(style);
        dtpFrom.getEditor().setStyle(style);
        dtpTo.getEditor().setStyle(style);
        cmbMeeting.setStyle(style);
    }

    private void fillTable(List<Attendance> attendances) {

        if (privilage.get("Attendance_select")) {

            if (attendances != null && attendances.size() != 0) {
                generatePagination(attendances);
            } else {

                for (Object m : MemberDao.getAll()) {

                    Attendance att = new Attendance();
                    att.setMeetingId(meeting);
                    att.setAttendancetypeId(AttendancetypeDao.getById(1));
                    att.setSmemberId((Smember) m);
                    att.setReason("");

                    attendances.add(att);

                }

                generatePagination(attendances);

            }

            atds = attendances;

        } else {

            pagination.setPageCount(1);
            tblAttendance.getItems().clear();

        }

    }

    private void loadTable() {

        txtSearchMemberID.setText("");

        colMemberName.setCellValueFactory(new PropertyValueFactory("smemberId"));

        colMemberID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Attendance, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Attendance, Integer> row) {
                return new SimpleObjectProperty(row.getValue().getSmemberId().getMemberid());
            }
        });

        colAttendanceType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Attendance, ComboBox>, ObservableValue<ComboBox>>() {
            @Override
            public ObservableValue<ComboBox> call(TableColumn.CellDataFeatures<Attendance, ComboBox> row) {

                ComboBox comboBox = new ComboBox();
                comboBox.setPromptText("පැමිණීම");
                comboBox.setItems(AttendancetypeDao.getAll());
                comboBox.getSelectionModel().select(row.getValue().getAttendancetypeId());

                comboBox.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        row.getValue().setAttendancetypeId((Attendancetype) comboBox.getSelectionModel().getSelectedItem());
                        updateCount();

                    }

                });
                return new SimpleObjectProperty(comboBox);

            }
        });

        colReason.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Attendance, TextField>, ObservableValue<TextField>>() {
            @Override
            public ObservableValue<TextField> call(TableColumn.CellDataFeatures<Attendance, TextField> row) {

                TextField txtField = new TextField();
                txtField.setPromptText("සටහන්");
                txtField.setText(row.getValue().getReason());

                txtField.setOnKeyReleased(new EventHandler<KeyEvent>() {

                    @Override
                    public void handle(KeyEvent event) {
                        row.getValue().setReason(txtField.getText().trim());
                    }

                });
                return new SimpleObjectProperty(txtField);

            }
        });

        pagination.setCurrentPageIndex(0);

    }

    private void generatePagination(List<Attendance> attendances) {

        int rowsCount = 50;
        int pageCount = ((attendances.size() - 1) / rowsCount) + 1;
        pagination.setPageCount(pageCount);

        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                int start = pageIndex * rowsCount;
                int end = pageIndex == pageCount - 1 ? attendances.size() : pageIndex * rowsCount + rowsCount;
                tblAttendance.getItems().clear();
                tblAttendance.setItems(FXCollections.observableArrayList(attendances.subList(start, end)));
                return tblAttendance;
            }
        });

    }

//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Data-Settings-Methods">
    
    
    private void setMeetings() {

        if (cmbMeetingType.getSelectionModel().getSelectedIndex() != -1
                && dtpFrom.getValue() != null
                && dtpTo.getValue() != null) {

            Meetingtype meetingtype = cmbMeetingType.getSelectionModel().getSelectedItem();
            Date from = java.sql.Date.valueOf(dtpFrom.getValue());
            Date to = java.sql.Date.valueOf(dtpTo.getValue());

            cmbMeeting.getItems().clear();
            cmbMeeting.setItems(MeetingDao.getAllByTypeDate(meetingtype, from, to));

        } else {

            cmbMeeting.getItems().clear();
            cmbMeeting.getSelectionModel().select(-1);

        }

    }

    private void updateCount() {

        Integer count = 0;

        for (Attendance atd : atds) {

            if (atd.getAttendancetypeId().getId() == 1) {
                count++;
            }

        }

        meeting.setCount(count);
        txtCount.setText(meeting.getCount().toString());

    }

    @FXML
    private void cmbMeetingTypeAP(ActionEvent event) {
        
        if(cmbMeetingType.getSelectionModel().getSelectedItem() != null){
            cmbMeetingType.setStyle(valid);
            setMeetings();
        }
        
        
    }

    @FXML
    private void cmbMeetingAP(ActionEvent event) {

        if (cmbMeeting.getSelectionModel().getSelectedIndex() != -1) {
            
            cmbMeeting.setStyle(valid);
            
            txtSearchMemberID.setDisable(false);

            meeting = cmbMeeting.getSelectionModel().getSelectedItem();

            txtExpectedCount.setText(meeting.getExpectedcount().toString());
            txtMeetingDate.setText(meeting.getDomeeting().toString());

            fillTable(FXCollections.observableArrayList(meeting.getAttendanceList()));
            
            updateCount();

        }

    }

    @FXML
    private void dtpFromAP(ActionEvent event) {
        if(dtpFrom.getValue() != null){
            dtpFrom.getEditor().setStyle(valid);
            setMeetings();
        }
    }

    @FXML
    private void dtpToAP(ActionEvent event) {
        if(dtpTo.getValue() != null){
            dtpTo.getEditor().setStyle(valid);
            setMeetings();
        }
    }
//</editor-fold>
  
//<editor-fold defaultstate="collapsed" desc="Form-Operation-Methods">
    private String getErrors() {
        String errors = "";
        
        if (meeting == null) {
            errors = errors + "කාරක සභාවක් තෝරා නොමැත\n";
        }
        
        
        
        return errors;
        
    }
    
    @FXML
    private void btnAddAP(ActionEvent event) {
        
        String errors = getErrors();
        
        if (errors.isEmpty()) {
            
            meeting.setAttendanceList(atds);
            
            String confermation = "පහත තොරතුරු ඇතුළත් කිරීමට අවශ්‍යද?\n "
                    + "\nදිනය :         \t\t" + meeting.getDomeeting()
                    + "\nබලාපොරොත්තු වූ ගණන :       \t\t" + meeting.getExpectedcount()
                    + "\nපැමිණි ගණන :  \t\t" + meeting.getCount();
            

            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);

            if (action.get() == CustomAlerts.btnYes) {
                
                Meetingstatus status = MeetingstatusDao.getById(1);
                meeting.setMeetingstatusId(status);
                
                MeetingDao.update(meeting);
                Notifications.create().title("සාර්ථකයි").text(meeting.getDomeeting()+ " දින පැවති කාරක සභාවෙහි පැමිණීම් තොරතුරු ඇතුළත් කරන ලදී").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                loadForm();
                pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                tblAttendance.getSelectionModel().select(tblAttendance.getItems().size() - 1);
                
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
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Search-Methods">
    @FXML
    private void txtSearchMemberIDAP(KeyEvent event) {
        
        if(txtSearchMemberID.getText().length() == 4 || txtSearchMemberID.getText().length() == 0){
            updateTable();
        }
        
    }
    
    private void updateTable() {
        
        List<Attendance> list = new ArrayList<Attendance>();
        
        String member = txtSearchMemberID.getText().trim();
        boolean smember = !member.isEmpty();
        
        //Load All Data
        if (!smember) {
            fillTable(atds);
        }
        
        
        //Serach By Member ID
        if (smember) {
            
           
            list = atds.stream()
                    .filter(x -> x.getSmemberId().getMemberid().startsWith(member))
                    .collect(Collectors.toList());
            
            generatePagination(list);
        }
        
        
    }
//</editor-fold>

}
