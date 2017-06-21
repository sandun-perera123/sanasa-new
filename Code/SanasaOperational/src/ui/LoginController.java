package ui;

import dao.AccountDao;
import dao.ModuleDao;
import dao.PrivilageDao;
import dao.UserDao;
import entity.Module;
import entity.Privilage;
import entity.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import static ui.Main.stage;
import util.Security;

public class LoginController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField pswPassword;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblAttempt;
//</editor-fold>

    private int attempt;
    public static User user;
    public static HashMap<String, Boolean> privilage;

    //<editor-fold defaultstate="collapsed" desc="Initialize-Methods">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        attempt = 2;
    }
//</editor-fold>

    @FXML
    private void btnCancelAP(ActionEvent event) {
        System.exit(3);
    }

    @FXML
    private void btnLoginAP(ActionEvent event) throws SQLException, FileNotFoundException, IOException {

        if (!txtUsername.getText().isEmpty() && !pswPassword.getText().isEmpty()) {//!txtUsername.getText().isEmpty() && !pswPassword.getText().isEmpty()) {

            if (txtUsername.getText().equals("admin") && pswPassword.getText().equals("abc123")) {//txtUsername.getText().equals("admin") && pswPassword.getText().equals("abc123")) {

                privilage = new HashMap<String, Boolean>();

                ObservableList<Module> x = ModuleDao.getAll();

                for (Module module : x) {
                    privilage.put(module.getName() + "_select", true);
                    privilage.put(module.getName() + "_insert", true);
                    privilage.put(module.getName() + "_update", true);
                    privilage.put(module.getName() + "_delete", true);
                }

                user = UserDao.getByName("admin");

            } else if (attempt == -1) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("අසාර්ථකයි !");
                alert.setHeaderText("ඇතුළත් වීම අසාර්ථකයි !");
                alert.setContentText("ඔබ වාර 3 කට වඩා වැරදි මුරපදයක් ඇතුළත් කර ඇත.");

                ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(btnOk);
                alert.showAndWait();

                System.exit(0);

            } else {

                Connection connection = null;
                Properties dbConnectionProperties;

                try {

                    dbConnectionProperties = new Properties();
                    //String path = System.getenv("DB_PATH");
                    String path = "/home/sandun/Desktop/SanasaOperational/Code/SanasaOperational/src";
                    File file = new File(path + "/hibernate.properties");
                    FileInputStream fi = new FileInputStream(file);
                    dbConnectionProperties.load(fi);

                    String location = dbConnectionProperties.getProperty("hibernate.connection.url");
                    String username = dbConnectionProperties.getProperty("hibernate.connection.username");
                    String password = dbConnectionProperties.getProperty("hibernate.connection.password");

                    connection = DriverManager.getConnection(location, username, password);
                    
                } catch (SQLException ex) {

                    JOptionPane.showMessageDialog(null, "1");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("අසාර්ථකයි !");
                    alert.setHeaderText("ඇතුළත් වීම අසාර්ථකයි !");
                    alert.setContentText("සර්වරය හා සම්බන්ධ වීමට නොහැක.");

                    ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(btnOk);
                    alert.showAndWait();

                }

                String query = "SELECT * FROM user WHERE name =? AND password = ?";

                try {

                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, txtUsername.getText());
                    statement.setString(2, Security.getHash(pswPassword.getText()));
                    ResultSet results = statement.executeQuery();

                    if (results.next()) {

                        user = UserDao.getById(results.getInt("id"));
                        privilage = new HashMap<String, Boolean>();

                        ObservableList<Module> x = ModuleDao.getAll();

                        for (Module module : x) {
                            privilage.put(module.getName() + "_select", false);
                            privilage.put(module.getName() + "_insert", false);
                            privilage.put(module.getName() + "_update", false);
                            privilage.put(module.getName() + "_delete", false);
                        }

                        ObservableList<Privilage> privilages = PrivilageDao.getAllByUser(user);

                        for (Privilage privi : privilages) {

                            String moduleName = privi.getModuleId().getName();

                            if (privi.getSel() == 1) {
                                if (!privilage.get(moduleName + "_select")) {
                                    privilage.put(moduleName + "_select", true);
                                }
                            }

                            if (privi.getIns() == 1) {
                                if (!privilage.get(moduleName + "_insert")) {
                                    privilage.put(moduleName + "_insert", true);
                                }
                            }

                            if (privi.getUpd() == 1) {
                                if (!privilage.get(moduleName + "_update")) {
                                    privilage.put(moduleName + "_update", true);
                                }
                            }

                            if (privi.getDel() == 1) {
                                if (!privilage.get(moduleName + "_delete")) {
                                    privilage.put(moduleName + "_delete", true);
                                }
                            }

                        }

                        statement.close();

                    } else {
                        lblAttempt.setText("ඇතුළත් වීම අසාර්ථකයි. ඔබට ඉතිරිව ඇති වාර ගණන : " + (attempt--));
                        pswPassword.setText("");
                    }

                } catch (SQLException ex) {

                    JOptionPane.showMessageDialog(null, "2");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("අසාර්ථකයි !");
                    alert.setHeaderText("ඇතුළත් වීම අසාර්ථකයි !");
                    alert.setContentText("දත්ත ගොනුව හා සම්බන්ධ වීමට නොහැක.");

                    ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(btnOk);
                    alert.showAndWait();

                }

            }
            if (privilage != null) {

                if (new Date().getDate() >= 25) {

                    if (AccountDao.getAllNonUpdatedAccounts(new Date()).size() > 0) {
                        loadUpdateProcessWindow();
                    } else {
                        loadMainWindow();
                    }

                } else {
                    loadMainWindow();
                }

            }
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("අසාර්ථකයි !");
            alert.setHeaderText("ඇතුළත් වීම අසාර්ථකයි !");
            alert.setContentText("නිවැරදි පරිශීලක නම හා මුරපදය ඇතුළත් කරන්න");

            ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(btnOk);
            alert.showAndWait();

        }
    }

    private void loadMainWindow() {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("MainWindowNew.fxml"));

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
            stage.close();
            stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image("image/sanasalogo.png"));
            stage.setTitle("සී/ර සමූපකාර ණය ගණුදෙනු සමිතිය - මාදෙල්ගමුව");

            stage.setMaximized(true);
            stage.show();

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(null, "3");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("අසාර්ථකයි !");
            alert.setHeaderText("ඇතුළත් වීම අසාර්ථකයි !");
            alert.setContentText("ප්‍රධාන මෙනුව වෙත යාමට නොහැක.");

            ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(btnOk);
            alert.showAndWait();

        }

    }

    private void loadUpdateProcessWindow() {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("AccountUpdateProcess.fxml"));

            Scene scene = new Scene(root);

            Stage stg = new Stage();
            stg.setScene(scene);
            stg.getIcons().add(new Image("image/sanasalogo.png"));
            stg.setTitle("සී/ර සමූපකාර ණය ගණුදෙනු සමිතිය - මාදෙල්ගමුව");

            stg.setX(0.0);
            stg.setX(0.0);
            stg.setResizable(false);
            stg.show();

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(null, "4");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("අසාර්ථකයි !");
            alert.setHeaderText("ඇතුළත් වීම අසාර්ථකයි !");
            alert.setContentText("ප්‍රධාන මෙනුව වෙත යාමට නොහැක.");

            ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(btnOk);
            alert.showAndWait();

        }

    }

}

//</editor-fold>

