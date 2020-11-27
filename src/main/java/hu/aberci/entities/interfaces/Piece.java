package hu.aberci.entities.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

import java.io.Serializable;

/**
 * Interface for a piece on a chess board. This class uses FAVAFX properties
 * because they are easy to use when connecting to the GUI.
 * */
public interface Piece {

    /**
     * JAVAFX property storing the color of this piece.
     *
     * @return The corresponding property
     * */
    ObjectProperty<PlayerColor> getPlayerColorProperty();

    /**
     * JAVAFX property storing the tile that this piece is occupying.
     *
     * @return The corresponding property.
     * */
    ObjectProperty<Tile> getTileProperty();

    /**
     * JAVAFX property storing the type of this piece.
     *
     * @return The corresponding property.
     * */
    ObjectProperty<PieceType> getPieceTypeProperty();

    /**
     * JAVAFX property storing the unique ID of this piece. This can be used to determine
     * whether two pieces are the same.
     *
     * @return The corresponding property.
     * */
    IntegerProperty getIDProperty();

    /**
     * Determines whether a piece equals another piece. This should be done
     * using the pieces' IDs.
     *
     * @return {@code true} if they are equal and {@code false} if they are not.
     * */
    boolean equals(Piece piece);

}
