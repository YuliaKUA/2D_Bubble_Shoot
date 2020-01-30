package bubbleShoot;

import java.awt.*;

public class Wave {
    //Fields
    private int waveNumber;
    private int waveMultiplier;

    private long waveTimer;
    private long waveDelay;
    private long waveTimerDiff;

    private String waveText;

    //Constructor
    public Wave(){
        waveNumber = 1;
        waveMultiplier = 5;

        waveTimer = 0;
        waveDelay = 5000;
        waveTimerDiff = 0;

        waveText = " W A V E -";
    }

    //Functions
    public int getWaveNumber(){return waveNumber;}

    public void createEnemies(){
        int enemyCount = waveNumber * waveMultiplier;
        if(waveNumber < 2){
            while (enemyCount > 0){
                int type = 1;
                int rank = 1;
                GamePanel.enemies.add(new Enemy(type, rank));
                enemyCount -= type * rank;
            }
            GamePanel.enemies.add(new Enemy(3, 1));
        } else if (waveNumber >= 2 && waveNumber <= 4){
            while (enemyCount > 0){
                int type = 1;
                int rank = 2;
                GamePanel.enemies.add(new Enemy(type, rank));
                enemyCount -= type * rank;
            }
            GamePanel.enemies.add(new Enemy(2, 1));
            GamePanel.enemies.add(new Enemy(2, 1));
        } else if( waveNumber > 4){
            for(int i = 0; i < enemyCount / 4; i++){
                int type = 3;
                int rank = 1;
                GamePanel.enemies.add(new Enemy(type, rank));
            }
            for(int i = 0; i < enemyCount / 4; i++){
                int type = 2;
                int rank = 1;
                GamePanel.enemies.add(new Enemy(type, rank));
            }
        }
        waveNumber++;
    }

    public void update(){
        if(GamePanel.enemies.isEmpty() && waveTimer == 0){
            waveTimer = System.nanoTime();
        }

        if(waveTimer > 0){
            waveTimerDiff += (System.nanoTime() - waveTimer) / 1000000;
            waveTimer = System.nanoTime();
        }

        if(waveTimerDiff > waveDelay){
            createEnemies();
            waveTimer = 0;
            waveTimerDiff = 0;
        }
    }
    public boolean showWave(){
        if(waveTimer != 0){
            return true;
        }
        else return false;
    }

    public void draw(Graphics2D g){
        double divider = waveDelay / 180;
        double alpha = waveTimerDiff / divider;
        alpha = 255 * Math.sin(Math.toRadians(alpha));

        if(alpha < 0) alpha = 0;
        if(alpha > 255) alpha = 255;

        g.setFont(new Font("TimesRoman", Font.ITALIC, 20));
        g.setColor(new Color(255, 255, 255,(int) alpha));

        String s = "- " + waveNumber + " " + waveText;
        long len = (int)g.getFontMetrics().getStringBounds(s,g).getWidth();

        g.drawString(s, GamePanel.WIDTH/2 - len /2, GamePanel.HEIGHT/2);
    }
}
