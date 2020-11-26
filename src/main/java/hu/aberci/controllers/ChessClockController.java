package hu.aberci.controllers;

import hu.aberci.entities.events.ChessBoardEvent;
import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.ChessClock;
import hu.aberci.entities.interfaces.PlayerColor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;

/**
 * Class responsible for handling a chess clock. Has two core functions: {@link #step() step} and {@link #click()}
 * */
public class ChessClockController {

    /**
     * The chess clock controlled by the controller.
     * */
    @Getter
    private ObjectProperty<ChessClock> chessClockProperty;

    /**
     * The parent element that should recieve events.
     * */
    @Setter
    Parent parent;

    /**
     * The BoardState that the events are valid for.
     * */
    BoardState board;

    /**
     * The Runnable that allows an ExecutorService to call the controller's step function
     * */
    @Getter
    private Runnable stepRunnable = this::step;

    /**
     * The only constructor for the class. Constructs a ChessClockController with the necessary properties.
     *
     * @param parentPane The Parent element for which the events are sent.
     * @param boardState The BoardState that is affected by sent events.
     * @param clock The ChessClock data class that will be manipulated.
     * */
    ChessClockController(Parent parentPane, BoardState boardState, ChessClock clock) {

        chessClockProperty = new SimpleObjectProperty<>(
                clock
        );

        parent = parentPane;
        board = boardState;

    }

    /**
     * This function does what a chess clock's button would do. It changes the current player's turn to the opposite
     * and adds the specified increment to the current player's turn. No event can occur while a chessclock is clicked.
     * */
    public void click() {

        PlayerColor currentColor = chessClockProperty.get().getPlayerTurnProperty().get();
        PlayerColor opponentColor = chessClockProperty.get().getPlayerTurnProperty().get() == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;

        if (PlayerColor.WHITE.equals(currentColor)) {

            chessClockProperty.get().getWhiteTimeProperty().set(
                    chessClockProperty.get().getWhiteTimeProperty().get() + chessClockProperty.get().getIncrement()
            );

        } else {

            chessClockProperty.get().getBlackTimeProperty().set(
                    chessClockProperty.get().getBlackTimeProperty().get() + chessClockProperty.get().getIncrement()
            );

        }

        chessClockProperty.get().getPlayerTurnProperty().set(
                opponentColor
        );

    }

    /**
     * Ticks the current player's clock and sends {@link ChessBoardEvent#CHESS_BOARD_EVENT_CLOCK_FLAG clock flag event} if a player's time ran out.
     *
     * @see ChessBoardEvent#CHESS_BOARD_EVENT_CLOCK_FLAG
     * */
    public void step() {

        PlayerColor currentColor = chessClockProperty.get().getPlayerTurnProperty().get();

        if (PlayerColor.WHITE.equals(currentColor)) {

            chessClockProperty.get().getWhiteTimeProperty().set(
                    chessClockProperty.get().getWhiteTimeProperty().get() - 1
            );

        } else {

            chessClockProperty.get().getBlackTimeProperty().set(
                    chessClockProperty.get().getBlackTimeProperty().get() - 1
            );

        }

        if (chessClockProperty.get().getBlackTimeProperty().get() == 0 || chessClockProperty.get().getWhiteTimeProperty().get() == 0) {

            parent.fireEvent(
                    new ChessBoardEvent(
                            ChessBoardEvent.CHESS_BOARD_EVENT_CLOCK_FLAG,
                            board
                    )
            );

        }

    }

}
