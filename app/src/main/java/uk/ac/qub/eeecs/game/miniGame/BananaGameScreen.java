package uk.ac.qub.eeecs.game.miniGame;

import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.R;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.gameScreens.CardScreen;
import uk.ac.qub.eeecs.game.gameScreens.MainMenuScreen;

/**
 * BananaGameScreen extends the game screen class.
 * It creates BananaSprites and a single basket sprite and listens for their collisions
 * @author Andrew Bell
 */

public class BananaGameScreen extends GameScreen {

    /**
     * Game object containing background bitmap to be displayed
     */
    protected GameObject background;

    /**
     * Single catcher sprite object
     */
    protected BasketSprite basketSprite;

    /**
     * Single banana sprite object
     */
    protected BananaSprite bananaSprite;

    /**
     * Counts bananas that have been released and the total to release
     */
    protected int bananasReleased = 0, bananasToRelease = 50;


    /**
     * ArrayList of BananaSprite
     */
    protected ArrayList<BananaSprite> bananaSprites = new ArrayList<>();


    /**
     * Bananas collected score int
     */
    protected int score = 0;
    /**
     * CardScreen object which is the main game to this mini gamem will be loaded when game is over.
     */
    protected CardScreen cardScreen;

    /**
     * Music object for background music
     */
    protected Music backgroundSound;


    /**
     * Object to load sound assets
     */
    protected AssetFileDescriptor afd;

    /**
     * Paint object to draw text
     */
    protected TextPaint paint;

    /**
     * Constructor that is called from card screen
     *
     * @param game Instance of Farflight Game
     * @param cardScreen Instance of CardScreen which the game was called from
     */
    public BananaGameScreen(Game game, CardScreen cardScreen) {
        super("BananaGameScreen", game);
        this.cardScreen = cardScreen;
        //Stop cardscreen background sound
        if (game.isAdded()) cardScreen.toggleSound();
        init();
    }

    /**
     * Constructor which is called via secret button and will go back to main menu
     *
     * @param game
     */
    public BananaGameScreen(Game game) {
        super("BananaGameScreen", game);
        cardScreen = null;
        init();
    }

    /**
     * Adds to the score while preventing the score from going below 0
     * @param scoreToAdd
     */
    protected void addToScore(int scoreToAdd) {
        if ((score + scoreToAdd) <= 0) {
            score = 0;
        } else {
            score += scoreToAdd;
        }
    }

    /**
     * Holder for methods which are required to initialise this game screen
     */
    protected void init() {
        AssetStore assetManager = mGame.getAssetManager();
        loadAssets(assetManager);
        initScreen();
        initSprites(assetManager);
        paint = initPaint();
        initBackgroundSound();

    }

    /**
     * Toggles the playing state of background music
     */
    public void toggleSound() {
        if (!getGame().isAdded()) return;
        if (backgroundSound.isPlaying()) {
            backgroundSound.stop();
        } else backgroundSound.play();
    }


    /**
     * Load sound resource
     * Set sound attributes
     * Start playing sound
     */
    protected void initBackgroundSound() {
        if (!getGame().isAdded()) return;
        afd = mGame.getResources().openRawResourceFd(R.raw.monkey_background);
        backgroundSound = new Music(afd);
        backgroundSound.setVolume(5.0f);
        backgroundSound.setLopping(true);
        toggleSound();
    }

    /**
     * Load assets required for miniGame
     *
     * @param assetManager Instance of assetStore used to manage assets
     * @return
     */
    protected boolean loadAssets(AssetStore assetManager) {
        assetManager.loadAndAddBitmap("backgroundM", "img/BananaMiniGame/monkey_background.jpg");
        assetManager.loadAndAddBitmap("1", "img/Characters/1.png");
        assetManager.loadAndAddBitmap("banana", "img/BananaMiniGame/banana.png");
        assetManager.loadAndAddBitmap("superBanana", "img/BananaMiniGame/superBanana.png");
        assetManager.loadAndAddBitmap("badBanana", "img/BananaMiniGame/badBanana.png");
        assetManager.loadAndAddBitmap("bananaBack", "img/BananaMiniGame/bananaBack.png");
        assetManager.loadAndAddBitmap("basket", "img/BananaMiniGame/basket.png");
        return true;
    }


    /**
     * Background dimensions
     */
    protected final float LEVEL_WIDTH = 600.0f;
    protected final float LEVEL_HEIGHT = 500.0f;
    /**
     * Initialise background sprite, basket sprite and initial bananas.
     *
     * @param assetManager
     */
    protected void initSprites(AssetStore assetManager) {
        // Create the background
        background = new GameObject(LEVEL_WIDTH / 2.0f,
                LEVEL_HEIGHT / 2.0f, (LEVEL_HEIGHT * 2.7f), LEVEL_HEIGHT, assetManager.getBitmap("backgroundM"), this);
        basketSprite = new BasketSprite(this);
        addBananaSprites(10);

    }

    /**
     * Add a variable number of BananaSprites to ArrayList of BananaSprites
     *
     * @param numberOfBananas number of bananas to add to list
     */
    protected void addBananaSprites(int numberOfBananas) {
        for (int i = 0; i < numberOfBananas; i++) {
            bananaSprite = new BananaSprite(this, mLayerViewport);
            bananaSprites.add(bananaSprite);
        }
        bananasReleased += numberOfBananas;
    }

