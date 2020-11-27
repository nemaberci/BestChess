package hu.aberci.entities.interfaces;

import java.io.Serializable;

/**
 * Serializable version of {@link Tile}.
 * */
public interface SerializableTile extends Serializable {

    Integer getX();
    Integer getY();

    SerializablePiece getPiece();

    SerializableTile setPiece(SerializablePiece piece);
    SerializableTile setX(Integer x);
    SerializableTile setY(Integer y);

}
