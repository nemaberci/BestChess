package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.PlayerColor;
import hu.aberci.entities.interfaces.SerializableChessClock;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SerializableChessClockImpl implements SerializableChessClock {

    PlayerColor playerColor;
    Integer blackTime, whiteTime, increment;

}
