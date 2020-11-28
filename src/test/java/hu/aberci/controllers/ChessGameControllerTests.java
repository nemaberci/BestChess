package hu.aberci.controllers;

import hu.aberci.entities.data.BoardStateImpl;
import hu.aberci.entities.data.PieceImpl;
import hu.aberci.entities.interfaces.*;
import hu.aberci.exceptions.PieceCannotMoveThereException;
import hu.aberci.exceptions.PieceIsNotPawnException;
import javafx.event.Event;
import javafx.scene.Parent;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class ChessGameControllerTests {

    ChessGameController chessGameController;

    BoardState boardState;

    Parent mockedParent = mock(Parent.class, withSettings().defaultAnswer(invocation -> null));

    List<Event> firedEvents;

    @BeforeEach
    public void init() {

        firedEvents = new ArrayList<>();

        boardState = new BoardStateImpl(
                0, 0, true, false
        );

        boardState.getPiecesProperty().put(
                PlayerColor.WHITE,
                new ArrayList<>()
        );

        boardState.getPiecesProperty().put(
                PlayerColor.BLACK,
                new ArrayList<>()
        );

        chessGameController = new ChessGameController(
                mockedParent,
                boardState
        );

        Piece whiteKing = new PieceImpl(
                boardState.getTilesProperty().get(0).get(7),
                PieceType.KING,
                PlayerColor.WHITE,
                101
        );

        Piece blackKing = new PieceImpl(
                boardState.getTilesProperty().get(7).get(0),
                PieceType.KING,
                PlayerColor.BLACK,
                102
        );

        boardState.getTilesProperty().get(
                0
        ).get(7).getPieceProperty().set(
                whiteKing
        );

        boardState.getTilesProperty().get(7).get(0).getPieceProperty().set(
                blackKing
        );

        boardState.getPiecesProperty().get(PlayerColor.WHITE).add(
                whiteKing
        );

        boardState.getPiecesProperty().get(PlayerColor.BLACK).add(
                blackKing
        );

    }

    @Test
    public void testPieceCanBeMovedToACorrectTile() {

        Tile tile = boardState.getTilesProperty().get(
                3
        ).get(
                3
        );

        Piece piece = new PieceImpl(
                tile,
                PieceType.PAWN,
                PlayerColor.WHITE,
                100
        );

        boardState.getPiecesProperty().get(PlayerColor.WHITE).add(
                piece
        );

        tile.getPieceProperty().set(
                piece
        );

        chessGameController.movePieceToTile(
                piece,
                boardState.getTilesProperty().get(
                        4
                ).get(3)
        );

        verify(mockedParent, times(1)).fireEvent(
                notNull()
        );

        assertTrue(
                chessGameController.boardStateProperty.get().getTilesProperty().get()
                .get(4).get(3).getPieceProperty().get().equals(
                        piece
                )
        );

    }

    @Test
    public void testMovingPieceWhereItCannotMoveThrowsCorrectException() {

        Tile tile = boardState.getTilesProperty().get(
                3
        ).get(
                3
        );

        Piece piece = new PieceImpl(
                tile,
                PieceType.PAWN,
                PlayerColor.WHITE,
                100
        );

        boardState.getPiecesProperty().get(PlayerColor.WHITE).add(
                piece
        );

        tile.getPieceProperty().set(
                piece
        );

        assertThrows(
                PieceCannotMoveThereException.class,
                () -> chessGameController.movePieceToTile(
                        piece,
                        boardState.getTilesProperty().get(
                                2
                        ).get(5)
                )
        );


        verify(mockedParent, times(0)).fireEvent(
                notNull()
        );

        assertTrue(
                chessGameController.boardStateProperty.get().getTilesProperty().get()
                        .get(3).get(3).getPieceProperty().get().equals(
                        piece
                )
        );

    }

    @Test
    public void testCanPromotePawnToQueen() {

        Tile tile = boardState.getTilesProperty().get(
                7
        ).get(
                3
        );

        Piece piece = new PieceImpl(
                tile,
                PieceType.PAWN,
                PlayerColor.WHITE,
                100
        );

        boardState.getPiecesProperty().get(PlayerColor.WHITE).add(
                piece
        );

        tile.getPieceProperty().set(
                piece
        );

        assertDoesNotThrow(
                () -> chessGameController.promotePawnToPieceType(
                        piece,
                        PieceType.QUEEN
                )
        );

        verify(mockedParent, times(0)).fireEvent(
                notNull()
        );

        assertEquals(chessGameController.boardStateProperty.get().getTilesProperty().get()
                .get(7).get(3).getPieceProperty().get().getPieceTypeProperty().get(), PieceType.QUEEN);

    }

    @Test
    public void testPawnPromotionMoveFiresTwoEvents() {

        Tile tile = boardState.getTilesProperty().get(
                6
        ).get(
                3
        );

        Piece piece = new PieceImpl(
                tile,
                PieceType.PAWN,
                PlayerColor.WHITE,
                100
        );

        boardState.getPiecesProperty().get(PlayerColor.WHITE).add(
                piece
        );

        tile.getPieceProperty().set(
                piece
        );

        assertDoesNotThrow(
                () -> chessGameController.movePieceToTile(
                        piece,
                        chessGameController.boardStateProperty.get().getTilesProperty().get(
                                7
                        ).get(3)
                )
        );

        verify(mockedParent, times(2)).fireEvent(
                notNull()
        );

        assertEquals(chessGameController.boardStateProperty.get().getTilesProperty().get()
                .get(7).get(3).getPieceProperty().get().getPieceTypeProperty().get(), PieceType.PAWN);

    }

    @Test
    public void testNotPawnPromotionThrowsCorrectException() {

        Tile tile = boardState.getTilesProperty().get(
                7
        ).get(
                3
        );

        Piece piece = new PieceImpl(
                tile,
                PieceType.KNIGHT,
                PlayerColor.WHITE,
                100
        );

        boardState.getPiecesProperty().get(PlayerColor.WHITE).add(
                piece
        );

        tile.getPieceProperty().set(
                piece
        );

        assertThrows(
                PieceIsNotPawnException.class,
                () -> chessGameController.promotePawnToPieceType(
                        piece,
                        PieceType.QUEEN
                )
        );

        verify(mockedParent, times(0)).fireEvent(
                notNull()
        );

        assertEquals(chessGameController.boardStateProperty.get().getTilesProperty().get()
                .get(7).get(3).getPieceProperty().get().getPieceTypeProperty().get(), PieceType.KNIGHT);

    }

}
