package object;

import java.awt.image.BufferedImage;

public class Item {

    public String name;
    public BufferedImage image;
    public int amount;
    public int maxStack = 64;
    public int id;

    public Item(String name){
        this.name = name;
    }

}