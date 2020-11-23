package hu.aberci.controllers;

import hu.aberci.entities.data.BoardStateImpl;
import hu.aberci.entities.data.TileImpl;
import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.PlayerColor;
import hu.aberci.entities.interfaces.Tile;
import hu.aberci.main.GameMain;
import hu.aberci.views.ChessBoardView;
import hu.aberci.views.PieceView;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class GameController implements Initializable {

    ChessGameController chessGameController;

    @FXML
    ChessBoardView gridPane;

    ChessBoardView chessBoard;

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

        chessGameController = new ChessGameController(null, clockTime, clockIncrement);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        chessGameController.setParent(gridPane);

        chessBoard = gridPane;
        chessBoard.initialize();

        chessBoard.getBoardStateProperty()
                .set(chessGameController.getBoardState());

    }
}
