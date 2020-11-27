package hu.aberci.entities.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Parent;

import java.io.Serializable;

/**
 * Interface for a chess clock's current state. This class uses FAVAFX properties
 * because they are easy to use when connecting to the GUI.
 * */
public interface ChessClock {

    /**
     * JAVAFX property storing which player's turn it is. When the clock ticks
     * this player's time gets decremented.
     *
     * @return The corresponding property
     * */
    ObjectProperty<PlayerColor> getPlayerTurnProperty();

    /**
     * JAVAFX property storing the black player's time left in seconds.
     *
     * @return The corresponding property
     * */
    IntegerProperty getBlackTimeProperty();

    /**
     * JAVAFX property storing the white player's time left in seconds.
     *
     * @return The corresponding property
     * */
    IntegerProperty getWhiteTimeProperty();

    /**
     * Returns the increment (in seconds) that gets added to each player's clock after their move.
     * This is not stored as a Property.
     *
     * @return The increment used in seconds.
     * */
    int getIncrement();

    /**
     * Sets the increment (in seconds) to be used in this clock.
     *
     * @param increment The new increment to be used in this clock.
     * @return This object. This can be used to chain setters.
     * */
    ChessClock setIncrement(int increment);

}
