package sample;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.util.Duration;

import javax.swing.*;
import java.sql.Time;

/**
 * Created by zhaozhongyu on 3/30/2017.
 */
public class FXMLController {
    @FXML private Label playerTimeLabel;
    @FXML private Label robotTimeLabel;
    @FXML public Canvas canvas;
    @FXML public ImageView menu;

    private Color[][] allChesses=new Color[14][14];//所有的棋格
    private Color whoSmile;
    KeyFrame playerTimer;
    KeyFrame robotTimer;
    public boolean isGameBegin = false;//游戏是否开始
    public boolean isGameOver=true;//游戏是否结束
    private Stack stack = new Stack();//链栈对象
    private Robot robot = new Robot();//机器人对象
    private int unDoTime=5;//悔棋的时间间隔
    private boolean isUndo;
    private Thread unDoThread;
    GraphicsContext gc;
    double xLine;
    double yLine;
    public int playerTime = 1800;
    public String playerMinute;
    public String playerSecond;
    public int robotTime = 1800;
    public String robotMinute;
    public String robotSecond;
    Timeline robottimeline = new Timeline();
    Timeline playertimeline = new Timeline();
    public Image black = new Image("sample/black.png");
    public Image white = new Image("sample/white.png");


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
            startTimer(true);
        }
        else {

        }
    }

    protected void startTimer(boolean start) {
        if(start) {
            /*if (isGameBegin) {
                String message = "The game has started.";
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("确认");
                alert.setHeaderText(message);
                //alert.initOwner(new Window());
                alert.show();
                return;
            }*/
            gc = canvas.getGraphicsContext2D();
            xLine = canvas.getWidth() / 14;
            yLine = canvas.getHeight() / 14;
            isGameBegin = true;
            Duration duration = Duration.millis(1000);

            robotTimer = new KeyFrame(duration, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (robot.getMyIsPlayingChess() == true && isGameBegin == true && isUndo == false) {
                        robotMinute = Integer.toString(robotTime / 60);
                        if (robotMinute.length() == 1)
                            robotMinute = "0" + robotMinute;
                        robotSecond = Integer.toString(robotTime % 60);
                        if (robotSecond.length() == 1)
                            robotSecond = "0" + robotSecond;
                        robotTimeLabel.setText(robotMinute + ":" + robotSecond);
                        robotTime--;
                        if (robotTime == 0) {
                            isGameBegin = false;
                            isGameOver = true;
                            whoSmile = Color.WHITE;
                        }
                    }
                }
            });
            robottimeline = new Timeline();
            robottimeline.setCycleCount(Timeline.INDEFINITE);
            robottimeline.getKeyFrames().add(robotTimer);
            robottimeline.play();
            KeyFrame playerTimer = new KeyFrame(duration, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (robot.getMyIsPlayingChess() == false && isGameBegin == true && isUndo == false) {
                        playerMinute = Integer.toString(playerTime / 60);
                        if (playerMinute.length() == 1)
                            playerMinute = "0" + playerMinute;
                        playerSecond = Integer.toString(playerTime % 60);
                        if (playerSecond.length() == 1)
                            playerSecond = "0" + playerSecond;
                        playerTimeLabel.setText(playerMinute + ":" + playerSecond);
                        playerTime--;
                        if (playerTime == 0) {
                            isGameBegin = false;
                            isGameOver = true;
                            whoSmile = Color.BLACK;
                        }
                    }
                }
            });
            playertimeline = new Timeline();
            playertimeline.setCycleCount(Timeline.INDEFINITE);
            playertimeline.getKeyFrames().add(playerTimer);
            playertimeline.play();

           /* playerTimer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    if (robot.getMyIsPlayingChess() == false && isGameBegin == true && isUndo == false) {
                        playerMinute = Integer.toString(playerTime / 60);
                        if (playerMinute.length() == 1)
                            playerMinute = "0" + playerMinute;
                        playerSecond = Integer.toString(playerTime % 60);
                        if (playerSecond.length() == 1)
                            playerSecond = "0" + playerSecond;
                        playerTimeLabel.setText(playerMinute + ":" + playerSecond);
                        playerTime--;
                        if(playerTime==0) {//如果玩家的时间用完了，表示玩家输
                            isGameBegin = false;//游戏没有开始
                            isGameOver = true;//游戏结束
                            whoSmile = Color.BLACK;//黑色笑
                            new Alert(Alert.AlertType.INFORMATION, "you lost.").show();
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            playerTimer.start();*/
            unDoThread=new Thread(new Runnable(){
                public void run(){//到执行start方法时线程启动
                    while(true){
                        if(unDoTime==0){//悔棋时间到,开始走棋
                            System.out.println("unDO running");
                            unDoTime--;//设置悔棋时间为-1
                            if(stack.getStackTop()!=null){//如果栈顶不为空表示有记录
                                if(stack.getStackTop().getChessColor()==Color.WHITE){{//如果为白色表示该机器人走棋
                                    robot.cerebra(allChesses, canvas, Color.BLACK,stack);//机器人走棋
                                    if(checkRowIsFive(robot.getRow(), robot.getCol())||checkColIsFive(robot.getRow(), robot.getCol())||checkRightBias(robot.getRow(), robot.getCol())||checkLeftBias(robot.getRow(), robot.getCol())){//机器人是否赢
                                        isGameBegin=false;
                                        isGameOver=true;
                                        whoSmile=Color.BLACK;
                                        new Alert(Alert.AlertType.INFORMATION, "you lost.").show();
                                        return;
                                    }
                                    if(isDogfall()==true){//是否平局
                                        isGameBegin=false;
                                        isGameOver=true;

                                        new Alert(Alert.AlertType.INFORMATION, "平局了").show();
                                        return;
                                    }
                                }
                                }
                            }
                            else{//表示棋盘上没有棋子了，也该机器人走棋
                                robot.cerebra(allChesses, canvas, Color.BLACK, stack);
                                if(checkRowIsFive(robot.getRow(), robot.getCol())||checkColIsFive(robot.getRow(), robot.getCol())||checkRightBias(robot.getRow(), robot.getCol())||checkLeftBias(robot.getRow(), robot.getCol())){
                                    isGameBegin=false;
                                    isGameOver=true;
                                    whoSmile=Color.BLACK;
                                    new Alert(Alert.AlertType.INFORMATION, "机器人赢了").show();
                                    return;
                                }
                            }
                            isUndo=false;//继续走棋
                        }
                        else{
                            try{
                                System.out.print("undo time --");
                                unDoThread.sleep(1000);  //线程睡一秒钟
                                unDoTime--;//悔棋时间减一
                                if(unDoTime==-10){
                                    unDoTime=-1;
                                }
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
           // unDoThread.start();
        }
        else { //点击暂停
            isGameBegin = false;
            robottimeline.stop();
            playertimeline.stop();
        }
    }

    @FXML protected void canvasClick(MouseEvent event) {
        if (gc == null){
            gc = canvas.getGraphicsContext2D();
            xLine = canvas.getWidth() / 14;
            yLine = canvas.getHeight() / 14;
        }
        int col = (int)(event.getX()/xLine);
        int row = (int)(event.getY()/yLine);
        if(allChesses[row][col]==null) {//如果在该棋盘位置等于空就表示该位置没有下棋可以走棋
            allChesses[row][col] = Color.WHITE;//将白色赋值给该位置的二维数组
            stack.Push(row, col, Color.WHITE);//将该信息压入栈

            repaint();
            if (checkRowIsFive(row, col) || checkColIsFive(row, col) || checkRightBias(row, col) || checkLeftBias(row, col)) {    //调用自定义函数判断四个方向是否有五子连珠，是否羸
                isGameBegin = false;//游戏没有开始
                isGameOver = true;//游戏结束
                whoSmile = Color.WHITE;//玩家羸，玩家笑
                repaint();//重新绘制
                JOptionPane.showMessageDialog(null, "你赢了！");
                return;
            }
            if (isDogfall() == true) {//如果玩家没有羸判断是否是平局
                isGameBegin = false;//游戏没有开始
                isGameOver = true;//游戏结束
                repaint();//重新绘制
                JOptionPane.showMessageDialog(null, "平局了！");
                return;
            }
            robot.cerebra(allChesses, canvas, Color.BLACK, stack);//玩家没有蠃，机器人走棋
            repaint();
            if (checkRowIsFive(robot.getRow(), robot.getCol()) || checkColIsFive(robot.getRow(), robot.getCol()) || checkRightBias(robot.getRow(), robot.getCol()) || checkLeftBias(robot.getRow(), robot.getCol())) {  ////调用自定义函数判断四个方向是否有五子连珠，是否羸
                isGameBegin = false;//游戏没有开始
                isGameOver = true;//游戏结束
                whoSmile = Color.BLACK;//机器人羸，机器人笑
                repaint();//重新绘制
                JOptionPane.showMessageDialog(null, "机器人赢了！");
                return;
            }
            if (isDogfall() == true) {//如果机器人没有赢，判断是否是平局
                isGameBegin = false;//游戏没有开始
                isGameOver = true;//游戏结束
                repaint();//重新绘制
                JOptionPane.showMessageDialog(null, "平局了！");
                return;
            }
        }
        else {
            JOptionPane.showMessageDialog(null,"该位置有棋子了！");
        }
    }

    public void repaint(){
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        for(int i = 0; i < 14; i++){
            for(int j = 0; j < 14; j++){
                if(allChesses[j][i] == Color.BLACK)
                    gc.drawImage(black, i * xLine + 1, j * yLine + 1);
                else if(allChesses[j][i] == Color.WHITE)
                    gc.drawImage(white, i * xLine + 1, j * yLine + 1);
            }
        }
    }

    public boolean isDogfall(){//是否是平局
        if(stack.StackLength()==14*14){//栈中记录了棋盘的信息，如果最后一步棋没有决定胜负,栈中的元素个数等于棋盘的棋子数就表示平局
            return true;
        }
        else{
            return false;
        }
    }

    public boolean checkLeftBias(int row,int col){//检查左斜
        int leftBiasUp=checkLeftBiasUp(row, col);//得到左斜上的个数
        int leftBiasDown=checkLeftBiasDown(row, col);//得到右斜下的个数
        int sum=leftBiasUp+leftBiasDown-1;//在计算过程中多算了一颗棋子，所以要减一颗棋子
        if(sum>=5){//已经五子连珠
            return true;
        }
        else{
            return false;
        }
    }

    public int checkLeftBiasDown(int row,int col){//检查左斜下
        int i=row;//将行赋值给i
        int j=col;//将列赋值给j
        int count=1;//将1赋值给count，count表示棋子的个数
        while(i<allChesses.length-1&&j>0){//终结条件
            if(allChesses[i][j]==allChesses[i+1][j-1]){//如果相同就进行统计
                count++;//棋子数加一
                i=i+1;//将i和j赋值为上次比较的位置
                j=j-1;
            }
            else {//如果有一次不相同就退出循环，比较结束
                break;
            }
        }
        return count;//返回统计的棋子数
    }

    public int checkLeftBiasUp(int row,int col){//统计左斜上的棋子数
        int i=row;
        int j=col;
        int count=1;
        while(i>0&&j<allChesses.length-1){
            if(allChesses[i-1][j+1]==allChesses[i][j]){
                count++;
                i=i-1;
                j=j+1;
            }
            else {
                break;
            }
        }
        return count;
    }

    public boolean checkRightBias(int row,int col){//统计右斜的棋子数
        int rightBiasUp=checkRightBiasUp(row, col);
        int rightBiasDown=checkRightBiasDown(row, col);
        int sum=rightBiasUp+rightBiasDown-1;
        if(sum>=5){
            return true;
        }
        else{
            return false;
        }
    }
    public int checkRightBiasUp(int row,int col){//统计右斜上的棋子数
        int i=row;
        int j=col;
        int count=1;
        while(i>0&&j>0){
            if(allChesses[i-1][j-1]==allChesses[i][j]){
                count++;
                i=i-1;
                j=j-1;
            }
            else {
                break;
            }
        }
        return count;
    }

    public int checkRightBiasDown(int row,int col){//统计右斜下的棋子数
        int i=row;
        int j=col;
        int count=1;
        while(i<allChesses.length-1&&j<allChesses.length-1){
            if(allChesses[i][j]==allChesses[i+1][j+1]){
                count++;
                i=i+1;
                j=j+1;
            }
            else {
                break;
            }
        }
        return count;
    }

    public boolean checkColIsFive(int row,int col){//统计列的棋子数
        int upCount=checkColUpIsFive(row, col);
        int downCount=checkColDownIsFive(row, col);
        int sum=upCount+downCount-1;
        if(sum>=5){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean checkRowIsFive(int row,int col){//统计列的棋子数
        int rightCount=checkRowRightIsFive(row, col);
        int leftCount=checkRowLeftIsFive(row,col);
        int sum=rightCount+leftCount-1;
        if(sum>=5){
            return true;
        }
        else{
            return false;
        }
    }
    public int checkColDownIsFive(int row,int col){//统计列下的棋子数
        int count=1;
        for(int i=row;i<allChesses.length-1;i++){
            if(allChesses[i][col]==allChesses[i+1][col]){
                count++;
            }
            else{
                break;
            }
        }
        return count;
    }

    public int checkColUpIsFive(int row,int col){//统计列上的棋子数
        int count=1;
        for(int i=row;i>0;i--){
            if(allChesses[i-1][col]==allChesses[i][col]){
                count++;
            }
            else{
                break;
            }
        }
        return count;
    }

    public int checkRowLeftIsFive(int row,int col){//统计列左的棋子数
        int count=1;
        for(int i=col;i>0;i--){
            if(allChesses[row][i-1]==allChesses[row][i]){
                count++;
            }
            else{
                break;
            }
        }
        return count;
    }
    public int checkRowRightIsFive(int row,int col){//统计行右的棋子数
        int count=1;
        for(int i=col;i<allChesses.length-1;i++){
            if(allChesses[row][i]==allChesses[row][i+1]){
                count++;
            }
            else{
                break;
            }
        }
        return count;
    }
}
