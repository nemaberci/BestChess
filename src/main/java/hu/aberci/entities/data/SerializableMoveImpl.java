package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.SerializableBoardState;
import hu.aberci.entities.interfaces.SerializableMove;
import hu.aberci.entities.interfaces.SerializablePiece;
import hu.aberci.entities.interfaces.SerializableTile;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SerializableMoveImpl implements SerializableMove {

    SerializableTile sourceTile, targetTile;

    SerializablePiece piece;

    SerializableBoardState boardState;

}
