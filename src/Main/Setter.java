package Main;

import gamepanel.GamePanel;
import monster.Boss;
import monster.Egg;
import npc.Bro;
import object.Brick;

/**This class is used to load the objects, npc, and monsters into the game after the map loading.
 * 1. Add each entity to array.
 * 2. Set its position.
 * */
public class Setter {

    GamePanel gamePanel;

    public Setter(GamePanel gamePanel) {

        this.gamePanel = gamePanel;
    }

    // Set objects position: objects[mapNumber][index]
    public void setObject() {

        gamePanel.objects[0][0] = new Brick(gamePanel);
        gamePanel.objects[0][0].worldX = 35 * gamePanel.tileSize;
        gamePanel.objects[0][0].worldY = 37 * gamePanel.tileSize;
    }

    // Set npc initial position: npc[npcNumber][index]
    public void setNPC() {

        gamePanel.npc[0][0] = new Bro(gamePanel);
        gamePanel.npc[0][0].worldX = 35 * gamePanel.tileSize;
        gamePanel.npc[0][0].worldY = 40 * gamePanel.tileSize;
    }

    // Set monsters initial position: monster[monsterNumber][index]
    public void setMonster() {

        gamePanel.monsters[0][0] = new Egg(gamePanel);
        gamePanel.monsters[0][0].worldX = 35 * gamePanel.tileSize;
        gamePanel.monsters[0][0].worldY = 48 * gamePanel.tileSize;
    }

    // Set boss
    public void setBoss() {
        gamePanel.monsters[1][0] = new Boss(gamePanel);
        gamePanel.monsters[1][0].worldX = gamePanel.tileSize * 10;
        gamePanel.monsters[1][0].worldY = gamePanel.tileSize * 10;
    }
}
