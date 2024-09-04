package com.rajg98.flappy_bird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {
    public static FlappyBird flappyBird;
    public final int WIDTH=1200,HEIGHT=700;
    public Renderer renderer;
    public Rectangle bird;
    public Random rand;
    public int ticks,yMotion,score;
    public boolean gameOver,started;
    ArrayList<Rectangle> columns;
    public FlappyBird(){
        JFrame jFrame= new JFrame();
        Timer timer = new Timer(20,this);
        renderer= new Renderer();
        rand= new Random();
        jFrame.add(renderer);
        jFrame.setTitle("Flappy Bird");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(WIDTH,HEIGHT);
        jFrame.addMouseListener(this);
        jFrame.addKeyListener(this);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
        bird= new Rectangle(WIDTH/2-10,HEIGHT/2-10,20,20);
        columns= new ArrayList<>();
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();
    }
    public void addColumn(boolean start){
        int space=150;
        int width=100;
        int height= 50 + rand.nextInt(300);
        if(start){
            columns.add(new Rectangle(WIDTH+width+ (columns.size()*300),HEIGHT-height-120,width,height));
            columns.add(new Rectangle(WIDTH+width+((columns.size()-1)*300),0,width,HEIGHT-height-space-120));
        }else{
            columns.add(new Rectangle(columns.getLast().x+600,HEIGHT-height-120,width,height));
            columns.add(new Rectangle(columns.getLast().x,0,width,HEIGHT-height-space-120));

        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int speed=10;
        ticks++;
        if(started){
            for (Rectangle column : columns) {
                column.x -= speed;
            }
            if(ticks%2==0 && yMotion<15){
                yMotion+=2;
            }
            bird.y+=yMotion;
            for(Rectangle column:columns){
                if(column.y==0 && bird.x+ bird.width/2>column.x + column.width/2-10  &&
                        bird.x+ bird.width/2<column.x + column.width/2+10){
                    score++;
                }
                if(column.intersects(bird)){
                    gameOver=true;
                    bird.x=column.x- bird.width;
                }
            }
            for(int i=0;i< columns.size();i++){
                Rectangle column = columns.get(i);
                if(column.x+column.width<0){
                    columns.remove(column);
                    if(column.y==0){
                        addColumn(false);
                    }
                }
            }
            if(bird.y>HEIGHT-120 || bird.y<0){
                gameOver=true;
            }
            if(bird.y+yMotion>=HEIGHT-120){
                bird.y=HEIGHT-120- bird.height;
            }

        }
        renderer.repaint();
    }
    public void paintColumn(Graphics g, Rectangle column){
        g.setColor(Color.green.darker());
        g.fillRect(column.x,column.y,column.width,column.height);
    }
    public void repaint(Graphics g) {
        g.setColor(Color.cyan);
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(Color.orange);
        g.fillRect(0,HEIGHT-120,WIDTH,150);
        g.setColor(Color.green);
        g.fillRect(0,HEIGHT-120,WIDTH,20);
        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);
        for(Rectangle column:columns){
            paintColumn(g,column);
        }
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD,100));
        if(!gameOver && !started){
            g.drawString("Click to Start",WIDTH/2-320,HEIGHT/2-50);
        }
        if(gameOver){
            g.drawString("Game Over",WIDTH/2-280,HEIGHT/2-20);
        }
        if(!gameOver && started){
            g.drawString(String.valueOf(score),WIDTH/2-25,100);
        }
    }
    public static void main(String[] args) {
        flappyBird= new FlappyBird();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        jump();
    }

    public void jump() {
        if(gameOver){
            bird= new Rectangle(WIDTH/2-10,HEIGHT/2-10,20,20);
            columns.clear();
            yMotion=0;
            score=0;
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);
            gameOver=false;
        }
        if(!started){
            started=true;
        }else {
            if(yMotion>0){
                yMotion=0;
            }
            yMotion-=10;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            jump();
        }
    }
}
