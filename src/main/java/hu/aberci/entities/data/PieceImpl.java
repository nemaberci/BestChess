package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

public class PieceImpl implements Piece {

    @Getter
    ObjectProperty<PlayerColor> playerColorProperty;

    @Getter
    ObjectProperty<PieceType> pieceTypeProperty;

    @Getter
    ObjectProperty<Tile> tileProperty;

    PieceImpl(Tile startingTile, PieceType pieceType, PlayerColor playerColor) {

        playerColorProperty = new SimpleObjectProperty<>(
                playerColor
        );

        pieceTypeProperty = new SimpleObjectProperty<>(
                pieceType
        );

        tileProperty = new SimpleObjectProperty<>(
                startingTile
        );

    }

    public PieceImpl(Piece piece, BoardState boardState) {

        playerColorProperty = new SimpleObjectProperty<>();
        playerColorProperty.set(
                piece.getPlayerColorProperty().get()
        );

        pieceTypeProperty = new SimpleObjectProperty<>();
        pieceTypeProperty.set(
                piece.getPieceTypeProperty().get()
        );

        tileProperty = new SimpleObjectProperty<>();
        tileProperty.set(boardState.getTilesProperty().get()
                .get(piece.getTileProperty().get().getXProperty().get())
                .get(piece.getTileProperty().get().getYProperty().get()));

    }

}
