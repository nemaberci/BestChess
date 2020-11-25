package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.*;
import hu.aberci.exceptions.NoTilesException;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import lombok.Getter;

import java.lang.reflect.Array;
import java.util.*;

@Getter
public class BoardStateImpl implements BoardState {

    /*
    * UTIL FUNCTIONS TO MAKE CODE SHORTER
    * */

    private Piece createRook(Tile tile, PlayerColor playerColor) {
        return new PieceImpl(tile, PieceType.ROOK, playerColor);
    }
    private Piece createBishop(Tile tile, PlayerColor playerColor) {
        return new PieceImpl(tile, PieceType.BISHOP, playerColor);
    }
    private Piece createQueen(Tile tile, PlayerColor playerColor) {
        return new PieceImpl(tile, PieceType.QUEEN, playerColor);
    }
    private Piece createKing(Tile tile, PlayerColor playerColor) {
        return new PieceImpl(tile, PieceType.KING, playerColor);
    }
    private Piece createPawn(Tile tile, PlayerColor playerColor) {
        return new PieceImpl(tile, PieceType.PAWN, playerColor);
    }
    private Piece createKnight(Tile tile, PlayerColor playerColor) {
        return new PieceImpl(tile, PieceType.KNIGHT, playerColor);
    }

    private void createOfficers(List<List<Tile>> tiles, int x, int y, List<Piece> pieces, PlayerColor playerColor) {
        if (y % 7 == 0) { // 0 or 7
            Piece piece = createRook(tiles.get(x).get(y), playerColor);
            pieces.add(piece);
            tiles.get(x).get(y).getPieceProperty().set(piece);
        }
        if (y % 5 == 1) { // 1 or 6
            Piece piece = createKnight(tiles.get(x).get(y), playerColor);
            pieces.add(piece);
            tiles.get(x).get(y).getPieceProperty().set(piece);
        }
        if (y == 2 || y == 5) {
            Piece piece = createBishop(tiles.get(x).get(y), playerColor);
            pieces.add(piece);
            tiles.get(x).get(y).getPieceProperty().set(piece);
        }
        if (y == 3) {
            Piece piece = createQueen(tiles.get(x).get(y), playerColor);
            pieces.add(piece);
            tiles.get(x).get(y).getPieceProperty().set(piece);
        }
        if (y == 4) {
            Piece piece = createKing(tiles.get(x).get(y), playerColor);
            pieces.add(piece);
            tiles.get(x).get(y).getPieceProperty().set(piece);
        }
    }

    private Piece findPieceWithID(Integer ID) {

        for (PlayerColor playerColor: Arrays.asList(PlayerColor.WHITE, PlayerColor.BLACK)) {

            for (Piece piece: piecesProperty.get(playerColor)) {

                if (ID.equals(piece.getIDProperty().get())) {

                    return piece;

                }

            }

        }

        for (Piece piece: takenPiecesProperty.get()) {

            if (ID.equals(piece.getIDProperty().get())) {

                return piece;

            }

        }

        return null;

    }

    private List<List<Tile>> generateTiles() {

        List<List<Tile>> tiles = new ArrayList<>();

        for (int x = 0; x < 8; x++) {

            tiles.add(new ArrayList<>());

            for (int y = 0; y < 8; y++) {

                tiles.get(x).add(new TileImpl(null, x, y));

            }

        }

        return tiles;

    }

    @Getter
    ObjectProperty<PlayerColor> playerTurnProperty;

    @Getter
    ObjectProperty<ChessClock> chessClockProperty;

    @Getter
    ListProperty<List<Tile>> tilesProperty;

    @Getter
    MapProperty<PlayerColor, List<Piece>> piecesProperty;

    @Getter
    ListProperty<Move> movesProperty;

    @Getter
    ListProperty<Piece> takenPiecesProperty;

    @Getter
    MapProperty<String, Integer> positionCounterProperty;

    @Getter
    BooleanProperty isTimeControlledProperty;

