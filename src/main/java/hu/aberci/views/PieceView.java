package hu.aberci.views;

import hu.aberci.entities.interfaces.Piece;
import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
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

        pieceProperty.set(piece);

        setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {



                    }
                }
        );

    }

}
