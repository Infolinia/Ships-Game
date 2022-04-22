package client;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class Ship {

    private final String name;
    private final ImageView imageView;
    private final Image removeImage;
    private Image putImage;
    private int count;
    private int large;

  
    public Ship(String name, ImageView iw, int count) {
        this.name = name;
        this.imageView = iw;
        this.count = count;
        this.removeImage = new Image(getClass().getResource("/resources/" + name + "_x.png").toExternalForm());
    }
    
     public int getCount() {
        return count;
    }

    public void onDragDetected() {
        imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                if (getCount() > 0) {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        putImage = new Image(getClass().getResource("/resources/" + name + "_r.png").toExternalForm());
                    } else {
                        putImage = new Image(getClass().getResource("/resources/" + name + ".png").toExternalForm());
                    }

                    db.setDragView(putImage);
                    content.putImage(putImage);

                    db.setContent(content);
                    event.consume();
                }
            }

        });
    }

    public void onDragDone() {
        imageView.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getTransferMode() == TransferMode.MOVE) {
                    if (getCount() > 0) {
                        count--;
                    }
                    if (getCount() == 0) {
                        imageView.setImage(removeImage);
                    }
                }
                event.consume();
            }
        });
    }
}
