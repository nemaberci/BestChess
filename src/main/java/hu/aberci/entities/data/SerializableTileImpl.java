package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.SerializablePiece;
import hu.aberci.entities.interfaces.SerializableTile;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SerializableTileImpl implements SerializableTile {

    Integer X, Y;

    SerializablePiece piece;

}
