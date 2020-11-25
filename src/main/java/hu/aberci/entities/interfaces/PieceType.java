package hu.aberci.entities.interfaces;

import hu.aberci.util.PossibleMovesImpls;
import hu.aberci.util.interfaces.PossibleMoves;

import java.io.Serializable;

public enum PieceType implements Serializable {
    PAWN(PossibleMovesImpls.possiblePawnMoves, 'p', "pawn"),
    ROOK(PossibleMovesImpls.possibleRookMoves, 'r', "rook"),
    BISHOP(PossibleMovesImpls.possibleBishopMoves, 'b', "bishop"),
    KNIGHT(PossibleMovesImpls.possibleKnightMoves, 'n', "knight"),
    QUEEN(PossibleMovesImpls.possibleQueenMoves, 'q', "queen"),
    KING(PossibleMovesImpls.possibleKingMoves, 'k', "king");

    public final PossibleMoves possibleMoves;

    public final char FENchar;

    public final String name;

    PieceType(PossibleMoves _possibleMoves, char feNchar, String name) {
        possibleMoves = _possibleMoves;
        FENchar = feNchar;
        this.name = name;
    }

}
