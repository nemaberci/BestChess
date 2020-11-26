package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.ChessClock;
import hu.aberci.entities.interfaces.PlayerColor;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data class responsible for storing the properties of the chess clock
 * No special logic is implemented in this class, for more info see: {@link ChessClock}
 * */
@Accessors(chain = true)
@NoArgsConstructor
public class ChessClockImpl implements ChessClock {

    @Getter
    @Setter
    int increment;

    @Getter
    ObjectProperty<PlayerColor> playerTurnProperty;

    @Getter
    IntegerProperty whiteTimeProperty, blackTimeProperty;

    public ChessClockImpl(int startingTime, int timeIncrement) {

        increment = timeIncrement;

        whiteTimeProperty = new SimpleIntegerProperty(startingTime);
        blackTimeProperty = new SimpleIntegerProperty(startingTime);

        playerTurnProperty = new SimpleObjectProperty<>(
                PlayerColor.WHITE
        );

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
