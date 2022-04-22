package client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class ClientDocumentController implements Initializable {

    @FXML
    public GridPane myGridPane;
    @FXML
    public GridPane opponentGridPane;
    @FXML
    public ImageView ship_0;
    @FXML
    public ImageView ship_1;
    @FXML
    public ImageView ship_2;
    @FXML
    public ImageView ship_3;
    @FXML
    public TextField nickField;
    @FXML
    public ChoiceBox choiceBox;
    @FXML
    public Button button;

    private OwnShipPane osp;
    private Player p;
    private Connect connect;

    @FXML
    private void onJoinClick(ActionEvent event) throws IOException {
        //if (osp.shipPositionIsSet()) {
        if (connect.isConneted()) {
            String nick = nickField.getText();
            if (!nick.isEmpty()) {
                if (button.getText().equals("Dołącz do pokoju")) {
                    p.login(UUID.randomUUID().toString());
                    p.joinRoom("Default Room");
                    String choice = choiceBox.getSelectionModel().getSelectedItem().toString();
                    if (p.checkNickExist(nick, choice) == false) {
                        int getPlayerInRoom = p.onlinePlayerRoom(choice);
                        if (getPlayerInRoom < 2) {
                            if (getPlayerInRoom == 0) {
                                p.setId(0);
                            } else {
                                p.setId(1);
                            }
                            p.leaveRoom();
                            p.login(nick);
                            p.joinRoom(choice);
                            p.sendShipPosition(osp.getShipPositionArray());
                            nickField.setDisable(true);
                            choiceBox.setDisable(true);
                            button.setText("Odejdź z pokoju");
                        } else {
                            new AlertBox("Max Connection", "Max Connection. This room is busy!");
                        }
                    } else {
                        new AlertBox("Client Settings", "Your nick '" + nick + "' is busy.");
                    }
                } else {
                    p.leaveRoom();
                    nickField.setDisable(false);
                    choiceBox.setDisable(false);
                    button.setText("Dołącz do pokoju");
                }
            } else {
                new AlertBox("Nick Settings", "Plase set your name.");
            }
        } else {
            new AlertBox("Server Connection", "Server is Offline. Error Server Connection!");

        }
        //} else {
        //  new AlertBox("Ship Error", "Please set all ships on the board.");
        //}
    }

    public void drawOwnGui(GridPane myGridPane) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                ImageView imageView = new ImageView(getClass().getResource("/resources/1.png").toExternalForm());
                myGridPane.add(imageView, x, y);
            }
        }
    }

    public void drawOponentGui(GridPane opponentGridPane) {

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                ImageView imageView = new ImageView(getClass().getResource("/resources/1.png").toExternalForm());

                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        ImageView dot = new ImageView(getClass().getResource("/resources/dot.png").toExternalForm());
                        ImageView busyShip = new ImageView(getClass().getResource("/resources/boat_0_x.png").toExternalForm());
                        Node node = event.getPickResult().getIntersectedNode();
                        Integer columnIndex = GridPane.getColumnIndex(node);
                        Integer rowIndex = GridPane.getRowIndex(node);
                        if (p.isShipOnPosition(columnIndex, rowIndex)) {
                            opponentGridPane.add(busyShip, columnIndex, rowIndex, 1, 1);
                        } else {
                            opponentGridPane.add(dot, columnIndex, rowIndex, 1, 1);
                        }
                        System.out.println("X: " + columnIndex + " Y: " + rowIndex + " " + p.isShipOnPosition(columnIndex, rowIndex));
                    }
                });
                opponentGridPane.add(imageView, x, y);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        drawOwnGui(myGridPane);
        drawOponentGui(opponentGridPane);

        Ship s3 = new Ship("boat_3", ship_3, 1);
        s3.onDragDetected();
        s3.onDragDone();

        Ship s2 = new Ship("boat_2", ship_2, 2);
        s2.onDragDetected();
        s2.onDragDone();

        Ship s1 = new Ship("boat_1", ship_1, 3);
        s1.onDragDetected();
        s1.onDragDone();

        Ship s0 = new Ship("boat_0", ship_0, 4);
        s0.onDragDetected();
        s0.onDragDone();

        osp = new OwnShipPane(myGridPane);
        osp.setOnDragOver();
        osp.setOnDragDropped();
        p = new Player();
        connect = new Connect();
        //connect.checking();

    }
}
