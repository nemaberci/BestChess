package hu.aberci.entities.interfaces;

/**
 * Forsyth–Edwards Notation used for describing a chess board position. This can be
 * used to communicate with other chess applications.
 *
 * For more info read: https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation .
 * */
public interface FENCode {

    /**
     * Returns the board. For the exact structure of this string visit https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation#Definition.
     *
     * @return The string representing the current board.
     * */
    String getBoard();

    /**
     * Returns which player's turn it is currently.
     *
     * @return "w" if white moves next and "b" if black moves next.
     * */
    String getTurn();

    /**
     * Returns which side can castle and where. For the exact structure of this string visit https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation#Definition.
     *
     * @return Characters representing possible castling moves, or "-" if neither side can castle.
     * */
    String getCastle();

    /**
     * Returns whether last move can be taken en passant. For the exact structure of this string visit https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation#Definition.
     *
     * @return The tile behind the pawn that moved twice or "-" if no en passant is possible this move.
     * */
    String getEnPassant();

    /**
     * Returns how many half turns have been played since the last capture or pawn move. For the exact structure of this string visit https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation#Definition.
     *
     * @return A number representing the half turns since the last capture or pawn move.
     * */
    String getHalfTurns();

    /**
     * Returns the current turn number. For the exact structure of this string visit https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation#Definition.
     *
     * @return The string representing the current turn number.
     * */
    String getTurnNumber();

    /**
     * Chains together the different parts of an FEN code and separates them by spaces. For the exact structure of this string visit https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation#Definition.
     *
     * @return The full FEN code.
     * */
    String getFENCode();

}
