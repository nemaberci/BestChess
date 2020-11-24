package hu.aberci.entities.interfaces;

import java.io.Serializable;

public interface SerializableMove extends Serializable {

    SerializablePiece getPiece();
    SerializableBoardState getBoardState();
    SerializableTile getSourceTile();
    SerializableTile getTargetTile();

}
