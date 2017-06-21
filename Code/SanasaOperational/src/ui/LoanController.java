/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.DaoException;
import dao.LoanDao;
import dao.LoanStatusDao;
import dao.LoanTypeDao;
import dao.MemberDao;
import entity.Loan;
import entity.Loanstatus;
import entity.Loantype;
import entity.Smember;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import static ui.LoginController.privilage;
import static ui.LoginController.user;
import util.CustomAlerts;
import static util.CustomAlerts.btnOk;

/**
 * FXML Controller class
 *
 * @author Sandun
 */
public class LoanController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private ComboBox<Smember> cmbMember;
    @FXML
    private ComboBox<Loantype> cmbLoanType;
    @FXML
    private TextField txtLoanAmount;
    @FXML
    private DatePicker dtpLoanDate;
    @FXML
    private TextField txtPayDate;
    @FXML
    private TextField txtDuration;
    @FXML
    private TextArea txaRemarks;
    @FXML
    private ComboBox<Loanstatus> cmbStatus;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;
    @FXML
    private ComboBox<Loanstatus> cmbSearchStatus;
    @FXML
    private ComboBox<Loantype> cmbSearchLoanType;
    @FXML
    private ComboBox<Smember> cmbSearchMember;
    @FXML
    private TableView<Loan> tblLoan;
    @FXML
    private TableColumn<Loan, Smember> colName;
    @FXML
    private TableColumn<Loan, Loantype> colLoanType;
    @FXML
    private TableColumn<Loan, BigDecimal> colAmount;
    @FXML
    private TableColumn<Loan, Loanstatus> colStatus;
    @FXML
    private Pagination pagination;
    @FXML
    private Button btnSearchClear;
    @FXML
    private Button btnSearchLock;
    @FXML
    private ImageView imgMemberSearch;
    @FXML
    private ImageView imgSearchMemberSearch;
    @FXML
    private TextField txtInterest;
    @FXML
    private ImageView imgLock;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Module-Data">
    //Color Indication of Input Controls
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Loan loan;

    //Update Identification
    private Loan oldLoan;

    //Table Row, Page Selected
    private int page;
    private int row;

    //Search Lock
    private boolean lock;

    //Update Lock
    private boolean updateLock = true;
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Initializing-Methods">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadForm();
        loadTable();
    }

    private void loadForm() {

        loan = new Loan();
        oldLoan = null;

        cmbMember.setDisable(false);
        imgMemberSearch.setDisable(false);

        cmbLoanType.setItems(LoanTypeDao.getAll());
        cmbLoanType.getSelectionModel().select(-1);

        cmbStatus.setItems(LoanStatusDao.getAll());
        cmbStatus.getSelectionModel().select(1);
        loan.setLoanstatusId(cmbStatus.getSelectionModel().getSelectedItem());
        cmbStatus.setStyle(valid);
        cmbStatus.setDisable(true);

        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        cmbMember.setItems(MemberDao.getAll());
        cmbMember.getSelectionModel().select(-1);

        txtLoanAmount.setText("");
        txtDuration.setText("");
        txtPayDate.setText("");
        txaRemarks.setText("");
        txtInterest.setText("");

        dtpLoanDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

        dissableButtons(false, false, true, true);
        setStyle(initial);

    }

    private void loadTable() {
        lock = false;
        btnSearchLock.setText("අගුලු දමන්න");
        imgLock.setImage(new Image("/image/unlock.png"));
        

        cmbSearchMember.setItems(MemberDao.getAll());
        cmbSearchMember.getSelectionModel().select(-1);
        cmbSearchLoanType.setItems(LoanTypeDao.getAll());
        cmbSearchLoanType.getSelectionModel().select(-1);
        cmbSearchStatus.setItems(LoanStatusDao.getAll());
        cmbSearchStatus.getSelectionModel().select(-1);

        colName.setCellValueFactory(new PropertyValueFactory("smemberId"));
        colLoanType.setCellValueFactory(new PropertyValueFactory("loantypeId"));
        colAmount.setCellValueFactory(new PropertyValueFactory("amount"));
        colStatus.setCellValueFactory(new PropertyValueFactory("loanstatusId"));

        fillTable(LoanDao.getAll());
        pagination.setCurrentPageIndex(0);

    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Loan_insert"));
        btnUpdate.setDisable(update || !privilage.get("Loan_update"));
        btnDelete.setDisable(delete || !privilage.get("Loan_delete"));

        try {
            btnSearchClear.setDisable(select || !privilage.get("Loan_select"));
            cmbSearchLoanType.setDisable(select || !privilage.get("Loan_select"));
            btnSearchLock.setDisable(select || !privilage.get("Loan_select"));
            cmbSearchMember.setDisable(select || !privilage.get("Loan_select"));
            cmbSearchStatus.setDisable(select || !privilage.get("Loan_select"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void setStyle(String style) {
        cmbMember.setStyle(style);
        cmbLoanType.setStyle(style);
        cmbStatus.setStyle(style);

        txtLoanAmount.setStyle(style);
        txtPayDate.setStyle(style);
        txtDuration.setStyle(style);

        if (!txaRemarks.getChildrenUnmodifiable().isEmpty()) {
            ((ScrollPane) txaRemarks.getChildrenUnmodifiable().get(0)).getContent().setStyle(style);
        }

        dtpLoanDate.getEditor().setStyle(style);

    }

    private void fillTable(ObservableList<Loan> loans) {

        if (privilage.get("Loan_select") && loans != null && loans.size() != 0) {

            int rowsCount = 9;
            int pageCount = ((loans.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? loans.size() : pageIndex * rowsCount + rowsCount;
                    tblLoan.getItems().clear();
                    tblLoan.setItems(FXCollections.observableArrayList(loans.subList(start, end)));
                    return tblLoan;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblLoan.getItems().clear();
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Data-Settings-Methods">
    private void setInterest() {

        if (cmbLoanType.getSelectionModel().getSelectedItem() != null) {

            loan.setInterest(cmbLoanType.getSelectionModel().getSelectedItem().getInterest());
            txtInterest.setText(loan.getInterest().toString());

        }

    }

    private void resetLoanTypeFields() {

        if (updateLock && cmbLoanType.getSelectionModel().getSelectedItem() != null) {

            txtLoanAmount.clear();
            txtPayDate.clear();
            txtDuration.clear();

            txtLoanAmount.setStyle(invalid);
            txtPayDate.setStyle(invalid);
            txtDuration.setStyle(invalid);

            loan.setAmount(null);
            loan.setDopay(null);
            loan.setDuration(null);

        }

    }

    @FXML
    private void cmbMemberAP(ActionEvent event) {

        loan.setSmemberId(cmbMember.getSelectionModel().getSelectedItem());
        if (oldLoan != null && !loan.getSmemberId().equals(oldLoan.getSmemberId())) {
            cmbMember.setStyle(updated);
        } else {
            cmbMember.setStyle(valid);
        }

    }

    @FXML
    private void cmbLoanTypeAP(ActionEvent event) {

        loan.setLoantypeId(cmbLoanType.getSelectionModel().getSelectedItem());
        if (oldLoan != null && !loan.getLoantypeId().equals(oldLoan.getLoantypeId())) {
            cmbLoanType.setStyle(updated);
            setInterest();
            resetLoanTypeFields();
        } else {
            cmbLoanType.setStyle(valid);
            setInterest();
            resetLoanTypeFields();
        }

    }

    @FXML
    private void txtLoanAmountKR(KeyEvent event) {

        Loantype loantype = cmbLoanType.getSelectionModel().getSelectedItem();
        BigDecimal max = loantype.getMaxamount();
        BigDecimal min = loantype.getMinamount();
        String amount = txtLoanAmount.getText().trim();

        if (amount.matches("^(?!^0\\.00$)(([1-9][\\d]{0,6})|([0]))\\.[\\d]{2}$")
                && ((new BigDecimal(amount).compareTo(max) == 0 || new BigDecimal(amount).compareTo(max) == -1)
                && (new BigDecimal(amount).compareTo(min) == 0 || new BigDecimal(amount).compareTo(min) == 1))
                && loan.setAmount(BigDecimal.valueOf(Double.valueOf(amount)))) {

            if (oldLoan != null && !loan.getAmount().equals(oldLoan.getAmount())) {
                txtLoanAmount.setStyle(updated);
            } else {
                txtLoanAmount.setStyle(valid);
            }
        } else {
            txtLoanAmount.setStyle(invalid);
            loan.setAmount(null);
        }

    }

    @FXML
    private void dtpLoanDateAP(ActionEvent event) {

        if (dtpLoanDate.getValue() != null) {
            Date doRequest = new Date(); //Requested Date
            Date doLoan = java.sql.Date.valueOf(dtpLoanDate.getValue());

            if (doLoan.after(doRequest)) {
                loan.setDoloan(doLoan);
                if (oldLoan != null && !loan.getDoloan().equals(oldLoan.getDoloan())) {
                    dtpLoanDate.getEditor().setStyle(updated);
                } else {
                    dtpLoanDate.getEditor().setStyle(valid);
                }
            } else {
                dtpLoanDate.getEditor().setStyle(invalid);
                loan.setDoloan(null);
            }
        }

    }

    @FXML
    private void txtPayDateKR(KeyEvent event) {

        String payDate = txtPayDate.getText().trim();
        //(0[1-9]|[12]\\d|3[01])
        if (payDate.matches("(0[1-9]|1[0-9]|2[0-8])") && loan.setDopay(Integer.valueOf(payDate))) {
            if (oldLoan != null && !loan.getDopay().equals(oldLoan.getDopay())) {
                txtPayDate.setStyle(updated);
            } else {
                txtPayDate.setStyle(valid);
            }
        } else {
            txtPayDate.setStyle(invalid);
            loan.setDopay(null);
        }

    }

    @FXML
    private void txtDurationKR(KeyEvent event) {

        String duration = txtDuration.getText().trim();
        Integer min = cmbLoanType.getSelectionModel().getSelectedItem().getMinduration();
        Integer max = cmbLoanType.getSelectionModel().getSelectedItem().getMaxduration();

        if (duration.matches("\\d+")
                && (Integer.valueOf(duration) >= min && Integer.valueOf(duration) <= max)
                && loan.setDuration(Integer.valueOf(duration))) {

            if (oldLoan != null && !loan.getDuration().equals(oldLoan.getDuration())) {
                txtDuration.setStyle(updated);
            } else {
                txtDuration.setStyle(valid);
            }
        } else {
            txtDuration.setStyle(invalid);
            loan.setDuration(null);
        }

    }

    @FXML
    private void txaRemarksKR(KeyEvent event) {

        if (loan.setRemarks(txaRemarks.getText().trim())) {
            if (oldLoan != null && !loan.getRemarks().equals(oldLoan.getRemarks())) {
                ((ScrollPane) txaRemarks.getChildrenUnmodifiable().get(0)).getContent().setStyle(updated);
            } else {
                ((ScrollPane) txaRemarks.getChildrenUnmodifiable().get(0)).getContent().setStyle(valid);
            }
        } else {
            ((ScrollPane) txaRemarks.getChildrenUnmodifiable().get(0)).getContent().setStyle(invalid);
        }

    }

    @FXML
    private void cmbStatusAP(ActionEvent event) {

        loan.setLoanstatusId(cmbStatus.getSelectionModel().getSelectedItem());
        if (oldLoan != null && !loan.getLoanstatusId().equals(oldLoan.getLoanstatusId())) {
            cmbStatus.setStyle(updated);
        } else {
            cmbStatus.setStyle(valid);
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Form-Operation-Methods">
    private String getErrors() {

        String errors = "";

        if (loan.getSmemberId() == null) {
            errors = errors + "සාමාජිකයා තෝරා නොමැත.\n";
        }

        if (loan.getLoantypeId() == null) {
            errors = errors + "ණය වර්ගය තෝරා නොමැත.\n";
        }

        if (loan.getAmount() == null) {
            errors = errors + "වටිනාකම ඇතුළත් කර නොමැත.\n";
        }

        if (loan.getDoloan() == null) {
            errors = errors + "මුදල් අවශ්‍ය දිනය වැරදියි.\n";
        }
        if (loan.getDopay() == null) {
            errors = errors + "මුදල් ගෙවන දිනය වැරදියි.\n";
        }
        if (loan.getDuration() == null) {
            errors = errors + "කාල සීමාව වැරදියි.\n";
        }

        if (txaRemarks.getText() != null && !loan.setRemarks(txaRemarks.getText().trim())) {
            errors = errors + "වෙනත් කරුණු වැරදියි.\n";
        }

        return errors;

    }

    private String getUpdates() {

        String updates = "";

        if (oldLoan != null) {

            if (loan.getSmemberId() != null && !loan.getSmemberId().equals(oldLoan.getSmemberId())) {
                updates = updates + "සාමාජිකයා : " + oldLoan.getSmemberId() + " --> " + loan.getSmemberId() + "\n";
            }

            if (!loan.getLoantypeId().equals(oldLoan.getLoantypeId())) {
                updates = updates + "ණය වර්ගය : " + oldLoan.getLoantypeId() + " --> " + loan.getLoantypeId() + "\n";
            }

            if (!loan.getAmount().equals(oldLoan.getAmount())) {
                updates = updates + "වටිනාකම : " + oldLoan.getAmount() + " --> " + loan.getAmount() + "\n";
            }

            if (!loan.getDoloan().equals(oldLoan.getDoloan())) {
                updates = updates + "මුදල් අවශ්‍ය දිනය : " + oldLoan.getDoloan() + " --> " + loan.getDoloan() + "\n";
            }

            if (!loan.getDopay().equals(oldLoan.getDopay())) {
                updates = updates + "මුදල් ගෙවන දිනය : " + oldLoan.getDopay() + " --> " + loan.getDopay() + "\n";
            }

            if (!loan.getDuration().equals(oldLoan.getDuration())) {
                updates = updates + "කාල සීමාව : " + oldLoan.getDuration() + " --> " + loan.getDuration() + "\n";
            }

            if (!(oldLoan.getRemarks() != null
                    && loan.getRemarks() != null
                    && oldLoan.getRemarks().equals(loan.getRemarks()))) {
                if (oldLoan.getRemarks() != loan.getRemarks()) {
                    updates = updates + "වෙනත් කරුණු : " + oldLoan.getRemarks()
                            + " --> " + loan.getRemarks() + "\n";
                }
            }

            if (!loan.getLoanstatusId().equals(oldLoan.getLoanstatusId())) {
                updates = updates + "තත්ත්වය : " + oldLoan.getLoanstatusId() + " --> " + loan.getLoanstatusId() + "\n";
            }

        }

        return updates;
    }

    private void fillForm() {

        if (tblLoan.getSelectionModel().getSelectedItem() != null) {

            if (tblLoan.getSelectionModel().getSelectedItem().getLoanstatusId().getId() != 1) {

                updateLock = false;
                dissableButtons(false, true, false, false);
                setStyle(valid);

                cmbMember.setDisable(true);
                imgMemberSearch.setDisable(true);

                oldLoan = LoanDao.getById(tblLoan.getSelectionModel().getSelectedItem().getId());
                loan = LoanDao.getById(tblLoan.getSelectionModel().getSelectedItem().getId());

                cmbMember.getSelectionModel().select((Smember) loan.getSmemberId());
                cmbLoanType.getSelectionModel().select((Loantype) loan.getLoantypeId());
                cmbStatus.getSelectionModel().select((Loanstatus) loan.getLoanstatusId());

                txtLoanAmount.setText(loan.getAmount().toString());
                txtPayDate.setText(loan.getDopay().toString());
                txtDuration.setText(loan.getDuration().toString());
                txaRemarks.setText(loan.getRemarks());

                dtpLoanDate.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(loan.getDoloan()));

                page = pagination.getCurrentPageIndex();
                row = tblLoan.getSelectionModel().getSelectedIndex();

                updateLock = true;

            } else {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("දැනුම්දීමයි !");
                alert.setHeaderText("තෝරා ගන්නා ලද ණය විස්තරය අනුමත වූ ණය විස්තරයක් බැවින් යාවත්කාලීන කිරීමට හෝ දත්ත ගොනුවෙන් ඉවත් කිරීමට නොහැක.");

                alert.getButtonTypes().setAll(btnOk);
                alert.showAndWait();

                tblLoan.getSelectionModel().clearSelection();
            }
        }

    }

    @FXML
    private void btnAddAP(ActionEvent event) {

        String errors = getErrors();
        if (errors.isEmpty()) {
            loan.setDorequest(new Date());
            String confermation = "ඔබට පහත තොරතුරු ඇතුළත් කිරීමට අවශ්‍යද?\n "
                    + "\nසාමාජිකයා :--> " + loan.getSmemberId().getName()
                    + "\nණය වර්ගය :--> " + loan.getLoantypeId().getName()
                    + "\nණය අවශ්‍ය ප්‍රමාණය :--> " + loan.getAmount()
                    + "\nණය ඉල්ලූ දිනය :--> " + LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(loan.getDorequest()))
                    + "\nමුදල් අවශ්‍ය දිනය :--> " + loan.getDoloan()
                    + "\nමුදල් ගෙවන දිනය :--> " + loan.getDopay()
                    + "\nකාලය :--> " + loan.getDuration()
                    + "\nවෙනත් කරුණු :--> " + loan.getRemarks()
                    + "\nණය තත්වය :--> " + loan.getLoanstatusId().getName();
            
            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);
            
            
            if (action.get() == CustomAlerts.btnYes) {

                try {
                    loan.setEmployeeId(user.getEmployeeId());
                    LoanDao.add(loan);
                    Notifications.create().title("සාර්ථකයි")
                            .text("නම " + loan.getSmemberId()
                                    .getName()
                                    + " වූ සාමාජිකයාගේ ණය තොරතුරු ඇතුළත් කරන ලදී.")
                            .position(Pos.TOP_RIGHT)
                            .hideAfter(Duration.seconds(5.0)).showInformation();
                    loadForm();
                    updateTable();
                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblLoan.getSelectionModel().select(tblLoan.getItems().size() - 1);

                } catch (DaoException ex) {
                    Notifications.create().title("අසාර්ථකයි").
                            text("නම " + loan.getSmemberId().getName()
                                    + " වූ සාමාජිකයාගේ ණය තොරතුරු ඇතුළත් කිරීමට නොහැක. \n නැවත උත්සහ කරන්න.")
                            .position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                }
            }
        } else {
            
            CustomAlerts.showAddError(errors);
            
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

                    LoanDao.update(loan);
                    Notifications.create().title("සාර්ථකයි !").text("ණය තොරතුරු යාවත්කාලීන කරන ලදී.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

                    updateTable();
                    loadForm();
                    pagination.setCurrentPageIndex(page);
                    tblLoan.getSelectionModel().select(row);

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
            
            if (action.get() == CustomAlerts.btnYes) {

                LoanDao.delete(loan);
                Notifications.create().title("සාර්ථකයි !").text("ණය තොරතුරු දත්ත ගොනුවෙන් ඉවත් කරන ලදී.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                loadForm();
                updateTable();
                pagination.setCurrentPageIndex(page);
                tblLoan.getSelectionModel().select(row);

            }
        } else {

            CustomAlerts.showDeleteError();
            
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
    private void tblLoanMC(MouseEvent event) {
        fillForm();
    }

    @FXML
    private void tblLoanKP(MouseEvent event) {
        fillForm();
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
    
//<editor-fold defaultstate="collapsed" desc="Search-Methods">
    private void updateTable() {

        Smember member = cmbSearchMember.getSelectionModel().getSelectedItem();
        boolean smember = cmbSearchMember.getSelectionModel().getSelectedIndex() != -1;
        Loantype loanType = cmbSearchLoanType.getSelectionModel().getSelectedItem();
        boolean sLoanType = cmbSearchLoanType.getSelectionModel().getSelectedIndex() != -1;
        Loanstatus loanStatus = cmbSearchStatus.getSelectionModel().getSelectedItem();
        boolean sLoanStatus = cmbSearchStatus.getSelectionModel().getSelectedIndex() != -1;

        if (!smember && !sLoanType && !sLoanStatus) {
            fillTable(LoanDao.getAll());
        }
        if (smember && !sLoanType && !sLoanStatus) {
            fillTable(LoanDao.getAllByMember(member));
        }
        if (!smember && !sLoanType && sLoanStatus) {
            fillTable(LoanDao.getAllByLoanstatus(loanStatus));
        }
        if (!smember && sLoanType && !sLoanStatus) {
            fillTable(LoanDao.getAllByLoantype(loanType));
        }
        if (smember && sLoanType && !sLoanStatus) {
            fillTable(LoanDao.getAllByMemberLoantype(member, loanType));
        }
        if (smember && !sLoanType && sLoanStatus) {
            fillTable(LoanDao.getAllByMemberLoanstatus(member, loanStatus));
        }
        if (!smember && sLoanType && sLoanStatus) {
            fillTable(LoanDao.getAllByLoantypeLoanstatus(loanType, loanStatus));
        }
        if (smember && sLoanType && sLoanStatus) {
            fillTable(LoanDao.getAllByMemberLoantypeLoanstatus(member, loanType, loanStatus));
        }

    }

    @FXML
    private void cmbSearchStatusAP(ActionEvent event) {

        if (cmbSearchStatus.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                cmbSearchLoanType.getSelectionModel().select(-1);
                cmbSearchMember.getSelectionModel().select(-1);
            }
            updateTable();
        }

    }

    @FXML
    private void cmbSearchLoanTypeAP(ActionEvent event) {

        if (cmbSearchLoanType.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                cmbSearchStatus.getSelectionModel().select(-1);
                cmbSearchMember.getSelectionModel().select(-1);
            }
            updateTable();
        }

    }

    @FXML
    private void cmbSearchMemberAP(ActionEvent event) {

        if (cmbSearchMember.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                cmbSearchStatus.getSelectionModel().select(-1);
                cmbSearchLoanType.getSelectionModel().select(-1);
            }
            updateTable();
        }

    }

    @FXML
    private void btnSearchClearAP(ActionEvent event) {


        Optional<ButtonType> action = CustomAlerts.showClearFormConfirmation();
        
        if (action.get()==CustomAlerts.btnYes) {
            loadTable();
        }

    }

    @FXML
    private void btnSearchLockAP(ActionEvent event) {

        if (lock) {
            btnSearchLock.setText("අගුලු දමන්න");
            lock = false;
            imgLock.setImage(new Image("/image/unlock.png"));
            fillTable(LoanDao.getAll());
        } else {
            btnSearchLock.setText("අගුලු හරින්න");
            lock = true;
            imgLock.setImage(new Image("/image/lock.png"));
            updateTable();
        }

    }
//</editor-fold>

}
