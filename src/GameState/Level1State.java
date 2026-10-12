package GameState;
/**Клас відповідає за створення стану гри Рівень 1*/
import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;
import Audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class Level1State extends GameState {

    private TileMap tileMap;
    private Background bg;
    private Player player;

    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;

    private HUD hud;
    HashMap<String, AudioPlayer> music;

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {

        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/Planet.png");
        tileMap.loadMap("/Maps/level1.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        bg = new Background("/Backgrounds/Planet1.jpg", 0.1);

        player = new Player(tileMap);
        player.setPosition(100, 100);
        player.levelkills = 4;
        player.setHealth(10);
        player.setMaxHealth(10);
        populateEnemies();

        explosions = new ArrayList<Explosion>();

        hud = new HUD(player);

        music = new HashMap<String, AudioPlayer>();
        music.put("lose", new AudioPlayer("/Music/lose.mp3"));
        music.put("win", new AudioPlayer("/Music/win.mp3"));
        music.put("walk", new AudioPlayer("/SFX/walk.mp3"));
    }

    private void populateEnemies() {

        enemies = new ArrayList<Enemy>();

        PurpleGuy s;
        Point[] points = new Point[] {
                new Point(860, 190),
                new Point(1560, 155),
                new Point(1660, 155),
                new Point(1790, 155)
        };
        for(int i = 0; i < points.length; i++) {
            s = new PurpleGuy(tileMap);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }

    }
    public void update() {
        if(player.isDead()){
            music.get("lose").play();
            player.kills = 0;
            gsm.setState(6);
        }

        if(player.isWinner()){
            music.get("win").play();
            gsm.setState(7);
            if(gsm.level!=5) gsm.level++;
        }
        player.update();
        tileMap.setPosition(
                GamePanel.WIDTH / 2 - player.getx(),
                GamePanel.HEIGHT / 2 - player.gety()
        );
        bg.setPosition(tileMap.getx(), tileMap.gety());
        player.checkAttack(enemies);
        for(int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();
            if(e.isDead()) {
                enemies.remove(i);
                player.kills++;
                i--;
                explosions.add(
                        new Explosion(e.getx(), e.gety()));
            }
        }
        for(int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if(explosions.get(i).shouldRemove()) {
                explosions.remove(i);
                i--;
            }
        }
    }

    public void draw(Graphics2D g) {
        bg.draw(g);

        tileMap.draw(g);

        player.draw(g);

        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        for(int i = 0; i < explosions.size(); i++) {
            explosions.get(i).setMapPosition(
                    (int)tileMap.getx(), (int)tileMap.gety());
            explosions.get(i).draw(g);
        }

        hud.draw(g);

    }

    public void keyPressed(int k) {
        if(k == KeyEvent.VK_LEFT) {music.get("walk").play(); player.setLeft(true);}
        if(k == KeyEvent.VK_RIGHT) player.setRight(true);
        if(k == KeyEvent.VK_UP) player.setUp(true);
        if(k == KeyEvent.VK_DOWN) player.setDown(true);
        if(k == KeyEvent.VK_W) player.setJumping(true);
        if(k == KeyEvent.VK_E) player.setGliding(true);
        if(k == KeyEvent.VK_R) player.setAttacking();
        if(k == KeyEvent.VK_F) player.setThrowing();
        if(k == KeyEvent.VK_ESCAPE) {player.kills = 0; gsm.setState(0);}
    }

    public void keyReleased(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) player.setRight(false);
        if(k == KeyEvent.VK_UP) player.setUp(false);
        if(k == KeyEvent.VK_DOWN) player.setDown(false);
        if(k == KeyEvent.VK_W) player.setJumping(false);
        if(k == KeyEvent.VK_E) player.setGliding(false);
    }

}












