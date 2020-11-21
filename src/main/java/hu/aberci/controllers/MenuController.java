package hu.aberci.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;

public class MenuController {

    @Getter
    @FXML
    private CheckBox AIEnabled;

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

    public void AIEnabledChanged() {

        System.out.println("AI IS ENABLED:" + AIEnabled.isSelected());

    }

    @Getter
    @FXML
    private CheckBox chessClockEnabled;

    @FXML
    public void initialize() {

        System.out.println("This was called");

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
                        try {
                            ((Stage) startButton.getScene().getWindow()).setScene(
                                    new Scene(new FXMLLoader(getClass().getClassLoader().getResource("fxml/game.fxml")).load())
                            );
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
        );

    }

}
