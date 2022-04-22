package server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ServerDocument.fxml"));
        Parent root = loader.load();
        ServerController controller = loader.getController();
        
        Scene scene = new Scene(root);
        AlertBox close = new AlertBox();
        
        stage.setScene(scene);
        stage.setTitle("Server - Ships Game");
        stage.setOnCloseRequest(e -> close.onClosingWindow(e));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
