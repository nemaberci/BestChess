package hu.aberci.entities.interfaces;

import java.io.Serializable;

/**
 * Serializable version of {@link Move}.
 * */
public interface SerializableMove extends Serializable {

    SerializablePiece getPiece();
    SerializableBoardState getBoardState();
    SerializableTile getSourceTile();
    SerializableTile getTargetTile();

    SerializableMove setPiece(SerializablePiece serializablePiece);
    SerializableMove setBoardState(SerializableBoardState serializableBoardState);
    SerializableMove setSourceTile(SerializableTile serializableTile);
    SerializableMove setTargetTile(SerializableTile serializableTile);

}
