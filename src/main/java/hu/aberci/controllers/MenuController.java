package hu.aberci.controllers;

import hu.aberci.entities.data.BoardStateImpl;
import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.PlayerColor;
import hu.aberci.entities.interfaces.SerializableBoardState;
import hu.aberci.main.GameMain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class MenuController {

    @Getter
    @FXML
    private CheckBox AIEnabled;

    @Getter
    @FXML
    private ComboBox<PlayerColor> aiColorComboBox;

    @FXML
    private Label aiColorLabel;

    @FXML
    private Pane chessClockConfig;

    @Getter
    @FXML
    private TextField clockTime;

    @Getter
    @FXML
    private TextField clockIncrement;

    @FXML
    private Button startButton;

    @FXML
    Button continueGame;

    @FXML
    Button deleteSavedGame;

    public void AIEnabledChanged() {

        // System.out.println("AI IS ENABLED:" + AIEnabled.isSelected());

    }

    @Getter
    @FXML
    private CheckBox chessClockEnabled;

    @Getter
    private BoardState boardState;

    public MenuController() {

        boardState = null;

    }

    private void loadGame() {

        try {

            final FXMLLoader loader = new FXMLLoader(
                    MenuController.class.getResource("/fxml/game.fxml")
            );

            loader.setRoot(null);

            ((Stage) startButton.getScene().getWindow()).setScene(
                    new Scene(loader.load())
            );

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @FXML
    public void initialize() {

        GameMain.setMenuController(this);

        aiColorComboBox.getItems().add(
                PlayerColor.WHITE
        );
        aiColorComboBox.getItems().add(
                PlayerColor.BLACK
        );

        aiColorComboBox.getSelectionModel().select(
                PlayerColor.BLACK
        );

        aiColorComboBox.visibleProperty().bindBidirectional(
                AIEnabled.selectedProperty()
        );

        aiColorLabel.visibleProperty().bindBidirectional(
                AIEnabled.selectedProperty()
        );

        try {

            if (new File(GameMain.savedGameFileName).isFile()) {

                FileInputStream fileInputStream = new FileInputStream(GameMain.savedGameFileName);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                boardState = new BoardStateImpl((SerializableBoardState) objectInputStream.readObject());

                objectInputStream.close();
                fileInputStream.close();

                continueGame.setVisible(true);
                deleteSavedGame.setVisible(true);

            } else {

                continueGame.setVisible(false);
                deleteSavedGame.setVisible(false);

            }

        } catch (Exception exception) {

            exception.printStackTrace();

            continueGame.setVisible(false);
            deleteSavedGame.setVisible(false);

        }

        continueGame.visibleProperty().bindBidirectional(
                deleteSavedGame.visibleProperty()
        );

        chessClockConfig.visibleProperty().bind(
                chessClockEnabled.selectedProperty()
        );

        clockTime.textProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (!newValue.matches("[0-9]+")) {
                            clockTime.setText(oldValue);
                        } else {
                            if (Integer.parseInt(newValue) < 30) {
                                clockTime.setText("30");
                            }
                            if (Integer.parseInt(newValue) > 5400) {
                                clockTime.setText("5400");
                            }
                        }
                    }
                }
        );

        clockIncrement.textProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (!newValue.matches("[0-9]+")) {
                            clockIncrement.setText(oldValue);
                        } else {
                            if (Integer.parseInt(newValue) < 1) {
                                clockIncrement.setText("1");
                            }
                            if (Integer.parseInt(newValue) > 120) {
                                clockIncrement.setText("120");
                            }
                        }
                    }
                }
        );

        startButton.setOnAction(
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {

                        System.out.println("SETTING BOARDSTATE TO NULL");

                        boardState = null;

                        loadGame();

                    }
                }
        );

        continueGame.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                        File file = new File(GameMain.savedGameFileName);

                        if (file.isFile()) {

                            try {

                                FileInputStream fileInputStream = new FileInputStream(GameMain.savedGameFileName);
                                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                                boardState = new BoardStateImpl((SerializableBoardState) objectInputStream.readObject());

                                System.out.println("SETTING BOARDSTATE TO " + boardState);

                                objectInputStream.close();
                                fileInputStream.close();

                                loadGame();

                            } catch (Exception exception) {

                                exception.printStackTrace();

                            }

                        }

                    }
                }
        );

        deleteSavedGame.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                        File file = new File(GameMain.savedGameFileName);

                        if (file.isFile()) {

                            file.delete();
                            deleteSavedGame.setVisible(false);
                            boardState = null;

                        }

                    }
                }
        );

    }

}
