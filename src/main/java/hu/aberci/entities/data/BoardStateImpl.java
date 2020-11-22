package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.*;
import hu.aberci.exceptions.NoTilesException;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import lombok.Getter;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            pieces.add(createRook(tiles.get(x).get(y), playerColor));
        }
        if (y % 5 == 1) { // 1 or 6
            pieces.add(createKnight(tiles.get(x).get(y), playerColor));
        }
        if (y == 2 || y == 5) {
            pieces.add(createBishop(tiles.get(x).get(y), playerColor));
        }
        if (y == 3) {
            pieces.add(createQueen(tiles.get(x).get(y), playerColor));
        }
        if (y == 4) {
            pieces.add(createKing(tiles.get(x).get(y), playerColor));
        }
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
    SimpleListProperty<List<Tile>> tilesProperty;

    @Getter
    SimpleMapProperty<PlayerColor, List<Piece>> piecesProperty;

    @Getter
    ListProperty<Move> movesProperty;

    public BoardStateImpl(int startingTime, int increment, boolean generateTiles, boolean generatePieces) throws NoTilesException {

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

        movesProperty = new SimpleListProperty<>();

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
                                whitePieces.add(createPawn(tiles.get(x).get(y), PlayerColor.WHITE));
                                break;
                            case 6:
                                blackPieces.add(createPawn(tiles.get(x).get(y), PlayerColor.BLACK));
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

        playerTurnProperty = new SimpleObjectProperty<>(
                boardState.getPlayerTurnProperty().get()
        );

        tilesProperty = new SimpleListProperty<>();
        tilesProperty.set(FXCollections.observableList(generateTiles()));

        piecesProperty = new SimpleMapProperty<>();
        Map<PlayerColor, List<Piece>> map = new HashMap<>();
        List<Piece> pieces = new ArrayList<>();
        for (Piece piece: boardState.getPiecesProperty().get().get(PlayerColor.WHITE)) {
            pieces.add(new PieceImpl(piece, this));
        }
        map.put(PlayerColor.WHITE, pieces);
        pieces.clear();
        for (Piece piece: boardState.getPiecesProperty().get().get(PlayerColor.BLACK)) {
            pieces.add(new PieceImpl(piece, this));
        }
        map.put(PlayerColor.BLACK, pieces);
        piecesProperty.set(FXCollections.observableMap(map));

        chessClockProperty = new SimpleObjectProperty<>();
        chessClockProperty.set(
                new ChessClockImpl(boardState.getChessClockProperty().get())
        );

        /*
        * We do not replicate moves for the copy constructor
        *  */

        movesProperty = new SimpleListProperty<>();

    }

}
