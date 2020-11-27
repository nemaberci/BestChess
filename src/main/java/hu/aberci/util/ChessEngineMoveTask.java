package hu.aberci.util;

import hu.aberci.entities.data.MoveImpl;
import hu.aberci.entities.interfaces.*;
import javafx.concurrent.Task;
import lombok.Setter;

import static hu.aberci.util.ChessEngineUtil.parseStockfishAnswer;

/**
 * Task that returns a Move to be played by the engine.
 * */
public class ChessEngineMoveTask extends Task<Move> {

    /**
     * The BoardState to be sent to the engine.
     * */
    @Setter
    BoardState boardState;

    /**
     * Overridden call method of the Task class. Gets the engine's move from {@link ChessEngineUtil#parseStockfishAnswer}
     * and passes it to {@link ChessEngineMoveTask#parseAIMove}.
     *
     * @return The move to be made by the engine.
     * */
    @Override
    protected Move call() {

        return parseAIMove(
                boardState,
                parseStockfishAnswer().split(" ")[1]
        );

    }

    /**
     * Converts the AI move to a {@link Move}.
     *
     * @param boardState The BoardState that the moves happen on.
     * @param move The AI's move string that was read.
     * @return The move to be made by the engine.
     * */
    public Move parseAIMove(BoardState boardState, String move) {

        Piece pieceToMove;
        Tile tileToMoveTo;

        pieceToMove = boardState.getTilesProperty().get()
                .get(
                        Integer.parseInt(String.valueOf(move.charAt(1))) - 1
                ).get(
                        Integer.parseInt(String.valueOf(move.charAt(0) - 'a'))
                ).getPieceProperty().get();

        tileToMoveTo = boardState.getTilesProperty().get()
                .get(
                        Integer.parseInt(String.valueOf(move.charAt(3))) - 1
                ).get(
                        Integer.parseInt(String.valueOf(move.charAt(2) - 'a'))
                );

        if (move.length() == 4) {

            return new MoveImpl(
                    boardState,
                    tileToMoveTo,
                    pieceToMove,
                    false
            );

        } else {

            // Promotion

            return new MoveImpl(
                    boardState,
                    tileToMoveTo,
                    pieceToMove,
                    false,
                    PieceType.getPieceTypeByFenChar(move.charAt(4))
            );

        }

    }

}