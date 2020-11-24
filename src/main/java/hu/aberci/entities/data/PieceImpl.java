package hu.aberci.entities.data;

import hu.aberci.controllers.ChessGameController;
import hu.aberci.entities.interfaces.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

public class PieceImpl implements Piece {

    @Getter
    ObjectProperty<PlayerColor> playerColorProperty;

    @Getter
    ObjectProperty<PieceType> pieceTypeProperty;

    @Getter
    ObjectProperty<Tile> tileProperty;

    @Getter
    IntegerProperty IDProperty;

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

        IDProperty = new SimpleIntegerProperty(
                ChessGameController.nextPieceId()
        );

    }

    PieceImpl(Tile startingTile, PieceType pieceType, PlayerColor playerColor, Integer ID) {

        playerColorProperty = new SimpleObjectProperty<>(
                playerColor
        );

        pieceTypeProperty = new SimpleObjectProperty<>(
                pieceType
        );

        tileProperty = new SimpleObjectProperty<>(
                startingTile
        );

        IDProperty = new SimpleIntegerProperty(
                ID
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

        IDProperty = new SimpleIntegerProperty(
                piece.getIDProperty().get()
        );

    }

    public boolean equals(Piece piece) {

        return piece.getIDProperty().get() == getIDProperty().get();

    }

}