    public BoardStateImpl(int startingTime, int increment, boolean generateTiles, boolean generatePieces) throws NoTilesException {

        isTimeControlledProperty = new SimpleBooleanProperty(false);

        positionCounterProperty = new SimpleMapProperty<>(
                FXCollections.observableMap(
                        new HashMap<>()
                )
        );

        chessClockProperty = new SimpleObjectProperty<>(
                new ChessClockImpl(startingTime, increment)
        );

        playerTurnProperty = new SimpleObjectProperty<>(PlayerColor.WHITE);

        tilesProperty = new SimpleListProperty<>(
                FXCollections.observableArrayList(new ArrayList<>())
        );

        piecesProperty = new SimpleMapProperty<>(
                FXCollections.observableMap(
                        new HashMap<>()
                )
        );

        movesProperty = new SimpleListProperty<>(
                FXCollections.observableList(
                        new ArrayList<>()
                )
        );

        takenPiecesProperty = new SimpleListProperty<>(
                FXCollections.observableList(
                        new ArrayList<>()
                )
        );

        if (generatePieces && !generateTiles) {
            throw new NoTilesException();
        }

        if (generateTiles) {

            List<List<Tile>> tiles = generateTiles();

            tilesProperty.set(
                    FXCollections.observableList(tiles)
            );

            if (generatePieces) {

                Map<PlayerColor, List<Piece>> pieces = new HashMap<>();

                List<Piece> whitePieces = new ArrayList<>();

                List<Piece> blackPieces = new ArrayList<>();

                for (int x = 0; x < 8; x++) {

                    for (int y = 0; y < 8; y++) {

                        switch (x) {
                            case 0:
                                createOfficers(tiles, x, y, whitePieces, PlayerColor.WHITE);
                                break;
                            case 1:
                                Piece piece1 = createPawn(tiles.get(x).get(y), PlayerColor.WHITE);
                                whitePieces.add(piece1);
                                tiles.get(x).get(y).getPieceProperty().set(piece1);
                                break;
                            case 6:
                                Piece piece2 = createPawn(tiles.get(x).get(y), PlayerColor.BLACK);
                                blackPieces.add(piece2);
                                tiles.get(x).get(y).getPieceProperty().set(piece2);
                                break;
                            case 7:
                                createOfficers(tiles, x, y, blackPieces, PlayerColor.BLACK);
                                break;
                            default:
                                // Do not create pieces for other tiles
                                break;
                        }

                    }

                }

                pieces.put(PlayerColor.WHITE, whitePieces);
                pieces.put(PlayerColor.BLACK, blackPieces);

                piecesProperty.set(
                        FXCollections.observableMap(pieces)
                );

            }

        }

    }

    public BoardStateImpl(BoardState boardState) {

        isTimeControlledProperty = new SimpleBooleanProperty(
                boardState.getIsTimeControlledProperty().get()
        );

        positionCounterProperty = new SimpleMapProperty<>(
                FXCollections.observableMap(
                        new HashMap<>()
                )
        );
        for (String key: boardState.getPositionCounterProperty().keySet()) {

            positionCounterProperty.put(
                    key,
                    boardState.getPositionCounterProperty().get(key)
            );

        }

        playerTurnProperty = new SimpleObjectProperty<>();
        playerTurnProperty.set(
                boardState.getPlayerTurnProperty().get()
        );

        tilesProperty = new SimpleListProperty<>();
        tilesProperty.set(FXCollections.observableList(generateTiles()));

        takenPiecesProperty = new SimpleListProperty<>(
                FXCollections.observableList(
                        new ArrayList<>()
                )
        );

        piecesProperty = new SimpleMapProperty<>();
        Map<PlayerColor, List<Piece>> map = new HashMap<>();
        List<Piece> whitePieces = new ArrayList<>();
        for (Piece piece: boardState.getPiecesProperty().get().get(PlayerColor.WHITE)) {
            Piece newPiece = new PieceImpl(piece, this);
            whitePieces.add(newPiece);
            tilesProperty.get(
                    piece.getTileProperty().get().getXProperty().get()
            ).get(
                    piece.getTileProperty().get().getYProperty().get()
            ).getPieceProperty().set(
                    newPiece
            );
        }
        map.put(PlayerColor.WHITE, whitePieces);
        List<Piece> blackPieces = new ArrayList<>();
        for (Piece piece: boardState.getPiecesProperty().get().get(PlayerColor.BLACK)) {
            Piece newPiece = new PieceImpl(piece, this);
            blackPieces.add(newPiece);
            tilesProperty.get(
                    piece.getTileProperty().get().getXProperty().get()
            ).get(
                    piece.getTileProperty().get().getYProperty().get()
            ).getPieceProperty().set(
                    newPiece
            );
        }
        map.put(PlayerColor.BLACK, blackPieces);
        piecesProperty.set(FXCollections.observableMap(map));

        chessClockProperty = new SimpleObjectProperty<>();
        chessClockProperty.set(
                new ChessClockImpl(
                        boardState.getChessClockProperty().get()
                )
        );

        /*
        * We do not replicate moves for the copy constructor
        * We only replicate taken pieces when we replicate moves aswell
        *  */

        movesProperty = new SimpleListProperty<>();

    }

