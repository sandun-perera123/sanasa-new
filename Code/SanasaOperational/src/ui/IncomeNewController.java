/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.CommonaccountDao;
import dao.CommonaccountcategoryDao;
import dao.CommonaccounttransactionDao;
import dao.CommonaccounttransactiontypeDao;
import dao.DaoException;
import dao.MemberDao;
import entity.Commonaccount;
import entity.Commonaccountcategory;
import entity.Commonaccountsubcategory;
import entity.Commonaccounttransaction;
import entity.Commonaccounttransactiontype;
import entity.Smember;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import static ui.LoginController.user;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class IncomeNewController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private Button btnSave;
    @FXML
    private ComboBox<Smember> cmbMember;
    @FXML
    private TextField txtMemberID;
    @FXML
    private DatePicker dtpDate;
    @FXML
    private Button btnConfirm;
    @FXML
    private TextField txtVoucherNo;
    @FXML
    private ImageView imgMemberSearch;
    @FXML
    private TextField txtTotal;
    @FXML
    private ComboBox<Commonaccountcategory> cmbMainCategory;
    @FXML
    private ComboBox<Commonaccountsubcategory> cmbSubCategory;
    @FXML
    private ComboBox<Commonaccount> cmbAccount;
    @FXML
    private ComboBox<Commonaccounttransactiontype> cmbTransactionType;
    @FXML
    private TextField txtAmount;
    @FXML
    private Button btnAdd;
    @FXML
    private TableView<Commonaccounttransaction> tblTransactions;
    @FXML
    private TableColumn<Commonaccounttransaction, Commonaccount> colAccount;
    @FXML
    private TableColumn<Commonaccounttransaction, Commonaccounttransactiontype> colType;
    @FXML
    private TableColumn<Commonaccounttransaction, BigDecimal> colAmount;
    @FXML
    private TableColumn<Commonaccounttransaction, Button> colDelete;
    @FXML
    private Rectangle pneBasicForm;
    @FXML
    private Rectangle pneSubForm;
    @FXML
    private Label lblBalance;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Globle-Data">
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Smember globleMember;
    private Date globleDate;
    private String globleVoucherNo;
    private BigDecimal globleTotal;

    private Commonaccounttransaction commonaccounttransaction;
    @FXML
    private Button btnClear;

