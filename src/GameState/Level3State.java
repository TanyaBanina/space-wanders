package GameState;
/**Клас відповідає за створення стану гри Рівень 3*/
import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;
import Audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class Level3State extends GameState {

    private TileMap tileMap;
    private Background bg;

    private Player player;

    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;

    private HUD hud;
    HashMap<String, AudioPlayer> music;

    public Level3State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {
        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/Cave.png");
        tileMap.loadMap("/Maps/level3.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        bg = new Background("/Backgrounds/Cave.jpg", 0.1);

        player = new Player(tileMap);
        player.setPosition(100, 100);
        player.levelkills = 12;
        player.setHealth(20);
        player.setMaxHealth(20);
        populateEnemies();

        explosions = new ArrayList<Explosion>();

        hud = new HUD(player);

        music = new HashMap<String, AudioPlayer>();
        music.put("lose", new AudioPlayer("/Music/lose.mp3"));
        music.put("win", new AudioPlayer("/Music/win.mp3"));

    }

    private void populateEnemies() {

        enemies = new ArrayList<Enemy>();
        VioletGuy v; Shroom s;
        Point[] points1 = new Point[] {
                new Point(3145, 158),
                new Point(3045, 158),
                new Point(1479, 174),
                new Point(1417, 152),
                new Point(1531, 154)
        };
        Point[] points2 = new Point[] {
                new Point(3690, 108),
                new Point(3790, 105),
                new Point(3753, 105),
                new Point(1796,159),
                new Point(1830,159),
                new Point(981, 155),
                new Point(1056, 161)
        };
        for(int i = 0; i < points1.length; i++) {
            v = new VioletGuy(tileMap);
            v.setPosition(points1[i].x, points1[i].y);
            enemies.add(v);
        }
        for(int i = 0; i < points2.length; i++) {
            s = new Shroom(tileMap);
            s.setPosition(points2[i].x, points2[i].y);
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
                explosions.add(new Explosion(e.getx(), e.gety()));
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
        if(k == KeyEvent.VK_LEFT) player.setLeft(true);
        if(k == KeyEvent.VK_RIGHT) player.setRight(true);
        if(k == KeyEvent.VK_UP) player.setUp(true);
        if(k == KeyEvent.VK_DOWN) player.setDown(true);
        if(k == KeyEvent.VK_W) player.setJumping(true);
        if(k == KeyEvent.VK_E) player.setGliding(true);
        if(k == KeyEvent.VK_R) player.setAttacking();
        if(k == KeyEvent.VK_F) player.setThrowing();
        if(k == KeyEvent.VK_ESCAPE) { player.kills = 0; gsm.setState(0);}
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












