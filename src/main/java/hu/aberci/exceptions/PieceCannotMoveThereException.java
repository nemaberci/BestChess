package hu.aberci.exceptions;

/**
 * Exception thrown when a piece is attempting to move to a Tile
 * that it cannot move to legally.
 * */
public class PieceCannotMoveThereException extends RuntimeException {

    public PieceCannotMoveThereException() {

        super("PieceCannotMoveThereException");

    }

}
