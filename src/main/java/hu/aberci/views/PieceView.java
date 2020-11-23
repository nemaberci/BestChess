package hu.aberci.views;

import hu.aberci.entities.interfaces.Piece;
import hu.aberci.util.Util;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

public class PieceView extends Button {

    @Getter
    @Setter
    private ObjectProperty<Piece> pieceProperty;

    private ChessBoardView parentPane;

    public PieceView(ChessBoardView parent, Piece piece) {

        super();

        parentPane = parent;

        pieceProperty = new SimpleObjectProperty<>(piece);

        setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                        System.out.println("I WAS CLICKED, I CONTAIN A PIECE " + pieceProperty.get().getPieceTypeProperty().get() + " THAT IS " + pieceProperty.get().getPlayerColorProperty().get());

                    }
                }
        );

        ImageView imageView = new ImageView(
                Util.getPieceImage(
                        pieceProperty.get()
                )
        );

        imageView.fitWidthProperty().bind(
                widthProperty()
        );

        imageView.fitHeightProperty().bind(
                heightProperty()
        );

        imageView.setPreserveRatio(true);

        graphicProperty().setValue(
                imageView
        );

        setStyle(
                "-fx-background-color: transparent; -fx-border-color: transparent"
        );

    }

}
