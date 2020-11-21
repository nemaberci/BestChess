package hu.aberci.controllers;

import hu.aberci.entities.data.BoardStateImpl;
import hu.aberci.entities.data.TileImpl;
import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Tile;
import hu.aberci.main.GameMain;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class GameController {

    ChessGameController chessGameController;

    @FXML
    GridPane chessBoard;

    List<List<Tile>> tiles;

    private void colorBoard() {

        // TODO

        tiles = new ArrayList<>();

        for (int i = 0; i < 8; i++) {

            tiles.add(new ArrayList<>());
            for (int j = 0; j < 8; j++) {

                StackPane stackPane = new StackPane();

                chessBoard.add(stackPane, i, j);

            }
        }

        for (Node node: chessBoard.getChildren()) {

            if ((GridPane.getColumnIndex(node) + GridPane.getRowIndex(node)) % 2 == 0) {
                node.setStyle("-fx-background-color: white;");
            } else {
                node.setStyle("-fx-background-color: black;");
            }

        }

    }

    public GameController() {

        boolean isChessClock = false;
        int clockTime = 0;
        int clockIncrement = 0;

        try {

            MenuController menuController = GameMain.getMenuController();

            isChessClock = menuController.getChessClockEnabled().isSelected();
            clockIncrement = parseInt(menuController.getClockIncrement().getText());
            clockTime = parseInt(menuController.getClockTime().getText());

        } catch (Exception ignore) {

            ignore.printStackTrace();

        }

        chessGameController = new ChessGameController(clockTime, clockIncrement);



        // TODO

    }

    @FXML
    void initialize() {
        colorBoard();
    }

}
