/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.DaoException;
import dao.MemberDao;
import dao.SharegainDao;
import dao.SharetypeDao;
import entity.Share;
import entity.Sharegain;
import entity.Sharetype;
import entity.Smember;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import org.eclipse.persistence.sessions.Record;
import report.ReportView;
import static ui.LoginController.privilage;
import static ui.LoginController.user;
import util.CustomAlerts;

/**
 * FXML Controller class
 *
 * @author Sandun
 */
public class ShareController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private ComboBox<Smember> cmbMember;
    @FXML
    private DatePicker dtpDate;
    @FXML
    private ComboBox<Sharetype> cmbUnitPrice;
    @FXML
    private TextField txtCount;
    @FXML
    private Button btnInnerAdd;
    @FXML
    private TableView<Share> tblShare;
    @FXML
    private TableColumn<Share, Sharetype> colUnitprice;
    @FXML
    private TableColumn<Share, Integer> colCount;
    @FXML
    private TableColumn<Share, BigDecimal> colLinetotal;
    @FXML
    private TableColumn<Share, Button> colDelete;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;
    @FXML
    private ComboBox<Smember> cmbSearchMember;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Sharegain> tblSharegain;
    @FXML
    private TableColumn<Sharegain, Smember> colMember;
    @FXML
    private TableColumn<Sharegain, Date> colDate;
    @FXML
    private TableColumn<Sharegain, Integer> colTotalshare;
    @FXML
    private TableColumn<Sharegain, BigDecimal> colTotalvalue;
    @FXML
    private Button btnSearchClear;
    @FXML
    private TextField txtTotalCount;
    @FXML
    private TextField txtTotalValue;
    @FXML
    private ImageView imgMemberSearch;
    @FXML
    private ImageView imgSearchMemberSearch;
    @FXML
    private DatePicker dtpFromDate;
    @FXML
    private DatePicker dtpToDate;
    @FXML
    private Button btnPrint;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Module-Data">
