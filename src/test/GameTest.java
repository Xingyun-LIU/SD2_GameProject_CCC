package test;

import Entity.Entity;
import Entity.Player;
import Main.Camera;
import Main.CollisionChecker;
import Main.EventChecker;
import gamepanel.GamePanel;
import org.junit.jupiter.api.*;
import tile.Tile;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameTest {

    static VirtualGamePanel gamePanel;

    /**The current environment does not have a monitor, keyboard, or mouse;
     *  please do not attempt to access these hardware resources.*/
    @BeforeAll
    static void setUpEnvironment() {
        System.setProperty("java.awt.headless", "true");
        gamePanel = new VirtualGamePanel();
        gamePanel.buildVirtualGame();
    }

    @BeforeEach
    void resetGame() {
        gamePanel.buildVirtualGame();
    }

    private static KeyEvent keyEvent(int keyCode) {
        return new KeyEvent(
                gamePanel,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                keyCode,
                KeyEvent.CHAR_UNDEFINED
        );
    }

    static class VirtualGamePanel extends GamePanel {

        VirtualGamePanel() {
            super(null);
        }

        @Override
        public void startThread() {
            gameThread = null;
        }

        void buildVirtualGame() {
            buildMap();

            collisionChecker = new CollisionChecker(this);
            eventChecker = new EventChecker(this);

            objects = new Entity[totalMap][20];
            npc = new Entity[totalMap][20];
            monsters = new Entity[totalMap][20];

            player1 = new VirtualPlayer(this, true);
            player2 = new VirtualPlayer(this, false);

            objects[0][0] = new VirtualEntity(this);
            objects[0][0].worldX = 35 * tileSize;
            objects[0][0].worldY = 37 * tileSize;
            objects[0][0].collision = true;

            npc[0][0] = new VirtualEntity(this);
            npc[0][0].worldX = 35 * tileSize;
            npc[0][0].worldY = 40 * tileSize;

            monsters[0][0] = new VirtualMonster(this);
            monsters[0][0].worldX = 35 * tileSize;
            monsters[0][0].worldY = 48 * tileSize;

            gameState = playing;
            currentMap = 0;

            key.w = false;
            key.a = false;
            key.s = false;
            key.d = false;
            key.up = false;
            key.down = false;
            key.left = false;
            key.right = false;
            key.j = false;
            key.numpad1 = false;
        }

        private void buildMap() {
            tileManager.tiles = new Tile[30];
            tileManager.mapTileNum = new int[totalMap][worldCol][worldRow];

            for (int i = 0; i < tileManager.tiles.length; i++) {
                tileManager.tiles[i] = new Tile();
                tileManager.tiles[i].collision = true;
                tileManager.tiles[i].image = virtualImage(tileSize, tileSize);
            }

            for (int m = 0; m < totalMap; m++) {
                for (int col = 0; col < worldCol; col++) {
                    for (int row = 0; row < worldRow; row++) {
                        tileManager.mapTileNum[m][col][row] = 0;
                    }
                }
            }
        }
    }

    static class VirtualPlayer extends Player {

        VirtualPlayer(GamePanel gp, boolean p1) {
            super(gp, p1);
            maxLife = 10;
            life = 10;
        }

        @Override
        public void getImage() {
            up1 = virtualImage(gamePanel.tileSize, gamePanel.tileSize);
            up2 = virtualImage(gamePanel.tileSize, gamePanel.tileSize);
            down1 = virtualImage(gamePanel.tileSize, gamePanel.tileSize);
            down2 = virtualImage(gamePanel.tileSize, gamePanel.tileSize);
            left1 = virtualImage(gamePanel.tileSize, gamePanel.tileSize);
            left2 = virtualImage(gamePanel.tileSize, gamePanel.tileSize);
            right1 = virtualImage(gamePanel.tileSize, gamePanel.tileSize);
            right2 = virtualImage(gamePanel.tileSize, gamePanel.tileSize);
        }

        @Override
        public void getAttackImage() {
            upAttack1 = virtualImage(gamePanel.tileSize, gamePanel.tileSize * 2);
            upAttack2 = virtualImage(gamePanel.tileSize, gamePanel.tileSize * 2);
            downAttack1 = virtualImage(gamePanel.tileSize, gamePanel.tileSize * 2);
            downAttack2 = virtualImage(gamePanel.tileSize, gamePanel.tileSize * 2);
            leftAttack1 = virtualImage(gamePanel.tileSize * 2, gamePanel.tileSize);
            leftAttack2 = virtualImage(gamePanel.tileSize * 2, gamePanel.tileSize);
            rightAttack1 = virtualImage(gamePanel.tileSize * 2, gamePanel.tileSize);
            rightAttack2 = virtualImage(gamePanel.tileSize * 2, gamePanel.tileSize);
        }

        boolean testIsAttacking() {
            return attacking;
        }
    }

    static class VirtualEntity extends Entity {

        VirtualEntity(GamePanel gp) {
            super(gp);
            down1 = virtualImage(gp.tileSize, gp.tileSize);
            up1 = down1;
            left1 = down1;
            right1 = down1;
        }
    }

    static class VirtualMonster extends VirtualEntity {

        VirtualMonster(GamePanel gp) {
            super(gp);
            type = 2;
            maxLife = 3;
            life = 3;
        }
    }

    private static BufferedImage virtualImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
}
