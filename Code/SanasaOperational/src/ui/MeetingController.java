/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import com.jfoenix.controls.JFXDatePicker;
import dao.DaoException;
import dao.MeetingDao;
import dao.MeetingstatusDao;
import dao.MeetingtypeDao;
import dao.MemberDao;
import entity.Meeting;
import entity.Meetingstatus;
import entity.Meetingtype;
import entity.Smember;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
 * @author Sandun
 */
public class MeetingController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="Module-Data">
//Color Indication of Input Controls
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

    Date from, to;
    boolean sfrom, sto;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private ComboBox<Meetingtype> cmbType;
    @FXML
    private DatePicker dtpMeetingDate;
    @FXML
    private TextArea txaPurpose;
    @FXML
    private TextField txtExpectedCount;
    @FXML
    private TextField txtCount;
    @FXML
    private ListView<Smember> lstLeft;
    @FXML
    private ListView<Smember> lstRight;
    @FXML
    private ComboBox<Meetingstatus> cmbStatus;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;
    @FXML
    private ComboBox<Meetingtype> cmbSearchType;
    @FXML
    private DatePicker dtpSearchFromDate;
    @FXML
    private DatePicker dtpSearchToDate;
    @FXML
    private ComboBox<Meetingstatus> cmbSearchStatus;
    @FXML
    private Button btnSearchClear;
    @FXML
    private TableView<Meeting> tblMeeting;
    @FXML
    private TableColumn<Meeting, Date> colMeetingDate;
    @FXML
    private TableColumn<Meeting, String> colStartTime;
    @FXML
    private TableColumn<Meeting, String> colEndTime;
    @FXML
    private TableColumn<Meeting, Integer> colCount;
    @FXML
    private TableColumn<Meeting, Meetingstatus> colStatus;
    @FXML
    private Pagination pagination;
    @FXML
    private Button btnRight;
    @FXML
    private Button btnLeft;
    @FXML
    private Button btnRightAll;
    @FXML
    private Button btnLeftAll;
    @FXML
    private JFXDatePicker tpSTime;
    @FXML
    private JFXDatePicker tpETime;
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

        meeting = new Meeting();
        oldMeeting = null;

        cmbType.setItems(MeetingtypeDao.getAll());
        cmbType.getSelectionModel().select(-1);
        
        cmbStatus.setItems(MeetingstatusDao.getAll());
        cmbStatus.getSelectionModel().select(2);
        meeting.setMeetingstatusId(MeetingstatusDao.getById(3));

        tpSTime.timeProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                if (tpSTime.getTime() != null && validateTime()) {
                    meeting.setStarttime(java.sql.Time.valueOf(newValue));

                    if (oldMeeting != null && !meeting.getStarttime().equals(oldMeeting.getStarttime())) {

                        tpSTime.setStyle(updated);
                        System.out.println(meeting.getStarttime());

                    } else {
                        tpSTime.setStyle(valid);
                        System.out.println(meeting.getStarttime());
                    }
                } else {

                    meeting.setStarttime(null);
                    tpSTime.setStyle(invalid);
                }
            }
        });

        tpETime.timeProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                if (tpETime.getTime() != null && validateTime()) {
                    meeting.setEndtime(java.sql.Time.valueOf(newValue));

                    if (oldMeeting != null && !meeting.getEndtime().equals(oldMeeting.getEndtime())) {

                        tpETime.setStyle(updated);

                    } else {
                        tpETime.setStyle(valid);
                    }
                } else {

                    meeting.setEndtime(null);
                    tpETime.setStyle(invalid);
                }
            }
        });

        tpSTime.setTime(null);
        tpETime.setTime(null);
        tpSTime.getEditor().clear();
        tpETime.getEditor().clear();

        txaPurpose.setText("");
        txtExpectedCount.setText("");
        txtCount.setDisable(true);
        txtCount.setText("");

        lstLeft.setItems(MemberDao.getAll());
        lstRight.getItems().clear();

        dtpMeetingDate.setValue(null);

        dissableButtons(false, false, true, true);
        validateList();
        setStyle(initial);

    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {

        btnAdd.setDisable(insert || !privilage.get("Meeting_insert"));
        btnUpdate.setDisable(update || !privilage.get("Meeting_update"));
        btnDelete.setDisable(delete || !privilage.get("Meeting_delete"));

        cmbSearchType.setDisable(select || !privilage.get("Meeting_select"));
        cmbSearchStatus.setDisable(select || !privilage.get("Meeting_select"));
        dtpSearchFromDate.setDisable(select || !privilage.get("Meeting_select"));
        dtpSearchToDate.setDisable(select || !privilage.get("Meeting_select"));

        btnSearchClear.setDisable(select || !privilage.get("Meeting_select"));
    }

    private void setStyle(String style) {

        cmbType.setStyle(style);
        cmbStatus.setStyle(valid);

        tpSTime.setStyle(style);
        tpETime.setStyle(style);

        txtExpectedCount.setStyle(style);
        txtCount.setStyle(style);

        if (!txaPurpose.getChildrenUnmodifiable().isEmpty()) {
            ((ScrollPane) txaPurpose.getChildrenUnmodifiable().get(0)).getContent().setStyle(style);
        }

        lstLeft.setStyle(style);
        lstRight.setStyle(style);

        dtpMeetingDate.getEditor().setStyle(style);

    }

    private void loadTable() {

        cmbSearchType.setItems(MeetingtypeDao.getAll());
        cmbSearchType.getSelectionModel().select(-1);
        cmbSearchStatus.setItems(MeetingstatusDao.getAll());
        cmbSearchStatus.getSelectionModel().select(-1);

        dtpSearchFromDate.setValue(null);
        dtpSearchToDate.setValue(null);

        colMeetingDate.setCellValueFactory(new PropertyValueFactory("domeeting"));

        colStartTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meeting, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meeting, String> p) {
                if (p.getValue() != null) {

                    if (p.getValue().getStarttime().getHours() > 12) {

                        String hh = String.valueOf(String.format("%02d", p.getValue().getStarttime().getHours() - 12));
                        String mm = String.valueOf(String.format("%02d", p.getValue().getStarttime().getMinutes()));
                        return new SimpleStringProperty(hh + " : " + mm + " PM");

                    } else {

                        String hh = String.valueOf(String.format("%02d", p.getValue().getStarttime().getHours()));
                        String mm = String.valueOf(String.format("%02d", p.getValue().getStarttime().getMinutes()));
                        return new SimpleStringProperty(hh + " : " + mm + " AM");

                    }

                } else {
                    return new SimpleStringProperty();
                }

            }
        });

        colEndTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meeting, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meeting, String> p) {
                if (p.getValue() != null) {

                    if (p.getValue().getEndtime().getHours() > 12) {

                        String hh = String.valueOf(String.format("%02d", p.getValue().getEndtime().getHours() - 12));
                        String mm = String.valueOf(String.format("%02d", p.getValue().getEndtime().getMinutes()));
                        return new SimpleStringProperty(hh + " : " + mm + " PM");

                    } else {

                        String hh = String.valueOf(String.format("%02d", p.getValue().getEndtime().getHours()));
                        String mm = String.valueOf(String.format("%02d", p.getValue().getEndtime().getMinutes()));
                        return new SimpleStringProperty(hh + " : " + mm + " AM");

                    }

                } else {
                    return new SimpleStringProperty();
                }

            }
        });

        colCount.setCellValueFactory(new PropertyValueFactory("count"));
        colStatus.setCellValueFactory(new PropertyValueFactory("meetingstatusId"));

        fillTable(MeetingDao.getAll());
        pagination.setCurrentPageIndex(0);

    }

    private void fillTable(ObservableList<Meeting> meetings) {

        if (privilage.get("Meeting_select") && meetings != null && meetings.size() != 0) {

            int rowsCount = 10;
            int pageCount = ((meetings.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? meetings.size() : pageIndex * rowsCount + rowsCount;
                    tblMeeting.getItems().clear();
                    tblMeeting.setItems(FXCollections.observableArrayList(meetings.subList(start, end)));
                    return tblMeeting;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblMeeting.getItems().clear();
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Data-Setting-Methods">
    private boolean validateTime() {
        System.out.println("1");
        if (tpSTime.getTime() != null && tpETime.getTime() != null) {
            System.out.println("2");
            try {
                LocalTime stime = tpSTime.getTime();
                LocalTime etime = tpETime.getTime();

                SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                Date sdatetime = parser.parse(stime.toString());
                Date edatetime = parser.parse(etime.toString());
                System.out.println("3");
                if (sdatetime.after(edatetime)) {

                    return false;

                } else {

                    return true;

                }
            } catch (ParseException msg) {
                System.out.println("4");
                System.out.println(msg.getMessage());
            }

        }
        System.out.println("5");
        return true;

    }

    private void validateList() {

        if (meeting.setSmemberList(lstRight.getItems())) {
            lstRight.setStyle(valid);
        } else {
            lstRight.setStyle(invalid);
        }

        if (oldMeeting != null && meeting.getSmemberList() != null && !(meeting.getSmemberList().containsAll(oldMeeting.getSmemberList()) && oldMeeting.getSmemberList().containsAll(meeting.getSmemberList()))) {
            lstRight.setStyle(updated);
        }

        if (lstLeft.getItems().isEmpty()) {
            btnRight.setDisable(true);
            btnRightAll.setDisable(true);
            btnLeft.setDisable(false);
            btnLeftAll.setDisable(false);
        } else if (lstRight.getItems().isEmpty()) {
            btnRight.setDisable(false);
            btnRightAll.setDisable(false);
            btnLeft.setDisable(true);
            btnLeftAll.setDisable(true);
        } else {
            btnRight.setDisable(false);
            btnRightAll.setDisable(false);
            btnLeft.setDisable(false);
            btnLeftAll.setDisable(false);

        }

    }

    @FXML
    private void cmbTypeAP(ActionEvent event) {

        meeting.setMeetingtypeId(cmbType.getSelectionModel().getSelectedItem());
        if (oldMeeting != null && !meeting.getMeetingtypeId().equals(oldMeeting.getMeetingtypeId())) {
            cmbType.setStyle(updated);
        } else {
            cmbType.setStyle(valid);
        }

    }

    @FXML
    private void dtpMeetingDateAP(ActionEvent event) {

        if (dtpMeetingDate.getValue() != null) {

            Date domeeting = java.sql.Date.valueOf(dtpMeetingDate.getValue());
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            
            if (domeeting.after(cal.getTime())) {
                meeting.setDomeeting(domeeting);
                if (oldMeeting != null && !meeting.getDomeeting().equals(oldMeeting.getDomeeting())) {
                    dtpMeetingDate.getEditor().setStyle(updated);
                } else {
                    dtpMeetingDate.getEditor().setStyle(valid);
                }
            } else {
                dtpMeetingDate.getEditor().setStyle(invalid);
                meeting.setDomeeting(null);
            }

        }

    }

    @FXML
    private void btnRightAP(ActionEvent event) {

        lstRight.getItems().addAll(lstLeft.getSelectionModel().getSelectedItems());
        lstLeft.getItems().removeAll(lstLeft.getSelectionModel().getSelectedItems());
        validateList();

    }

    @FXML
    private void btnLeftAP(ActionEvent event) {

        lstLeft.getItems().addAll(lstRight.getSelectionModel().getSelectedItems());
        lstRight.getItems().removeAll(lstRight.getSelectionModel().getSelectedItems());
        validateList();

    }

    @FXML
    private void btnRightAllAP(ActionEvent event) {

        lstRight.setItems(MemberDao.getAll());
        lstLeft.getItems().clear();
        validateList();

    }

    @FXML
    private void btnLeftAllAP(ActionEvent event) {

        lstLeft.setItems(MemberDao.getAll());
        lstRight.getItems().clear();
        validateList();

    }

    @FXML
    private void txaPurposeKR(KeyEvent event) {
        if (meeting.setPurpose(txaPurpose.getText().trim())) {
            if (oldMeeting != null && !meeting.getPurpose().equals(oldMeeting.getPurpose())) {
                ((ScrollPane) txaPurpose.getChildrenUnmodifiable().get(0)).getContent().setStyle(updated);
            } else {
                ((ScrollPane) txaPurpose.getChildrenUnmodifiable().get(0)).getContent().setStyle(valid);
            }
        } else {
            ((ScrollPane) txaPurpose.getChildrenUnmodifiable().get(0)).getContent().setStyle(invalid);
        }
    }

    @FXML
    private void txtExpectedCountKR(KeyEvent event) {

        String count = txtExpectedCount.getText().trim();

        if (count.matches("^[0-9]{2}$") && meeting.setExpectedcount(Integer.valueOf(count))) {
            if (oldMeeting != null && !meeting.getExpectedcount().equals(oldMeeting.getExpectedcount())) {
                txtExpectedCount.setStyle(updated);
            } else {
                txtExpectedCount.setStyle(valid);
            }
        } else {
            txtExpectedCount.setStyle(invalid);
            meeting.setExpectedcount(null);
        }

    }

    @FXML
    private void cmbStatusAP(ActionEvent event) {

        meeting.setMeetingstatusId(cmbStatus.getSelectionModel().getSelectedItem());
        if (oldMeeting != null && !meeting.getMeetingstatusId().equals(oldMeeting.getMeetingstatusId())) {
            cmbStatus.setStyle(updated);
        } else {
            cmbStatus.setStyle(valid);
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Form-Operation-Methods">
    private String getErrors() {

        String errors = "";

        if (meeting.getMeetingtypeId() == null) {
            errors = errors + "සභා වර්ගය තෝරා නැත !\n";
        }
        if (meeting.getDomeeting() == null) {
            errors = errors + "සභා දිනය වැරදියි !\n";
        }
        if (meeting.getStarttime() == null) {
            errors = errors + "ආරම්භක වේලාව වැරදියි !\n";
        }

        if (meeting.getEndtime() == null) {
            errors = errors + "අවසන් වන වේලාව වැරදියි !\n";
        }

        if (txaPurpose.getText() != null && !meeting.setPurpose(txaPurpose.getText().trim())) {
            errors = errors + "හේතුව වැරදියි !\n";
        }

        if (meeting.getExpectedcount() == null) {
            errors = errors + "බලාපොරොත්තු ගණන වැරදියි !\n";
        }

        if (meeting.getSmemberList() == null) {
            errors = errors + "කාරක සභිකයන්  තෝරා නොමැත !\n";
        }

        if (meeting.getMeetingstatusId() == null) {
            errors = errors + "තත්ත්වය තෝරා නොමැත !\n";
        }

        return errors;

    }

    private String getUpdates() {

        String updates = "";

        if (oldMeeting != null) {

            if (meeting.getMeetingtypeId() != null && !meeting.getMeetingtypeId().equals(oldMeeting.getMeetingtypeId())) {
                updates = updates + oldMeeting.getMeetingtypeId() + " :-> " + meeting.getMeetingtypeId() + "\n";
            }

            if (!meeting.getDomeeting().equals(oldMeeting.getDomeeting())) {
                updates = updates + oldMeeting.getDomeeting() + " :-> " + meeting.getDomeeting() + "\n";
            }

            if (!meeting.getStarttime().equals(oldMeeting.getStarttime())) {
                updates = updates + oldMeeting.getStarttime() + " :-> " + meeting.getStarttime() + "\n";
            }

            if (!meeting.getEndtime().equals(oldMeeting.getEndtime())) {
                updates = updates + oldMeeting.getEndtime() + " :-> " + meeting.getEndtime() + "\n";
            }
            
            if (!(oldMeeting.getPurpose() != null && meeting.getPurpose() != null && oldMeeting.getPurpose().equals(meeting.getPurpose()))) {
                if (oldMeeting.getPurpose() != meeting.getPurpose()) {
                    updates = updates + oldMeeting.getPurpose() + " :-> " + meeting.getPurpose() + "\n";
                }
            }
            

            if (!meeting.getExpectedcount().equals(oldMeeting.getExpectedcount())) {
                updates = updates + oldMeeting.getExpectedcount() + " :-> " + meeting.getExpectedcount() + "\n";
            }

            if (meeting.getSmemberList() != null && !meeting.getSmemberList().equals(oldMeeting.getSmemberList())) {
                updates = updates + oldMeeting.getSmemberList().toString() + " :-> " + meeting.getSmemberList().toString() + "\n";
            }

            if (!meeting.getMeetingstatusId().equals(oldMeeting.getMeetingstatusId())) {
                updates = updates + oldMeeting.getMeetingstatusId() + " :-> " + meeting.getMeetingstatusId() + "\n";
            }

        }

        return updates;
    }

    private void fillForm() {
        if (tblMeeting.getSelectionModel().getSelectedItem() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldMeeting = MeetingDao.getById(tblMeeting.getSelectionModel().getSelectedItem().getId());
            meeting = MeetingDao.getById(tblMeeting.getSelectionModel().getSelectedItem().getId());

            cmbType.getSelectionModel().select(meeting.getMeetingtypeId());
            cmbStatus.getSelectionModel().select(meeting.getMeetingstatusId());

            dtpMeetingDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(meeting.getDomeeting())));

            if (meeting.getStarttime() != null && meeting.getEndtime() != null) {

                DateTimeFormatter formatter
                        = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S", Locale.US);

                String stime = meeting.getStarttime().toString();
                String etime = meeting.getEndtime().toString();

                LocalDateTime localSDateTime = LocalDateTime.parse(stime, formatter);
                LocalTime localSTime = localSDateTime.toLocalTime();

                LocalDateTime localEDateTime = LocalDateTime.parse(etime, formatter);
                LocalTime localETime = localEDateTime.toLocalTime();

                tpSTime.setTime(localSTime);
                tpETime.setTime(localETime);

            }

            txaPurpose.setText(meeting.getPurpose());
            txtExpectedCount.setText(String.valueOf(meeting.getExpectedcount()));
            txtCount.setText(String.valueOf(meeting.getCount()));

            lstRight.setItems(FXCollections.observableArrayList(meeting.getSmemberList()));
            lstLeft.setItems(MemberDao.getAll());
            lstLeft.getItems().removeAll(FXCollections.observableArrayList(meeting.getSmemberList()));

            validateList();

            
            
            page = pagination.getCurrentPageIndex();
            row = tblMeeting.getSelectionModel().getSelectedIndex();
        }
    }

    @FXML
    private void btnAddAP(ActionEvent event) {

        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "පහත තොරතුරු ඇතුළත් කිරීමට අවශ්‍යද ?\n "
                    + "\nසභා වර්ගය : -> " + meeting.getMeetingtypeId().getName()
                    + "\nසභා දිනය : -> " + meeting.getDomeeting()
                    + "\nආරම්භක වේලාව  : -> " + meeting.getStarttime()
                    + "\nඅවසන් වන වේලාව : -> " + meeting.getEndtime()
                    + "\nහේතුව : -> " + meeting.getPurpose()
                    + "\nබලාපොරොත්තු ගණන : -> " + meeting.getExpectedcount()
                    + "\nකාරක සභිකයන්  : -> " + meeting.getSmemberList().toString()
                    + "\nතත්ත්වය  : -> " + meeting.getMeetingstatusId().getName();

            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);

            if (action.get() == CustomAlerts.btnYes) {

                try {

                    meeting.setDate(new Date());
                    meeting.setEmployeeId(user.getEmployeeId());

                    MeetingDao.add(meeting);
                    Notifications.create().title("සාර්ථකයි !").text(meeting.getDomeeting() + " දින කාරක සභාව සාර්ථකව ඇතුළත් කරන ලදි.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                    loadForm();
                    updateTable();
                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblMeeting.getSelectionModel().select(tblMeeting.getItems().size() - 1);

                } catch (DaoException ex) {
                    Notifications.create().title("අසාර්ථකයි !").text(meeting.getDomeeting() + " දින කාරක සභාව ඇතුළත් කිරීමට නොහැක. \n නැවත උත්සහා කරන්න.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
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

                MeetingDao.delete(meeting);
                Notifications.create().title("සාර්ථකයි !").text(meeting.getDomeeting() + " දින කාරක සභාව දත්ත ගොනුවෙන් ඉවත් කරන ලදී.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                loadForm();
                updateTable();
                pagination.setCurrentPageIndex(page);
                tblMeeting.getSelectionModel().select(row);

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

                    MeetingDao.update(meeting);
                    Notifications.create().title("සාර්ථකයි !").text(meeting.getDomeeting() + " දින කාරක සභාව යාවත්කාලීන කරන ලදී.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                    updateTable();
                    loadForm();
                    pagination.setCurrentPageIndex(page);
                    tblMeeting.getSelectionModel().select(row);

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
    private void tblMeetingKP(KeyEvent event) {
        fillForm();
    }

    @FXML
    private void tblMeetingMC(MouseEvent event) {
        fillForm();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Search-Methods">
    private void updateTable() {

        Meetingtype meetingtype = cmbSearchType.getSelectionModel().getSelectedItem();
        boolean smeetingtype = cmbSearchType.getSelectionModel().getSelectedIndex() != -1;
        Meetingstatus meetingstatus = cmbSearchStatus.getSelectionModel().getSelectedItem();
        boolean smeetingstatus = cmbSearchStatus.getSelectionModel().getSelectedIndex() != -1;

        sfrom = sto = false;
        from = to = null;

        if (dtpSearchFromDate.getValue() != null && dtpSearchToDate.getValue() != null) {
            from = java.sql.Date.valueOf(dtpSearchFromDate.getValue());
            sfrom = dtpSearchFromDate.getValue() != null;

            to = java.sql.Date.valueOf(dtpSearchToDate.getValue());
            sto = dtpSearchToDate.getValue() != null;
        }

        if (!smeetingtype && !(sfrom && sto) && !smeetingstatus) {
            fillTable(MeetingDao.getAll());
        }

        if (smeetingtype && !(sfrom && sto) && !smeetingstatus) {
            fillTable(MeetingDao.getAllByMeetingtype(meetingtype));
        }

        if (!smeetingtype && !(sfrom && sto) && smeetingstatus) {
            fillTable(MeetingDao.getAllByMeetingstatus(meetingstatus));
        }

        if (!smeetingtype && (sfrom && sto) && !smeetingstatus) {
            fillTable(MeetingDao.getAllByMeetingdate(from, to));
        }

    }

    private void searchByDate() {
        if (dtpSearchFromDate.getValue() != null && dtpSearchToDate.getValue() != null) {
            cmbSearchType.getSelectionModel().select(-1);
            cmbSearchStatus.getSelectionModel().select(-1);
            updateTable();
        }
    }

    @FXML
    private void cmbSearchTypeAP(ActionEvent event) {

        if (cmbSearchType.getSelectionModel().getSelectedItem() != null) {
            dtpSearchFromDate.setValue(null);
            dtpSearchToDate.setValue(null);
            cmbSearchStatus.getSelectionModel().select(-1);
            updateTable();
        }

    }

    @FXML
    private void dtpSearchFromDateAP(ActionEvent event) {
        searchByDate();
    }

    @FXML
    private void dtpSearchToDateAP(ActionEvent event) {
        searchByDate();
    }

    @FXML
    private void cmbSearchStatusAP(ActionEvent event) {

        if (cmbSearchStatus.getSelectionModel().getSelectedItem() != null) {
            dtpSearchFromDate.setValue(null);
            dtpSearchToDate.setValue(null);
            cmbSearchType.getSelectionModel().select(-1);
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
