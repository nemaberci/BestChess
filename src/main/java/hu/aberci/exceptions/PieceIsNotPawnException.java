package hu.aberci.exceptions;

/**
 * Exception thrown when a piece is attempted to be promoted but
 * it is not a pawn (only pawns can be promoted).
 * */
public class PieceIsNotPawnException extends RuntimeException {

    public PieceIsNotPawnException() {

        super("PieceIsNotPawnException");

    }

}
