package Entity;
/**Клас відповідає за створення ХАДу з показниками здоров'я, каменів та монстрів*/
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class HUD {

    private Player player;

    private BufferedImage image;
    private Font font;

    public HUD(Player p) {
        player = p;
        try {
            image = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/HUD/hud.png"
                    )
            );
            font = new Font("Arial", Font.BOLD, 14);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g) {

        g.drawImage(image, 0, 10, null);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(
                player.getHealth() + "/" + player.getMaxHealth(),
                30,
                25
        );
        g.drawString(
                player.getStone() / 100 + "/" + player.getMaxStone() / 100,
                30,
                45
        );
        g.drawString(
                player.kills+ "/" + player.levelkills,
                30,
                66
        );

    }

}













