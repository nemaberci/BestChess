package hu.aberci.util;

import hu.aberci.controllers.ChessGameController;
import hu.aberci.entities.data.BoardStateImpl;
import hu.aberci.entities.data.MoveImpl;
import hu.aberci.entities.data.PieceImpl;
import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.PieceType;
import hu.aberci.entities.interfaces.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class UtilTests {

    BoardState boardState;

    Piece piece;

    @BeforeEach
    public void init() {

        boardState = new BoardStateImpl(
                10, 10, true, false
        );

        boardState.getPiecesProperty().put(
                PlayerColor.WHITE,
                new ArrayList<>()
        );

        boardState.getPiecesProperty().put(
                PlayerColor.BLACK,
                new ArrayList<>()
        );

    }

    @Test
    public void testGetAllLegalMovesGivesTheRightAmountForQueen() {

        Piece whiteKing = new PieceImpl(
                boardState.getTilesProperty().get(0).get(0),
                PieceType.KING,
                PlayerColor.WHITE,
                101
        );

        Piece blackKing = new PieceImpl(
                boardState.getTilesProperty().get(7).get(7),
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

        piece = new PieceImpl(
                boardState.getTilesProperty().get().get(
                        3
                ).get(4),
                PieceType.QUEEN,
                PlayerColor.WHITE
        );

        boardState.getTilesProperty().get(3).get(4).getPieceProperty().set(
                piece
        );

        boardState.getPiecesProperty().get(
                PlayerColor.WHITE
        ).add(
                piece
        );

        // The queen should be able to control 27 squares
        // Minus the king's square on 0, 0 making it 26.

        assertEquals(
                26,
                Util.getAllLegalMoves(
                        boardState,
                        piece
                ).size()
        );

    }

    @Test
    public void testGetAllMovesIncludesCastling() {

        Piece whiteKing = new PieceImpl(
                boardState.getTilesProperty().get(0).get(4),
                PieceType.KING,
                PlayerColor.WHITE,
                101
        );

        Piece blackKing = new PieceImpl(
                boardState.getTilesProperty().get(7).get(4),
                PieceType.KING,
                PlayerColor.BLACK,
                102
        );

        Piece whiteRook = new PieceImpl(
                boardState.getTilesProperty().get(0).get(7),
                PieceType.ROOK,
                PlayerColor.WHITE,
                103
        );

        boardState.getTilesProperty().get(
                0
        ).get(4).getPieceProperty().set(
                whiteKing
        );

        boardState.getTilesProperty().get(7).get(4).getPieceProperty().set(
                blackKing
        );

        boardState.getTilesProperty().get(0).get(7).getPieceProperty().set(
                whiteRook
        );

        boardState.getPiecesProperty().get(PlayerColor.WHITE).add(
                whiteKing
        );

        boardState.getPiecesProperty().get(PlayerColor.BLACK).add(
                blackKing
        );

        boardState.getPiecesProperty().get(PlayerColor.WHITE).add(
                whiteRook
        );

        assertTrue(
                Util.getAllLegalMoves(
                        boardState,
                        whiteKing
                ).contains(
                        boardState.getTilesProperty().get().get(
                                0
                        ).get(
                                6
                        )
                )
        );

    }

    @Test
    public void testGetAllMovesIncludesEnPassant() {

        Piece whiteKing = new PieceImpl(
                boardState.getTilesProperty().get(0).get(4),
                PieceType.KING,
                PlayerColor.WHITE,
                101
        );

        Piece blackKing = new PieceImpl(
                boardState.getTilesProperty().get(7).get(4),
                PieceType.KING,
                PlayerColor.BLACK,
                102
        );

        Piece whitePawn = new PieceImpl(
                boardState.getTilesProperty().get(4).get(7),
                PieceType.PAWN,
                PlayerColor.WHITE,
                103
        );

        Piece blackPawn = new PieceImpl(
                boardState.getTilesProperty().get(4).get(6),
                PieceType.PAWN,
                PlayerColor.BLACK,
                104
        );

        boardState.getTilesProperty().get(
                0
        ).get(4).getPieceProperty().set(
                whiteKing
        );

        boardState.getTilesProperty().get(7).get(4).getPieceProperty().set(
                blackKing
        );

        boardState.getTilesProperty().get(4).get(7).getPieceProperty().set(
                whitePawn
        );

        boardState.getTilesProperty().get(4).get(6).getPieceProperty().set(
                whitePawn
        );

        boardState.getPiecesProperty().get(PlayerColor.BLACK).add(
                blackKing
        );

        boardState.getPiecesProperty().get(PlayerColor.BLACK).add(
                blackPawn
        );

        boardState.getPiecesProperty().get(PlayerColor.WHITE).add(
                whiteKing
        );

        boardState.getPiecesProperty().get(PlayerColor.WHITE).add(
                whitePawn
        );

        boardState.getMovesProperty().add(
                new MoveImpl(
                        boardState,
                        boardState.getTilesProperty().get(6).get(6),
                        boardState.getTilesProperty().get(4).get(6),
                        blackPawn,
                        false
                )
        );

        assertTrue(
                Util.getAllLegalMoves(
                        boardState,
                        whitePawn
                ).contains(
                        boardState.getTilesProperty().get(5).get(6)
                )
        );

    }

}
