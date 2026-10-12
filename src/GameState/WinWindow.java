package GameState;
/**Клас відповідає за створення вікна виграшу*/
import Main.GamePanel;
import TileMap.Background;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
public class WinWindow extends GameState{
    private Background bg;
    private BufferedImage win;

    private int currentChoice = 7;

    public WinWindow(GameStateManager gsm) {
        this.gsm = gsm;
        try {
            if(gsm.level == 1)
                bg = new Background("/Backgrounds/Planet1.jpg", 1);
            else if(gsm.level == 2)
                bg = new Background("/Backgrounds/Jungle.gif", 1);
            else if(gsm.level == 3)
                bg = new Background("/Backgrounds/Cave.jpg", 1);
            else if(gsm.level == 4)
                bg = new Background("/Backgrounds/Desert.jpg", 1);
            else if(gsm.level == 5)
                bg = new Background("/Backgrounds/Hell.gif", 1);
            bg.setVector(-0.1, 0);
            win = ImageIO.read(
                    getClass().getResourceAsStream("/Backgrounds/Win.png")
            );
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void update(){bg.update();}
    public void init(){}

    public void draw(Graphics2D g) {
        bg.draw(g);
        g.drawImage(
                win,
                0,0,
                GamePanel.WIDTH,
                GamePanel.HEIGHT,
                null
        );
    }

    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ENTER) { gsm.setState(0);}
    }

    public void keyReleased(int k) {
    }



}


