package server;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ServerController implements Initializable {

    @FXML private Button button;
    @FXML private TextArea textArea;
    public static ServerController instance = null;


    public void setTextArea(String text) {
        this.textArea.setText(textArea.getText() + text + "\n");
    }
   
    public static ServerController getInstance(){
        return instance;
    }
    
    @FXML
    private void onClickButton(ActionEvent event) throws IOException {
       if(button.getText().equals("Start Server")){
            Server.getInstance().serverStart();
            setTextArea("Starting server.");
            setTextArea("Waiting for connections...");
            button.setText("Stop Server");
       }else{
            Server.getInstance().serverStop();
            setTextArea("Stoping server...");
            button.setText("Start Server");
       }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
    }
}
