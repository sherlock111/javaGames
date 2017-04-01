package sample;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Window;

/**
 * Created by zhaozhongyu on 3/30/2017.
 */
public class FXMLController {
    @FXML private Text white;
    @FXML private Label black;
    @FXML public Canvas canvas;
    @FXML public ImageView menu;
    Thread playerThread;
    public boolean isGameBegin = false;
    public int playerTime = 600;
    String playerMinute;
    String playerSecond;

    @FXML protected void menuClick(MouseEvent event) {
        System.out.println(event.getX());
        System.out.println(event.getY());
        if (event.getX() < 35) { //点击开始
            startTimer(true);
        }
        else if(event.getX() < 70) {
            startTimer(false);
        }
        else if (event.getX() < 100) {

        }
        else {

        }
    }

    @FXML protected void canvasClick(MouseEvent event) {
        System.out.println(event.getX());
        System.out.println(event.getY());
    }

    protected void startTimer(boolean start) {
        if(start) {
            if (isGameBegin) {
                String message = "The game has started.";
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("确认");
                alert.setHeaderText(message);
                //alert.initOwner(new Window());
                alert.show();
                return;
            }
            isGameBegin = true;
            int robotTime = 600;
            String robotMinute;
            String robotSecond;
            playerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int playerTime = 600;
                    String playerMinute;
                    String playerSecond;
                    while(true) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        playerMinute = Integer.toString(playerTime / 60);
                        playerSecond = Integer.toString(playerTime % 60);

                        playerTime--;
                    }
                }
            }
            );
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setPlayerTime();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setPlayerTime();
                }
            });


        }
        else {
            white.setText("pause");
            black.setText("pause");
        }
    }

    protected void setPlayerTime() {
        playerMinute = Integer.toString(playerTime / 60);
        playerSecond = Integer.toString(playerTime % 60);
        white.setText(playerSecond);
        playerTime--;
    }

}
