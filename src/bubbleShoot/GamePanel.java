package bubbleShoot;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.*;
import java.util.Date;

public class GamePanel extends JPanel implements Runnable{

    //Field
    public static int WIDTH = 400;
    public static int HEIGHT = 400;

    private Thread thread;

    private BufferedImage image;
    private Graphics2D g;

    private int FPS;
    private double millisToFPS;
    private long timerFPS;
    private int sleepTime;

    public static GameBackground background;
    public static Player player;

    public static ArrayList<Bullet> bullets;
    public static ArrayList<Enemy> enemies;

    public static Wave wave;

    public static boolean running;

    //Constructor
    public GamePanel(){
        super();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();

        addKeyListener(new Listeners());

        running = true;
    }

    //Functions
    public void startThread(){
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        FPS = 30;
        millisToFPS = 1000 / FPS;
        sleepTime = 0;

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D)image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        background = new GameBackground();
        player = new Player();
        bullets = new ArrayList<Bullet>();
        enemies = new ArrayList<Enemy>();

        wave = new Wave();

        while (running){
            timerFPS = System.nanoTime();

            gameUpdate();
            gameRender();
            gameDraw();

            timerFPS = (System.nanoTime() - timerFPS)/1000000;
            if(millisToFPS > timerFPS){
                sleepTime = (int)(millisToFPS - timerFPS);
            }
            else sleepTime = 1;

            try {
                thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            timerFPS = 0;
            sleepTime = 1;
        }

        g.drawImage(GameBackground.image, 0, 0, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        String s = "G A M E   O V E R";
        int length = (int)g.getFontMetrics().getStringBounds(s,g).getWidth();
        g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2);
        s = "Score: " + player.getScore();
        length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
        g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 25);
        gameDraw();

        writeResToFile();
    }

    public void gameUpdate(){
        //Background update
        background.update();

        //Player update
        player.update();

        //Bullets update
        for(int i = 0; i < bullets.size(); i++){
            bullets.get(i).update();
            boolean remove = bullets.get(i).remove();
            if (remove){
                bullets.remove(i);
                i--;
            }
        }

        //Enemies update
        for(int i = 0; i < enemies.size(); i ++){
            enemies.get(i).update();
        }

        //Bullets-enemies collide
        for(int i = 0; i < enemies.size(); i ++){
            Enemy e = enemies.get(i);
            double ex = e.getX();
            double ey = e.getY();

            for(int j = 0; j < bullets.size(); j++){
                Bullet b = bullets.get(j);
                double bx = b.getX();
                double by = b.getY();

                double dx = ex - bx;
                double dy = ey - by;

                double dist = Math.sqrt(dx * dx + dy * dy);

                if((int)dist <=  e.getRadius() + b.getRadius()){
                    e.hit();
                    bullets.remove(j);
                    j--;
                    boolean remove = e.remove();
                    if(remove){
                        player.addScore(e.getType() + e.getRank());
                        enemies.remove(i);
                        i--;
                        break;
                    }

                }

            }

        }

        // check dead player
        if(player.isDead()){
            running = false;
        }

        // Player-Enemy collides
        if(!player.isRecovering()){
        for(int i = 0; i < enemies.size(); i ++){
            Enemy e = enemies.get(i);
            double ex = e.getX();
            double ey = e.getY();

            double px = player.getX();
            double py = player.getY();

            double dx = ex - px;
            double dy = ey - py;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if((int)dist <= e.getRadius() + player.getRadius()){
                e.hit();
                player.hit();
                boolean remove = e.remove();
                if(remove){
                    player.addScore(e.getType() + e.getRank());
                    enemies.remove(i);
                    i--;
                }
            }
        }
        }

        //Wave update
        wave.update();

    }

    public void gameRender(){
        //Background draw
        background.draw(g);

        // Player draw
        player.draw(g);

        //Bullets draw
        for(int i = 0; i < bullets.size(); i++){
            bullets.get(i).draw(g);
        }

        //Enemies draw
        for(int i = 0; i < enemies.size(); i ++){
            enemies.get(i).draw(g);
        }

        //Wave draw
        if(wave.showWave()){
            wave.draw(g);
        }

        //draw player lives
        for(int i = 0; i < player.getHealth(); i ++){
            g.setColor(Color.WHITE);
            g.fillOval((int) 20 + (20 * i), 20, player.getRadius() * 2, player.getRadius() * 2);
            g.setStroke(new BasicStroke(3));
            g.setColor(Color.pink.darker());
            g.drawOval((int) 20 + (20 * i), 20, player.getRadius() * 2, player.getRadius() * 2);
            g.setStroke(new BasicStroke(1));
        }

        // draw player score
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 14));
        g.drawString("Score: " + player.getScore(), WIDTH - 80, 30);

    }

    private void gameDraw(){
        Graphics g_graphWin = this.getGraphics();
        g_graphWin.drawImage(image, 0, 0, null);
        g_graphWin.dispose();
    }

    private void writeResToFile(){
        try(FileWriter writer = new FileWriter("statistics.txt", true))
        {
            Date date = new Date();
            writer.append("\r\n");
            writer.write(date.toString() + "    level: " + wave.getWaveNumber() + ", score: " + player.getScore());
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