    public BoardStateImpl(BoardState boardState, boolean copyMoves) {

        this(boardState);

        if (copyMoves) {

            movesProperty = new SimpleListProperty<>(
                    FXCollections.observableList(new ArrayList<>())
            );

            for (Piece takenPiece: boardState.getTakenPiecesProperty().get()) {

                takenPiecesProperty.add(
                        new PieceImpl(null,
                                takenPiece.getPieceTypeProperty().get(),
                                takenPiece.getPlayerColorProperty().get(),
                                takenPiece.getIDProperty().get())
                );

            }

            for (Piece takenPiece: boardState.getTakenPiecesProperty().get()) {

                PlayerColor pieceColor = takenPiece.getPlayerColorProperty().get();

                int index = 0;

                while (index < piecesProperty.get(pieceColor).size()) {

                    Piece currentPiece = piecesProperty.get(pieceColor).get(index);

                    if (currentPiece.equals(takenPiece)) {

                        takenPiecesProperty.add(
                                currentPiece
                        );

                        piecesProperty.get(pieceColor).remove(
                                index
                        );

                        continue;

                    }

                    index++;

                }

            }

            for (Move move : boardState.getMovesProperty().get()) {

                Move newMove = new MoveImpl().setCapture(
                        move.isCapture()
                );

                newMove.setBoardState(boardState);

                for (List<Tile> tileList : boardState.getTilesProperty().get()) {

                    for (Tile tile : tileList) {

                        if (move.getTargetTile().getXProperty().get() == tile.getXProperty().get() &&
                                move.getTargetTile().getYProperty().get() == tile.getYProperty().get()) {

                            newMove.setTargetTile(tile);

                        }

                        if (move.getSourceTile().getXProperty().get() == tile.getXProperty().get() &&
                                move.getSourceTile().getYProperty().get() == tile.getYProperty().get()) {

                            newMove.setSourceTile(tile);

                        }

                    }

                }

                // We find the piece that was on the same tile

                List<Piece> allPieces = new ArrayList<>(piecesProperty.get(PlayerColor.WHITE));
                allPieces.addAll(piecesProperty.get(PlayerColor.BLACK));
                allPieces.addAll(takenPiecesProperty.get());

                for (Piece myPiece : allPieces) {

                    if (myPiece.equals(move.getPiece())) {

                        newMove.setPiece(myPiece);

                    }

                }

                movesProperty.add(
                        newMove
                );

            }

        }

    }

