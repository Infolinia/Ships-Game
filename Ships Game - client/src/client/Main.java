package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ClientDocument.fxml"));
        Scene scene = new Scene(root);
        scene.setCursor(Cursor.HAND);
        stage.setTitle("Client - Ships Game");
        stage.setScene(scene);
        stage.setOnCloseRequest(e ->  new AlertBox("Closing the Client", "Are you sure you want to disable the client?", e));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
