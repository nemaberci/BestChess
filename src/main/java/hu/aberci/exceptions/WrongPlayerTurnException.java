package hu.aberci.exceptions;

/**
 * Exception thrown when a player is attempting to move a piece
 * but it is the opponent's turn.
 * */
public class WrongPlayerTurnException extends RuntimeException {

    public WrongPlayerTurnException() {

        super("WrongPlayerTurnException");

    }

}
