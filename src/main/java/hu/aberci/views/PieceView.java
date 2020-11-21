package hu.aberci.views;

import hu.aberci.entities.events.ChessBoardEvent;
import hu.aberci.entities.events.ChessPieceEvent;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.PieceType;
import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import lombok.Setter;

public class PieceView extends AnchorPane {

    @Getter
    @Setter
    private ObjectProperty<Piece> pieceProperty;

    private GridPane parentPane;

    public PieceView(GridPane parent, Piece piece) {

        super();

        parentPane = parent;

        pieceProperty.set(piece);

        onMouseClickedProperty().set(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                    }
                }
        );

    }

}
