package test;

import inventory.Item;

import java.util.ArrayList;

class DummyGamePanel {
    public ArrayList<Item> itemList = new ArrayList<>();
}


class TestPlayer {

    public Item[] inventory = new Item[5];
    public int selectedItem = 0;

    public int worldX = 100;
    public int worldY = 100;

    DummyGamePanel gamePanel;

    public TestPlayer(DummyGamePanel gp) {
        this.gamePanel = gp;
    }

    // =============================
    // 拾取
    // =============================
    public void pickUpItem(Item item) {

        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                inventory[i] = item;
                System.out.println("Picked up: " + item.name);
                printInventory();
                return;
            }
        }

        System.out.println("Inventory full! Replacing selected slot...");

        dropItem(selectedItem);

        inventory[selectedItem] = item;

        System.out.println("Picked up (replaced): " + item.name);
        printInventory();
    }

    // =============================
    // 丢弃
    // =============================
    public void dropItem(int index) {

        if (inventory[index] == null) return;

        Item dropped = inventory[index];

        dropped.worldX = this.worldX;
        dropped.worldY = this.worldY;

        gamePanel.itemList.add(dropped);

        System.out.println("Dropped: " + dropped.name);

        inventory[index] = null;

        printInventory();
    }

    // =============================
    // 切换
    // =============================
    public void nextItem() {
        selectedItem = (selectedItem + 1) % inventory.length;
        System.out.println("Selected: " + selectedItem);
    }

    public void previousItem() {
        selectedItem = (selectedItem - 1 + inventory.length) % inventory.length;
        System.out.println("Selected: " + selectedItem);
    }

    // =============================
    // 打印
    // =============================
    public void printInventory() {

        System.out.print("Inventory: ");

        for (Item item : inventory) {
            if (item == null) {
                System.out.print("[ ] ");
            } else {
                System.out.print("[" + item.name + "] ");
            }
        }

        System.out.println(" | Selected: " + selectedItem);
    }
}

