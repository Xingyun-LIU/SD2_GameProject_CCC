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

    @Test
    void testVirtualGameCreated() {
        assertNotNull(gamePanel);
        assertNotNull(gamePanel.key);
        assertNotNull(gamePanel.player1);
        assertNotNull(gamePanel.player2);
        assertNotNull(gamePanel.collisionChecker);
        assertNotNull(gamePanel.eventChecker);

        assertEquals(gamePanel.playing, gamePanel.gameState);
        assertEquals(0, gamePanel.currentMap);
    }

    @Test
    void testGamePanelConstants() {
        assertEquals(48, gamePanel.tileSize);
        assertEquals(30, gamePanel.screenCol);
        assertEquals(18, gamePanel.screenRow);
        assertEquals(1440, gamePanel.screenWidth);
        assertEquals(864, gamePanel.screenHeight);
        assertEquals(64, gamePanel.worldCol);
        assertEquals(64, gamePanel.worldRow);
        assertEquals(10, gamePanel.totalMap);
    }

    @Test
    void testPlayerDefaultPosition() {
        assertEquals(gamePanel.tileSize * 30, gamePanel.player1.worldX);
        assertEquals(gamePanel.tileSize * 47, gamePanel.player1.worldY);

        assertEquals(gamePanel.tileSize * 37, gamePanel.player2.worldX);
        assertEquals(gamePanel.tileSize * 47, gamePanel.player2.worldY);
    }

    @Test
    void testKeyHandlerStateSwitch() {
        gamePanel.gameState = gamePanel.playing;

        gamePanel.key.keyPressed(keyEvent(KeyEvent.VK_ESCAPE));
        assertEquals(gamePanel.pausing, gamePanel.gameState);

        gamePanel.gameState = gamePanel.playing;
        gamePanel.key.keyPressed(keyEvent(KeyEvent.VK_M));
        assertEquals(gamePanel.mapState, gamePanel.gameState);

        gamePanel.key.keyPressed(keyEvent(KeyEvent.VK_M));
        assertEquals(gamePanel.playing, gamePanel.gameState);
    }

    @Test
    void testPlayer1MoveRight() {
        int startX = gamePanel.player1.worldX;

        gamePanel.key.d = true;
        gamePanel.player1.update(gamePanel.key);

        assertEquals(startX + gamePanel.player1.speed, gamePanel.player1.worldX);
    }

    @Test
    void testPlayer2MoveUp() {
        int startY = gamePanel.player2.worldY;

        gamePanel.key.up = true;
        gamePanel.player2.update(gamePanel.key);

        assertEquals(startY - gamePanel.player2.speed, gamePanel.player2.worldY);
    }

    @Test
    void testPlayerAttack() {
        gamePanel.key.j = true;
        gamePanel.player1.update(gamePanel.key);
        assertTrue(((VirtualPlayer) gamePanel.player1).testIsAttacking());

        gamePanel.buildVirtualGame();

        gamePanel.key.numpad1 = true;
        gamePanel.player2.update(gamePanel.key);
        assertTrue(((VirtualPlayer) gamePanel.player2).testIsAttacking());
    }

    @Test
    void testCameraFollow() {
        Camera cam = new Camera(gamePanel);

        gamePanel.player1.worldX = gamePanel.tileSize * 30;
        gamePanel.player1.worldY = gamePanel.tileSize * 47;

        cam.follow(gamePanel.player1);

        int expectedX = gamePanel.player1.worldX - gamePanel.screenWidth / 4 + gamePanel.tileSize / 2;
        int expectedY = gamePanel.player1.worldY - gamePanel.screenHeight / 2 + gamePanel.tileSize / 2;

        assertEquals(expectedX, cam.x);
        assertEquals(expectedY, cam.y);
    }

    @Test
    void testCheckPlayerCollision() {
        CollisionChecker checker = new CollisionChecker(gamePanel);

        Entity a = new Entity(gamePanel);
        Entity b = new Entity(gamePanel);

        a.worldX = 100;
        a.worldY = 100;
        a.direction = "right";
        a.speed = 4;
        a.solidArea = new Rectangle(0, 0, 48, 48);
        a.originalSolidAreaX = 0;
        a.originalSolidAreaY = 0;

        b.worldX = 120;
        b.worldY = 100;
        b.solidArea = new Rectangle(0, 0, 48, 48);
        b.originalSolidAreaX = 0;
        b.originalSolidAreaY = 0;

        boolean result = checker.checkPlayer(a, b);

        assertTrue(result);
        assertTrue(a.collisionOn);
    }

    @Test
    void testCheckEntity() {
        CollisionChecker checker = new CollisionChecker(gamePanel);

        Entity mover = new Entity(gamePanel);
        mover.worldX = 200;
        mover.worldY = 200;
        mover.direction = "down";
        mover.speed = 4;
        mover.solidArea = new Rectangle(0, 0, 48, 48);
        mover.originalSolidAreaX = 0;
        mover.originalSolidAreaY = 0;

        Entity[][] targets = new Entity[gamePanel.totalMap][20];
        targets[gamePanel.currentMap][3] = new VirtualEntity(gamePanel);
        targets[gamePanel.currentMap][3].worldX = 200;
        targets[gamePanel.currentMap][3].worldY = 230;
        targets[gamePanel.currentMap][3].solidArea = new Rectangle(0, 0, 48, 48);
        targets[gamePanel.currentMap][3].originalSolidAreaX = 0;
        targets[gamePanel.currentMap][3].originalSolidAreaY = 0;

        int index = checker.checkEntity(mover, targets);

        assertEquals(3, index);
    }

    @Test
    void testCheckObject() {
        CollisionChecker checker = new CollisionChecker(gamePanel);

        Entity mover = new Entity(gamePanel);
        mover.worldX = 300;
        mover.worldY = 300;
        mover.direction = "right";
        mover.speed = 4;
        mover.solidArea = new Rectangle(0, 0, 48, 48);
        mover.originalSolidAreaX = 0;
        mover.originalSolidAreaY = 0;

        gamePanel.objects = new Entity[gamePanel.totalMap][20];
        gamePanel.objects[gamePanel.currentMap][5] = new VirtualEntity(gamePanel);
        gamePanel.objects[gamePanel.currentMap][5].worldX = 320;
        gamePanel.objects[gamePanel.currentMap][5].worldY = 300;
        gamePanel.objects[gamePanel.currentMap][5].collision = true;
        gamePanel.objects[gamePanel.currentMap][5].solidArea = new Rectangle(0, 0, 48, 48);
        gamePanel.objects[gamePanel.currentMap][5].originalSolidAreaX = 0;
        gamePanel.objects[gamePanel.currentMap][5].originalSolidAreaY = 0;

        int index = checker.checkObject(mover, true);

        assertEquals(5, index);
        assertTrue(mover.collisionOn);
    }

    @Test
    void testTeleportEvent() {
        EventChecker checker = new EventChecker(gamePanel);

        gamePanel.player1.currentMap = 0;
        gamePanel.player1.worldX = 37 * gamePanel.tileSize;
        gamePanel.player1.worldY = 43 * gamePanel.tileSize;
        gamePanel.player1.direction = "down";
        gamePanel.player1.canTouchEvent = true;

        checker.checkEvent(gamePanel.player1);

        assertEquals(1, gamePanel.player1.currentMap);
        assertEquals(5 * gamePanel.tileSize, gamePanel.player1.worldX);
        assertEquals(5 * gamePanel.tileSize, gamePanel.player1.worldY);
        assertFalse(gamePanel.player1.canTouchEvent);
    }

    @Test
    void testEntityDefaultValues() {
        Entity e = new Entity(gamePanel);

        assertEquals(4, e.speed);
        assertEquals("down", e.direction);
        assertEquals(48, e.solidArea.width);
        assertEquals(48, e.solidArea.height);
    }
    @Test
    void testDamageMonster() {
        gamePanel.buildVirtualGame();

        int map = gamePanel.currentMap;
        int index = 0;
        VirtualMonster monster = (VirtualMonster) gamePanel.monsters[map][index];

        int originalLife = monster.life;

        gamePanel.player1.damageMonster(index);

        assertEquals(originalLife - 1, monster.life);
        assertTrue(monster.invincible);
    }

    @Test
    void testDamageMonsterDead() {
        gamePanel.buildVirtualGame();

        int map = gamePanel.currentMap;
        int index = 0;
        VirtualMonster monster = (VirtualMonster) gamePanel.monsters[map][index];

        monster.life = 1;
        gamePanel.player1.damageMonster(index);

        assertEquals(0, monster.life);
        assertTrue(monster.dying);
        assertTrue(monster.invincible);
    }

    @Test
    void testPickUpObjectBrick() {
        gamePanel.buildVirtualGame();

        int map = gamePanel.currentMap;
        int index = 0;

        if (gamePanel.objects[map][index] != null) {
            gamePanel.objects[map][index].name = "Brick";
        }

        boolean hadBrickBefore = ((VirtualPlayer) gamePanel.player1).hasBrick;

        gamePanel.player1.pickUpObject(index);

        assertTrue(((VirtualPlayer) gamePanel.player1).hasBrick);
        assertNull(gamePanel.objects[map][index]);
    }

    @Test
    void testContactMonster() {
        gamePanel.buildVirtualGame();

        VirtualPlayer player = (VirtualPlayer) gamePanel.player1;
        int originalLife = player.life;

        player.contactMonster(0);

        assertEquals(originalLife - 1, player.life);
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
