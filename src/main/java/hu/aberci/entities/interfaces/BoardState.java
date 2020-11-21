package hu.aberci.entities.interfaces;

import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.Serializable;
import java.util.List;

public interface BoardState extends Serializable {

    ObjectProperty<PlayerColor> getPlayerTurnProperty();

    ObjectProperty<ChessClock> getChessClockProperty();

    ListProperty<List<Tile>> getTilesProperty();

    MapProperty<PlayerColor, List<Piece>> getPiecesProperty();

}
