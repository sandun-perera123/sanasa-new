/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import entity.Smember;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class MemberSearchController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private TextField txtSearchMemberID;
    @FXML
    private TextField txtSearchName;
    @FXML
    private TextField txtSearchNIC;
    @FXML
    private TextField txtSearchAccountNo;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Smember> tblMember;
    @FXML
    private TableColumn<Smember, String> colName;
    @FXML
    private TableColumn<Smember, Integer> colMemberID;
    @FXML
    private TableColumn<Smember, String> colNIC;
    @FXML
    private TableColumn<Smember, String> colTel;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Module-Data">
    ObservableList<Smember> mbrs;

    ResourceBundle rb;

    ComboBox combo;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Initializing-Methods">
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if (rb != null) {
            this.rb = rb;
            combo = (ComboBox) rb.getObject("combo");
            mbrs = (ObservableList<Smember>) rb.getObject("list");
        }

        loadTable();
    }

    private void loadTable() {

        txtSearchAccountNo.setText("");
        txtSearchMemberID.setText("");
        txtSearchNIC.setText("");
        txtSearchName.setText("");

        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colMemberID.setCellValueFactory(new PropertyValueFactory("memberid"));
        colNIC.setCellValueFactory(new PropertyValueFactory("nic"));
        colTel.setCellValueFactory(new PropertyValueFactory("tel"));

        fillTable(mbrs);
        pagination.setCurrentPageIndex(0);

    }

    private void fillTable(ObservableList<Smember> members) {

        //privilage.get("Member_select") &&
        if (members != null && members.size() != 0) {

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

//<editor-fold defaultstate="collapsed" desc="Search-Methods">
    private void updateTable() {

        List<Smember> list = new ArrayList<Smember>();

        String accno = txtSearchAccountNo.getText().trim();
        boolean saccno = !accno.isEmpty();
        String memberid = txtSearchMemberID.getText().trim();
        boolean smemberid = !memberid.isEmpty() && memberid.matches("\\d+");
        String nic = txtSearchNIC.getText().trim();
        boolean snic = !nic.isEmpty();
        String name = txtSearchName.getText().trim();
        boolean sname = !name.isEmpty();

        //Load All Data
        if (!saccno && !smemberid && !sname && !snic) {
            fillTable(mbrs);
        }

        //Serach By Member ID
        if (!saccno && smemberid && !sname && !snic) {

            list = mbrs.stream()
                    .filter(x -> x.getMemberid().startsWith(memberid))
                    .collect(Collectors.toList());

            fillTable(FXCollections.observableArrayList(list));
        }

        //Search By Name
        if (!saccno && !smemberid && sname && !snic) {

            list = mbrs.stream()
                    .filter(x -> x.getName().contains(name))
                    .collect(Collectors.toList());

            fillTable(FXCollections.observableArrayList(list));

        }

        //Search By NIC
        if (!saccno && !smemberid && !sname && snic) {

            list = mbrs.stream()
                    .filter(x -> x.getNic()!= null)
                    .filter(x -> x.getNic().startsWith(nic))
                    .collect(Collectors.toList());

            fillTable(FXCollections.observableArrayList(list));

        }

    }

    @FXML
    private void txtSearchMemberIDKR(KeyEvent event) {

        txtSearchAccountNo.setText("");
        txtSearchNIC.setText("");
        txtSearchName.setText("");

        updateTable();

    }

    @FXML
    private void txtSearchNameKR(KeyEvent event) {

        txtSearchAccountNo.setText("");
        txtSearchNIC.setText("");
        txtSearchMemberID.setText("");

        updateTable();

    }

    @FXML
    private void txtSearchNICKR(KeyEvent event) {

        txtSearchAccountNo.setText("");
        txtSearchMemberID.setText("");
        txtSearchName.setText("");

        updateTable();

    }

    @FXML
    private void txtSearchAccountNoKR(KeyEvent event) {

        txtSearchMemberID.setText("");
        txtSearchNIC.setText("");
        txtSearchName.setText("");

        updateTable();

    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Form-Operation-Methods">
    @FXML
    private void tblMemberMC(MouseEvent event) {

        if (tblMember.getSelectionModel().getSelectedItems() != null) {

            if (event.getClickCount() == 2) {

                Smember m = (Smember) tblMember.getSelectionModel().getSelectedItem();
                combo.getSelectionModel().select(m);

                Stage window = (Stage) tblMember.getScene().getWindow();
                window.close();

            }

        }

    }
//</editor-fold>

}
