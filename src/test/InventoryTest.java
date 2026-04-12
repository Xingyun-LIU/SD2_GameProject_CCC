package test;
import inventory.Item;

public class InventoryTest {

    public static void main(String[] args) {

        // 创建一个假的 GamePanel（只需要 itemList）
        DummyGamePanel gp = new DummyGamePanel();

        // 创建玩家
        TestPlayer player = new TestPlayer(gp);

        System.out.println("==== TEST START ====\n");

        // ===== 测试 1：正常拾取 =====
        player.pickUpItem(new Item("Item A"));
        player.pickUpItem(new Item("Item B"));
        player.pickUpItem(new Item("Item C"));
        player.pickUpItem(new Item("Item D"));
        player.pickUpItem(new Item("Item E"));

        // ===== 测试 2：背包满 =====
        System.out.println("\n==== INVENTORY FULL TEST ====");
        player.pickUpItem(new Item("Item F")); // 应触发替换

        // ===== 测试 3：切换物品 =====
        System.out.println("\n==== SWITCH TEST ====");
        player.nextItem();
        player.nextItem();
        player.previousItem();

        // ===== 测试 4：丢弃 =====
        System.out.println("\n==== DROP TEST ====");
        player.dropItem(player.selectedItem);

        // ===== 查看地面物品 =====
        System.out.println("\n==== WORLD ITEMS ====");
        for (Item item : gp.itemList) {
            System.out.println(item.name + " at (" + item.worldX + "," + item.worldY + ")");
        }

        System.out.println("\n==== TEST END ====");
    }
}

