package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A Serializable version of {@link BoardStateImpl}.
 * */
@Data
@Accessors(chain = true)
public class SerializableBoardStateImpl implements SerializableBoardState {

    ArrayList<ArrayList<SerializableTile>> tiles;
    ArrayList<SerializableMove> moves;
    ArrayList<SerializablePiece> takenPieces;
    HashMap<PlayerColor, ArrayList<SerializablePiece>> pieces;
    SerializableChessClock chessClock;
    PlayerColor playerColor;
    HashMap<String, Integer> positionCounter;
    Boolean timeControlled;

    public Boolean isTimeControlled() {
        return timeControlled;
    }

    private SerializablePiece findPieceWithID(Integer ID) {

        for (PlayerColor playerColor: Arrays.asList(PlayerColor.WHITE, PlayerColor.BLACK)) {

            for (SerializablePiece piece: pieces.get(playerColor)) {

                if (piece.getID().equals(ID)) {

                    return piece;

                }

            }

        }

        for (SerializablePiece piece: takenPieces) {

            if (piece.getID().equals(ID)) {

                return piece;

            }

        }

        return null;

    }

    public SerializableBoardStateImpl(BoardState boardState) {

        tiles = new ArrayList<>();
        moves = new ArrayList<>();
        takenPieces = new ArrayList<>();
        pieces = new HashMap<>();
        positionCounter = new HashMap<>();
        timeControlled = boardState.getIsTimeControlledProperty().get();

        for (String key: boardState.getPositionCounterProperty().keySet()) {

            positionCounter.put(
                    key,
                    boardState.getPositionCounterProperty().get(key)
            );

        }

        playerColor = boardState.getPlayerTurnProperty().get();
        chessClock = new SerializableChessClockImpl()
                .setBlackTime(
                        boardState.getChessClockProperty().get().getBlackTimeProperty().get()
                ).setWhiteTime(
                        boardState.getChessClockProperty().get().getWhiteTimeProperty().get()
                ).setIncrement(
                        boardState.getChessClockProperty().get().getIncrement()
                ).setPlayerColor(
                        boardState.getChessClockProperty().get().getPlayerTurnProperty().get()
                );

        for (int x = 0; x < 8; x++) {

            tiles.add(
                    new ArrayList<>()
            );

            for (int y = 0; y < 8; y++) {

                tiles.get(x).add(
                        new SerializableTileImpl()
                                .setX(x)
                                .setY(y)
                );

            }

        }

        for (Piece takenPiece: boardState.getTakenPiecesProperty().get()) {

            takenPieces.add(
                    new SerializablePieceImpl()
                            .setPieceType(
                                    takenPiece.getPieceTypeProperty().get()
                            ).setID(
                                    takenPiece.getIDProperty().get()
                            ).setPlayerColor(
                                    takenPiece.getPlayerColorProperty().get()
                    )
            );

        }

        for (PlayerColor playerColor: Arrays.asList(PlayerColor.WHITE, PlayerColor.BLACK)) {

            pieces.put(playerColor, new ArrayList<>());

            for (Piece piece: boardState.getPiecesProperty().get(playerColor)) {

                SerializablePieceImpl serializablePiece = new SerializablePieceImpl()
                        .setPlayerColor(
                                piece.getPlayerColorProperty().get()
                        ).setID(
                                piece.getIDProperty().get()
                        ).setPieceType(
                                piece.getPieceTypeProperty().get()
                        ).setTile(
                                tiles.get(
                                        piece.getTileProperty().get().getXProperty().get()
                                ).get(
                                        piece.getTileProperty().get().getYProperty().get()
                                )
                        );

                tiles.get(
                        piece.getTileProperty().get().getXProperty().get()
                ).get(
                        piece.getTileProperty().get().getYProperty().get()
                ).setPiece(
                        serializablePiece
                );

                pieces.get(playerColor).add(
                        serializablePiece
                );

            }

        }

        for (Move move: boardState.getMovesProperty().get()) {

            SerializableMove serializableMove = new SerializableMoveImpl()
                    .setBoardState(this)
                    .setPiece(findPieceWithID(
                            move.getPiece().getIDProperty().get()
                    )).setSourceTile(
                            tiles.get(
                                    move.getSourceTile().getXProperty().get()
                            ).get(
                                    move.getSourceTile().getYProperty().get()
                            )
                    ).setTargetTile(
                            tiles.get(
                                    move.getTargetTile().getXProperty().get()
                            ).get(
                                    move.getTargetTile().getYProperty().get()
                            )
                    );

            moves.add(serializableMove);

        }

    }

}
