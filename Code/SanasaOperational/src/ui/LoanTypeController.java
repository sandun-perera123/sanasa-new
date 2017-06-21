/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.LoanTypeDao;
import dao.LoantypestatusDao;
import entity.Loantype;
import entity.Loantypestatus;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import static ui.LoginController.privilage;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class LoanTypeController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private TextField txtName;
    @FXML
    private Label txtInterest;
    @FXML
    private TextField txtMaxDuration;
    @FXML
    private TextField txtMinAmount;
    @FXML
    private TextField txtMaxAmount;
    @FXML
    private ComboBox<Loantypestatus> cmbStatus;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;
    @FXML
    private TableView<Loantype> tblLoantype;
    @FXML
    private TableColumn<Loantype, String> colName;
    @FXML
    private TableColumn<Loantype, BigDecimal> colInterest;
    @FXML
    private TableColumn<Loantype, Integer> colMaxDuration;
    @FXML
    private TableColumn<Loantype, Loantypestatus> colStatus;
    @FXML
    private TextField txtMinDuration;
//</editor-fold>
   
//<editor-fold defaultstate="collapsed" desc="Module-Data">
    //Color Indication of Input Controls
    private String valid;
    private String invalid;
    private String updated;
    private String initial;
    
    //Binding with the Form
    private Loantype loantype;
    
    //Update Identification
    private Loantype oldLoantype;
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
        
        loantype = new Loantype();
        oldLoantype = null;
        
        cmbStatus.setItems(LoantypestatusDao.getAll());
        cmbStatus.getSelectionModel().select(-1);
        
        txtName.setText("");
        txtInterest.setText("");
        txtMinDuration.setText("");
        txtMaxDuration.setText("");
        txtMinAmount.setText("");
        txtMaxAmount.setText("");
        
        dissableButtons(false, false, true, true);
        setStyle(initial);
        
    }
    
    private void loadTable() {
        
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colInterest.setCellValueFactory(new PropertyValueFactory("interest"));
        colMaxDuration.setCellValueFactory(new PropertyValueFactory("maxduration"));
        colStatus.setCellValueFactory(new PropertyValueFactory("loantypestatusId"));
        
        fillTable(LoanTypeDao.getAll());
        
    }
    
    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Loantype_insert"));
        btnUpdate.setDisable(update || !privilage.get("Loantype_update"));
        btnDelete.setDisable(delete || !privilage.get("Loantype_delete"));
    }
    
    private void setStyle(String style) {
        
        txtName.setStyle(style);
        txtInterest.setStyle(style);
        txtMinDuration.setStyle(style);
        txtMaxDuration.setStyle(style);
        txtMinAmount.setStyle(style);
        txtMaxAmount.setStyle(style);
        
        
    }
    
    private void fillTable(ObservableList<Loantype> loantypes) {
        
        if (privilage.get("Loantype_select") && loantypes != null && loantypes.size() != 0) {
            
            tblLoantype.getItems().clear();
            tblLoantype.setItems(FXCollections.observableArrayList(loantypes));
            
        } else {
            
            tblLoantype.getItems().clear();
        }
        
    }
//</editor-fold>
   
    
    
    

    @FXML
    private void txtNameKR(KeyEvent event) {
        if (loantype.setName(txtName.getText().trim())) {
            if (oldLoantype != null && !loantype.getName().equals(oldLoantype.getName())) {
                txtName.setStyle(updated);
            } else {
                txtName.setStyle(valid);
            }
        } else {
            txtName.setStyle(invalid);
            loantype.setName(null);
        }
    }

    @FXML
    private void txtInterestKR(KeyEvent event) {
        String interest = txtInterest.getText().trim();

        if (interest.matches("^(?!^0\\.00$)(([1-9][\\d]{0,6})|([0]))\\.[\\d]{2}$") && loantype.setInterest(BigDecimal.valueOf(Double.valueOf(interest)))) {
            if (oldLoantype != null && !loantype.getInterest().equals(oldLoantype.getInterest())) {
                txtInterest.setStyle(updated);
            } else {
                txtInterest.setStyle(valid);
            }
        } else {
            txtInterest.setStyle(invalid);
            loantype.setInterest(null);
        }
    }

    @FXML
    private void txtMaxDurationKR(KeyEvent event) {
        String min = txtMaxDuration.getText().trim();

        if (min.matches("^(?!^0\\.00$)(([1-9][\\d]{0,6})|([0]))\\.[\\d]{2}$") && loantype.setMaxduration(Integer.valueOf(min))) {
            if (oldLoantype != null && !loantype.getMaxduration().equals(oldLoantype.getMaxduration())) {
                txtMaxDuration.setStyle(updated);
            } else {
                txtMaxDuration.setStyle(valid);
            }
        } else {
            txtMaxDuration.setStyle(invalid);
            loantype.setMaxduration(null);
        }
    }

    @FXML
    private void txtMinAmountKR(KeyEvent event) {
    }

    @FXML
    private void txtMaxAmountKR(KeyEvent event) {
    }

    @FXML
    private void cmbStatusAP(ActionEvent event) {
    }

    @FXML
    private void btnAddAP(ActionEvent event) {
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {
    }

    @FXML
    private void btnClearAP(ActionEvent event) {
    }

    @FXML
    private void tblLoantypeMC(MouseEvent event) {
    }

    @FXML
    private void txtMinDurationKR(KeyEvent event) {
    }
    
}
