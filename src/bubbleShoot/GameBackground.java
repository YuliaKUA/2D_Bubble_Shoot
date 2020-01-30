package bubbleShoot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameBackground {
    //Fields
    //private Color color;

    public String filename = "back.jpg";
    public static Image image ;

    //Constructor
    public GameBackground()  {
        //color = Color.BLUE;
        try
        {
            image = ImageIO.read(new File("src/images/back.jpg"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    //Functions
    public void update(){

    }

    public void draw(Graphics2D g){
//        g.setColor(color);
//        g.fillRect(0,0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g.drawImage(image, 0, 0, null);
    }

}
