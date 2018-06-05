package uk.ac.qub.eeecs.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchHandler;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.InputHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.gameScreens.CardScreen;

/**
 * @authors Andrew Bell,Tom Purdon
 * Adding author Matthew Downey
 */
public class Card extends Sprite {


    /**
     * Instance of Game
     */
    protected Game mGame = mGameScreen.getGame();

    /**
     * Instance of AssetStore class needed to manage game assets
     */
    private AssetStore assetManager = mGame.getAssetManager();

    /**
     * Card front and back bitmaps
     */
    private Bitmap cardBitmap, cardBackBitmap;

    /**
     * Card sprite bounding boxes, copy of original bound and also a bigger bound to be activated while card sprite is touched
     */
    private BoundingBox originalBound, bigBound;

    /**
     * Instance of LayerViewport required for screen measurements
     */
    protected LayerViewport mLayerViewport = ((CardScreen) mGameScreen).getmLayerViewport();
    protected ScreenViewport mScreenViewport = ((CardScreen) mGameScreen).getmScreenViewport();

    /**
     * Instance of textPaint
     */
    private TextPaint textPaint;

    /**
     * Card specific strings required to be drawn
     */
    private String name, description, LeftSwipeAction, RightSwipeAction;

    /**
     * Character ID integer to identify the character
     */
    private int charID;

    /**
     * Integers that determine the next card/story after the current card depedning on swipe
     */
    private int lStoryPathway;
    private int rStoryPathway;

    /**
     * Int Arrays of card stat effects that are added to stats if dropped in r/l hotspot
     */
    private int[] lStatEffect = {0, 0, 0, 0}, rStatEffect = {0, 0, 0, 0};


    /**
     *@author Matthew Downey
     */
    public Boolean left = false;
    public Boolean right = false;



    public Card(GameScreen gameScreen, int charID, String name, String description, String leftSwipeAction,
                String rightSwipeAction, int lStoryPathway, int rStoryPathway, int[] lStatEffect, int[] rStatEffect) {
        super(0, 0, 500, 250, null, gameScreen);
        this.charID = charID;
        this.name = name;
        this.description = description;
        LeftSwipeAction = leftSwipeAction;
        RightSwipeAction = rightSwipeAction;
        this.lStoryPathway = lStoryPathway;
        this.rStoryPathway = rStoryPathway;
        this.lStatEffect = lStatEffect;
        this.rStatEffect = rStatEffect;
        mGameScreen = gameScreen;
        initPaint();
        setPosition(gameScreen.getGame().getScreenWidth() / 2, gameScreen.getGame().getScreenHeight() / 2);
        cardBitmap = assetManager.getBitmap("card_template1");
        cardBackBitmap = assetManager.getBitmap("cardback");
        mBitmap = cardBitmap;
        initCard();
    }


    /**
     * Constructor for card when no values are being set
     *
     * @param x          x position of card
     * @param y          y position of card
     * @param gameScreen instance of gamescreen required for super class constructor
     */
    public Card(float x, float y, GameScreen gameScreen) {
        // New dimensions for each card so that it matches design of FarFlight
        super(x, y, 500, 250, null, gameScreen);
        mBitmap = assetManager.getBitmap("card_template1");

    }


    /**
     * Draws swipe actions on screen if card is on left/ right of screen
     *
     * @param elapsedTime
     * @param graphics2D
     * @param layerViewport
     * @param screenViewport
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {

        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);

        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);

        // USER STORY 5 - Display text when card is dragged (Design WIP) @author Matthew Downey
        if (left) {
            graphics2D.drawText(LeftSwipeAction, screenViewport.centerX(), (screenViewport.height * 0.20f), textPaint);
        } else if (right) {
            graphics2D.drawText(RightSwipeAction, screenViewport.centerX(), (screenViewport.height * 0.20f), textPaint);
        }

    }


    /**
     * Initialises the card with the values from constructor
     * Creates cardBitmap with the card information drawn on
     * sets card position
     * sets card bounding boxes
     */
    protected void initCard() {
        //Draw name text to card
        cardBitmap = drawTextOnCard(cardBitmap, name, 8, 30, Color.BLACK);
        //Draw description text on card
        cardBitmap = drawTextOnCard(cardBitmap, description, 1.2f, 25, Color.DKGRAY);
        assetManager.loadAndAddBitmap(String.valueOf(charID), "img/Characters/" + charID + ".png");
        cardBitmap = drawIconOnCard(cardBitmap, assetManager.getBitmap(String.valueOf(charID)));
        originalBound = mBound;
        bigBound = mBound = new BoundingBox(mBound.x, mBound.y, mBound.halfWidth * 1.05f, mBound.halfHeight * 1.05f);
        setCardToMiddle();
        mBitmap = cardBackBitmap;
    }


