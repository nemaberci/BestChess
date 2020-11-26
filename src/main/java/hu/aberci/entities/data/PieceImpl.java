package hu.aberci.entities.data;

import hu.aberci.controllers.ChessGameController;
import hu.aberci.entities.interfaces.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

/**
 * Data class responsible for storing Pieces.
 * No special logic is implemented in this class, for more info see: {@link Piece}
 * */
public class PieceImpl implements Piece {

    @Getter
    ObjectProperty<PlayerColor> playerColorProperty;

    @Getter
    ObjectProperty<PieceType> pieceTypeProperty;

    @Getter
    ObjectProperty<Tile> tileProperty;

    @Getter
    IntegerProperty IDProperty;

    /**
     * Creates a new Piece with a new unique ID.
     *
     * @param startingTile The Piece's starting Tile.
     * @param pieceType The Piece's PieceType. This should not be changed later.
     * @param playerColor The Piece's Color. This should not be changed later.
     * */
    public PieceImpl(Tile startingTile, PieceType pieceType, PlayerColor playerColor) {

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

    /**
     * Creates a new Piece with a given ID. This constructor does not step the static ID counter.
     *
     * @param startingTile The Piece's starting Tile.
     * @param pieceType The Piece's PieceType. This should not be changed later.
     * @param playerColor The Piece's Color. This should not be changed later.
     * @param ID The Piece's ID.
     * */
    public PieceImpl(Tile startingTile, PieceType pieceType, PlayerColor playerColor, Integer ID) {

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

    /**
     * Copy constructor for a Piece. For a true deep copy we need the original Piece's BoardState.
     *
     * @param piece The original Piece.
     * @param boardState The original Piece's BoardState.
     * */
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

    /**
     * Determines whether two pieces are identical. Every piece's ID is unique, so comparing IDs is sufficient
     * in determining equality.
     * */
    public boolean equals(Piece piece) {

        return piece.getIDProperty().get() == getIDProperty().get();

    }

}
