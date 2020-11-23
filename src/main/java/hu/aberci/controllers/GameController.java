package hu.aberci.controllers;

import hu.aberci.main.GameMain;
import hu.aberci.views.ChessBoardView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.Getter;

import java.net.URL;
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

        chessBoard.setChessGameController(chessGameController);

    }
}
