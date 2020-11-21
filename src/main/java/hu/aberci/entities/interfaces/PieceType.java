package hu.aberci.entities.interfaces;

import hu.aberci.util.PossibleMovesImpls;
import hu.aberci.util.interfaces.PossibleMoves;

import java.io.Serializable;

public enum PieceType implements Serializable {
    PAWN(PossibleMovesImpls.possiblePawnMoves),
    ROOK(PossibleMovesImpls.possibleRookMoves),
    BISHOP(PossibleMovesImpls.possibleBishopMoves),
    KNIGHT(PossibleMovesImpls.possibleKnightMoves),
    QUEEN(PossibleMovesImpls.possibleQueenMoves),
    KING(PossibleMovesImpls.possibleKingMoves);

    public final PossibleMoves possibleMoves;

    PieceType(PossibleMoves _possibleMoves) {
        possibleMoves = _possibleMoves;
    }

}
