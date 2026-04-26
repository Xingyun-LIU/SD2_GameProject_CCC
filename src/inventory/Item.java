package inventory;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class Item {
    public String name;
    public BufferedImage image;

    public int worldX;
    public int worldY;
    public Item(String name, BufferedImage image) {
        this.name = name;
        this.image = image;
    }

    public Item() {
    }

}

