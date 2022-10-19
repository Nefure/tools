package org.nefure.tools.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.nefure.fxscaffold.annotion.Component;
import org.nefure.fxscaffold.annotion.Resource;
import org.nefure.fxscaffold.container.SceneFactory;
import org.nefure.tools.entity.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author nefure
 * @date 2022/10/14 11:19
 */
@Component
public class TreeController {

    private final Logger log = LoggerFactory.getLogger(TreeController.class);

    @Resource
    private Stage stage;

    @Resource
    private SceneFactory factory;

    @FXML
    private Canvas canvas;

    private int pos;

    private final TreeSet<Integer> tree = new TreeSet<>();

    @FXML
    private Spinner<Integer> input;

    private final Color red = Color.web("#F56C6C");

    private final Color black = Color.web("#606266");

    private final Color green = Color.web("#67C23A");

    private final int h = 32;

    private final int w = 54;


    private double getLen(String num){
        return Math.min((num.length()*5.4) +1,w)/2;
    }

    public void toLastPage(){
        stage.setScene(factory.getScene("root"));
    }

    public void clear(){
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        graphicsContext2D.setFill(Color.WHITE);
        graphicsContext2D.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
    }

    public void add(){
        Integer value = input.getValue();
        tree.add(value);
        clear();
        GraphicsContext g = canvas.getGraphicsContext2D();
        drawNode(g, value, green, 0, 0);
        draw(g);
    }

    public void draw(GraphicsContext g){
        double width = canvas.getWidth();
        TreeSet<Integer>.Node root = tree.getRoot(),tmp,idx;
        Queue<TreeSet<Integer>.Node> queue = new LinkedList<>();
        queue.add(root);
        int step = 0;
        while (!queue.isEmpty()){
            int len = queue.size();
            while (len -- > 0){
                idx = queue.poll();
                assert idx != null;
                if ((tmp = idx.left()) != null){
                    queue.add(tmp);
                }
                if ((tmp = idx.right()) != null){
                    queue.add(tmp);
                }
            }
            step++;
        }
        if (step > 31){
            log.error("树高已达到31");
            return;
        }
        queue.add(root);
        int cnt = 1 << (step -1);
        onDraw(g,root,pos + width/2,0,cnt*30);
        TreeSet<Integer>.Node first = tree.first();
        cnt = 0;
        do{
            step = first.key();
            drawNode(g,step,green,pos +cnt,600);
            first = first.next();
            cnt += 57;
        }while (first != null);
    }

    private void onDraw(GraphicsContext g, TreeSet<Integer>.Node idx,double x, double y, double step){
        TreeSet<Integer>.Node child;
        double tmp,l,r;

        drawNode(g,idx.key(),idx.isBlack()?black:red, x -27,y);
        g.setFill(Color.BLACK);
        g.strokeLine(x,y + h, x, tmp = (y + h +3));
        g.strokeLine(l = (x - step/2),tmp,r = (x + step/2),tmp);
        g.strokeLine(l,tmp,l,tmp +3);
        g.strokeLine(r,tmp,r,tmp +3);
        if ((child = idx.left()) != null){
            onDraw(g,child,l,tmp +3,step/2);
        }
        if ((child = idx.right()) != null){
            onDraw(g,child,r,tmp +3,step/2);
        }
    }

    public void del(){
        Integer value = input.getValue();
        tree.remove(value);
        clear();
        GraphicsContext g = canvas.getGraphicsContext2D();
        drawNode(g, value, red, 0, 0);
        draw(g);
    }

    public void addRandom(){
        Integer value = (int) (Math.random() * 1001);
        tree.add(value);
        clear();
        GraphicsContext g = canvas.getGraphicsContext2D();
        drawNode(g,value,green, 0,0);
        draw(g);
    }

    private void drawNode(GraphicsContext g, Integer value, Color back, double x, double y){
        g.setFill(back);
        g.fillRect(x,y,w,h);
        g.setFill(Color.WHITE);
        String text = value.toString();
        double len = getLen(text);
        g.fillText(text, x - len +27, y + 5 + h/2.0,len*2);
    }

    public void move(MouseEvent event) {
        double x = event.getX();
        if (x*2 > canvas.getWidth()){
            pos -= w;
        }else {
            pos += w;
        }
        clear();
        draw(canvas.getGraphicsContext2D());
    }
}
