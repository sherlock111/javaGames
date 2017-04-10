package com.zzy;

import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class clock extends Application {
    class testpane extends Pane {//testpane继承pane用来布局界面
        private Timeline timeline;
        private Button Start,Stop;
        private Line line1,line2,line3;//秒针，分针，时针
        private double x1,x2,x3;//当前时间秒、分、时指向的时间
        public testpane(){//构造函数
            HBox hbox=new HBox();//新建HBOX布局，放按钮的
            hbox.setSpacing(20);
            hbox.setLayoutX(310);
            hbox.setLayoutY(520);
            Start = new Button("Start");//建立start按钮
            Start.setPrefSize(80, 40);
            Stop = new Button("Stop");//建立stop按钮
            Stop.setPrefSize(80, 40);
            hbox.getChildren().addAll(Start,Stop);//将按钮加入HBOX
            getChildren().add(hbox);

            Start.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    System.out.println("Start button clicked");
                    timeline.play();//动画开始
                }
            });//start按钮功能
            Stop.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    System.out.println("Stop button clicked");
                    timeline.stop();//动画停止
                }
            });//stop按钮功能

            Circle circle = new Circle(400,300,200, Color.web("white", 0));//建立圆形，边长为200，白色,透明度为0
            circle.setStroke(Color.web("black", 1));//给园边框
            getChildren().add(circle);

            for (int i = 0; i < 60; i++) {//短的表盘指针
                Line line = new Line(400+200*Math.sin(Math.PI*i/30),(300-200*Math.cos(Math.PI*i/30)),400+190*Math.sin(Math.PI*i/30),(300-190*Math.cos(Math.PI*i/30)));
                getChildren().add(line);
            }
            for (int i = 0,j = 0; i < 60; i=i+5,j++) {//长的表盘指针和 表盘数字
                String[] n={"12","1","2","3","4","5","6","7","8","9","10","11"};
                Line line = new Line(400+200*Math.sin(Math.PI*i/30),(300-200*Math.cos(Math.PI*i/30)),400+180*Math.sin(Math.PI*i/30),(300-180*Math.cos(Math.PI*i/30)));
                getChildren().add(line);
                Text text = new Text(395+170*Math.sin(Math.PI*i/30),(300-170*Math.cos(Math.PI*i/30)),n[j]);
                getChildren().add(text);
            }
            line1 = new Line();
            line2 = new Line();
            line3 = new Line();
            paint();//画出初始时钟指针
            getChildren().addAll(line1,line2,line3);
            timeline = new Timeline();//时间轴
            timeline.setCycleCount(Timeline.INDEFINITE);
            Duration duration = Duration.millis(1000);//设定时钟动画每1秒变一次，关键帧时间间隔
            KeyFrame keyFrame = new KeyFrame(duration, new EventHandler<ActionEvent>() {

                public void handle(ActionEvent event) {//动画变化的代码
                    paint();//画出指针
                }
            });
            timeline.getKeyFrames().add(keyFrame);    //时间轴获取关键帧
            timeline.play();
        }

        public void paint(){//画指针的函数
            Calendar c = Calendar.getInstance();//取当前时间

            x1=c.get(Calendar.SECOND);
            x2=c.get(Calendar.MINUTE);
            x3=c.get(Calendar.HOUR)*5+x2/12;

            line1.setStartX(400);
            line1.setStartY(300);
            line1.setEndX(400+160*Math.sin(Math.PI*x1/30));
            line1.setEndY(300-160*Math.cos(Math.PI*x1/30));//秒针
            line1.setStroke(Color.BLACK);
            line1.setStrokeWidth(1);
            line2.setStartX(400);
            line2.setStartY(300);
            line2.setEndX(400+160*Math.sin(Math.PI*x2/30));
            line2.setEndY(300-160*Math.cos(Math.PI*x2/30));//分针
            line2.setStroke(Color.BLACK);
            line2.setStrokeWidth(1.5);
            line3.setStartX(400);
            line3.setStartY(300);
            line3.setEndX(400+120*Math.sin(Math.PI*x3/30));
            line3.setEndY(300-120*Math.cos(Math.PI*x3/30));//时针
            line3.setStroke(Color.BLACK);
            line3.setStrokeWidth(2);
        }
    }

    public void start(Stage stage) {
        testpane pane=new testpane();
        Scene scene = new Scene(pane,800,600);
        stage.setTitle("Myclock"); //设置stage标题
        stage.setScene(scene); // 把scene放入stage中
        stage.show();
        stage.setResizable(false);//界面大小固定
    }

    public static void main(String[] args) {
        System.out.println("launch application");
        launch(args);
    }
}
