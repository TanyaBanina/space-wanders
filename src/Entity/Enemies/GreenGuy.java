package Entity.Enemies;
/**Клас відповідає за створення зеленого монстра*/
import Entity.*;
import TileMap.TileMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

public class GreenGuy extends Enemy {

    private BufferedImage[] sprites;

    public GreenGuy(TileMap tm) {

        super(tm);

        moveSpeed = 1;
        maxSpeed = 1.5;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        width = 48;
        height = 45;
        cwidth = 35;
        cheight = 30;

        health = maxHealth = 5;
        damage = 2;
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Enemies/GreenGuy.png"
                    )
            );
            sprites = new BufferedImage[3];
            for(int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(
                        i * width,
                        0,
                        width,
                        height
                );
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);

        right = true;
        facingRight = false;
    }

    private void getNextPosition() {
        if(left) {
            dx -= moveSpeed;
            if(dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        }
        else if(right) {
            dx += moveSpeed;
            if(dx > maxSpeed) {
                dx = maxSpeed;
            }
        }
        if(falling) {
            dy += fallSpeed;
        }
    }

    public void update() {
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        if(flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 200) {
                flinching = false;
            }
        }
        if(right && dx == 0) {
            right = false;
            left = true;
            facingRight = true;
        }
        else if(left && dx == 0) {
            right = true;
            left = false;
            facingRight = false;
        }
        animation.update();
    }

    public void draw(Graphics2D g) {
        setMapPosition();
        super.draw(g);
    }

}











