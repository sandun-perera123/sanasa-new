/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.AccountPaymentDao;
import dao.MeetingDao;
import dao.SharegainDao;
import entity.Meeting;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Sandun-PC
 */
public class DashBoardController implements Initializable {

    @FXML
    private Pane pneChartDiposit;
    @FXML
    private Pane pneChartShares;
    @FXML
    private ListView<Meeting> lstNotificationList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        loadDipositChart(AccountPaymentDao.getMonthlyDipositSummery());
        loadShareChart(SharegainDao.getMonthlyShareSummery());
        loadUnmarkedMeetings(MeetingDao.getAllUnmarkedMeetings());

    }

    private String getMonthName(int id) {

        String months[] = {"none", "ජනවාරි", "පෙබරවාරි", "මාර්තු", "අප්‍රියෙල්", "මැයි", "ජුනි", "ජූලි", "අගෝස්තු", "සැප්තැම්බර්", "ඔක්තෝබර්", "නොවැම්බර්", "දෙසැම්බර්"};
        return months[id];

    }

    private void loadDipositChart(ObservableList<HashMap> list) {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("මාස");
        yAxis.setLabel("මූල්‍ය වටිනාකම");

        final LineChart<String, Number> lineChart
                = new LineChart<String, Number>(xAxis, yAxis);
        lineChart.setLegendVisible(false);

        lineChart.getXAxis().setAnimated(false);
        lineChart.getYAxis().setAnimated(true);

        lineChart.setTitle("මුදල් තැන්පතු");
        lineChart.setCreateSymbols(false);

        XYChart.Series series = new XYChart.Series();

        for (HashMap hm : list) {
            series.getData().add(new XYChart.Data(getMonthName((int) hm.get("Mth")), hm.get("Amount")));
        }

//        Timer timer = new Timer();
//        
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                Platform.runLater(() -> {
//                    System.out.println("la la");
//                    lineChart.getData().add(series);
//                    timer.cancel();
//                });
//            }
//        }, 100, 10000);
        lineChart.getData().add(series);
        pneChartDiposit.getChildren().clear();
        pneChartDiposit.getChildren().add(lineChart);
    }

    private void loadShareChart(ObservableList<HashMap> list) {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("මාස");
        yAxis.setLabel("කොටස් ගණන");

        final LineChart<String, Number> lineChart
                = new LineChart<String, Number>(xAxis, yAxis);
        lineChart.setLegendVisible(false);

        lineChart.getXAxis().setAnimated(false);
        lineChart.getYAxis().setAnimated(true);

        lineChart.setTitle("කොටස් ගැනීම්");
        lineChart.setCreateSymbols(false);

        XYChart.Series series = new XYChart.Series();

        for (HashMap hm : list) {
            series.getData().add(new XYChart.Data(getMonthName((int) hm.get("Mth")), hm.get("Count")));
        }

        lineChart.getData().add(series);
        pneChartShares.getChildren().clear();
        pneChartShares.getChildren().add(lineChart);
    }

    private void loadUnmarkedMeetings(ObservableList<Meeting> meetings) {

        lstNotificationList.getItems().clear();
        lstNotificationList.setItems(meetings);

    }

    @FXML
    private void lstNotificationListMC(MouseEvent event) {

        if (lstNotificationList.getSelectionModel().getSelectedItem() != null) {

            MyResourceBundle rb = new MyResourceBundle();
            HashMap hm = new HashMap();
            Meeting meeting = lstNotificationList.getSelectionModel().getSelectedItem();

            hm.put("meeting", meeting);
            rb.setHashMap(hm);

            try {
                Stage stage = new Stage();

                AnchorPane ui = FXMLLoader.load(Main.class.getResource("Attendance.fxml"), rb);
                Scene scene = new Scene(ui);
                stage.setScene(scene);
                stage.getIcons().add(new Image("image/sanasalogo.png"));
                stage.setResizable(false);
                stage.showAndWait();

            } catch (IOException ex) {
                Logger.getLogger(DashBoardController.class.getName()).log(Level.SEVERE, null, ex);
            }

            loadUnmarkedMeetings(MeetingDao.getAllUnmarkedMeetings());

        }

    }

}