    public BoardStateImpl(SerializableBoardState serializableBoardState) {

        isTimeControlledProperty = new SimpleBooleanProperty(
                serializableBoardState.isTimeControlled()
        );

        positionCounterProperty = new SimpleMapProperty<>(
                FXCollections.observableMap(
                        new HashMap<>()
                )
        );

        for (String key: serializableBoardState.getPositionCounter().keySet()) {

            positionCounterProperty.put(
                    key,
                    serializableBoardState.getPositionCounter().get(key)
            );

        }

        playerTurnProperty = new SimpleObjectProperty<>(
                serializableBoardState.getPlayerColor()
        );

        tilesProperty = new SimpleListProperty<>(
                FXCollections.observableList(new ArrayList<>())
        );

        for (int x = 0; x < 8; x++) {

            tilesProperty.add(
                    new ArrayList<>()
            );

            for (int y = 0; y < 8; y++) {

                tilesProperty.get(x).add(
                        new TileImpl(null, x, y)
                );

            }

        }

        piecesProperty = new SimpleMapProperty<>(
                FXCollections.observableMap(new HashMap<>())
        );

        for (PlayerColor playerColor: Arrays.asList(PlayerColor.WHITE, PlayerColor.BLACK)) {

            piecesProperty.put(
                playerColor,
                new ArrayList<>()
            );

            for (SerializablePiece serializablePiece : serializableBoardState.getPieces().get(playerColor)) {

                Piece piece = new PieceImpl(
                        tilesProperty
                                .get(serializablePiece.getTile().getX())
                                .get(serializablePiece.getTile().getY()),
                        serializablePiece.getPieceType(),
                        serializablePiece.getPlayerColor(),
                        serializablePiece.getID()
                );

                tilesProperty.get(
                        serializablePiece.getTile().getX()
                ).get(
                        serializablePiece.getTile().getY()
                ).getPieceProperty().set(
                        piece
                );

                piecesProperty.get(playerColor).add(
                        piece
                );

            }
        }

        takenPiecesProperty = new SimpleListProperty<>(
                FXCollections.observableList(new ArrayList<>())
        );

        for (SerializablePiece serializablePiece: serializableBoardState.getTakenPieces()) {

            takenPiecesProperty.add(
                    new PieceImpl(null,
                            serializablePiece.getPieceType(),
                            serializablePiece.getPlayerColor(),
                            serializablePiece.getID())
            );

        }

        movesProperty = new SimpleListProperty<>(
                FXCollections.observableList(new ArrayList<>())
        );

        for (SerializableMove serializableMove: serializableBoardState.getMoves()) {

            Move move = new MoveImpl();

            move.setPiece(
                    findPieceWithID(serializableMove.getPiece().getID())
            );

            move.setBoardState(this);

            move.setSourceTile(
                    tilesProperty.get(
                            serializableMove.getSourceTile().getX()
                    ).get(
                            serializableMove.getSourceTile().getY()
                    )
            );

            move.setTargetTile(
                    tilesProperty.get(
                            serializableMove.getTargetTile().getX()
                    ).get(
                            serializableMove.getTargetTile().getY()
                    )
            );

            movesProperty.add(
                    move
            );

        }

        ChessClock chessClock = new ChessClockImpl(
                0,
                serializableBoardState.getChessClock().getIncrement()
        );

        chessClock.getBlackTimeProperty().set(
                serializableBoardState.getChessClock().getBlackTime()
        );

        chessClock.getWhiteTimeProperty().set(
                serializableBoardState.getChessClock().getWhiteTime()
        );

        chessClock.getPlayerTurnProperty().set(
                serializableBoardState.getChessClock().getPlayerColor()
        );

        chessClockProperty = new SimpleObjectProperty<>(chessClock);

    }

