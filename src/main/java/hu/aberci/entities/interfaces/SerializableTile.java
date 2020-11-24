package hu.aberci.entities.interfaces;

import java.io.Serializable;

public interface SerializableTile extends Serializable {

    Integer getX();
    Integer getY();

    SerializablePiece getPiece();

}
