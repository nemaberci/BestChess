package hu.aberci.entities.interfaces;

import java.io.Serializable;

public interface SerializableChessClock extends Serializable {

    PlayerColor getPlayerColor();

    Integer getBlackTime();
    Integer getWhiteTime();

    Integer getIncrement();

}