//</editor-fold>
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        loadForm();

        colAccount.setCellValueFactory(new PropertyValueFactory("commonaccountId"));
        colType.setCellValueFactory(new PropertyValueFactory("commonaccounttransactiontypeId"));
        colAmount.setCellValueFactory(new PropertyValueFactory("amount"));

        colDelete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Commonaccounttransaction, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Commonaccounttransaction, Button> row) {
                Button btnImageDelete = new Button("Delete");

                btnImageDelete.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        tblTransactions.getItems().remove(row.getValue());
                        updateBalance();

                    }

                });
                return new SimpleObjectProperty(btnImageDelete);
            }
        });

    }

    private void loadForm() {

        disableBasicForm(false);
        lblBalance.setText("0.00");

        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        tblTransactions.getItems().clear();

        loadBasicForm();

        disableSubForm(true);

        // setStyle(initial);
    }

    private void loadBasicForm() {
        
        btnSave.setDisable(true);

        cmbMember.setItems(MemberDao.getAll());
        cmbMember.getSelectionModel().select(-1);
        cmbMember.getEditor().setStyle(initial);

        dtpDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
        globleDate = java.sql.Date.valueOf(dtpDate.getValue());
        dtpDate.getEditor().setStyle(valid);

        txtMemberID.setText("");
        txtVoucherNo.setText("");
        txtTotal.setText("");

        txtMemberID.setStyle(initial);
        txtTotal.setStyle(initial);
        txtVoucherNo.setStyle(initial);

    }

    private void loadSubForm() {

        commonaccounttransaction = new Commonaccounttransaction();
        
        commonaccounttransaction.setVoucherNo(globleVoucherNo);
        commonaccounttransaction.setAmount(globleTotal);
        commonaccounttransaction.setRemarks(globleMember.getMemberid() + " : " + globleMember.getName());
        commonaccounttransaction.setEmployeeId(user.getEmployeeId());

        cmbMainCategory.setItems(CommonaccountcategoryDao.getAll());
        cmbMainCategory.getSelectionModel().select(-1);

        cmbSubCategory.getItems().clear();
        cmbAccount.getItems().clear();

        cmbTransactionType.setItems(CommonaccounttransactiontypeDao.getByID(2));
        cmbTransactionType.getSelectionModel().select(-1);

        commonaccounttransaction.setDate(globleDate.toString());

        txtAmount.setText("");

        cmbMainCategory.getEditor().setStyle(initial);
        cmbSubCategory.setStyle(initial);
        cmbAccount.setStyle(initial);
        cmbTransactionType.setStyle(initial);

    }

    private void disableBasicForm(boolean state) {
        cmbMember.setDisable(state);
        dtpDate.setDisable(state);
        txtMemberID.setDisable(state);
        txtVoucherNo.setDisable(state);
        txtTotal.setDisable(state);
        btnConfirm.setDisable(state);
    }

    private void disableSubForm(boolean state) {
        cmbMainCategory.setDisable(state);
        cmbSubCategory.setDisable(state);
        cmbAccount.setDisable(state);
        cmbTransactionType.setDisable(state);
        txtAmount.setDisable(state);
        btnAdd.setDisable(state);
        tblTransactions.setDisable(state);
    }

    @FXML
    private void btnSaveAP(ActionEvent event) {

        if (commonaccounttransaction != null && tblTransactions.getItems().size() > 0 && new BigDecimal(lblBalance.getText()).compareTo(new BigDecimal("0.00")) == 0) {


            try {
                
                commonaccounttransaction.setCommonaccountId(CommonaccountDao.getById(9));
                commonaccounttransaction.setCommonaccounttransactiontypeId(CommonaccounttransactiontypeDao.getByID(1).get(0));
                CommonaccounttransactionDao.add(commonaccounttransaction);

                for(Commonaccounttransaction obj : tblTransactions.getItems()){
                    CommonaccounttransactionDao.add(obj);
                }

//                for (int i = 0; i < 50; i++) {
//                    CommonaccounttransactionDao.add(commonaccounttransaction);
//                }
                
                loadForm();
                
            } catch (DaoException ex) {
                Logger.getLogger(IncomeNewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Can't save. Some details not filled or invalid !");
        }

    }

    @FXML
    private void cmbMemberAP(ActionEvent event) {

        Smember member = cmbMember.getSelectionModel().getSelectedItem();

        if (member != null) {
            cmbMember.setStyle(valid);
            globleMember = member;
            txtMemberID.setText(member.getMemberid());
        } else {
            cmbMember.setStyle(initial);
            txtMemberID.setText("");
            globleMember = null;
        }

    }

    @FXML
    private void dtpDateAP(ActionEvent event) {

        LocalDate date = dtpDate.getValue();

        if (date != null) {
            dtpDate.getEditor().setStyle(valid);
            globleDate = java.sql.Date.valueOf(date);
            dtpDate.setValue(date);
        } else {
            dtpDate.getEditor().setStyle(initial);
            dtpDate.setValue(null);
            globleDate = null;
        }

    }

    @FXML
    private void btnConfirmAP(ActionEvent event) {

        boolean isMemberSelected = cmbMember.getSelectionModel().getSelectedItem() != null;
        boolean isDateSelected = dtpDate.getValue() != null;
        boolean isVoucherNoEntered = !txtVoucherNo.getText().isEmpty();
        boolean isTotalEntered = !txtTotal.getText().isEmpty() && txtTotal.getText().matches("^(?!^0\\.00$)(([1-9][\\d]{0,10})|([0]))\\.[\\d]{2}$");

        if (isMemberSelected && isDateSelected && isVoucherNoEntered && isTotalEntered) {
            
            btnSave.setDisable(false);

            disableBasicForm(true);
            disableSubForm(false);
            loadSubForm();

            lblBalance.setText(globleTotal.toString());

        } else {

            btnConfirm.setDisable(false);
            disableSubForm(true);
            lblBalance.setText("0.00");

            JOptionPane.showMessageDialog(null, "Some fields are not entered !");
        }

    }

    @FXML
    private void txtVoucherNoKR(KeyEvent event) {

        String voucherNo = txtVoucherNo.getText().trim();

        if (voucherNo != null) {
            txtVoucherNo.setStyle(valid);
            globleVoucherNo = voucherNo;
        } else {
            txtVoucherNo.setStyle(initial);
            globleVoucherNo = null;
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
    private void txtTotalKR(KeyEvent event) {

        String total = txtTotal.getText().trim();

        if (total != null && total.matches("^(?!^0\\.00$)(([1-9][\\d]{0,10})|([0]))\\.[\\d]{2}$")) {
            txtTotal.setStyle(valid);
            globleTotal = new BigDecimal(total);
        } else {
            txtTotal.setStyle(invalid);
            globleTotal = null;
        }

    }

    @FXML
    private void cmbMainCategoryAP(ActionEvent event) {

        if (cmbMainCategory.getSelectionModel().getSelectedItem() != null) {
            ObservableList<Commonaccountsubcategory> observableArrayList = FXCollections.observableArrayList(cmbMainCategory.getSelectionModel().getSelectedItem().getCommonaccountsubcategoryList());
            cmbSubCategory.setItems(observableArrayList);
            getAccount();
            cmbMainCategory.setStyle(valid);
        }

    }

    private void getAccount() {

        if (cmbMainCategory.getSelectionModel().getSelectedItem() != null && cmbSubCategory.getSelectionModel().getSelectedItem() != null) {

            Commonaccountcategory main = cmbMainCategory.getSelectionModel().getSelectedItem();
            Commonaccountsubcategory sub = cmbSubCategory.getSelectionModel().getSelectedItem();

            cmbAccount.setItems(CommonaccountDao.getAllByMainSub(main, sub));

        }

    }

    private void updateBalance() {

        BigDecimal sum = new BigDecimal("0.00");

        for (Commonaccounttransaction obj : tblTransactions.getItems()) {
            sum = sum.add(obj.getAmount());
        }

        BigDecimal newBalance = globleTotal.subtract(sum);
        lblBalance.setText(newBalance.toString());

    }

    @FXML
    private void cmbSubCategoryAP(ActionEvent event) {

        if (cmbSubCategory.getSelectionModel().getSelectedItem() != null) {
            getAccount();
            cmbSubCategory.setStyle(valid);
        }

    }

    @FXML
    private void cmbAccountAP(ActionEvent event) {

        if (cmbAccount.getSelectionModel().getSelectedItem() != null) {
            commonaccounttransaction.setCommonaccountId(cmbAccount.getSelectionModel().getSelectedItem());
            cmbAccount.setStyle(valid);
        }

    }

    @FXML
    private void cmbTransactionTypeAP(ActionEvent event) {

        if (cmbTransactionType.getSelectionModel().getSelectedItem() != null) {
            commonaccounttransaction.setCommonaccounttransactiontypeId(cmbTransactionType.getSelectionModel().getSelectedItem());
            cmbTransactionType.setStyle(valid);
        }

    }

    @FXML
    private void txtAmountKR(KeyEvent event) {

        String amount = txtAmount.getText().trim();

        if (amount != null && amount.matches("^(?!^0\\.00$)(([1-9][\\d]{0,10})|([0]))\\.[\\d]{2}$") && (new BigDecimal(amount).compareTo(globleTotal) == -1 || new BigDecimal(amount).compareTo(globleTotal) == 0)) {

            BigDecimal amountbigdecimal = new BigDecimal(amount);
            commonaccounttransaction.setAmount(amountbigdecimal);
            txtAmount.setStyle(valid);
        } else {
            txtAmount.setStyle(invalid);
            commonaccounttransaction.setAmount(null);
        }

    }

    @FXML
    private void btnAddAP(ActionEvent event) {

        if (commonaccounttransaction != null) {

            if (commonaccounttransaction.getAmount() == null || commonaccounttransaction.getCommonaccountId() == null || commonaccounttransaction.getCommonaccounttransactiontypeId() == null) {
                JOptionPane.showMessageDialog(null, "Some fields are not filled !");
            } else {

                BigDecimal sum = new BigDecimal("0.00");

                for (Commonaccounttransaction obj : tblTransactions.getItems()) {
                    sum = sum.add(obj.getAmount());
                }

                BigDecimal add = sum.add(commonaccounttransaction.getAmount());

                if (add.compareTo(globleTotal) == -1 || add.compareTo(globleTotal) == 0) {
                    tblTransactions.getItems().add(commonaccounttransaction);
                    updateBalance();
                    loadSubForm();
                } else {
                    JOptionPane.showMessageDialog(null, "Can't add. Check for amount");
                }

            }

        }

    }

    @FXML
    private void btnClearAP(ActionEvent event) {
        loadForm();
    }

}
