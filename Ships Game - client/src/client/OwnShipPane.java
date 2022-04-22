/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

public class OwnShipPane {

    private final GridPane myGridPane;
    private int[][] shipPositionArray = new int[10][10];

    public int[][] getShipPositionArray() {
        return shipPositionArray;
    }

    public boolean shipPositionIsSet() {
        int count = 0;
        for (int x = 0; x < shipPositionArray.length; x++) {
            for (int y = 0; y < shipPositionArray.length; y++) {
                if (shipPositionArray[x][y] == 1) {
                    count++;
                }
            }
        }
        if (count == 20) {
            return true;
        }
        return false;
    }

    public int getImageColspan(Image image) {
        if ((int) image.getHeight() > image.getWidth()) {
            return 1;
        }
        return (int) (image.getWidth() / image.getHeight());
    }

    // funkcja odpowiedzialna za sprawdzenie translacji
    public boolean isRotated(Image image) {
        if ((int) image.getHeight() > image.getWidth()) {
            return true;
        }
        return false;
    }

    // funkcja odpowiedzialna za sprawdzenie ilości wierszy statku
    public int getImageRowspan(Image image) {
        if ((int) image.getHeight() > image.getWidth()) {
            return (int) (image.getHeight() / image.getWidth());
        }
        return 1;
    }

    public OwnShipPane(GridPane myGridPane) {
        this.myGridPane = myGridPane;
    }

    public void setOnDragOver() {
        myGridPane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != myGridPane
                        && event.getDragboard().hasImage()) {
                    boolean isClear = true;
                    Node node = event.getPickResult().getIntersectedNode();
                    Integer columnIndex = GridPane.getColumnIndex(node);
                    Integer rowIndex = GridPane.getRowIndex(node);
                    Dragboard db = event.getDragboard();
                    Image image = db.getImage();
                    if (getImageColspan(image) + columnIndex <= 10 && getImageRowspan(image) + rowIndex <= 10) {
                        for (int i = columnIndex; i < columnIndex + getImageColspan(image); i++) {
                            for (int j = rowIndex; j < rowIndex + getImageRowspan(image); j++) {
                                if (shipPositionArray[i][j] == 1 || shipPositionArray[i][j] == 2) {
                                    isClear = false;
                                    break;
                                }
                            }
                        }
                        if (isClear) {
                            event.acceptTransferModes(TransferMode.MOVE);
                        }
                    }
                }
                event.consume();
            }
        }
        );
    }

    public void setOnDragDropped() {
        myGridPane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                Node node = event.getPickResult().getIntersectedNode();
                if (node != myGridPane && db.hasImage()) {
                    Integer columnIndex = GridPane.getColumnIndex(node);
                    Integer rowIndex = GridPane.getRowIndex(node);
                    int x = columnIndex == null ? 0 : columnIndex;
                    int y = rowIndex == null ? 0 : rowIndex;
                    Image i = db.getImage();
                    ImageView image = new ImageView(i);
                    myGridPane.add(image, x, y, getImageColspan(i), getImageRowspan(i));

                    if (!isRotated(i)) {
                        for (int j = x - 1; j < getImageColspan(i) + x + 1; j++) {
                            for (int k = y - 1; k < getImageRowspan(i) + y + 1; k++) {
                                if (j >= 0 && k >= 0 && j < 10 && k < 10) {
                                    if (y == k && j >= x && j < getImageColspan(i) + x) {
                                        shipPositionArray[j][k] = 1;
                                    } else {
                                        if (shipPositionArray[j][k] != 1) {
                                            shipPositionArray[j][k] = 2;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        for (int j = x - 1; j < getImageColspan(i) + x + 1; j++) {
                            for (int k = y - 1; k < getImageRowspan(i) + y + 1; k++) {
                                if (j >= 0 && k >= 0 && j < 10 && k < 10) {
                                    if (x == j && k >= y && k < getImageRowspan(i) + y) {
                                        shipPositionArray[k][j] = 1;
                                    } else {
                                        if (shipPositionArray[j][k] != 1) {
                                            shipPositionArray[j][k] = 2;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        }
        );

    }
}
