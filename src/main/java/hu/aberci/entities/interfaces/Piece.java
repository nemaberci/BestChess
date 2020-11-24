package hu.aberci.entities.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

import java.io.Serializable;

public interface Piece {

    ObjectProperty<PlayerColor> getPlayerColorProperty();

    ObjectProperty<Tile> getTileProperty();

    ObjectProperty<PieceType> getPieceTypeProperty();

    IntegerProperty getIDProperty();

    boolean equals(Piece piece);

}
