package hu.aberci.views;

import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.PlayerColor;
import hu.aberci.entities.interfaces.Tile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.scene.layout.GridPane;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ChessBoardView extends GridPane {

    ObjectProperty<BoardState> boardStateProperty;
    ObservableMap<PlayerColor, List<PieceView>> pieceViews;
    ObservableList<TileView> tileViews;

    @Getter
    ObjectProperty<PieceView> selectedPieceView;
    @Getter
    ObservableSet<TileView> availableTileViews;

    public ChessBoardView(BoardState boardState) {

        boardStateProperty = new SimpleObjectProperty<>(boardState);

        for (List<Tile> tileList: boardStateProperty.get().getTilesProperty().get()) {

            for (Tile tile: tileList) {

                TileView tileView = new TileView(this, tile);
                tileViews.add(tileView);
                add(tileView, tile.getXProperty().get(), tile.getYProperty().get());

            }

        }

        List<PieceView> whitePieceViews = new ArrayList<>();
        List<PieceView> blackPieceViews = new ArrayList<>();

        for (Piece whitePiece: boardStateProperty.get().getPiecesProperty().get().get(PlayerColor.WHITE)) {

            PieceView pieceView = new PieceView(this, whitePiece);
            whitePieceViews.add(pieceView);

        }
        for (Piece blackPiece: boardStateProperty.get().getPiecesProperty().get().get(PlayerColor.BLACK)) {

            blackPieceViews.add(new PieceView(this, blackPiece));

        }

        pieceViews.put(PlayerColor.WHITE, whitePieceViews);
        pieceViews.put(PlayerColor.BLACK, blackPieceViews);

        selectedPieceView = new SimpleObjectProperty<>();
        availableTileViews = new SimpleSetProperty<>();

    }

}
