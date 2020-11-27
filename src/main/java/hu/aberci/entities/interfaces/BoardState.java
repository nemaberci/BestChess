package hu.aberci.entities.interfaces;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for a chess board's current state. This class uses FAVAFX properties
 * because they are easy to use when connecting to the GUI.
 * */
public interface BoardState {

    /**
     * JAVAFX property storing the current player turn
     *
     * @return The corresponding property
     * */
    ObjectProperty<PlayerColor> getPlayerTurnProperty();

    /**
     * JAVAFX property storing the chess clock used in this game
     *
     * @return The corresponding property
     * */
    ObjectProperty<ChessClock> getChessClockProperty();

    /**
     * JAVAFX property storing a 2D list of all Tiles of this board
     *
     * @return The corresponding property
     * */
    ListProperty<List<Tile>> getTilesProperty();

    /**
     * JAVAFX property storing all the pieces still in the game. Pieces are grouped by
     * Player color, so accessing a player's piece looks like this:
     * getPiecesProperty().get([player color]).get([piece index])
     *
     * @return The corresponding property
     * */
    MapProperty<PlayerColor, List<Piece>> getPiecesProperty();

    /**
     * JAVAFX property storing all pieces that have been taken. These pieces are not grouped.
     *
     * @return The corresponding property
     * */
    ListProperty<Piece> getTakenPiecesProperty();

    /**
     * JAVAFX property storing all moves made on this board.
     *
     * @return The corresponding property
     * */
    ListProperty<Move> getMovesProperty();

    /**
     * JAVAFX property storing how many times a certain position has been reached.
     * The key is the first part of a FEN code.
     * The value is how many times this has been reached.
     *
     * @return The corresponding property
     * */
    MapProperty<String, Integer> getPositionCounterProperty();

    /**
     * JAVAFX property storing whether the game played on this board is tile controlled.
     *
     * @return The corresponding property
     * */
    BooleanProperty getIsTimeControlledProperty();

    /**
     * Gets the FENCode for this position. For more info read {@link FENCode}.
     *
     * @return The corresponding FENCode
     * @see FENCode
     * */
    FENCode getFEN();

}
