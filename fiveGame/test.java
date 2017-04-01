import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.Timer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends Application {
    private TextArea textField;
    private Timer timer;

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root, 800, 400));
        textField = new TextArea();
        Canvas canvas = new Canvas();

        startTime();
        resetTextField();
        root.getChildren().addAll(textField);
        primaryStage.show();
    }
    public void startTime() {
        int delay = 1000;//1000毫秒
        /*timer = new Timer(delay, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        resetTextField();
                    }
                });
            }
        });
        timer.start();*/
        new Timeline(new KeyFrame(
                Duration.millis(2000),
                ae -> resetTextField()
        )).play();

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        resetTextField();
                    }
                });
            }
        }).start();*/

    }
    public void resetTextField() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        textField.setText(date);

    }
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
    }
    public static void main(String args[]) {
        launch(args);
    }
}
