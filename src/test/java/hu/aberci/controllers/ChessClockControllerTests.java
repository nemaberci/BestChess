package hu.aberci.controllers;

import hu.aberci.entities.data.BoardStateImpl;
import hu.aberci.entities.data.ChessClockImpl;
import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.ChessClock;
import hu.aberci.entities.interfaces.PlayerColor;
import javafx.scene.Parent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ChessClockControllerTests {

    ChessClockController chessClockController;

    Parent mockedParent;

    BoardState mockedBoardState;

    ChessClock chessClock;

    @BeforeEach
    public void init() {

        mockedParent = mock(
                Parent.class
        );

        mockedBoardState = mock(
                BoardStateImpl.class
        );

        chessClock = new ChessClockImpl(100, 1);

        chessClockController = new ChessClockController(
                mockedParent,
                mockedBoardState,
                chessClock
        );

    }

    @Test
    public void testClockStepRemovesTimeFromTheRightPlayer() {

        chessClockController.step();

        assertEquals(
                chessClockController.getChessClockProperty().get().getWhiteTimeProperty().get(),
                99
        );

        assertEquals(
                chessClockController.getChessClockProperty().get().getBlackTimeProperty().get(),
                100
        );

    }

    @Test
    public void testClockTickAddsIncrementToTheRightPlayer() {

        chessClockController.click();

        assertEquals(
                chessClockController.getChessClockProperty().get().getWhiteTimeProperty().get(),
                101
        );

        assertEquals(
                chessClockController.getChessClockProperty().get().getBlackTimeProperty().get(),
                100
        );

    }

    @Test
    public void testClockTickSwitchesPlayerTurn() {

        chessClockController.click();

        assertEquals(
                chessClockController.getChessClockProperty().get().getPlayerTurnProperty().get(),
                PlayerColor.BLACK
        );

    }

}
