package hu.aberci.exceptions;

/**
 * Exception thrown when a pawn is trying to promote but it has
 * nothing to promote to.
 * */
public class NoPromotionSetException extends RuntimeException {

    public NoPromotionSetException() {

        super("NoPromotionSetException");

    }

}
