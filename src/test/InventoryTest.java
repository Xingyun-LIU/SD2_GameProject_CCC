package test;
import inventory.Item;

public class InventoryTest {

    public static void main(String[] args) {

        //create dummy game panel
        DummyGamePanel gp = new DummyGamePanel();

        // create test player
        TestPlayer player = new TestPlayer(gp);

        System.out.println("==== TEST START ====\n");

        //normal pick up
       // player.pickUpItem(new Item("Item A"));
       // player.pickUpItem(new Item("Item B"));
       // player.pickUpItem(new Item("Item C"));
       // player.pickUpItem(new Item("Item D"));
        //player.pickUpItem(new Item("Item E"));

        //if inventory full, should replace selected item
        System.out.println("\n==== INVENTORY FULL TEST ====");
        //player.pickUpItem(new Item("Item F"));

        //check inventory after replacement
        System.out.println("\n==== SWITCH TEST ====");
        player.nextItem();
        player.nextItem();
        player.previousItem();

        //drop item test
        System.out.println("\n==== DROP TEST ====");
        player.dropItem(player.selectedItem);

        // check inventory after dropping
        System.out.println("\n==== WORLD ITEMS ====");
        for (Item item : gp.itemList) {
            System.out.println(item.name + " at (" + item.worldX + "," + item.worldY + ")");
        }

        System.out.println("\n==== TEST END ====");
    }
}

