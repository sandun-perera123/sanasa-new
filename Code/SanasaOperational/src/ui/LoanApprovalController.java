/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.LoanDao;
import dao.LoanStatusDao;
import dao.LoanTypeDao;
import entity.Loan;
import entity.Loanstatus;
import entity.Loantype;
import entity.Smember;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import static ui.LoginController.privilage;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class LoanApprovalController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private TextField txtSearchNIC;
    @FXML
    private TextField txtSearchName;
    @FXML
    private TextField txtSearchMemberID;
    @FXML
    private ComboBox<Loanstatus> cmbSearchStatus;
    @FXML
    private ComboBox<Loantype> cmbSearchLoanType;
    @FXML
    private DatePicker dtpSearchFromDate;
    @FXML
    private DatePicker dtpSearchToDate;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Loan> tblLoan;
    @FXML
    private TableColumn<Loan, Smember> colMemberID;
    @FXML
    private TableColumn<Loan, Loantype> colLoanType;
    @FXML
    private TableColumn<Loan, Date> colRequestedDate;
    @FXML
    private TableColumn<Loan, Date> colRequireDay;
    @FXML
    private TableColumn<Loan, Integer> colPayDate;
    @FXML
    private TableColumn<Loan, Integer> colDuration;
    @FXML
    private TableColumn<Loan, BigDecimal> colRequestedAmount;
    @FXML
    private TableColumn<Loan, BigDecimal> colApprovedAmount;
    @FXML
    private TableColumn<Loan, Loanstatus> colStatus;
    @FXML
    private TableColumn<Loan, Button> colDelete;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Initializing-Methods">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTable();
    }

    private void loadTable() {

        Image imageDecline = new Image(getClass().getResourceAsStream("/image/approve2.png"));

        cmbSearchLoanType.setItems(LoanTypeDao.getAll());
        cmbSearchLoanType.getSelectionModel().select(-1);
        cmbSearchStatus.setItems(LoanStatusDao.getAll());
        cmbSearchStatus.getSelectionModel().select(-1);

        txtSearchNIC.setText("");
        txtSearchName.setText("");
        txtSearchMemberID.setText("");

        dtpSearchFromDate.setValue(null);
        dtpSearchToDate.setValue(null);

        colMemberID.setCellValueFactory(new PropertyValueFactory("smemberId"));
        colLoanType.setCellValueFactory(new PropertyValueFactory("loantypeId"));
        colRequestedDate.setCellValueFactory(new PropertyValueFactory("dorequest"));
        colRequireDay.setCellValueFactory(new PropertyValueFactory("doloan"));
        colPayDate.setCellValueFactory(new PropertyValueFactory("dopay"));
        colDuration.setCellValueFactory(new PropertyValueFactory("duration"));
        colRequestedAmount.setCellValueFactory(new PropertyValueFactory("amount"));
        colApprovedAmount.setCellValueFactory(new PropertyValueFactory("approvedamount"));
        colStatus.setCellValueFactory(new PropertyValueFactory("loanstatusId"));

        colDelete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Loan, Button>, ObservableValue<Button>>() {
            @Override

            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Loan, Button> row) {
                Button btnImageDelete = new Button();
                btnImageDelete.getStyleClass().add("dynamicButton");
                btnImageDelete.setGraphic(new ImageView(imageDecline));
                btnImageDelete.setStyle("-fx-background-color:white; -fx-border-color:black");

                MyResourceBundle rb = new MyResourceBundle();
                HashMap hm = new HashMap();

                btnImageDelete.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        Stage subUI = new Stage();

                        AnchorPane ui = null;
                        try {
                            hm.put("Loan", row.getValue());
                            rb.setHashMap(hm);
                            ui = FXMLLoader.load(MemberLoanApprovalController.class.getResource("MemberLoanApproval.fxml"), rb);

                        } catch (IOException ex) {
                            Logger.getLogger(LoanApprovalController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        subUI.setResizable(false);
                        subUI.setTitle("සාමාජික සාරාංශ තොරතුරු");
                        subUI.getIcons().add(new Image("image/sanasalogo.png"));
                        subUI.setScene(new Scene(ui));
                        subUI.showAndWait();
                        fillTable(LoanDao.getAll());

                    }

                });
                return new SimpleObjectProperty<>(btnImageDelete);
            }
        });

        tblLoan.setRowFactory(new Callback<TableView<Loan>, TableRow<Loan>>() {
            @Override
            public TableRow<Loan> call(TableView<Loan> param) {

                return new TableRow<Loan>() {

                    @Override
                    protected void updateItem(Loan paramT, boolean paramBoolean) {
                        super.updateItem(paramT, paramBoolean);

                        if (paramT != null && paramT.getLoanstatusId().getId() == 1) {
                            String style = "-fx-background-color: rgba(154,254,46, 0.6);";
                            setStyle(style);
                        } else if (paramT != null && paramT.getLoanstatusId().getId() == 3) {
                            String style = "-fx-background-color: rgba(245,188,169, 0.6);";
                            setStyle(style);
                        } else {
                            setStyle(null);
                        }

                    }

                };

            }
        });

        fillTable(LoanDao.getAll());
        pagination.setCurrentPageIndex(0);

    }

    private void fillTable(ObservableList<Loan> loans) {

        if (privilage.get("Loan_select") && loans != null && loans.size() != 0) {

            int rowsCount = 10;
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

    private void updateTable() {

        String nic = txtSearchNIC.getText().trim();
        boolean snic = !nic.isEmpty();
        String name = txtSearchName.getText().trim();
        boolean sname = !name.isEmpty();
        String memberid = txtSearchMemberID.getText().trim();
        boolean smemberid = !memberid.isEmpty();

        Loanstatus status = cmbSearchStatus.getSelectionModel().getSelectedItem();
        boolean sstatus = cmbSearchStatus.getSelectionModel().getSelectedIndex() != -1;
        Loantype loantype = cmbSearchLoanType.getSelectionModel().getSelectedItem();
        boolean sloantype = cmbSearchLoanType.getSelectionModel().getSelectedIndex() != -1;

        Date fdate = null;
        Date tdate = null;
        boolean ssdate = dtpSearchFromDate.getValue() != null & dtpSearchToDate.getValue() != null;

        if (ssdate) {
            fdate = java.sql.Date.valueOf(dtpSearchFromDate.getValue());
            tdate = java.sql.Date.valueOf(dtpSearchToDate.getValue());
        }

        if (!snic && !sname && !smemberid && !sstatus && !sloantype && !ssdate) {
            fillTable(LoanDao.getAll());
        }

        if (snic && !sname && !smemberid && !sstatus && !sloantype && !ssdate) {
            fillTable(LoanDao.getAllByNIC(nic));
        }

        if (!snic && sname && !smemberid && !sstatus && !sloantype && !ssdate) {
            fillTable(LoanDao.getAllByMemberName(name));
        }

        if (!snic && !sname && smemberid && !sstatus && !sloantype && !ssdate) {
            fillTable(LoanDao.getAllByMemberID(memberid));
        }

        if (!snic && !sname && !smemberid && sstatus && !sloantype && !ssdate) {
            fillTable(LoanDao.getAllByStatus(status));
        }

        if (!snic && !sname && !smemberid && !sstatus && sloantype && !ssdate) {
            fillTable(LoanDao.getAllByLoantype(loantype));
        }

        if (!snic && !sname && !smemberid && !sstatus && !sloantype && ssdate) {
            fillTable(LoanDao.getAllByLoanDateRange(fdate, tdate));
        }

    }

    @FXML
    private void txtSearchNICKR(KeyEvent event) {

        txtSearchName.setText("");
        txtSearchMemberID.setText("");

        cmbSearchLoanType.getSelectionModel().select(-1);
        cmbSearchStatus.getSelectionModel().select(-1);

        dtpSearchFromDate.setValue(null);
        dtpSearchToDate.setValue(null);

        updateTable();
    }

    @FXML
    private void txtSearchNameKR(KeyEvent event) {

        txtSearchNIC.setText("");
        txtSearchMemberID.setText("");

        cmbSearchLoanType.getSelectionModel().select(-1);
        cmbSearchStatus.getSelectionModel().select(-1);

        dtpSearchFromDate.setValue(null);
        dtpSearchToDate.setValue(null);

        updateTable();

    }

    @FXML
    private void txtSearchMemberIDKR(KeyEvent event) {

        txtSearchNIC.setText("");
        txtSearchName.setText("");

        cmbSearchLoanType.getSelectionModel().select(-1);
        cmbSearchStatus.getSelectionModel().select(-1);

        dtpSearchFromDate.setValue(null);
        dtpSearchToDate.setValue(null);

        updateTable();

    }

    @FXML
    private void cmbSearchStatus(ActionEvent event) {

        if (cmbSearchStatus.getSelectionModel().getSelectedItem() != null) {

            txtSearchNIC.setText("");
            txtSearchName.setText("");
            txtSearchMemberID.setText("");

            cmbSearchLoanType.getSelectionModel().select(-1);

            dtpSearchFromDate.setValue(null);
            dtpSearchToDate.setValue(null);

            updateTable();

        }

    }

    @FXML
    private void cmbSearchLoanTypeAP(ActionEvent event) {

        if (cmbSearchLoanType.getSelectionModel().getSelectedItem() != null) {
            txtSearchNIC.setText("");
            txtSearchName.setText("");
            txtSearchMemberID.setText("");

            cmbSearchStatus.getSelectionModel().select(-1);

            dtpSearchFromDate.setValue(null);
            dtpSearchToDate.setValue(null);

            updateTable();
        }

    }

    @FXML
    private void dtpSearchFromDateAP(ActionEvent event) {

        if (dtpSearchFromDate.getValue() != null) {

            txtSearchNIC.setText("");
            txtSearchName.setText("");
            txtSearchMemberID.setText("");

            cmbSearchLoanType.getSelectionModel().select(-1);
            cmbSearchStatus.getSelectionModel().select(-1);

            updateTable();

        }

    }

    @FXML
    private void dtpSearchToDateAP(ActionEvent event) {

        if (dtpSearchToDate.getValue() != null) {

            txtSearchNIC.setText("");
            txtSearchName.setText("");
            txtSearchMemberID.setText("");

            cmbSearchLoanType.getSelectionModel().select(-1);
            cmbSearchStatus.getSelectionModel().select(-1);

            updateTable();

        }

    }

}
