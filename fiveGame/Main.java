package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("five.fxml"));
        primaryStage.setTitle("五子棋");
        Image imageBG = new Image("sample/bg_game.JPG");
        /*Group root = new Group();
        //Image imageBG = new Image("sample/bg_game.JPG");
        Canvas canvas = new Canvas(imageBG.getWidth(), imageBG.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.drawImage(imageBG, 0, 0, imageBG.getWidth(), imageBG.getHeight());
        gc.drawImage(new Image("sample/W.png"), 26, 42);
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(event.getX());
                System.out.println(event.getY());
                System.out.println(event.getSceneX());
                System.out.println(event.getSceneY());
            }
        });
        root.getChildren().add(canvas);*/

        primaryStage.setScene(new Scene(root, imageBG.getWidth(), imageBG.getHeight()));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
