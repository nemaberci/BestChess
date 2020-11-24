package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.ChessClock;
import hu.aberci.entities.interfaces.PlayerColor;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.*;

public class ChessClockImpl implements ChessClock {

    @Getter
    @Setter
    int increment;

    @Getter
    ObjectProperty<PlayerColor> playerTurnProperty;

    @Getter
    IntegerProperty whiteTimeProperty, blackTimeProperty;

    ScheduledExecutorService scheduledExecutorService;

    ScheduledFuture<?> runningClock;

    @Override
    public void step() {
        if (playerTurnProperty.get() == PlayerColor.WHITE) {
            whiteTimeProperty.set(whiteTimeProperty.get() - 1);
        } else {
            blackTimeProperty.set(blackTimeProperty.get() - 1);
        }
    }

    public synchronized void click() {
        if (playerTurnProperty.get() == PlayerColor.WHITE) {
            whiteTimeProperty.set(whiteTimeProperty.get() + increment);
            playerTurnProperty.set(PlayerColor.BLACK);
        } else {
            blackTimeProperty.set(blackTimeProperty.get() + increment);
            playerTurnProperty.set(PlayerColor.WHITE);
        }
    }

    @Override
    public void startClock() {

        runningClock = scheduledExecutorService.schedule(this::step, 1, TimeUnit.SECONDS);

    }

    public void stopClock() {

        if (runningClock != null) {

            runningClock.cancel(true);

        }

    }

    ChessClockImpl(int startingTime, int timeIncrement) {

        increment = timeIncrement;

        whiteTimeProperty = new SimpleIntegerProperty(startingTime);
        blackTimeProperty = new SimpleIntegerProperty(startingTime);

        scheduledExecutorService = Executors.newScheduledThreadPool(1);

        playerTurnProperty = new SimpleObjectProperty<>(
                PlayerColor.WHITE
        );

        runningClock = null;

    }

    public ChessClockImpl(ChessClock chessClock) {

        increment = chessClock.getIncrement();

        whiteTimeProperty = new SimpleIntegerProperty(chessClock.getWhiteTimeProperty().get());
        blackTimeProperty = new SimpleIntegerProperty(chessClock.getBlackTimeProperty().get());

        playerTurnProperty = new SimpleObjectProperty<>(
                chessClock.getPlayerTurnProperty().get()
        );

    }

}