    public FENCode getFEN() {

        FENCodeImpl toReturn = new FENCodeImpl();

        int toSkip = 0;

        for (int x = 7; x >= 0; x--) {

            for (int y = 0; y < 8; y++) {

                Tile tile = tilesProperty.get(x).get(y);

                if (tile.getPieceProperty().get() == null) {
                    toSkip++;
                } else {
                    char letter = tile.getPieceProperty().get().getPieceTypeProperty().get().FENchar;
                    if (PlayerColor.WHITE.equals(tile.getPieceProperty().get().getPlayerColorProperty().get())) {
                        letter = Character.toUpperCase(letter);
                    }
                    if (toSkip != 0) {
                        toReturn.setBoard(toReturn.getBoard() + toSkip);
                        toSkip = 0;
                    }
                    toReturn.setBoard(toReturn.getBoard() + letter);

                }

            }

            if (toSkip != 0) {
                toReturn.setBoard(toReturn.getBoard() + toSkip);
            }

            toSkip = 0;

            if (x != 0) {

                toReturn.setBoard(toReturn.getBoard() + '/');

            }

        }

        if (PlayerColor.WHITE.equals(playerTurnProperty.get())) {

            toReturn.setTurn("w");

        } else {

            toReturn.setTurn("b");

        }

        Integer whiteRookKingID = -1, whiteRookQueenID = -1, blackRookKingID = -1, blackRookQueenID = -1;

        for (Piece whitePiece: piecesProperty.get(PlayerColor.WHITE)) {

            if (PieceType.ROOK.equals(whitePiece.getPieceTypeProperty().get())) {

                if (whitePiece.getTileProperty().get().getXProperty().get() == 0) {

                    if (whitePiece.getTileProperty().get().getYProperty().get() == 0) {

                        whiteRookQueenID = whitePiece.getIDProperty().get();

                    }

                    if (whitePiece.getTileProperty().get().getYProperty().get() == 7) {

                        whiteRookKingID = whitePiece.getIDProperty().get();

                    }

                }

            }

        }

        for (Piece blackPiece: piecesProperty.get(PlayerColor.BLACK)) {

            if (PieceType.ROOK.equals(blackPiece.getPieceTypeProperty().get())) {

                if (blackPiece.getTileProperty().get().getXProperty().get() == 7) {

                    if (blackPiece.getTileProperty().get().getYProperty().get() == 0) {

                        blackRookQueenID = blackPiece.getIDProperty().get();

                    }

                    if (blackPiece.getTileProperty().get().getYProperty().get() == 7) {

                        blackRookKingID = blackPiece.getIDProperty().get();

                    }

                }

            }

        }

        boolean canBlackCastleKingside = blackRookKingID != -1;
        boolean canBlackCastleQueenside = blackRookQueenID != -1;
        boolean canWhiteCastleKingside = whiteRookKingID != -1;
        boolean canWhiteCastleQueenside = whiteRookQueenID != -1;

        for (Move move: movesProperty.get()) {

            if (PieceType.KING.equals(move.getPiece().getPieceTypeProperty().get())) {
                if (PlayerColor.BLACK.equals(move.getPiece().getPlayerColorProperty().get())) {
                    canBlackCastleKingside = false;
                    canBlackCastleQueenside = false;
                } else {
                    canWhiteCastleKingside = false;
                    canWhiteCastleQueenside = false;
                }
            }

            if (move.getPiece().getIDProperty().get() == whiteRookKingID) {
                canWhiteCastleKingside = false;
            }

            if (move.getPiece().getIDProperty().get() == whiteRookQueenID) {
                canWhiteCastleQueenside = false;
            }

            if (move.getPiece().getIDProperty().get() == blackRookKingID) {
                canBlackCastleKingside = false;
            }

            if (move.getPiece().getIDProperty().get() == blackRookQueenID) {
                canBlackCastleQueenside = false;
            }

        }

        if (!canBlackCastleKingside && !canBlackCastleQueenside && !canWhiteCastleKingside && !canWhiteCastleQueenside) {

            toReturn.setCastle("-");

        } else {

            if (canWhiteCastleKingside) {
                toReturn.setCastle("K");
            }
            if (canWhiteCastleQueenside) {
                toReturn.setCastle(toReturn.getCastle() + "Q");
            }
            if (canBlackCastleKingside) {
                toReturn.setCastle(toReturn.getCastle() + "k");
            }
            if (canBlackCastleQueenside) {
                toReturn.setCastle(toReturn.getCastle() + "q");
            }

        }

        if (movesProperty.size() != 0) {

            if (PieceType.PAWN.equals(movesProperty.get(movesProperty.size() - 1).getPiece().getPieceTypeProperty().get()) &&
                Math.abs(
                        movesProperty.get(movesProperty.size() - 1).getSourceTile().getXProperty().get()
                      - movesProperty.get(movesProperty.size() - 1).getTargetTile().getXProperty().get()
                ) == 2) {

                char letter = (char) ('a' + (movesProperty.get(movesProperty.size() - 1).getTargetTile().getYProperty().get()));
                int number = ((movesProperty.get(movesProperty.size() - 1).getTargetTile().getXProperty().get() + movesProperty.get(movesProperty.size() - 1).getSourceTile().getXProperty().get()) / 2) + 1;

                toReturn.setEnPassant(Character.toString(letter));
                toReturn.setEnPassant(toReturn.getEnPassant() + number);

            } else {

                toReturn.setEnPassant("-");

            }

        } else {

            toReturn.setEnPassant("-");

        }

        if (movesProperty.size() != 0) {

            int counter = 0;

            for (int i = movesProperty.size() - 1; i >= 0; i--) {

                if (movesProperty.get(i).isCapture() || PieceType.PAWN.equals(movesProperty.get(i).getPiece().getPieceTypeProperty().get())) {

                    break;

                }

                counter++;

            }

            toReturn.setHalfTurns(Integer.toString(counter));

        } else {

            toReturn.setHalfTurns("0");

        }

        toReturn.setTurnNumber(Integer.toString(movesProperty.get().size() / 2));

        return toReturn;

    }

}
