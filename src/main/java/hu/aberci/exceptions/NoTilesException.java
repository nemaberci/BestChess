package hu.aberci.exceptions;

/**
 * Exception thrown when a BoardStateImpl is attempting to generate
 * pieces but was told not to generate tiles.
 * */
public class NoTilesException extends RuntimeException {

    public NoTilesException() {

        super("NoTilesException");

    }

}
