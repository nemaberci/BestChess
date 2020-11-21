package hu.aberci.entities.interfaces;

import javafx.beans.property.ObjectProperty;

import java.io.Serializable;

public interface Piece extends Serializable {

    ObjectProperty<PlayerColor> getPlayerColorProperty();

    ObjectProperty<Tile> getTileProperty();

    ObjectProperty<PieceType> getPieceTypeProperty();

}
