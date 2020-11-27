package hu.aberci.entities.interfaces;

import java.io.Serializable;

/**
 * Serializable version of {@link ChessClock}.
 * */
public interface SerializableChessClock extends Serializable {

    PlayerColor getPlayerColor();

    Integer getBlackTime();
    Integer getWhiteTime();

    Integer getIncrement();

    SerializableChessClock setPlayerColor(PlayerColor playerColor);

    SerializableChessClock setBlackTime(Integer blackTime);
    SerializableChessClock setWhiteTime(Integer whiteTime);

    SerializableChessClock setIncrement(Integer increment);

}
