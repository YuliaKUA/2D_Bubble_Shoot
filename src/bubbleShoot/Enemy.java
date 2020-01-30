package bubbleShoot;

import java.awt.*;

public class Enemy {
    //Fields
    private double x, y;
    private int radius;

    private double speed;
    private double dx, dy;

    private double health;

    private int type;
    private int rank;

    private boolean ready;
    private boolean dead;

    private Color color;

    //Constructor
    public Enemy(int type, int rank){
        this.type = type;
        this.rank = rank;

        switch (type){
            case 1:
                color = Color.GREEN;
                switch (rank){
                    case 1:
                        radius = 7;
                        speed = 2;
                        health = 1;
                        break;
                    case 2:
                        radius = 14;
                        speed = 3;
                        health = 2;
                        break;
                    case 3:
                        radius = 21;
                        speed = 1.5;
                        health = 3;
                        break;
                    case 4:
                        radius = 30;
                        speed = 2;
                        health = 4;
                        break;
                }
                break;
            case 2:
                color = Color.CYAN;
                switch (rank){
                    case 1:
                        radius = 10;
                        speed = 1;
                        health = 5;
                        break;
                }
                break;
            case 3:
                color = Color.orange;
                switch (rank){
                    case 1:
                        radius = 15;
                        speed = 6;
                        health = 3;
                        break;
                }
                break;
        }

        x = Math.random() * GamePanel.WIDTH;
        y = 0;

        double angle = Math.toRadians(Math.random() * 360);
        dx = Math.sin(angle) * speed;
        dy = Math.cos(angle) * speed;

        ready = false;
        dead = false;
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

    public int getType(){return type;}
    public int getRank(){return rank;}

    public boolean remove(){
        if(health <= 0){
            return true;
        }
        return false;
    }

    public boolean isDead() {
        return dead;
    }

    public void hit(){
        health--;
        if(health <= 0){
            dead = true;
        }
    }

    public void update(){
        x += dx;
        y += dy;

        if(!ready){
            if(x > radius && x < GamePanel.WIDTH - radius &&
            y > radius && y < GamePanel.HEIGHT - radius){
                ready = true;
            }
        }
        if(x < 0 && dx < 0) dx = - dx;
        if(x > GamePanel.WIDTH && dx > 0) dx = - dx;
        if(y < 0 && dy < 0) dy = -dy;
        if(y > GamePanel.HEIGHT && dy > 0) dy = -dy;
    }

    public void draw(Graphics2D g){
        g.setColor(color);
        g.fillOval((int)(x - radius), (int)(y - radius),2 * radius, 2 * radius);

        g.setStroke(new BasicStroke(3));
        g.setColor(color.darker());
        g.drawOval((int)(x - radius), (int)(y - radius),2 * radius, 2 * radius);
        g.setStroke(new BasicStroke(1));
    }
}
