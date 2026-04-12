package test;
import inventory.Item;

public class InventoryTest {

    public static void main(String[] args) {

        DummyGamePanel gp = new DummyGamePanel();

        TestPlayer player = new TestPlayer(gp);

        System.out.println("==== TEST START ====\n");

        player.pickUpItem(new Item("Item A"));
        player.pickUpItem(new Item("Item B"));
        player.pickUpItem(new Item("Item C"));
        player.pickUpItem(new Item("Item D"));
        player.pickUpItem(new Item("Item E"));

        System.out.println("\n==== INVENTORY FULL TEST ====");
        player.pickUpItem(new Item("Item F"));

        System.out.println("\n==== SWITCH TEST ====");
        player.nextItem();
        player.nextItem();
        player.previousItem();

        System.out.println("\n==== DROP TEST ====");
        player.dropItem(player.selectedItem);

        System.out.println("\n==== WORLD ITEMS ====");
        for (Item item : gp.itemList) {
            System.out.println(item.name + " at (" + item.worldX + "," + item.worldY + ")");
        }

        System.out.println("\n==== TEST END ====");
    }
}

