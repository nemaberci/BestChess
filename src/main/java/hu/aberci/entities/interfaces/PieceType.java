package hu.aberci.entities.interfaces;

import hu.aberci.entities.piecetypes.*;
import hu.aberci.util.interfaces.MoveGenerator;

import java.io.Serializable;

/**
 * Enum used for describing a piece's type.
 * */
public enum PieceType implements Serializable {

    /**
     * Describes a pawn.
     * */
    PAWN(new Pawn(), 'p', "pawn"),
    /**
     * Describes a rook.
     * */
    ROOK(new Rook(), 'r', "rook"),
    /**
     * Describes a bishop.
     * */
    BISHOP(new Bishop(), 'b', "bishop"),
    /**
     * Describes a knight.
     * */
    KNIGHT(new Knight(), 'n', "knight"),
    /**
     * Describes a queen.
     * */
    QUEEN(new Queen(), 'q', "queen"),
    /**
     * Describes a king.
     * */
    KING(new King(), 'k', "king");

    /**
     * functional interface returning the possible tiles that can be reached by a given piece
     * on a given board state.
     * */
    public final MoveGenerator moveGenerator;

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
    PieceType(MoveGenerator _moveGenerator, char feNchar, String name) {
        moveGenerator = _moveGenerator;
        FENchar = feNchar;
        this.name = name;
    }

}
