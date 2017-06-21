/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Sandun-PC
 */
public class CustomAlerts {

//<editor-fold defaultstate="collapsed" desc="Globle-Data">
    public static ButtonType btnYes = new ButtonType("ඔව්", ButtonBar.ButtonData.YES);
    public static ButtonType btnNo = new ButtonType("නැත", ButtonBar.ButtonData.NO);

    public static ButtonType btnOk = new ButtonType("හරි", ButtonBar.ButtonData.OK_DONE);
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Add-Operation">
    public static Optional<ButtonType> showAddConfirmation(String confirmation) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("තහවුරු කිරීම");
        alert.setHeaderText("");
        alert.setContentText(confirmation);

        alert.getButtonTypes().setAll(btnNo, btnYes);
        return alert.showAndWait();

    }

    public static Optional<ButtonType> showAddError(String errors) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("තොරතුරු වැරදි !");
        alert.setHeaderText("පහත තොරතුරු වැරදි !");
        alert.setContentText(errors);

        alert.getButtonTypes().setAll(btnOk);
        return alert.showAndWait();

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Delete-Operation">
    public static Optional<ButtonType> showDeleteConfirmation() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("දත්ත ගොනුවෙන් ඉවත් කිරීම");
        alert.setHeaderText(" ");
        alert.setContentText("තෝරාගන්නා ලද අයිතමය දත්ත ගොනුවෙන් ඉවත් කිරීමට අවශ්‍යද?");

        alert.getButtonTypes().setAll(btnNo, btnYes);
        return alert.showAndWait();

    }

    public static Optional<ButtonType> showDeleteError() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("දත්ත ගොනුවෙන් ඉවත් කිරීම");
        alert.setHeaderText(" ");
        alert.setContentText("දත්ත ගොනුවෙන් ඉවත් කළ නොහැක. \nඇතැම් තොරතුරු යාවත්කාලීන කර ඇත.");

        alert.getButtonTypes().setAll(btnOk);
        return alert.showAndWait();

    }
    
    public static Optional<ButtonType> showDeleteAssignedDataError() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("දත්ත ගොනුවෙන් ඉවත් කිරීම");
        alert.setHeaderText(" ");
        alert.setContentText("දත්ත ගොනුවෙන් ඉවත් කළ නොහැක. \n අනුයුක්තිත දත්ත ඇත.");

        alert.getButtonTypes().setAll(btnOk);
        return alert.showAndWait();

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Update-Operation">
    public static Optional<ButtonType> showUpdateConfirmation(String updates) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("තහවුරු කිරීම");
        alert.setHeaderText("තොරතුරු යාවත්කාලීන කිරීම");
        alert.setContentText(updates);
        
        alert.getButtonTypes().setAll(btnNo, btnYes);
        
        return alert.showAndWait();
    }
    
    public static Optional<ButtonType> showNoUpdatesError() {
        
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("අසාර්ථකයි !");
        alert.setHeaderText(" ");
        alert.setContentText("තොරතුරු යාවත්කාලීන කර නැත");
        
        alert.getButtonTypes().setAll(btnOk);
        return alert.showAndWait();
        
    }
    
    public static Optional<ButtonType> showUpdatesError(String errors) {
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("අසාර්ථකයි !");
        alert.setHeaderText("තොරතුරු යාවත්කාලීන කළ නොහැක.");
        alert.setContentText(errors);
        
        alert.getButtonTypes().setAll(btnOk);
        return alert.showAndWait();
        
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Clear-Operation">
    public static Optional<ButtonType> showClearFormConfirmation(){
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("තහවුරු කිරීම");
        alert.setHeaderText(" ");
        alert.setContentText("පෝරමය නැවත මුල් තත්ත්වයට පත් කිරීමට අවශ්‍යද?");
        
        alert.getButtonTypes().setAll(btnNo, btnYes);
        return alert.showAndWait();
        
    }
//</editor-fold>
 
//<editor-fold defaultstate="collapsed" desc="Clear-Search-Operation">
    public static Optional<ButtonType> showSearchClearConfirmation() {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("තහවුරු කිරීම");
        alert.setHeaderText(" ");
        alert.setContentText("සෙවුම මුල් තත්ත්වයට පත්කිරීමට අවශ්‍යද?");
        
        alert.getButtonTypes().setAll(btnNo, btnYes);
        return alert.showAndWait();
        
    }
//</editor-fold>
 
}