    /**
     * Screen and layer viewport objects
     */
    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;
    /**
     * Initialise the screen viewport
     * Adapted from the SpaceshipDemoScreen in GAGE
     * @return
     */
    public void initScreen() {
        mScreenViewport = new ScreenViewport(0, 0, mGame.getScreenWidth(),
                mGame.getScreenHeight());

        if (mScreenViewport.width > mScreenViewport.height)
            mLayerViewport = new LayerViewport(240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240,
                    240.0f * mScreenViewport.height / mScreenViewport.width);
        else
            mLayerViewport = new LayerViewport(240.0f * mScreenViewport.height
                    / mScreenViewport.width, 240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240);
    }

    /**
     * Update method from superclass
     * Calls update on catcher sprite
     * Calls update on all of the banana sprites
     * Checks banana sprite collisions
     * Removes bananas which are off the screen
     * Releases bananas
     *
     * @param elapsedTime Elapsed time information for the frame
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        basketSprite.update(elapsedTime, mLayerViewport, mScreenViewport);
        //Iterate through each banana in list
        for (int i = 0; i < bananaSprites.size(); i++) {
            bananaSprite = bananaSprites.get(i);
            bananaSprite.update(elapsedTime);
            //Check if banana caught
            handleBananaCollision(bananaSprite, basketSprite);
            //Remove any banana sprites that are off the screen
            removeBananaIfOffScreen(bananaSprite);
        }
        //Handle the release of bananas
        releaseBananas();

    }

    /**
     * Change gamescreen
     * @param gameScreen Gamescreen to change to.
     */
    protected void changeToScreen(GameScreen gameScreen) {
        mGame.getScreenManager().removeScreen(this.getName());
        mGame.getScreenManager().addScreen(gameScreen);
    }

    /**
     * Decide next screen to load
     * If null cardscreen the game must of been called via scret button on main menu
     * If CardScreen is not null load this instance again.
     * Turn off game sound
     */
    protected void decideNextScreen() {
        if (cardScreen == null) {
            changeToScreen(new MainMenuScreen(mGame));
        } else {
            changeToScreen(cardScreen);
            cardScreen.toggleSound();
        }
        toggleSound();
    }

    /**
     * Manages the releasing of banana sprites
     * Checks if enough bananas released and if so it ends the mini game
     * otherwise it keeps adding more bananas when there are only 2 bananas in the list
     */
    protected void releaseBananas() {
        if (bananasReleased >= bananasToRelease) decideNextScreen();
        else if (bananaSprites.size() < 3) addBananaSprites(4);
    }

    /**
     * Called from update method
     * Decides if a banana has collided
     * Checks banana type
     * if good banana, 1 point added to score and banana is removed from list
     * if bad banana, 5 points are taken from the score and banana is removed from list
     * if super banana, 3 points added to score, catcher sprite size increased and banana removed from list
     *
     * @param bananaSprite
     */
    public void handleBananaCollision(BananaSprite bananaSprite, BasketSprite basketSprite) {
        if (bananaSprite.getBound().intersects(basketSprite.getBound())) {
            if (getGame().isAdded()) playCollisionSound(selectSoundEffect(bananaSprite.getBananaType()));
            switch (bananaSprite.getBananaType()) {
                case GOOD: {
                    bananaSprites.remove(bananaSprite);
                    addToScore(1);
                    break;
                }
                case BAD: {
                    bananaSprites.remove(bananaSprite);
                    addToScore(-5);
                    break;
                }
                case SUPER: {
                    bananaSprites.remove(bananaSprite);
                    addToScore(3);
                    basketSprite.setSuperSizeMode();
                    break;
                }

            }
        }

    }

    /**
     * Overrides draw method from Sprite
     * Draws the background, character, bananas and text on the canvas
     *
     * @param elapsedTime Elapsed time information for the frame
     * @param graphics2D  Graphics instance used to draw the screen
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        background.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        basketSprite.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        for (int i = 0; i < bananaSprites.size(); i++) {
            bananaSprite = bananaSprites.get(i);
            bananaSprite.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }
        graphics2D.drawText("Bananas caught: " + score, 50, 50, paint);
    }

    /**
     * Check if banana sprite is off the bottom of the screen and remove from arraylist if it is
     *
     * @param bananaSprite bananaSprite to be checked
     */
    protected void removeBananaIfOffScreen(BananaSprite bananaSprite) {
        if (bananaSprite.position.y < mLayerViewport.getBottom() - bananaSprite.getBound().halfHeight) {
            bananaSprites.remove(bananaSprite);
        }
    }

    /**
     * Initialise paint object
     */
    protected TextPaint initPaint() {
        TextPaint paintr = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paintr.setColor(Color.BLACK);
        paintr.setTextSize(50);
        paintr.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        return paintr;
    }


    /**
     * Is called when a banana has been caught and plays a sound effect
     *
     * @param bananaType used to decided which sound effect to play
     */
    protected AssetFileDescriptor selectSoundEffect(BananaType bananaType) {
        AssetFileDescriptor afd;
        switch (bananaType) {
            case GOOD:
                afd = getGame().getResources().openRawResourceFd(R.raw.good);
                break;
            case BAD:
                afd = getGame().getResources().openRawResourceFd(R.raw.bad);
                break;
            case SUPER:
                afd = getGame().getResources().openRawResourceFd(R.raw.powerup);
                break;
            default:
                afd = getGame().getResources().openRawResourceFd(R.raw.good);
        }


        return afd;

    }
    /**
     * Music object for collision sounds
     */
    public Music collisionSound;

    /**
     * Plays collision sound given a AssetFileDescriptor
     * @param afd AssetFileDescriptor Sound file to play
     */
    private void playCollisionSound(AssetFileDescriptor afd) {
        collisionSound = new Music(afd);
        collisionSound.setVolume(10f);
        collisionSound.play();
    }
}

