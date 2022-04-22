
package server;

import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;


public class AlertBox {
    Server server = Server.getInstance();
   public void onClosingWindow(WindowEvent e)  {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Closing the Server");
        alert.setHeaderText("Are you sure you want to disable the server?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (server.isEnabled()) {
                try {
                    server.serverStop();
                } catch (IOException ex) {
                    System.out.println(ex.getCause());
                }
            }
            Platform.exit();
            System.exit(0);
        } else {
            e.consume();
        }
    } 
}
