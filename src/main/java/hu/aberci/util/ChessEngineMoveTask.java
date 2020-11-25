package hu.aberci.util;

import hu.aberci.entities.data.MoveImpl;
import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Move;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.Tile;
import javafx.concurrent.Task;
import lombok.Setter;

import static hu.aberci.util.ChessEngineUtil.parseStockfishAnswer;

public class ChessEngineMoveTask extends Task<Move> {

    @Setter
    BoardState boardState;

    @Override
    protected Move call() {

        return parseAIMove(
                boardState,
                parseStockfishAnswer().split(" ")[1]
        );

    }


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

        return new MoveImpl(
                boardState,
                tileToMoveTo,
                pieceToMove,
                false
        );

    }

}