package Entity;

import Main.Camera;
import Main.KeyHandler;
import gamepanel.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import Main.KeyHandler;

public class Player extends Entity {

    public boolean p1;

    // State
    boolean invincible = false;
    public boolean hasBrick = false;

    // EventChecker
    // make sure the event will not repeatedly trigger in a short period of time.
    public int currentMap = 0;
    public int eventPreviousX;
    public int eventPreviousY;
    public boolean canTouchEvent = true;

    // Counter
    int p1SpriteCounter = 0;
    int p2SpriteCounter = 0;
    int invincibleCounter = 0;

    public Player(GamePanel gamePanel, boolean p1) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        this.p1 = p1;

        // Define the collision area for player, cannot be tile size,it looks unnatural
        solidArea = new Rectangle(12, 18, 24, 24);
        originalSolidAreaX = solidArea.x;
        originalSolidAreaY = solidArea.y;

        attackArea.width = 36;
        attackArea.height = 36;

        setDefaultValue();
        getImage();
        getAttackImage();
    }

    public void setDefaultValue() {

        if (p1) {
            worldX = gamePanel.tileSize * 30;
            worldY = gamePanel.tileSize * 47;
        } else {
            worldX = gamePanel.tileSize * 37;
            worldY = gamePanel.tileSize * 47;
        }

        //Player States
        speed = 4;
        maxLife = 6;
        life = maxLife;

    }

    public void getImage() {

        System.out.println("Loading image: " + (p1 ? "P1" : "P2"));

        if (p1) {
            up1 = setup("/player/player1_up1.png", gamePanel.tileSize, gamePanel.tileSize);
            up2 = setup("/player/player1_up2.png", gamePanel.tileSize, gamePanel.tileSize);
            down1 = setup("/player/player1_down1.png", gamePanel.tileSize, gamePanel.tileSize);
            down2 = setup("/player/player1_down2.png", gamePanel.tileSize, gamePanel.tileSize);
            left1 = setup("/player/player1_left1.png", gamePanel.tileSize, gamePanel.tileSize);
            left2 = setup("/player/player1_left2.png", gamePanel.tileSize, gamePanel.tileSize);
            right1 = setup("/player/player1_right1.png", gamePanel.tileSize, gamePanel.tileSize);
            right2 = setup("/player/player1_right2.png", gamePanel.tileSize, gamePanel.tileSize);
        } else {
            up1 = setup("/player/player2_up1.png", gamePanel.tileSize, gamePanel.tileSize);
            up2 = setup("/player/player2_up2.png", gamePanel.tileSize, gamePanel.tileSize);
            down1 = setup("/player/player2_down1.png", gamePanel.tileSize, gamePanel.tileSize);
            down2 = setup("/player/player2_down2.png", gamePanel.tileSize, gamePanel.tileSize);
            left1 = setup("/player/player2_left1.png", gamePanel.tileSize, gamePanel.tileSize);
            left2 = setup("/player/player2_left2.png", gamePanel.tileSize, gamePanel.tileSize);
            right1 = setup("/player/player2_right1.png", gamePanel.tileSize, gamePanel.tileSize);
            right2 = setup("/player/player2_right2.png", gamePanel.tileSize, gamePanel.tileSize);
        }

    }

    public void getAttackImage() {

        if (p1) {
            upAttack1 = setup("/player/player1_up_attack1.png", gamePanel.tileSize, gamePanel.tileSize * 2);
            upAttack2 = setup("/player/player1_up_attack2.png", gamePanel.tileSize, gamePanel.tileSize * 2);
            downAttack1 = setup("/player/player1_down_attack1.png", gamePanel.tileSize, gamePanel.tileSize * 2);
            downAttack2 = setup("/player/player1_down_attack2.png", gamePanel.tileSize, gamePanel.tileSize * 2);
            leftAttack1 = setup("/player/player1_left_attack1.png", gamePanel.tileSize * 2, gamePanel.tileSize);
            leftAttack2 = setup("/player/player1_left_attack2.png", gamePanel.tileSize * 2, gamePanel.tileSize);
            rightAttack1 = setup("/player/player1_right_attack1.png", gamePanel.tileSize * 2, gamePanel.tileSize);
            rightAttack2 = setup("/player/player1_right_attack2.png", gamePanel.tileSize * 2, gamePanel.tileSize);
        } else {
            upAttack1 = setup("/player/player2_up_attack1.png", gamePanel.tileSize, gamePanel.tileSize * 2);
            upAttack2 = setup("/player/player2_up_attack2.png", gamePanel.tileSize, gamePanel.tileSize * 2);
            downAttack1 = setup("/player/player2_down_attack1.png", gamePanel.tileSize, gamePanel.tileSize * 2);
            downAttack2 = setup("/player/player2_down_attack2.png", gamePanel.tileSize, gamePanel.tileSize * 2);
            leftAttack1 = setup("/player/player2_left_attack1.png", gamePanel.tileSize * 2, gamePanel.tileSize);
            leftAttack2 = setup("/player/player2_left_attack2.png", gamePanel.tileSize * 2, gamePanel.tileSize);
            rightAttack1 = setup("/player/player2_right_attack1.png", gamePanel.tileSize * 2, gamePanel.tileSize);
            rightAttack2 = setup("/player/player2_right_attack2.png", gamePanel.tileSize * 2, gamePanel.tileSize);
        }

    }

    // Interact npc?
    public void attack() {
        if (p1) {
            if (gamePanel.key.j) {
                attacking = true;
            }
        } else {
            if (gamePanel.key.numpad1) {
                attacking = true;
            }
        }

    }

    public void attacking() {

        spriteCounter++;

        if (spriteCounter <= 5) {
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteNum <= 25) {
            spriteNum = 2;

            // When all attack happened, reset the solidArea and check collision.

            // Save the current position.
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Set solidArea to attackArea
            switch (direction) {
                case "up":
                    worldY -= attackArea.height;
                    break;
                case "down":
                    worldY += attackArea.height;
                    break;
                case "left":
                    worldX -= attackArea.width;
                    break;
                case "right":
                    worldX += attackArea.width;
                    break;
            }
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Check monster collision
            int monsterIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.monsters);
            damageMonster(monsterIndex);

            // Reset to the original data.
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        }
        if (spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void damageMonster(int i) {
        if (i != 999) {
            if (!gamePanel.monsters[gamePanel.currentMap][i].invincible) {
                gamePanel.monsters[currentMap][i].life -= 1;
                System.out.println(gamePanel.monsters[currentMap][i].life);
                gamePanel.monsters[currentMap][i].invincible = true;

                if (gamePanel.monsters[gamePanel.currentMap][i].life <= 0) {
                    gamePanel.monsters[gamePanel.currentMap][i].dying = true;
                }
            }
        }
    }

    // Pick up objects
    public void pickUpObject(int i) {
        if (i != 999) {
            if (gamePanel.objects[gamePanel.currentMap][i] != null) {
                if (gamePanel.objects[gamePanel.currentMap][i].name.equals("Brick")) {
                    gamePanel.objects[gamePanel.currentMap][i] = null;
                    hasBrick = true;
                }
            }
        }
    }

    public void interactNPC(int i) {

    }

    public void contactMonster(int i) {
        if (i != 999) {
            if (!invincible) {
                life -= 1;
                invincible = true;
                System.out.println(this.life);
            }
        }
    }

    public void update(KeyHandler key) {

        if (attacking) {
            attacking();
        }
        // first set the direction, then check collision, then move
        else if (p1) {

            if (key.w || key.s || key.d || key.a) {
                if (key.w) {
                    direction = "up";
                }
                if (key.s) {
                    direction = "down";
                }
                if (key.a) {
                    direction = "left";
                }
                if (key.d) {
                    direction = "right";
                }

                // Walk step by step
                p1SpriteCounter++;
                if (p1SpriteCounter > 12) {
                    if (spriteNum == 1) {
                        spriteNum = 2;
                    } else if (spriteNum == 2) {
                        spriteNum = 1;
                    }
                    p1SpriteCounter = 0;
                }
            }

        } else {
            if (key.up || key.down || key.left || key.right) {
                if (key.up) {
                    direction = "up";
                }
                if (key.down) {
                    direction = "down";
                }
                if (key.left) {
                    direction = "left";
                }
                if (key.right) {
                    direction = "right";
                }

                p2SpriteCounter++;
                if (p2SpriteCounter > 12) {
                    if (spriteNum == 1) {
                        spriteNum = 2;
                    } else if (spriteNum == 2) {
                        spriteNum = 1;
                    }
                    p2SpriteCounter = 0;
                }
            }
        }

        // Check tile collision
        collisionOn = false;
        gamePanel.collisionChecker.checkTile(this);

        // Check for collisions between players
        if (p1) {
            gamePanel.collisionChecker.checkPlayer(this, gamePanel.player2);
        } else {
            gamePanel.collisionChecker.checkPlayer(this, gamePanel.player1);
        }

        //check object collision
        int objectIndex = gamePanel.collisionChecker.checkObject(this, true);
        pickUpObject(objectIndex);

        //check npc collision
        int npcIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.npc);
        interactNPC(npcIndex);
        attack();

        //check monster collision
        int monsterIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.monsters);
        contactMonster(monsterIndex);

        // Check event
        gamePanel.eventChecker.checkEvent(this);

        if (!collisionOn && !attacking) {
            if (p1) {

                if (key.w) {
                    worldY -= speed;
                }
                if (key.s) {
                    worldY += speed;
                }
                if (key.a) {
                    worldX -= speed;
                }
                if (key.d) {
                    worldX += speed;
                }
            } else {

                if (key.up) {
                    worldY -= speed;
                }
                if (key.down) {
                    worldY += speed;
                }
                if (key.left) {
                    worldX -= speed;
                }
                if (key.right) {
                    worldX += speed;
                }
            }
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > gamePanel.FPS) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // Player1
        if (this == gamePanel.player1) {
            if (key.p1NextItemPressed) {
                nextItem();
                key.p1NextItemPressed = false;
            }
        }

        // Player2
        if (this == gamePanel.player2) {
            if (key.p2NextItemPressed) {
                nextItem();
                key.p2NextItemPressed = false;
            }
        }

    }

    public void draw(Graphics2D graphics2D, Camera camera) {

        BufferedImage image = null;

        int drawX = worldX - camera.x;
        int drawY = worldY - camera.y;

        int screenX = drawX;
        int screenY = drawY;

        switch (direction) {

            //set different picture when moving different direction
            case "up":
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                }
                if (attacking) {
                    // While attacking the position of player image should be change.
                    screenY = drawY - gamePanel.tileSize;
                    if (spriteNum == 1) {
                        image = upAttack1;
                    }
                    if (spriteNum == 2) {
                        image = upAttack2;
                    }
                }
                break;
            case "down":
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                }
                if (attacking) {
                    if (spriteNum == 1) {
                        image = downAttack1;
                    }
                    if (spriteNum == 2) {
                        image = downAttack2;
                    }
                }
                break;
            case "left":
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                }
                if (attacking) {
                    screenX = drawX - gamePanel.tileSize;
                    if (spriteNum == 1) {
                        image = leftAttack1;
                    }
                    if (spriteNum == 2) {
                        image = leftAttack2;
                    }
                }
                break;
            case "right":
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                }
                if (attacking) {
                    if (spriteNum == 1) {
                        image = rightAttack1;
                    }
                    if (spriteNum == 2) {
                        image = rightAttack2;
                    }
                }
                break;
        }

        graphics2D.drawImage(image, screenX, screenY, null);
    }
}