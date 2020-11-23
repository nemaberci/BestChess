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

        boardState = new BoardStateImpl(0, 0, true, true);

    }

    @Test
    public void canDoubleMoveAfterCreation() {


    }

    @Test
    public void cannotJumpOverPiece() {


    }

    @Test
    public void pawnCanTakePiece() {


    }

    @Test
    public void pawnCannotTakeOwnPawns() {


    }

}
