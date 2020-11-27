package hu.aberci.util.interfaces;

import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.Tile;

import java.io.Serializable;
import java.util.Set;

/**
 * Functional interface describing a function that returns a certain piece's legal moves
 * not considering the board's previous moves.
 * */
@FunctionalInterface
public interface MoveGenerator extends Serializable {

    /**
     * The function that returns the tiles that this piece can reach. This does not account
     * for previously made moves and as such does not include en passant and castling moves.
     * This list of tiles also does not account for other pieces on the board. To get the full
     * list of legal moves, use: {@link hu.aberci.util.Util#getAllLegalMoves}.
     *
     * @param boardState The chess board that the piece is occupying.
     * @param piece The piece whose possible moves are to be returned
     * @return A set of Tiles reachable from the piece.
     * */
    Set<Tile> moveGenerator(BoardState boardState, Piece piece);

}