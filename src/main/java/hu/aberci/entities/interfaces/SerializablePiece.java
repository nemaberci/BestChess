package hu.aberci.entities.interfaces;

import java.io.Serializable;

public interface SerializablePiece extends Serializable {

    SerializableTile getTile();

    PieceType getPieceType();

    PlayerColor getPlayerColor();

    Integer getID();

}
