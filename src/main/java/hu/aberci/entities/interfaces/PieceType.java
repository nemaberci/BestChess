package hu.aberci.entities.interfaces;

import hu.aberci.util.PossibleMovesImpls;
import hu.aberci.util.interfaces.PossibleMoves;

import java.io.Serializable;

/**
 * Enum used for describing a piece's type.
 * */
public enum PieceType implements Serializable {

    /**
     * Describes a pawn.
     * */
    PAWN(PossibleMovesImpls.possiblePawnMoves, 'p', "pawn"),
    /**
     * Describes a rook.
     * */
    ROOK(PossibleMovesImpls.possibleRookMoves, 'r', "rook"),
    /**
     * Describes a bishop.
     * */
    BISHOP(PossibleMovesImpls.possibleBishopMoves, 'b', "bishop"),
    /**
     * Describes a knight.
     * */
    KNIGHT(PossibleMovesImpls.possibleKnightMoves, 'n', "knight"),
    /**
     * Describes a queen.
     * */
    QUEEN(PossibleMovesImpls.possibleQueenMoves, 'q', "queen"),
    /**
     * Describes a king.
     * */
    KING(PossibleMovesImpls.possibleKingMoves, 'k', "king");

    /**
     * functional interface returning the possible tiles that can be reached by a given piece
     * on a given board state.
     * */
    public final PossibleMoves possibleMoves;

    /**
     * Character used in a FENCode's board describing portion.
     * */
    public final char FENchar;

    /**
     * String used in finding a piece's image.
     * */
    public final String name;

    /**
     * Finds a piece type by a given character appearing in a FENCode's board
     * describing portion.
     *
     * @param c The character describing the piece type.
     * @return The corresponding piece type.
     * */
    public static PieceType getPieceTypeByFenChar(char c) {

        for (PieceType pieceType: PieceType.values()) {

            if (pieceType.FENchar == c) {

                return pieceType;

            }

        }

        return null;

    }

    /**
     * Only constructor.
     * */
    PieceType(PossibleMoves _possibleMoves, char feNchar, String name) {
        possibleMoves = _possibleMoves;
        FENchar = feNchar;
        this.name = name;
    }

}
