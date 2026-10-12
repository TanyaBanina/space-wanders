package Main;
/**Клас відповідає за створення панелі гри зі всіма об'єктами*/
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JPanel;

import GameState.GameStateManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel
        implements Runnable, KeyListener{

    // параметри
    public static final int WIDTH = 420;
    public static final int HEIGHT = 240;
    public static final int SCALE = 2;

    // тред
    private Thread thread;
    private boolean running;
    private int FPS = 60;
    private long targetTime = 1000 / FPS;

    // зображення
    private BufferedImage image;
    private Graphics2D g;

    // керівник станами
    private GameStateManager gsm;

    public GamePanel() {
        super();
        setPreferredSize(
                new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        requestFocus();
    }

    public void addNotify() {
        super.addNotify();
        if(thread == null) {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    private void init() {
        image = new BufferedImage(
                WIDTH, HEIGHT,
                BufferedImage.TYPE_INT_RGB
        );
        g = (Graphics2D) image.getGraphics();

        running = true;

        gsm = new GameStateManager();
    }

    public void run() {
        init();
        long start;
        long elapsed;
        long wait;

        while(running) {

            start = System.nanoTime();

            update();
            draw();
            drawToScreen();

            elapsed = System.nanoTime() - start;

            wait = targetTime - elapsed / 1000000;
            if(wait < 0) wait = 5;

            try {
                Thread.sleep(wait);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        gsm.update();
    }
    private void draw() {
        gsm.draw(g);
    }
    private void drawToScreen() {
        Graphics g2 = getGraphics();
        g2.drawImage(image, 0, 0,
                WIDTH * SCALE, HEIGHT * SCALE,
                null);
        g2.dispose();
    }

    public void keyTyped(KeyEvent key) {}
    public void keyPressed(KeyEvent key) {
        try {
            gsm.keyPressed(key.getKeyCode());
        }catch (NullPointerException ex){

        }
    }
    public void keyReleased(KeyEvent key) {
        try {
            gsm.keyReleased(key.getKeyCode());
        }catch (NullPointerException ex){

        }
    }
}
















