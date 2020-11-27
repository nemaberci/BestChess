package hu.aberci.entities.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

import java.io.Serializable;

/**
 * Interface for a chess board's single tile. This class uses FAVAFX properties
 * because they are easy to use when connecting to the GUI.
 * */
public interface Tile {

    /**
     * JAVAFX property that stores the piece currently on this tile.
     *
     * @return The corresponding property.
     * */
    ObjectProperty<Piece> getPieceProperty();

    /**
     * JAVAFX property that stores the X coordinate of the tile on the board.
     *
     * @return The corresponding property.
     * */
    IntegerProperty getXProperty();

    /**
     * JAVAFX property that stores the Y coordinate of the tile on the board.
     *
     * @return The corresponding property.
     * */
    IntegerProperty getYProperty();

}
