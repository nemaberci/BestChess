package hu.aberci.controllers;

import hu.aberci.entities.events.ChessBoardEvent;
import hu.aberci.entities.interfaces.PlayerColor;
import hu.aberci.main.GameMain;
import hu.aberci.views.ChessBoardView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class GameController implements Initializable {

    ChessGameController chessGameController;

    @FXML
    ChessBoardView gridPane;

    @FXML
    Button backToMenu;

    @FXML
    Button newGame;

    @FXML
    TextArea textArea;

    ChessBoardView chessBoard;

    int time, increment;

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

        time = clockTime;
        increment = clockIncrement;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        chessGameController.setParent(gridPane);

        chessBoard = gridPane;
        chessBoard.initialize();

        chessBoard.getBoardStateProperty()
                .bindBidirectional(chessGameController.getBoardStateProperty());

        chessBoard.setChessGameController(chessGameController);

        textArea.setVisible(false);

        newGame.setVisible(false);
        newGame.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                        textArea.setVisible(false);

                        chessGameController = new ChessGameController(null, time, increment);

                        chessGameController.setParent(gridPane);

                        chessBoard.getBoardStateProperty()
                                .bindBidirectional(chessGameController.getBoardStateProperty());

                        chessBoard.setChessGameController(chessGameController);
                    }
                }
        );

        backToMenu.setOnAction(
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try {

                            final FXMLLoader loader = new FXMLLoader(
                                    MenuController.class.getResource("/fxml/menu.fxml")
                            );

                            loader.setRoot(null);

                            ((Stage) backToMenu.getScene().getWindow()).setScene(
                                    new Scene(loader.load())
                            );


                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
        );

        chessBoard.addEventHandler(
                ChessBoardEvent.CHESS_BOARD_EVENT_CHECKMATE,
                chessBoardEvent -> {

                    textArea.setVisible(true);

                    textArea.setText("Checkmate");
                    newGame.setVisible(true);

                    textArea.setText(
                            textArea.getText() + "\n" + (PlayerColor.WHITE.equals(chessBoard.getBoardStateProperty().get().getPlayerTurnProperty().get()) ? "Black" : "White") + " wins"
                    );

                }
        );

        chessBoard.addEventHandler(
                ChessBoardEvent.CHESS_BOARD_EVENT_DRAW,
                chessBoardEvent -> {

                    textArea.setVisible(true);

                    textArea.setText("Draw");
                    newGame.setVisible(true);

                }
        );

    }
}
