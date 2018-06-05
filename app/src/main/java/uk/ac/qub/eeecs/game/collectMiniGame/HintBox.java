package uk.ac.qub.eeecs.game.collectMiniGame;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

// Created by Matthew Downey

public class HintBox {

    private GameObject hintBox;

    private int duration = 60;
    private int countdown = duration;

    private ArrayList<String> text = new ArrayList<String>();

    private float boxPosition;
    private float textPosition;

    private Paint paint = new Paint();

    private Game game;
    private float levelWidth;

    public HintBox(GameScreen gameScreen, float levelWidth) {
        game = gameScreen.getGame();
        this.levelWidth = levelWidth;

        game.getAssetManager().loadAndAddBitmap("HintBox", "img/CollectMiniGame/HintBox.png");
        hintBox = new GameObject(levelWidth / 2f, 0, levelWidth, levelWidth / 2.5f, game.getAssetManager().getBitmap("HintBox"), gameScreen);
        textPosition = game.getScreenHeight() - (game.getScreenHeight() / 7.2f);

        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    public void drawHintBox(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport mLayerViewport, ScreenViewport mScreenViewport) {
        if (text.size() >= 3) {
            if (countdown == duration || countdown == 0) {
                boxPosition = 240;
            } else if (countdown == (duration - 1) || countdown == 1) {
                boxPosition = 200;
            } else if (countdown == (duration - 2) || countdown == 2) {
                boxPosition = 160;
            } else if (countdown == (duration - 3) || countdown == 3) {
                boxPosition = 120;
            } else if (countdown == (duration - 4) || countdown == 4) {
                boxPosition = 80;
            } else if (countdown == (duration - 5) || countdown == 5) {
                boxPosition = 40;
            } else if (countdown == (duration - 6) || countdown == 6) {
                boxPosition = 0;
            }

            hintBox.position.y -= boxPosition;
            hintBox.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            graphics2D.drawText(text.get(0), game.getScreenWidth() / 4.6f, textPosition + boxPosition * 2.2f, paint);
            graphics2D.drawText(text.get(1), game.getScreenWidth() / 4.6f, textPosition + 70 + boxPosition * 2.2f, paint);
            graphics2D.drawText(text.get(2), game.getScreenWidth() / 4.6f, textPosition + 140 + boxPosition * 2.2f, paint);
            hintBox.position.y = mLayerViewport.getBottom() + levelWidth / 5;
            countdown--;
            if (countdown == 0) {
                removeMessage();
                countdown = duration;
            }
        }
    }

    public void showMessageOnHintBox(String line1, String line2, String line3) {
        text.add(line1);
        text.add(line2);
        text.add(line3);
    }

    public void removeMessage() {
        text.remove(0);
        text.remove(0);
        text.remove(0);
    }
}
