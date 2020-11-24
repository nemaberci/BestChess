package hu.aberci.entities.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

import java.io.Serializable;

public interface Tile {

    ObjectProperty<Piece> getPieceProperty();

    IntegerProperty getXProperty();
    IntegerProperty getYProperty();

}
