package inventory;

public class Inventory {
    public String inventory;
        public Item[] items = new Item[5];
        public int selectedItem = 0;

        public void pickUpItem(Item item) {

            for (int i = 0; i < items.length; i++) {
                if (items[i] == null) {
                    items[i] = item;
                    System.out.println("Picked up: " + item.name);
                    printInventory();
                    return;
                }
            }

            System.out.println("Inventory full! Replacing selected slot...");

            dropItem(selectedItem);

            items[selectedItem] = item;

            System.out.println("Picked up (replaced): " + item.name);
            printInventory();
        }

        public void dropItem(int index) {

            if (items[index] == null) return;

            Item dropped = items[index];

            // Here you would set the dropped item's world position to the player's position
            // For example:
            // dropped.worldX = player.worldX;
            // dropped.worldY = player.worldY;

            System.out.println("Dropped: " + dropped.name);
            items[index] = null;
            printInventory();
        }

        public void printInventory() {
            System.out.println("Inventory:");
            for (int i = 0; i < items.length; i++) {
                String itemName = (items[i] != null) ? items[i].name : "Empty";
                String selectedMarker = (i == selectedItem) ? " <==" : "";
                System.out.println("Slot " + i + ": " + itemName + selectedMarker);
            }
        }
}
