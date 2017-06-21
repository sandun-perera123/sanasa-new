/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import entity.Smember;
import entity.WebCamInfo;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 *
 * @author SahaN
 */
public class WebcamController implements Initializable {

    private Webcam webCam = null;
    private boolean stopCamera = false;
    private BufferedImage grabbedImage;
    ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();

    ResourceBundle rb;

    @FXML
    private ComboBox<WebCamInfo> cmbCamera;
    @FXML
    private ImageView imgCamera;
    @FXML
    private Button btnCapture;
    @FXML
    private ImageView v;
    @FXML
    private Button btnClearImg;
    @FXML
    private Button btnAddImg;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createTopPanel();
        this.rb = rb;
    }

    @FXML
    private void cmbCameraAP(ActionEvent event) {

    }

    private void createTopPanel() {
        v.setImage(new Image("/image/user.png"));
        int webCamCounter = 0;

        ObservableList<WebCamInfo> options = FXCollections.observableArrayList();

        for (Webcam webcam : Webcam.getWebcams()) {
            WebCamInfo webCamInfo = new WebCamInfo();
            webCamInfo.setWebCamIndex(webCamCounter);
            webCamInfo.setWebCamName(webcam.getName());

            options.add(webCamInfo);
            webCamCounter++;
        }

        cmbCamera.setItems(options);

        cmbCamera.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<WebCamInfo>() {

            @Override
            public void changed(ObservableValue<? extends WebCamInfo> arg0, WebCamInfo arg1, WebCamInfo arg2) {
                if (arg2 != null) {

                    initializeWebCam(arg2.getWebCamIndex());
                }
            }
        });

    }

    protected void initializeWebCam(final int webCamIndex) {

        Task<Void> webCamTask = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                if (webCam != null) {

                    webCam = Webcam.getWebcams().get(webCamIndex);
                    webCam.close();
                    webCam.open();

                } else {
                    webCam = Webcam.getWebcams().get(webCamIndex);
                    webCam.close();
                    webCam.open();

                }

                startWebCamStream();
                return null;
            }
        };

        Thread webCamThread = new Thread(webCamTask);
        webCamThread.setDaemon(true);
        webCamThread.start();

    }

    protected void startWebCamStream() {

        stopCamera = false;
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                while (!stopCamera) {
                    try {
                        if ((grabbedImage = webCam.getImage()) != null) {

//							System.out.println("Captured Image height*width:"+grabbedImage.getWidth()+"*"+grabbedImage.getHeight());
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (grabbedImage != null) {

                                        final Image mainiamge = SwingFXUtils.toFXImage(grabbedImage, null);
                                        imageProperty.set(mainiamge);
                                    }
                                }
                            });

                            grabbedImage.flush();

                        }
                    } catch (Exception e) {
                    } finally {

                    }

                }

                return null;

            }

        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgCamera.imageProperty().bind(imageProperty);

    }

    @FXML
    private void btnCaptureAP(ActionEvent event) {
        WebcamUtils.capture(webCam, "test1", ImageUtils.FORMAT_JPG);

        BufferedImage image = webCam.getImage();

        v.setImage(SwingFXUtils.toFXImage(image, null));

    }

    @FXML
    private void btnClearImgAP(ActionEvent event) {
        v.setImage(new Image("/image/user.png"));
    }

    @FXML
    private void btnAddImgAP(ActionEvent event) {

        try {
            byte[] bytes = WebcamUtils.getImageBytes(webCam, "jpg");

            Smember member = (Smember) rb.getObject("member");
            member.setPhoto(bytes);
        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ඡායාරූපයක් තෝරා නොමැත.");
            alert.setHeaderText(" ");
            alert.setContentText("ඡායාරූපයක් තෝරා නොමැත.");

            ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(btnOk);
            alert.showAndWait();

        }

        ((Node) (event.getSource())).getScene().getWindow().hide();

        if (webCam.open()) {
            webCam.close();
        }

    }

}
