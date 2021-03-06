package sample;

/**
 * Created by zhaozhongyu on 3/31/2017.
 */
import javafx.scene.paint.Color;

class Stack{

    private LNode headLNode;

    public Stack(){
        initStack();
    }
    public void initStack(){
        headLNode=new LNode();
    }
    public LNode getHeadNode(){
        return this.headLNode;
    }

    public int StackLength(){
        LNode p=headLNode.getNext();
        int count=0;
        while(p!=null){
            count++;
            p=p.getNext();
        }
        return count;
    }

    public void Push(int row,int col,Color chessColor){
        LNode p=new LNode(row,col,chessColor);
        p.setNext(headLNode.getNext());
        headLNode.setNext(p);
    }
    public LNode Pop(){
        LNode p;
        if(headLNode.getNext()==null){
            return null;
        }
        p=headLNode.getNext();
        headLNode.setNext(p.getNext());
        return p;
    }
    public LNode getStackTop(){
        if(headLNode.getNext()==null){
            return null;
        }
        else{
            return headLNode.getNext();
        }
    }
    public void clearStack(){
        headLNode.setNext(null);
    }
}
