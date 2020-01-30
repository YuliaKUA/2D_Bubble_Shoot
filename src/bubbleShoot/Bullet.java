package bubbleShoot;

import java.awt.*;

public class Bullet {

    private double x, y;
    private int radius, speed;

    private Color color;

    //Constructor
    public Bullet(){
        x = GamePanel.player.getX();
        y = GamePanel.player.getY();
        radius = 2;

        speed = 15;

        color = Color.WHITE;
    }

    //Functions
    public double getX(){ return x;}
    public double getY(){ return y;}
    public int getRadius(){ return radius;}

    public boolean remove(){
        if(y < 0){
            return true;
        }

        return false;
    }

    public void update(){
        y -= speed;
    }

    public void draw(Graphics2D g){
        g.setColor(color);
        g.fillOval((int)x, (int)y, radius, 2*radius);
    }
}
