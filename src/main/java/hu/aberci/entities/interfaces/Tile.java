package hu.aberci.entities.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

import java.io.Serializable;

public interface Tile extends Serializable {

    ObjectProperty<Piece> getPieceProperty();

    IntegerProperty getXProperty();
    IntegerProperty getYProperty();

}