    /**
     * Toggles the bitmap on card between front and back bitmaps
     *
     * @param cardFront True sets card to show front, False toggles card to show back.
     * @return True if card is now front, false if card is showing back
     * @author Andrew Bell
     */
    protected boolean toggleCardBitmap(Boolean cardFront) {
        if (cardFront) {
            mBitmap = cardBitmap;
            return true;
        } else {
            mBitmap = cardBackBitmap;
            return false;
        }
    }

    /**
     * Set card to the middle of the screen and reset its movement values
     *
     * @return true if card is successfully set to middle
     */
    public boolean setCardToMiddle() {
        if (mLayerViewport != null) {
            setPosition(mLayerViewport.halfWidth, mLayerViewport.halfHeight);
            orientation = 0f;
            velocity.x = 0;
            angularVelocity = 0;
            left = false;
            right = false;
            return true;
        }
        return false;
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);
    }

    /**
     * Draw text on card according to parameters
     *
     * @param bitmap     Bitmap of to draw text on currently
     * @param textToDraw String of text to draw
     * @param yFactor    Scale for Y axis to tex ton bitmap
     * @param textSize   Text size to set
     * @param textColour TextColour to set
     * @return Bitmap with text drawn on
     * @author Andrew Bell. Adapted from https://www.skoumal.com/en/android-drawing-multiline-text-on-bitmap/
     */
    protected Bitmap drawTextOnCard(Bitmap bitmap,
                                    String textToDraw, float yFactor, int textSize, int textColour) {
        // Create a copy of card bitmap that is now mutable so it can be set on canvas
        bitmap = bitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, true);

        //New canvas from card bitmap
        Canvas canvas = new Canvas(bitmap);

        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColour);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));

        //Create text layout and align text in center
        StaticLayout textToDrawLayout = new StaticLayout(
                textToDraw, paint, (canvas.getWidth() - textSize), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        canvas.save();
        //Set the horizontal middle
        float x = (bitmap.getWidth() - (canvas.getWidth() - textSize)) / 2;
        //Set the vertical position using the y factor from parameter
        float y = (bitmap.getHeight() - textToDrawLayout.getHeight()) / yFactor;
        //Position text on canvas
        canvas.translate(x, y);
        textToDrawLayout.draw(canvas);
        canvas.restore();
        return bitmap;
    }

    /**
     * @param bitmap
     * @param characterIcon
     * @return
     * @authors Tom Purdon, Andrew Bell
     */
    protected Bitmap drawIconOnCard(Bitmap bitmap, Bitmap characterIcon) {

        // Create a copy of card bitmap that is now mutable so it can be set on canvas
        bitmap = bitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, true);
        //New canvas from card bitmap
        Canvas canvas = new Canvas(bitmap);
        float x, y;
        x = mBound.halfWidth - (bitmap.getWidth() / 9 * 4);
        y = mBound.halfHeight;
        canvas.drawBitmap(characterIcon, x, y, null);
        return bitmap;
    }


    /**
     * Initialises card paint variable
     */
    protected void initPaint() {
        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
    }


    protected void setBigBound() {
        mBound = bigBound;
    }

    protected void setOriginalBound() {
        mBound = originalBound;
    }

    /**
     * GETTERS
     */


    public int getlStoryPathway() {
        return lStoryPathway;
    }

    public int getrStoryPathway() {
        return rStoryPathway;
    }

    public int[] getlStatEffect() {
        return lStatEffect;
    }

    public int[] getrStatEffect() {
        return rStatEffect;
    }


    /**
     * SETTERS
     */
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }


}



