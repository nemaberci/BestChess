package hu.aberci.entities.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Parent;

import java.io.Serializable;

public interface ChessClock {

    ObjectProperty<PlayerColor> getPlayerTurnProperty();

    IntegerProperty getBlackTimeProperty();

    IntegerProperty getWhiteTimeProperty();

    int getIncrement();

    ChessClock setIncrement(int increment);

}
