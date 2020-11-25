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

public class ChessClockController {

    @Getter
    private ObjectProperty<ChessClock> chessClockProperty;

    @Setter
    Parent parent;

    BoardState board;

    @Getter
    private Runnable stepRunnable = this::step;

    ChessClockController(Parent parentPane, BoardState boardState, ChessClock clock) {

        //System.out.println(this + " was created");

        chessClockProperty = new SimpleObjectProperty<>(
                clock
        );

        parent = parentPane;
        board = boardState;

    }

    public void click() {

        System.out.println("CLICKING");

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

    public void step() {

        //System.out.println(this + " was stepped");

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

        //System.out.println("TIMES: " + chessClockProperty.get().getWhiteTimeProperty().get() + ", " + chessClockProperty.get().getBlackTimeProperty().get());

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
