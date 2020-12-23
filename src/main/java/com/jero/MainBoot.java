package com.jero;

import com.jero.biz.controller.MainController;
import com.jero.ui.LayoutInflater;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * @Description 主启动程序
 * @Author lixuetao
 * @Date 2020/12/15
 **/
public class MainBoot extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader fxmlLoader = LayoutInflater.getFxmlLoad("activity_main");
            Parent root = fxmlLoader.load();

            MainController mainController = fxmlLoader.getController();

            Scene scene = new Scene(root);
            primaryStage.setTitle(Constants.APP_NAME);
            primaryStage.getIcons().add(Constants.APP_LOGO);
            primaryStage.initStyle(StageStyle.DECORATED);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue)
                    primaryStage.setMaximized(false);
            });
            primaryStage.show();

            mainController.init(primaryStage, mainController);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
