package bubbleShoot;

import java.awt.*;

public class Player {
    //Fields
    private double x, y, dx, dy;
    private int radius;

    private int speed;
    private int health;

    private Color color1, color2;

    public static boolean up;
    public static boolean down;
    public static boolean left;
    public static boolean right;

    public static boolean isFiring;

    private boolean recovering;
    private long recoveryTimer;

    private int score;


    //Constructor
    public Player(){

        x = GamePanel.WIDTH / 2;
        y = GamePanel.HEIGHT / 2;
        radius = 5;

        speed = 5;
        dx = 0;
        dy = 0;

        health = 3;

        color1 = Color.MAGENTA;
        color2 = Color.RED;

        up = false;
        down = false;
        left = false;
        right = false;
        isFiring = false;

        recovering = false;
        recoveryTimer = 0;

        score = 0;
    }

    //Functions
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public int getRadius(){
        return radius;
    }
    public boolean isDead(){
        return (health <= 0);
    }

    public void hit(){
        health--;
        recovering = true;
        recoveryTimer = System.nanoTime();
    }

    public int getScore(){return score;}

    public int getHealth(){return health;}

    public boolean isRecovering(){ return recovering;}

    public void addScore(int i){
        score += i;
    }

    public void update(){
        if(up && y > radius){
            dy = -speed;
        }
        if(down && y < GamePanel.HEIGHT - radius){
            dy = speed;
        }
        if(left && x > radius){
            dx = -speed;
        }
        if(right && x < GamePanel.WIDTH - radius){
            dx = speed;
        }

        if(up && left || up && right || down && left || down && right){
            double angle = Math.toRadians(45);
            dy *= Math.sin(angle);
            dx *= Math.cos(angle);
        }

        y += dy;
        x += dx;

        dy = 0;
        dx = 0;

        if(isFiring){
            GamePanel.bullets.add(new Bullet());
        }

        long elapsed = (System.nanoTime() - recoveryTimer) / 1000000;
        if(elapsed > 2000){
            recovering = false;
            recoveryTimer = 0;
        }
    }

    public void draw(Graphics2D g){
        if(recovering){
            g.setColor(color2);
            g.fillOval((int)(x-radius), (int)(y - radius), 2*radius, 2*radius);
            g.setStroke(new BasicStroke(3));
            g.setColor(color2.darker());
            g.drawOval((int)(x-radius), (int)(y - radius), 2*radius, 2*radius);
            g.setStroke(new BasicStroke(1));
        }else{

            g.setColor(color1);
            g.fillOval((int)(x-radius), (int)(y - radius), 2*radius, 2*radius);
            g.setStroke(new BasicStroke(3));
            g.setColor(color1.darker());
            g.drawOval((int)(x-radius), (int)(y - radius), 2*radius, 2*radius);
            g.setStroke(new BasicStroke(1));
        }
    }

}

