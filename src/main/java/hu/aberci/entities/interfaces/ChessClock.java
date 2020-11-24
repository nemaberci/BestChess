package hu.aberci.entities.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

import java.io.Serializable;

public interface ChessClock {

    ObjectProperty<PlayerColor> getPlayerTurnProperty();

    IntegerProperty getBlackTimeProperty();

    IntegerProperty getWhiteTimeProperty();

    int getIncrement();

    void setIncrement(int increment);

    void step();

    void click();

    void startClock();

    void stopClock();

}
