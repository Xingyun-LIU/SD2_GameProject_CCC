package Main;

import gamepanel.GamePanel;
import monster.Bird1;
import monster.Boss;
import monster.Egg;
import npc.Bro;
import npc.Gan;
import object.Brick;

/**
 * This class is used to load the objects, npc, and monsters into the game after the map loading.
 * 1. Add each entity to array.
 * 2. Set its position.
 */
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

        gamePanel.npc[0][1] = new Gan(gamePanel);
        gamePanel.npc[0][1].worldX = 33 * gamePanel.tileSize;
        gamePanel.npc[0][1].worldY = 40 * gamePanel.tileSize;
    }

    // Set monsters initial position: monster[monsterNumber][index]
    public void setMonster() {

        // Map 0
        gamePanel.monsters[0][0] = new Egg(gamePanel);
        gamePanel.monsters[0][0].worldX = 35 * gamePanel.tileSize;
        gamePanel.monsters[0][0].worldY = 48 * gamePanel.tileSize;

//        gamePanel.monsters[0][1] = new Bird1(gamePanel);
//        gamePanel.monsters[0][1].worldX = 40 * gamePanel.tileSize;
//        gamePanel.monsters[0][1].worldY = 45 * gamePanel.tileSize;
//
//        gamePanel.monsters[0][2] = new Bird1(gamePanel);
//        gamePanel.monsters[0][2].worldX = 42 * gamePanel.tileSize;
//        gamePanel.monsters[0][2].worldY = 45 * gamePanel.tileSize;

        // Map 1
        gamePanel.monsters[1][0] = new Egg(gamePanel);
        gamePanel.monsters[1][0].worldX = 25 * gamePanel.tileSize;
        gamePanel.monsters[1][0].worldY = 8 * gamePanel.tileSize;

        gamePanel.monsters[1][1] = new Egg(gamePanel);
        gamePanel.monsters[1][1].worldX = 33 * gamePanel.tileSize;
        gamePanel.monsters[1][1].worldY = 8 * gamePanel.tileSize;

        gamePanel.monsters[1][2] = new Bird1(gamePanel);
        gamePanel.monsters[1][2].currentMap = 1;
        gamePanel.monsters[1][2].worldX = 22 * gamePanel.tileSize;
        gamePanel.monsters[1][2].worldY = 31 * gamePanel.tileSize;

        gamePanel.monsters[1][3] = new Bird1(gamePanel);
        gamePanel.monsters[1][3].currentMap = 1;
        gamePanel.monsters[1][3].worldX = 32 * gamePanel.tileSize;
        gamePanel.monsters[1][3].worldY = 31 * gamePanel.tileSize;

        gamePanel.monsters[1][4] = new Bird1(gamePanel);
        gamePanel.monsters[1][4].currentMap = 1;
        gamePanel.monsters[1][4].worldX = 40 * gamePanel.tileSize;
        gamePanel.monsters[1][4].worldY = 31 * gamePanel.tileSize;

        // Boss map
        gamePanel.monsters[2][0] = new Boss(gamePanel, 2);
        gamePanel.monsters[2][0].worldX = 32 * gamePanel.tileSize;
        gamePanel.monsters[2][0].worldY = 32 * gamePanel.tileSize;

    }
}
