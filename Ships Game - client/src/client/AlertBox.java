package client;

import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;

public class AlertBox {

    String title;
    String setHeaderText;
    Alert alert = new Alert(AlertType.CONFIRMATION);

    public AlertBox(String title, String setHeaderText) {
        this.title = title;
        this.setHeaderText = setHeaderText;

        alert.setTitle(this.title);
        alert.setHeaderText(this.setHeaderText);
        alert.showAndWait();
    }

    public AlertBox(String title, String setHeaderText, WindowEvent e) {
        this.title = title;
        this.setHeaderText = setHeaderText;

        alert.setTitle(this.title);
        alert.setHeaderText(this.setHeaderText);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        } else {
            e.consume();
        }
    }

}
