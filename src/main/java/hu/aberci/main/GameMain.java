package hu.aberci.main;

import hu.aberci.controllers.MenuController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import lombok.Getter;

import java.io.File;
import java.io.ObjectInputStream;

public class GameMain extends Application {

    @Getter
    private static MenuController menuController;

    public static String savedGameFileName = "savegame.data";

    public static void main(String args[]) {

        launch(args);

    }

    public void start(Stage stage) throws Exception {

        stage.setTitle("BestChess");

        // System.out.println(getClass().getClassLoader().getResource("fxml/menu.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/menu.fxml"));
        AnchorPane p = fxmlLoader.load();

        menuController = fxmlLoader.getController();

        Scene scene = new Scene(p);
        stage.setScene(scene);
        stage.show();

    }

}
