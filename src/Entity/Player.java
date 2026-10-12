package Entity;
/**Клас відповідає за створення гравця*/
import TileMap.*;
import Audio.AudioPlayer;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Player extends MapObject {
    // параметри гравця
    private int health;
    private int maxHealth;
    private int stone;
    public int kills;
    public int levelkills;
    private int maxStone;
    private boolean flinching;

    //камені
    private boolean throwing;
    private boolean win;
    private int throwCost;
    private int throwDamage;
    private ArrayList<Stone> stoneBalls;

    // атака
    private boolean attacking;
    private int attackDamage;
    private int attackRange;

    // політ
    private boolean gliding;

    //анімація
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {4,6,8,4,4,6,7};

    // дії гравця
    private static final int THROW = 0;
    private static final int WALK = 1;
    private static final int JUMP = 2;
    private static final int IDLE = 3;
    private static final int HURT = 4;
    private static final int ATTACK = 5;
    private static final int DIE = 6;
    private TileMap tm;
    private HashMap<String, AudioPlayer> sfx;

    public Player(TileMap tm) {

        super(tm);
        this.tm = tm;
        width = 32;
        height = 31;
        cwidth = 32;
        cheight = 30;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;
        stone = maxStone = 1000;

        throwCost = 100;
        throwDamage = 4;
        stoneBalls = new ArrayList<Stone>();

        attackDamage = 2;
        attackRange = 40;

        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Player/Hero.png"
                    )
            );

            sprites = new ArrayList<BufferedImage[]>();
            for(int i = 0; i < 7; i++) {

                BufferedImage[] bi =
                        new BufferedImage[numFrames[i]];

                for(int j = 0; j < numFrames[i]; j++) {

                    if(i != ATTACK) {
                        bi[j] = spritesheet.getSubimage(
                                j * width,
                                i * height,
                                width,
                                height
                        );
                    }
                    else {
                        bi[j] = spritesheet.getSubimage(
                                j * (width+2),
                                i * height,
                                (width+2),
                                height
                        );
                    }
                }
                sprites.add(bi);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(200);

        sfx = new HashMap<String, AudioPlayer>();
        sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
        sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));
        sfx.put("hurt", new AudioPlayer("/SFX/hurt.mp3"));
        sfx.put("walk", new AudioPlayer("/SFX/walk.mp3"));
        sfx.put("throw", new AudioPlayer("/SFX/throw.mp3"));
        sfx.put("fly", new AudioPlayer("/SFX/throw.mp3"));

    }
    public boolean isWinner(){return win;}
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getStone() { return stone; }
    public int getMaxStone() { return maxStone; }

    public void setHealth(int health) { this.health = health; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    public void setThrowing() {
        throwing = true;
    }
    public void setAttacking() {
        attacking = true;
    }
    public void setGliding(boolean b) {
        gliding = b;
    }

    public void checkAttack(ArrayList<Enemy> enemies) {
        for(int i = 0; i < enemies.size(); i++) {

            Enemy e = enemies.get(i);
            if(attacking) {
                if(facingRight) {
                    if(
                            e.getx() > x &&
                                    e.getx() < x + attackRange &&
                                    e.gety() > y - height / 2 &&
                                    e.gety() < y + height / 2
                    ) {
                        e.hit(attackDamage);
                    }
                }
                else {
                    if(
                            e.getx() < x &&
                                    e.getx() > x - attackRange &&
                                    e.gety() > y - height / 2 &&
                                    e.gety() < y + height / 2
                    ) {
                        e.hit(attackDamage);
                    }
                }
            }
            for(int j = 0; j < stoneBalls.size(); j++) {
                if(stoneBalls.get(j).intersects(e)) {
                    e.hit(throwDamage);
                    stoneBalls.get(j).setHit();
                    break;
                }
            }
           if(intersects(e)) {
                hit(e.getDamage());
           }
        }
    }
    public void hit(int damage) {
        if(flinching) return;
        health -= damage;
        if(health < 0) health = 0;
        if(health == 0)die = true;
        attacking = false;
        left = false;
        right = false;
        throwing = false;
        jumping = false;
        gliding = false;
        flinching = true;
    }

    private void getNextPosition() {
        if(currentAction == DIE) {
            dx = 0;
            attacking = false;
            flinching = false;
            left = false;
            right = false;
            throwing = false;
            jumping = false;
            gliding = false;
        }
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
        else {
            if(dx > 0) {
                dx -= stopSpeed;
                if(dx < 0) {
                    dx = 0;
                }
            }
            else if(dx < 0) {
                dx += stopSpeed;
                if(dx > 0) {
                    dx = 0;
                }
            }
        }
        if((currentAction == ATTACK || currentAction == THROW) && !(jumping || falling)) {
            dx = 0;
        }
        if(jumping && !falling) {
            sfx.get("jump").play();
            dy = jumpStart;
            falling = true;
        }
        if(falling) {
            if(dy > 0 && gliding) dy += fallSpeed * 0.1;
            else dy += fallSpeed;

            if(dy > 0) jumping = false;
            if(dy < 0 && !jumping) dy += stopJumpSpeed;

            if(dy > maxFallSpeed) dy = maxFallSpeed;
        }
    }

    public void update() {
        if(!dead) {
            if (currentAction == DIE) {
                if (animation.hasPlayedOnce()) {
                    this.setDead();
                    die = false;
                    return;
                }
            }
            if (currentAction == HURT) {
                if (animation.hasPlayedOnce()) flinching = false;
            }
            if (reached() && kills == levelkills) {
                win = true;
                return;
            }
            getNextPosition();
            checkTileMapCollision();
            setPosition(xtemp, ytemp);
            for(int j = 0; j < stoneBalls.size(); j++) {
                if (stoneBalls.get(j).getx() > tm.getWidth() - width) {
                    stoneBalls.remove(j);
                    break;
                }
            }
            if (currentAction == ATTACK) {
                if (animation.hasPlayedOnce()) attacking = false;
            }
            if (currentAction == THROW) {
                if (animation.hasPlayedOnce()) throwing = false;
            }

            if (currentAction == JUMP) {
                if (animation.hasPlayedOnce()) jumping = false;
            }
            stone += 1;
            if (stone > maxStone) stone = maxStone;
            if (throwing && currentAction != THROW) {
                if (stone > throwCost) {
                    stone -= throwCost;
                    Stone s = new Stone(tileMap, facingRight);
                    s.setPosition(x, y);
                    stoneBalls.add(s);
                }
            }
            for (int i = 0; i < stoneBalls.size(); i++) {
                stoneBalls.get(i).update();
                if (stoneBalls.get(i).shouldRemove()) {
                    stoneBalls.remove(i);
                    i--;
                }
            }
            if (attacking) {
                if (currentAction != ATTACK) {
                    sfx.get("scratch").play();
                    currentAction = ATTACK;
                    animation.setFrames(sprites.get(ATTACK));
                    animation.setDelay(80);
                    width = 34;
                }
            } else if (throwing) {
                if (currentAction != THROW) {
                    sfx.get("throw").play();
                    currentAction = THROW;
                    animation.setFrames(sprites.get(THROW));
                    animation.setDelay(100);
                    width = 32;
                }
            } else if (dy > 0) {
                if (gliding) {
                    if (currentAction != JUMP) {
                        currentAction = JUMP;
                        animation.setFrames(sprites.get(JUMP));
                        animation.setDelay(100);
                        width = 32;
                    }
                } else if (currentAction != JUMP) {
                    currentAction = JUMP;
                    animation.setFrames(sprites.get(JUMP));
                    animation.setDelay(100);
                    width = 32;
                }
            } else if (dy < 0) {
                if (currentAction != JUMP) {
                    currentAction = JUMP;
                    animation.setFrames(sprites.get(JUMP));
                    animation.setDelay(100);
                    width = 32;
                }
            } else if (y > 200 || die) {
                if (currentAction != DIE) {
                    currentAction = DIE;
                    animation.setFrames(sprites.get(DIE));
                    animation.setDelay(100);
                    width = 32;
                }
            } else if (left || right) {
                if (currentAction != WALK) {
                    sfx.get("walk").play();
                    currentAction = WALK;
                    animation.setFrames(sprites.get(WALK));
                    animation.setDelay(40);
                    width = 32;
                }
            } else if (flinching) {
                if (currentAction != HURT) {
                    sfx.get("hurt").play();
                    currentAction = HURT;
                    animation.setFrames(sprites.get(HURT));
                    animation.setDelay(20);
                    width = 32;
                }
            } else {
                if (currentAction != IDLE) {
                    currentAction = IDLE;
                    animation.setFrames(sprites.get(IDLE));
                    animation.setDelay(200);
                    width = 32;
                }
            }
            animation.update();
            if (currentAction != ATTACK && currentAction != THROW) {
                if (right) facingRight = true;
                if (left) facingRight = false;
            }
        }
    }

    public void draw(Graphics2D g) {
        if(!isDead()) {
            setMapPosition();
            for(int i = 0; i < stoneBalls.size(); i++) {
            stoneBalls.get(i).draw(g);
            }
            super.draw(g);
        }
    }
}

















