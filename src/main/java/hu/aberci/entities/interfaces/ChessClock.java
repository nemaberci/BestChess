package hu.aberci.entities.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

import java.io.Serializable;

public interface ChessClock extends Serializable, Runnable {

    ObjectProperty<PlayerColor> getPlayerTurnProperty();

    IntegerProperty getBlackTimeProperty();

    IntegerProperty getWhiteTimeProperty();

    int getIncrement();

    void step();

    void click();

}
