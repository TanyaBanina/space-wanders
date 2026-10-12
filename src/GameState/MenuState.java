package GameState;
/**Клас відповідає за створення стану гри Меню*/
import Audio.AudioPlayer;
import TileMap.Background;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {
    private Background bg;
    private AudioPlayer bgMusic;

    private int currentChoice = 0;
    private String[] options = {
            "Start",
            "Restart",
            "Quit"
    };
    private Font titleFont;
    private Font font;
    public MenuState(GameStateManager gsm) {
        this.gsm = gsm;

        try {

            bg = new Background("/Backgrounds/Planet.png", 1);
            bg.setVector(-0.1, 0);
            titleFont = new Font(
                    "Century Gothic",
                    Font.BOLD,
                    28);

            font = new Font("Arial", Font.PLAIN, 15);
            bgMusic = new AudioPlayer("/Music/level1.mp3");
            bgMusic.play();
            bgMusic.loop();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {}

    public void update() {
        bg.update();
    }

    public void draw(Graphics2D g) {
        bg.draw(g);
        g.setColor(Color.WHITE);
        g.setFont(titleFont);
        g.drawString("Space Wanders", 105 , 70);
        g.setFont(font);
        for(int i = 0; i < options.length; i++) {
            if(i == currentChoice) {
                g.setColor(Color.CYAN);
            }
            else {
                g.setColor(Color.WHITE);
            }
            g.drawString(options[i], 190, 120 + i * 15);
        }
    }

    private void select() {
        bgMusic.stop();
        if(currentChoice == 0) {
            if(gsm.level == 1)
                gsm.setState(GameStateManager.LEVEL1STATE);
            else if(gsm.level == 2)
                gsm.setState(GameStateManager.LEVEL2STATE);
            else if(gsm.level == 3)
                gsm.setState(GameStateManager.LEVEL3STATE);
            else if(gsm.level == 4)
                gsm.setState(GameStateManager.LEVEL4STATE);
            else if(gsm.level == 5)
                gsm.setState(GameStateManager.LEVEL5STATE);
            else
                gsm.setState(GameStateManager.MENUSTATE);
        }
        if(currentChoice == 1) {
            gsm.level = 1;
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if(currentChoice == 2) {
            System.exit(0);
        }
    }

    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ENTER){
            select();
        }
        if(k == KeyEvent.VK_UP) {
            currentChoice--;
            if(currentChoice == -1) {
                currentChoice = options.length - 1;
            }
        }
        if(k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if(currentChoice == options.length) {
                currentChoice = 0;
            }
        }
    }
    public void keyReleased(int k) {}
}










