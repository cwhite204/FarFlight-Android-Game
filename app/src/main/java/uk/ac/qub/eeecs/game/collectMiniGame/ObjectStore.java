package uk.ac.qub.eeecs.game.collectMiniGame;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

// Created by Matthew Downey

public class ObjectStore {

    private String itemName;
    private String unicode;
    private int score, targetScore;
    private ArrayList<GameObject> array = new ArrayList<GameObject>();
    private float levelWidth, levelHeight;

    private Game game;
    private GameScreen gameScreen;
    private ArrayList<ObjectStore> parentArray;

    public ObjectStore(String itemName, String unicode, String bitmapLocation, int quantity, int targetScore, ArrayList<ObjectStore> parentArray, Game game, float levelWidth, float levelHeight, GameScreen gameScreen) {
        this.itemName = itemName;
        this.unicode = unicode;
        this.score = 0;
        this.targetScore = targetScore;
        this.parentArray = parentArray;
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        this.game = game;

        game.getAssetManager().loadAndAddBitmap(itemName, bitmapLocation);

        for (int i = 0; i < quantity; i++) {
            addNewItem();
        }
    }

    private void addNewItem() {
        boolean conflict;
        float newX, newY;
        BoundingBox newPosition;
        Random random = new Random();

        do {
            conflict = false;
            // Generates new coordinates, ensuring they are above the player starting point
            if (itemName.equals("Rock")) {
                newY = (random.nextFloat() * (levelHeight / 2) - 400) + 200;
            } else {
                newY = (random.nextFloat() * (levelHeight - 200)) + 200;
            }
            newX = (random.nextFloat() * (levelWidth - 35) + 35);
            newPosition = new BoundingBox(newX, newY, 30, 30);

            for (ObjectStore objectStore: parentArray) {
                for (GameObject item : objectStore.getArray()) {
                    if (newPosition.intersects(item.getBound())) {
                        conflict = true;
                    }
                }
            }

        } while (conflict);

        array.add(new GameObject(newX, newY, 60, 60, game.getAssetManager().getBitmap(itemName), gameScreen));
    }

    public void drawItems(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport mLayerViewport, ScreenViewport mScreenViewport) {
        for (GameObject object : array) {
            object.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }
    }

    public boolean objectHit(BoundingBox bound) {
        for (GameObject object : array) {
            if (bound.intersects(object.getBound())) {
                score++;
                array.remove(object);
                return true;
            }
        }
        return false;
    }

    public String scoreText() {
        if (score > targetScore) {
            return unicode + " " + targetScore + "/" + targetScore;
        } else {
            return unicode + " " + score + "/" + targetScore;
        }
    }

    public void checkQuantities() {
        while (array.size() < targetScore) {
            addNewItem();
        }
    }

    public boolean targetMet() {
        return (score >= targetScore);
    }

    public void wipeScore() {
        score = 0;
    }

    public ArrayList<GameObject> getArray() {
        return array;
    }

    public String getItemName() {
        return itemName;
    }

    public String getUnicode() {
        return unicode;
    }
}
