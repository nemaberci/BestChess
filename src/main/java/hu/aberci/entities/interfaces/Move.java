package hu.aberci.entities.interfaces;

/**
 * Interface describing a move. Moves describe a Piece moving to a Tile. A move can have special
 * properties, such as if it is a piece capture or a pawn promotion.
 * */
public interface Move {

    /**
     * Returns the piece that was moved.
     *
     * @return the piece that was moved.
     * */
    Piece getPiece();

    /**
     * Returns the BoardState that this move happened on.
     *
     * @return the BoardState that this move happened on.
     * */
    BoardState getBoardState();

    /**
     * Returns the Tile that the Piece moved to.
     *
     * @return the Tile that the Piece moved to.
     * */
    Tile getTargetTile();

    /**
     * Returns the Tile that the Piece moved from.
     *
     * @return the Tile that the Piece moved from.
     * */
    Tile getSourceTile();

    /**
     * Sets the Tile that the piece moved to. This returns the Move itself, meaning this setter can be chained.
     *
     * @param tile The tile that the piece moved to.
     * @return this object, meaning this setter can be chained.
     * */
    Move setTargetTile(Tile tile);

    /**
     * Sets the Tile that the piece moved from. This returns the Move itself, meaning this setter can be chained.
     *
     * @param tile The tile that the piece moved from.
     * @return this object, meaning this setter can be chained.
     * */
    Move setSourceTile(Tile tile);

    /**
     * Sets the board that this move affects. This returns the Move itself, meaning this setter can be chained.
     *
     * @param boardState The board that this move affects.
     * @return this object, meaning this setter can be chained.
     * */
    Move setBoardState(BoardState boardState);

    /**
     * Sets the piece that was moved. This returns the Move itself, meaning this setter can be chained.
     *
     * @param piece The piece that was moved.
     * @return this object, meaning this setter can be chained.
     * */
    Move setPiece(Piece piece);

    /**
     * Returns whether this move caused a piece to be captured.
     *
     * @return {@code true} if a piece was taken and {@code false} otherwise
     * */
    boolean isCapture();

    /**
     * Sets the piece type that is being promoted to. This is used for promotion logic.
     *
     * @param pieceType The new piece type of the pawn being promoted.
     * @return this object, meaning this setter can be chained.
     * */
    Move setPromotingTo(PieceType pieceType);

    /**
     * Gets the piece type that this piece is being promoted to. This should return null if the move
     * does not cause a promotion and one of [KNIGHT, BISHOP, ROOK, QUEEN] if it is a promotion.
     *
     * @return The piece type that the pawn will become.
     * */
    PieceType getPromotingTo();

}