//Color Indication of Input Controls
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Sharegain sharegain;

    //Update Identification
    private Sharegain oldSharegain;

    //Table Row, Page Selected
    private int page;
    private int row;
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

        sharegain = new Sharegain();
        oldSharegain = null;

        List<Share> shares = new ArrayList();
        sharegain.setShareList(shares);

        cmbMember.setDisable(false);
        cmbMember.setItems(MemberDao.getAll());
        cmbMember.getSelectionModel().select(-1);

        cmbSearchMember.setDisable(false);

        dtpDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

        dissableButtons(false, false, true, true);
        setStyle(initial);

        loadInnerForm();

    }

    private void loadInnerForm() {

        cmbUnitPrice.setItems(SharetypeDao.getAll());
        cmbUnitPrice.getSelectionModel().select(-1);
        txtCount.setText("");

        setInnerStyle(initial);

    }

    private void loadTable() {

        cmbSearchMember.setItems(MemberDao.getAll());
        cmbSearchMember.getSelectionModel().select(-1);
        dtpFromDate.setValue(null);
        dtpToDate.setValue(null);

        colMember.setCellValueFactory(new PropertyValueFactory("smemberId"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));
        colTotalshare.setCellValueFactory(new PropertyValueFactory("totalcount"));
        colTotalvalue.setCellValueFactory(new PropertyValueFactory("totalvalue"));

        fillTable(SharegainDao.getAll());
        pagination.setCurrentPageIndex(0);

    }
    


    private void loadInnerTable() {

        Image imageDecline = new Image(getClass().getResourceAsStream("/image/inner-delete.png"));

        tblShare.getItems().clear();

        colUnitprice.setCellValueFactory(new PropertyValueFactory("sharetypeId"));
        colCount.setCellValueFactory(new PropertyValueFactory("count"));
        colLinetotal.setCellValueFactory(new PropertyValueFactory("linetotal"));

//        colDelete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Share, Button>, ObservableValue<Button>>() {
//            @Override
//            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Share, Button> row) {
//                Button btnImageDelete = new Button();
//                btnImageDelete.setGraphic(new ImageView(imageDecline));
//
//                btnImageDelete.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//
//                        sharegain.getShareList().remove(row.getValue());
//                        tblShare.setItems(FXCollections.observableArrayList(sharegain.getShareList()));
//                        updateTotal();
//                        
//
//                    }
//
//                });
//                return new SimpleObjectProperty(btnImageDelete);
//            }
//        });
        
        
                
        
        colDelete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Share, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Share, Button> row) {
                Button btnImageDelete = new Button();
                btnImageDelete.setGraphic(new ImageView(imageDecline));

                btnImageDelete.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        sharegain.getShareList().remove(row.getValue());
                        tblShare.setItems(FXCollections.observableArrayList(sharegain.getShareList()));
                        updateTotal();
                        

                    }

                });
                return new SimpleObjectProperty(btnImageDelete);
            }
        });
        
        
        
        
        
        
        
        
        

    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Share_insert"));
        btnUpdate.setDisable(update || !privilage.get("Share_update"));
        btnDelete.setDisable(delete || !privilage.get("Share_delete"));

        cmbSearchMember.setDisable(select || !privilage.get("Share_select"));
        dtpFromDate.setDisable(select || !privilage.get("Share_select"));
        dtpToDate.setDisable(select || !privilage.get("Share_select"));
        btnSearchClear.setDisable(select || !privilage.get("Share_select"));

    }

    private void setStyle(String style) {
        cmbMember.setStyle(style);
        dtpDate.getEditor().setStyle(style);
    }

    private void setInnerStyle(String style) {
        cmbUnitPrice.setStyle(style);
        txtCount.setStyle(style);
    }

    private void fillTable(ObservableList<Sharegain> sharegains) {

        if (privilage.get("Share_select") && sharegains != null && sharegains.size() != 0) {

            int rowsCount = 11;
            int pageCount = ((sharegains.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? sharegains.size() : pageIndex * rowsCount + rowsCount;
                    tblSharegain.getItems().clear();
                    tblSharegain.setItems(FXCollections.observableArrayList(sharegains.subList(start, end)));
                    return tblSharegain;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblSharegain.getItems().clear();
        }

    }

    private void fillInnerTable(ObservableList<Share> shares) {

        if (shares != null && shares.size() != 0) {

            tblShare.getItems().clear();
            tblShare.setItems(FXCollections.observableArrayList(shares));

        } else {
            tblShare.getItems().clear();
        }

    }

//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Form-Operation-Methods">
    private void updateTotal() {

        //Update Total Count and Total Value
        int totalCount = 0;
        BigDecimal totalValue = new BigDecimal(0.00);

        for (Share s : sharegain.getShareList()) {
            totalCount += s.getCount();
            totalValue = totalValue.add(s.getLinetotal());
        }

        sharegain.setTotalcount(totalCount);
        sharegain.setTotalvalue(totalValue);

        txtTotalCount.setText(String.valueOf(sharegain.getTotalcount()));
        txtTotalValue.setText(String.valueOf(sharegain.getTotalvalue()));

    }

    private void generateReport(Sharegain sharegain) {

        HashMap hm = new HashMap();
        hm.put("sharegainid", sharegain.getId());

        new ReportView("/report/SharegainById.jasper", hm);

    }

    private String getErrors() {

        String errors = "";

        if (sharegain.getSmemberId() == null) {
            errors = errors + "සාමාජිකයා තෝරා නොමැත\n";
        }
//        if (sharegain.getDate()== null) {
//            errors = errors + "Date \tis Invalid\n";
//        }
        if (sharegain.getShareList().isEmpty()) {
            errors = errors + "කොටස් මිලදී ගැනීම් ඇතුළත් කර නැත\n";
        }

        return errors;

    }

    private String getUpdates() {

        String updates = "";

        try {

            if (oldSharegain != null) {

                if (sharegain.getShareList() != null && !sharegain.getShareList().containsAll(oldSharegain.getShareList()) || !oldSharegain.getShareList().containsAll(sharegain.getShareList())) {
                    updates = updates + oldSharegain.getShareList() + " ---> " + sharegain.getShareList() + "\n";
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

        if (tblSharegain.getSelectionModel().getSelectedItem() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            sharegain = SharegainDao.getById(tblSharegain.getSelectionModel().getSelectedItem().getId());
            oldSharegain = SharegainDao.getById(tblSharegain.getSelectionModel().getSelectedItem().getId());

            /**
             * ****Disable Fields********
             */
            cmbMember.setDisable(true);
            cmbSearchMember.setDisable(true);
            /**
             * ****Disable Fields********
             */

            cmbMember.getSelectionModel().select((Smember) sharegain.getSmemberId());
            dtpDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(sharegain.getDate())));

            fillInnerTable(FXCollections.observableArrayList(sharegain.getShareList()));
            updateTotal();

            page = pagination.getCurrentPageIndex();
            row = tblSharegain.getSelectionModel().getSelectedIndex();
        }
    }

    @FXML
    private void btnInnerAddAP(ActionEvent event) {

        if (cmbUnitPrice.getSelectionModel().getSelectedIndex() != -1 && txtCount.getStyle().contains(Style.valid)) {

            Share share = new Share();

            share.setSharegainId(sharegain);//Refference main object
            share.setSharetypeId(cmbUnitPrice.getSelectionModel().getSelectedItem());
            share.setCount(Integer.valueOf(txtCount.getText().trim()));

            BigDecimal unitprice = share.getSharetypeId().getUnitprice();
            Integer count = share.getCount();
            BigDecimal linetotal = unitprice.multiply(BigDecimal.valueOf(count));
            share.setLinetotal(linetotal);

            sharegain.getShareList().add(share);
            tblShare.setItems(FXCollections.observableArrayList(sharegain.getShareList()));

            updateTotal();

            loadInnerForm();

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("වැරදියි !");
            alert.setHeaderText(" ");
            alert.setContentText("ඒකක මිල හා කොටස් ගණන නිවැරදිව ඇතුළත් කර ඇතිදැයි නැවත පරීක්ෂා කරන්න.");

            ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(btnOk);
            alert.showAndWait();

        }

    }

    @FXML
    private void btnAddAP(ActionEvent event) {

        sharegain.setEmployeeId(user.getEmployeeId());
        sharegain.setDate(java.sql.Date.valueOf(dtpDate.getValue()));

        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "ඔබට පහත තොරතුරු ඇතුළත් කිරීමට අවශ්‍යද?\n "
                    + "\nසාමාජිකයා:       \t\t" + sharegain.getSmemberId().getName()
                    + "\nදිනය :         \t\t" + sharegain.getDate()
                    + "\nමුලු කොටස් ප්‍රමාණය :         \t\t" + sharegain.getTotalcount()
                    + "\nකොටස් වල මුලු එකතුව :         \t\t" + sharegain.getTotalvalue();

            Optional<ButtonType> action = CustomAlerts.showAddConfirmation(confermation);

            if (action.get() == CustomAlerts.btnYes) {

                try {

                    SharegainDao.add(sharegain);
                    Notifications.create().title("සාර්ථකයි !").text("නම " + sharegain.getSmemberId().getName() + " වන සාමාජිකයාගේ කොටස් ඇතුළත් කරන ලදී.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(10.0)).showInformation();
                    loadForm();
                    loadInnerForm();
                    fillTable(SharegainDao.getAll());
                    fillInnerTable(FXCollections.observableArrayList(sharegain.getShareList()));

                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblSharegain.getSelectionModel().select(tblSharegain.getItems().size() - 1);

                } catch (DaoException ex) {
                    Notifications.create().title("අසාර්ථකයි !").text("නම " + sharegain.getSmemberId().getName() + " වන සාමාජිකයාගේ කොටස් ඇතුළත් කිරීමට නොහැක.  \n නැවත උත්සහා කරන්න.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

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

                    SharegainDao.update(sharegain);
                    Notifications.create().title("සාර්ථකයි !").text("කොටස් මිලදී ගැනීම් තොරතුරු යාවත්කාලීන කරන ලදී.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();

                    //updateTable();
                    fillTable(SharegainDao.getAll());
                    loadForm();
                    loadInnerForm();
                    fillTable(SharegainDao.getAll());
                    fillInnerTable(FXCollections.observableArrayList(sharegain.getShareList()));

                    pagination.setCurrentPageIndex(page);
                    tblSharegain.getSelectionModel().select(row);

                }
            } else {

                CustomAlerts.showNoUpdatesError();

            }
        } else {

            CustomAlerts.showUpdatesError(getErrors());

        }

    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {

        if (getUpdates().isEmpty() && getErrors().isEmpty()) {

            Optional<ButtonType> action = CustomAlerts.showDeleteConfirmation();

            if (action.get() == CustomAlerts.btnYes) {

                SharegainDao.delete(sharegain);
                Notifications.create().title("සාර්ථකයි !").text("කොටස් මිලදී ගැනීම් තොරතුරු දත්ත ගොනුවෙන් ඉවත් කරන ලදී.").position(Pos.TOP_RIGHT).hideAfter(Duration.seconds(5.0)).showInformation();
                fillTable(SharegainDao.getAll());
                loadForm();
                loadInnerForm();
                fillTable(SharegainDao.getAll());
                fillInnerTable(FXCollections.observableArrayList(sharegain.getShareList()));

                // updateTable();
                pagination.setCurrentPageIndex(page);
                tblSharegain.getSelectionModel().select(row);

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
            loadInnerForm();
            fillTable(SharegainDao.getAll());
            fillInnerTable(FXCollections.observableArrayList(sharegain.getShareList()));
        }

    }

    @FXML
    private void tblSharegainKP(KeyEvent event) {
        fillForm();
    }

    @FXML
    private void tblSharegainMC(MouseEvent event) {
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

    @FXML
    private void btnPrintAP(ActionEvent event) {

        if (oldSharegain != null) {
            generateReport(oldSharegain);
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("අසාර්ථකයි !");
            alert.setHeaderText(" ");
            alert.setContentText("කොටස් මිලදී ගැනීම් තොරතුරු තෝරා නොමැත.");

            ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(btnOk);
            alert.showAndWait();

        }

    }

//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Data-Setting-Methods">
    @FXML
    private void cmbMemberAP(ActionEvent event) {
        sharegain.setSmemberId(cmbMember.getSelectionModel().getSelectedItem());
        if (oldSharegain != null && !sharegain.getSmemberId().equals(oldSharegain.getSmemberId())) {
            cmbMember.setStyle(updated);
        } else {
            cmbMember.setStyle(valid);
        }
    }

    @FXML
    private void cmbUnitPriceAP(ActionEvent event) {
        if (cmbUnitPrice.getSelectionModel().getSelectedItem() != null) {
            cmbUnitPrice.setStyle(valid);

        } else {
            cmbUnitPrice.setStyle(invalid);
        }
    }

    @FXML
    private void txtCountKR(KeyEvent event) {

        String count = txtCount.getText().trim();

        if (count != null && count.matches("\\d+") && Integer.valueOf(count) <= 1000 && Integer.valueOf(count) > 0) {
            txtCount.setStyle(valid);

        } else {
            txtCount.setStyle(invalid);
        }

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Searching-Methods">
    private void updateTable() {

        Smember member = cmbSearchMember.getSelectionModel().getSelectedItem();
        boolean smember = cmbSearchMember.getSelectionModel().getSelectedIndex() != -1;

        boolean sdate = dtpFromDate.getValue() != null && dtpToDate.getValue() != null;
        Date fdate = null;
        Date tdate = null;

        if (sdate) {
            fdate = java.sql.Date.valueOf(dtpFromDate.getValue());
            tdate = java.sql.Date.valueOf(dtpToDate.getValue());
        }

        if (!smember && !sdate) {
            fillTable(SharegainDao.getAll());
        }
        if (!smember && sdate) {
            fillTable(SharegainDao.getAllByDateRange(fdate, tdate));
        }
        if (smember && !sdate) {
            fillTable(SharegainDao.getAllByMember(member));
        }

    }

    @FXML
    private void cmbSearchMemberAP(ActionEvent event) {
        if (cmbSearchMember.getSelectionModel().getSelectedItem() != null) {
            dtpFromDate.setValue(null);
            dtpToDate.setValue(null);
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
    private void dtpFromDateAP(ActionEvent event) {
        if (dtpFromDate.getValue() != null) {
            cmbSearchMember.getSelectionModel().select(-1);
            updateTable();
        }
    }

    @FXML
    private void dtpToDateAP(ActionEvent event) {

        if (dtpToDate.getValue() != null) {
            cmbSearchMember.getSelectionModel().select(-1);
            updateTable();
        }

    }

    @FXML
    private void dtpDateAP(ActionEvent event) {

    }
//</editor-fold>

}
