package hu.aberci.util.interfaces;

import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.Tile;

import java.util.Set;

@FunctionalInterface
public
interface PossibleMoves {

    Set<Tile> possibleMoves(BoardState boardState, Piece piece);

}