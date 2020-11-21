package hu.aberci.entities.pieces;

import hu.aberci.entities.data.BoardStateImpl;
import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.PlayerColor;
import hu.aberci.entities.interfaces.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class PawnTests {

    BoardState boardState;

    @BeforeEach
    void createBoardState() {

        boardState = new BoardStateImpl(false, 0, 0);

    }

    @Test
    public void canDoubleMoveAfterCreation() {

        List<Tile> pawnCanMoveTo = boardState.getTiles().get(1).get(1).getCurrentPiece().canMoveToTiles();

        assertTrue(pawnCanMoveTo.size() == 2);

        assertTrue(pawnCanMoveTo.contains(boardState.getTiles().get(3).get(1)));

    }

    @Test
    public void cannotJumpOverPiece() {

        boardState.getTiles().get(2).get(0).setCurrentPiece(new Pawn(boardState.getTiles().get(2).get(0), PlayerColor.WHITE));
        boardState.getTiles().get(2).get(1).setCurrentPiece(new Pawn(boardState.getTiles().get(2).get(1), PlayerColor.WHITE));

        List<Tile> pawnOneCanMoveTo = boardState.getTiles().get(1).get(0).getCurrentPiece().canMoveToTiles();
        List<Tile> pawnTwoCanMoveTo = boardState.getTiles().get(1).get(1).getCurrentPiece().canMoveToTiles();

        assertTrue(pawnOneCanMoveTo.size() == 0);
        assertTrue(pawnTwoCanMoveTo.size() == 0);

    }

    @Test
    public void pawnCanTakePiece() {

        boardState.getTiles().get(2).get(1).setCurrentPiece(new Pawn(boardState.getTiles().get(2).get(1), PlayerColor.BLACK));

        List<Tile> leftPawn = boardState.getTiles().get(1).get(0).getCurrentPiece().canMoveToTiles();
        List<Tile> rightPawn = boardState.getTiles().get(1).get(2).getCurrentPiece().canMoveToTiles();

        assertTrue(leftPawn.contains(boardState.getTiles().get(2).get(1)));
        assertTrue(rightPawn.contains(boardState.getTiles().get(2).get(1)));

    }

    @Test
    public void pawnCannotTakeOwnPawns() {

        boardState.getTiles().get(2).get(6).setCurrentPiece(new Pawn(boardState.getTiles().get(2).get(6), PlayerColor.WHITE));

        List<Tile> leftPawn = boardState.getTiles().get(1).get(5).getCurrentPiece().canMoveToTiles();
        List<Tile> rightPawn = boardState.getTiles().get(1).get(7).getCurrentPiece().canMoveToTiles();

        assertFalse(leftPawn.contains(boardState.getTiles().get(2).get(6)));
        assertFalse(rightPawn.contains(boardState.getTiles().get(2).get(6)));

    }

}
