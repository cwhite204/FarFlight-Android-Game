package uk.ac.qub.eeecs.game.collectMiniGame;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.game.gameScreens.CardScreen;
import uk.ac.qub.eeecs.game.gameScreens.MainMenuScreen;

// Created by Matthew Downey

public class CollectMiniGame extends GameScreen {

    // Viewport and screen
    private final float LEVEL_WIDTH = 480f;
    private final float LEVEL_HEIGHT = 10000.0f;
    private LayerViewport mLayerViewport;
    private ScreenViewport mScreenViewport;
    private Vector2 screenCentre = new Vector2();

    private Player player;

    // Environment
    private ArrayList<GameObject> backgroundTiles = new ArrayList<GameObject>();;
    private GameObject river;
    private GameObject stonePath;

    // Objects
    private ArrayList<ObjectStore> allObjects = new ArrayList<ObjectStore>();
    private ArrayList<GameObject> crabs = new ArrayList<GameObject>();;

    // Paint
    private Paint paint;
    private Paint updatePaint;

    // Scoreboard text
    private String scoreText;

    // Crab-related variables
    private boolean hitByCrab = false;
    private boolean firstCrabHit = true;
    private int redTime = 20;
    private boolean movingRight = true;
    private double crabTimer;

    // Variables for message that displays when player picks up an item
    private boolean hit = false;
    private String message = "";
    private int countdown = 40;
    private int position = getGame().getScreenHeight() - 60;

    // Hint box
    private HintBox hintBox;

    // River-related variables
    private boolean firstLookAtRiver = true;
    private boolean riverCrossable = false;

    // Card game screen
    CardScreen cardScreen;

    // Fading away paint
    private Paint rectanglePaint;

    public CollectMiniGame(Game game, CardScreen cardScreen) {
        super("collectGame", game);
        this.cardScreen = cardScreen;
        initialiseGame(game);
    }
    public CollectMiniGame(Game game) {
        super("collectGame", game);
        initialiseGame(game);
    }

    // Give up button
    PushButton giveUpButton;


    public void initialiseGame(Game game) {
        // Creates viewports and sets up screen
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        mLayerViewport = new LayerViewport(240.0f,
                240.0f * mScreenViewport.height / mScreenViewport.width,
                240,
                240.0f * mScreenViewport.height / mScreenViewport.width);
        screenCentre.x = getGame().getScreenWidth() / 2;
        screenCentre.y = getGame().getScreenHeight() / 2;

        // Load in assets
        AssetStore assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("SandBackground", "img/CollectMiniGame/SandBackground.jpg");
        assetManager.loadAndAddBitmap("Player", "img/CollectMiniGame/Player.png");
        assetManager.loadAndAddBitmap("Crab", "img/CollectMiniGame/Crab.png");
        assetManager.loadAndAddBitmap("River", "img/CollectMiniGame/River.png");
        assetManager.loadAndAddBitmap("Path", "img/CollectMiniGame/Path.png");
        assetManager.loadAndAddBitmap("GiveUpButton", "img/CollectMiniGame/GiveUpButton.png");

        // Add object types to all objects
        allObjects.add(new ObjectStore("Banana", "\uD83C\uDF4C", "img/CollectMiniGame/Banana.png", 5, 3, allObjects, getGame(), LEVEL_WIDTH, LEVEL_HEIGHT, this));
        allObjects.add(new ObjectStore("Melon","\uD83C\uDF49", "img/CollectMiniGame/Melon.png", 5, 3, allObjects, getGame(), LEVEL_WIDTH, LEVEL_HEIGHT, this));
        allObjects.add(new ObjectStore("Peach", "\uD83C\uDF51", "img/CollectMiniGame/Peach.png", 5, 3, allObjects, getGame(), LEVEL_WIDTH, LEVEL_HEIGHT, this));
        allObjects.add(new ObjectStore("Pineapple", "\uD83C\uDF4D", "img/CollectMiniGame/Pineapple.png", 5, 3, allObjects, getGame(), LEVEL_WIDTH, LEVEL_HEIGHT, this));
        allObjects.add(new ObjectStore("Rock", "\uD83D\uDC1A", "img/CollectMiniGame/Rock.png", 5, 3, allObjects, getGame(), LEVEL_WIDTH, LEVEL_HEIGHT, this));

        // Generate crabs
        addCrabs();

        // Generate background tiles
        for (int i = 0; i < (LEVEL_HEIGHT / LEVEL_WIDTH) + 1; i++) {
            backgroundTiles.add(new GameObject(0, i * LEVEL_WIDTH, LEVEL_WIDTH, LEVEL_WIDTH, getGame().getAssetManager().getBitmap("SandBackground"), this));
            backgroundTiles.add(new GameObject(LEVEL_WIDTH, i * LEVEL_WIDTH, LEVEL_WIDTH, LEVEL_WIDTH, getGame().getAssetManager().getBitmap("SandBackground"), this));
        }

        // Set up game other objects
        player = new Player(LEVEL_WIDTH / 2.0f, 100, this);
        river = new GameObject(LEVEL_WIDTH / 2.0f, LEVEL_HEIGHT / 2.0f, LEVEL_WIDTH, 150, getGame().getAssetManager().getBitmap("River"), this);
        stonePath = new GameObject((LEVEL_WIDTH / 3.0f), LEVEL_HEIGHT / 2.0f, 90, 150, getGame().getAssetManager().getBitmap("Path"), this);

        // Setup paint types
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);

