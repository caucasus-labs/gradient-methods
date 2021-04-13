package com.caucasus.optimization.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("main.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Minimization methods");
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }
}
