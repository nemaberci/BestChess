package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.ChessClock;
import hu.aberci.entities.interfaces.PlayerColor;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;

import java.util.TimerTask;

public class ChessClockImpl implements ChessClock {

    @Getter
    int increment;

    @Getter
    ObjectProperty<PlayerColor> playerTurnProperty;

    @Getter
    IntegerProperty whiteTimeProperty, blackTimeProperty;

    @Override
    public void step() {
        if (playerTurnProperty.get() == PlayerColor.WHITE) {
            whiteTimeProperty.set(whiteTimeProperty.get() - 1);
        } else {
            blackTimeProperty.set(blackTimeProperty.get() - 1);
        }
    }

    public void click() {
        if (playerTurnProperty.get() == PlayerColor.WHITE) {
            whiteTimeProperty.set(whiteTimeProperty.get() + increment);
            playerTurnProperty.set(PlayerColor.BLACK);
        } else {
            blackTimeProperty.set(blackTimeProperty.get() + increment);
            playerTurnProperty.set(PlayerColor.WHITE);
        }
    }

    ChessClockImpl(int startingTime, int timeIncrement) {

        increment = timeIncrement;

        whiteTimeProperty = new SimpleIntegerProperty(startingTime);
        blackTimeProperty = new SimpleIntegerProperty(startingTime);

    }

    public ChessClockImpl(ChessClock chessClock) {

        increment = chessClock.getIncrement();

        whiteTimeProperty = new SimpleIntegerProperty(chessClock.getWhiteTimeProperty().get());
        blackTimeProperty = new SimpleIntegerProperty(chessClock.getBlackTimeProperty().get());

    }

    @Override
    public void run() {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                step();
            }
        };

    }
}