        updatePaint = new Paint();
        updatePaint.setColor(Color.BLACK);
        updatePaint.setTextSize(50);
        updatePaint.setTextAlign(Paint.Align.CENTER);

        rectanglePaint = new Paint();
        rectanglePaint.setColor(Color.BLACK);

        // Setup hint box and load in first message
        hintBox = new HintBox(this, LEVEL_WIDTH);
        hintBox.showMessageOnHintBox("Collect enough food to keep", "going and meet the targets at", "the top of the screen.");
        hintBox.showMessageOnHintBox("Tap on the screen where you", "want to go, and tap 'Give Up'", " if it's too much for you.");

        // Give up button
        giveUpButton = new PushButton(getGame().getScreenWidth() / 2, getGame().getScreenHeight() / 7, getGame().getScreenWidth() / 3, getGame().getScreenWidth() / 8, "GiveUpButton", this);
    }

    public void update(ElapsedTime elapsedTime) {

        Input input = getGame().getInput();

        // PLAYER MOVEMENT
        // (Adapted from GAGE)

        player.update(elapsedTime);

        // Focus the layer viewport on the player
        mLayerViewport.y = player.position.y;

        // Ensure the player cannot leave the confines of the world
        BoundingBox playerBound = player.getBound();
        if (playerBound.getLeft() < 0)
            player.position.x -= playerBound.getLeft();
        else if (playerBound.getRight() > LEVEL_WIDTH)
            player.position.x -= (playerBound.getRight() - LEVEL_WIDTH);

        if (playerBound.getBottom() < 0)
            player.position.y -= playerBound.getBottom();
        else if (playerBound.getTop() > LEVEL_HEIGHT)
            player.position.y -= (playerBound.getTop() - LEVEL_HEIGHT);

        // Ensure the viewport cannot leave the confines of the world
        if (mLayerViewport.getLeft() < 0)
            mLayerViewport.x -= mLayerViewport.getLeft();
        else if (mLayerViewport.getRight() > LEVEL_WIDTH)
            mLayerViewport.x -= (mLayerViewport.getRight() - LEVEL_WIDTH);

        if (mLayerViewport.getBottom() < 0)
            mLayerViewport.y -= mLayerViewport.getBottom();
        else if (mLayerViewport.getTop() > LEVEL_HEIGHT)
            mLayerViewport.y -= (mLayerViewport.getTop() - LEVEL_HEIGHT);

        // Display a message if the player hits an object
        for (ObjectStore objectStore : allObjects) {
            if (objectStore.objectHit(playerBound) && !objectStore.targetMet()) {
                hit = true;
                message = objectStore.getUnicode() + " + 1";
                countdown = 40;
                position = getGame().getScreenHeight() - 60;
            }
        }

        // CRAB MOVEMENT
        for (GameObject crab : crabs) {

            crabTimer = elapsedTime.totalTime % 6f;
            if (crabTimer > 3) {
                crab.position.x = (float)(crabTimer * 160);
            } else {
                crab.position.x = (float)(((crabTimer * 160) - LEVEL_WIDTH) - LEVEL_WIDTH);
            }



            // Check if the crab needs to change direction
            if (crab.position.x >= (LEVEL_WIDTH - 30)) {
                movingRight = false;
            } else if (crab.position.x <= 30) {
                movingRight = true;
            }

            System.out.println(elapsedTime.totalTime % 6f);

            // Move the crab
            if (movingRight) {
                // crab.position.x += Math.ceil(elapsedTime.stepTime) * 10;
                crab.position.x = (float)(elapsedTime.totalTime % 3f) * 160f;
            } else {
                // crab.position.x -= Math.ceil(elapsedTime.stepTime) * 10;
                crab.position.x = LEVEL_WIDTH - ((float)(elapsedTime.totalTime % 3f) * 160f);
            }

            // Check if the crab has hit the player
            if (playerBound.intersects(crab.getBound())) {
                for (ObjectStore objectStore : allObjects) {
                    objectStore.wipeScore();
                }
                player.velocity.x = -0.5f * player.velocity.x;
                player.velocity.y = -0.5f * player.velocity.y;
                hitByCrab = true;
                // If the player hits a crab for the first time, explain what happened
                if (firstCrabHit) {
                    hintBox.showMessageOnHintBox("Avoid getting hit by crabs!", "They'll take everything you've", "collected.");
                    firstCrabHit = false;
                }
            }

        }

        // Ensure the player can't cross the river
        // If the player has collected enough stones, allow them to move over the stones
        if ((riverCrossable && playerBound.intersects(river.getBound()) && !playerBound.intersects(stonePath.getBound())) || (!riverCrossable && playerBound.intersects((river.getBound())))) {
            player.velocity.x = -0.5f * player.velocity.x;
            player.velocity.y = -0.5f * player.velocity.y;
            player.maxVelocity = 0;
            player.maxAcceleration = 0;
            player.maxAngularAcceleration = 0;
            player.maxAngularVelocity = 0;
            player.position.y -= 1;
        }
        player.maxVelocity = 1500.0f;
        player.angularVelocity *= 0.75f;
        player.maxAcceleration = 1500.0f;
        player.angularAcceleration *= 0.95f;

        // Displays a message if...
        if (playerBound.getTop() > 4800 && firstLookAtRiver && !riverCrossable) {
            if (firstLookAtRiver && !allObjects.get(4).targetMet()) {
                // ...you see the river for the first time but you don't have enough rocks to cross it
                hintBox.showMessageOnHintBox("You'll need some rocks if", "you want to cross the river.", "Three rocks should do it.");
            } else {
                // ...you see the river for the first time and you do have enough rocks to cross it
                riverCrossable = true;
                hintBox.showMessageOnHintBox("Those rocks have come in", "handy for crossing the river!", "");
                allObjects.get(4).wipeScore();
            }
            firstLookAtRiver = false;
        }
        // ...this isn't the first time you've seen the river but you have enough rocks to cross it
        if (playerBound.getTop() > 4700 && !riverCrossable && allObjects.get(4).targetMet() && !firstLookAtRiver) {
            hintBox.showMessageOnHintBox("Great! You have enough rocks", "to cross the river!", "");
            allObjects.get(4).wipeScore();
            riverCrossable = true;
        }

        // Check if all targets are complete
        if (allTargetsAreComplete() || giveUpButton.isPushTriggered()) {
            endGame();
        }

        if (giveUpButtonPressed(input)) {
            endGame();
        }

    }

    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        // Draw background
        graphics2D.clear(Color.BLACK);
        graphics2D.clipRect(mScreenViewport.toRect());
        for (GameObject tile : backgroundTiles) {
            tile.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }

        // Draw river
        river.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        // Draw path if crossable
        if (riverCrossable) {
            stonePath.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }

        // Draw all items
        for (ObjectStore objectStore : allObjects) {
            objectStore.checkQuantities();
            objectStore.drawItems(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }

        // Draw the crabs
        for (GameObject crab : crabs) {
            crab.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }

        // Draw the player
        player.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        // Draw the score at the top
        scoreText = "";
        for (ObjectStore objectStore : allObjects) {
            scoreText += objectStore.scoreText() + "   ";
        }

        // If you get hit by a crab, the scores will flash red to alert you you've lost all of your items
        if (hitByCrab) {
            if (redTime == 10 || redTime == 1) {
                paint.setARGB(255, 60, 0, 0);
            } else if (redTime == 9 || redTime == 2) {
                paint.setARGB(255, 120, 0, 0);
            } else if (redTime == 8 || redTime == 3) {
                paint.setARGB(255, 180, 0, 0);
            } else if (redTime == 7 || redTime == 4) {
                paint.setARGB(255, 220, 0, 0);
            } else if (redTime == 6 || redTime == 5) {
                paint.setARGB(255, 255, 0, 0);
            } else if (redTime == 0) {
                hitByCrab = false;
                redTime = 10;
            }
            redTime--;
        }
        graphics2D.drawText(scoreText, mScreenViewport.centerX(), 100f, paint);

        // Draw hint box
        hintBox.drawHintBox(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        //
        if (hit) {
            if (countdown == 0) {
                countdown = 40;
                position = getGame().getScreenHeight() - 60;
            } else if (countdown <= 5) {
                position += 20;
            }
            graphics2D.drawText(message, 120f, position, updatePaint);
            countdown--;

            if (countdown == 0) {
                hit = false;
            }
        }

        // Give up button
        giveUpButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

    }

    public void addCrabs() {
        Random random = new Random();
        float newX, newY;
        BoundingBox newPosition;

        float distance = 200;   // Minimum distance between food items
        boolean conflict;


        // Generate crabs
        for (int i = 0; i < 8; i++) {
            do {
                conflict = false;
                // Generates new coordinates, ensuring they are above the player starting point
                newY = random.nextFloat() * (LEVEL_HEIGHT - 200) + 200;
                newPosition = new BoundingBox(0, newY, 30, 30);
                for (GameObject crab : crabs) {
                    if (newPosition.intersects(crab.getBound())) {
                        conflict = true;
                    }
                }
            } while (conflict);
            crabs.add(new GameObject(30, newY, 60, 60, getGame().getAssetManager().getBitmap("Crab"), this));
        }
    }

    public void endGame() {
        if (cardScreen == null) {
            mGame.getScreenManager().removeScreen(this.getName());
            mGame.getScreenManager().addScreen(new MainMenuScreen(mGame));
            ;
        } else {
            mGame.getScreenManager().removeScreen(this.getName());
            mGame.getScreenManager().addScreen(cardScreen);
        }
    }

    public boolean allTargetsAreComplete() {
        boolean targetsAreCompleted = true;
        for (ObjectStore objectStore : allObjects) {
            if (!objectStore.getItemName().equals("Rock") && !objectStore.targetMet()) {
                targetsAreCompleted = false;
            }
        }
        return targetsAreCompleted;
    }

    public boolean giveUpButtonPressed(Input input) {
        // giveUpButton = new PushButton(getGame().getScreenWidth() / 2, getGame().getScreenHeight() / 7, getGame().getScreenWidth() / 3, getGame().getScreenWidth() / 8, "GiveUpButton", this);

        boolean givenUp = false;
        float buttonWidth = getGame().getScreenWidth() / 3;
        float buttonHeight = getGame().getScreenWidth() / 8;
        float buttonLeft = getGame().getScreenWidth() / 2 - (buttonWidth / 2);
        float buttonRight = getGame().getScreenWidth() / 2 + (buttonWidth / 2);
        float buttonTop = getGame().getScreenHeight() / 7 - (buttonHeight / 2);
        float buttonBottom = getGame().getScreenHeight() / 7 + (buttonHeight / 2);

        if (input.existsTouch(0)) {
            float inputX = input.getTouchX(0);
            float inputY = input.getTouchY(0);

            if (inputX > buttonLeft && inputX < buttonRight && inputY > buttonTop && inputY < buttonBottom) {
                givenUp = true;
            }
        }
        return givenUp;
    }
}

