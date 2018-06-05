package uk.ac.qub.eeecs.game.gameScreens;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.R;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.miniGame.BananaGameScreen;

/**
 * This class extends the GameScreen class
 * This is where the main menu is displayed, including
 * buttons to go to different screens
 *
 * @author Christopher White
 */
public class MainMenuScreen extends GameScreen {

    /**
     * Sprites
     */
    private Sprite mPlane, mLogo;

    /**
     * Main menu buttons
     */
    private PushButton mPlayButton;
    private PushButton mScoreboardButton;
    private PushButton mSecretButton;

    /**
     * Holds the background sound object
     */
    private Music mSound;

    /**
     * Object to manage Audio fle resources
     */
    private AssetFileDescriptor afd;

    /**
     * Layer and Screen viewports used for positioning
     */
    private LayerViewport mLayerViewport;
    private ScreenViewport mScreenViewport;

    /**
     * GameObject object which holds background bitmap and is first thing drawn on screen
     */
    private GameObject mFFBackground;

    /**
     * Constructor for MainMenuScreen
     *
     * @param game
     */
    public MainMenuScreen(Game game) {
        super("MainMenuScreen", game);

        initScreen(game);
        AssetStore assetManager = mGame.getAssetManager();
        loadAssets(assetManager);
        initSprites(game, assetManager);
        initSpriteAnimation();
        loadSound(game);
    }

    /**
     * Update the main menu screen
     *
     * @param elapsedTime Elapsed time information for the frame
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        mPlane.update(elapsedTime);

        // If the plane is off the screen the x position of it is reset
        if(mPlane.position.x > mLayerViewport.getWidth()*2) {
            mPlane.setPosition(0-400f, mLayerViewport.getHeight()-350);
        }

        // Allows for user touch input
        Input input = mGame.getInput();

        // Create list of touch events
        List<TouchEvent> touchEvents = input.getTouchEvents();

        if (touchEvents.size() > 0) {
            Context mContext = mGame.getContext();
            TouchEvent touchEvent = touchEvents.get(0);

            mPlayButton.update(elapsedTime);
            mScoreboardButton.update(elapsedTime);
            mSecretButton.update(elapsedTime);

            // If play button is pressed user is taken to CardScreen screen
            if (mPlayButton.isPushTriggered())
                changeToScreen(new CardScreen(mGame));

            // If scoreboard button is pressed user is taken to sccreboard screen
            else if (mScoreboardButton.isPushTriggered())
                changeToScreen(new ScoreboardScreen(mGame));

            // If secret button is pressed user is taken to the minigame
            else if (mSecretButton.isPushTriggered())
                changeToScreen(new BananaGameScreen(mGame));
        }
    }

    /**
     * Draw the main menu screen
     *
     * @param elapsedTime Elapsed time information for the frame
     * @param graphics2D  Graphics instance used to draw the screen
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.BLUE);

        // Draw background
        mFFBackground.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        // Draw PushButtons
        mPlayButton.draw(elapsedTime, graphics2D);
        mScoreboardButton.draw(elapsedTime, graphics2D);
        mSecretButton.draw(elapsedTime, graphics2D);

        // Draw Sprites
        mLogo.draw(elapsedTime,graphics2D);
        mPlane.draw(elapsedTime, graphics2D);
    }

    /**
     * Used to change from this screen to another screen
     *
     * @param screen
     */
    private void changeToScreen(GameScreen screen) {
        mGame.getScreenManager().removeScreen(this.getName());
        mGame.getScreenManager().addScreen(screen);
    }

    /**
     * Toggle sounds on and off
     */
    public void toggleSound(){
        if(mSound.isPlaying()){
            mSound.stop();
        }
        else mSound.play();
    }

    /**
     * Initialize the viewports which are used to size different sprites
     *
     * @param game
     * @return true upon completion
     */
    protected boolean initScreen(Game game) {
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

        if (mScreenViewport.width > mScreenViewport.height)
            mLayerViewport = new LayerViewport(240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240,
                    240.0f * mScreenViewport.height / mScreenViewport.width);
        else
            mLayerViewport = new LayerViewport(240.0f * mScreenViewport.height
                    / mScreenViewport.width, 240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240);

        return true;
    }

    /**
     * Load assets into the asset manager
     *
     * @param assetManager
     * @return true upon completion
     */
    protected boolean loadAssets(AssetStore assetManager) {
        // Load in background and sprites
        assetManager.loadAndAddBitmap("FFBackground", "img/Backgrounds/FFBackground.png");
        assetManager.loadAndAddBitmap("plane", "img/MainMenu/plane.png");
        assetManager.loadAndAddBitmap("logoFF", "img/MainMenu/FarFlight-logo.png");

        // Load in buttons
        assetManager.loadAndAddBitmap("PlayButton", "img/MainMenu/button_play.png");
        assetManager.loadAndAddBitmap("ScoreboardButton", "img/MainMenu/button_scoreboard.png");
        assetManager.loadAndAddBitmap("SecretButton", "img/BananaMiniGame/bananaBack.png");

        return true;
    }

    /**
     * Initialize all sprites
     *
     * @param game
     * @param assetManager
     * @return true upon completion
     */
    protected boolean initSprites(Game game, AssetStore assetManager) {

        // Variables used to poition sprites on the screen
        int spacingX = game.getScreenWidth() / 5;
        int spacingY = game.getScreenHeight() / 3;
        float LEVEL_VALUE = 1000.0f;

        // Define size and position of background
        mFFBackground = new GameObject(LEVEL_VALUE / 2.0f,
                LEVEL_VALUE / 2.0f, (LEVEL_VALUE * 2.7f), LEVEL_VALUE, getGame()
                .getAssetManager().getBitmap("FFBackground"), this);

        // Define size and position of sprites
        mPlane = new Sprite(mLayerViewport.halfWidth + 60f, mLayerViewport.halfHeight + 120f,
                400f, 184f, assetManager.getBitmap("plane"), this);
        mLogo = new Sprite(spacingX * 2.5f, mLayerViewport.halfHeight + 120f,
                700f, 200f, assetManager.getBitmap("logoFF"), this);

        // Define size and position of buttons
        mPlayButton = new PushButton(spacingX * 2.5f, spacingY * 1.2f, 700, 200, "PlayButton", this);
        mScoreboardButton = new PushButton(spacingX * 2.5f, spacingY * 2.0f, 700, 200, "ScoreboardButton", this);
        mSecretButton = new PushButton(950, 50, 200, 200, "SecretButton", this);

        return true;
    }

    /**
     * Initialize the plane animation
     *
     * @return true upon completion
     */
    protected boolean initSpriteAnimation() {

        mPlane.setPosition(0-400f,mLayerViewport.getHeight()-350);
        mPlane.velocity.x=700;

        return true;
    }

    /**
     * Load the background sound
     *
     * @param game
     * @return true upon completion
     */
    protected boolean loadSound(Game game) {
        // Loading background sound
        if(!getGame().isAdded())return false;
        afd = game.getResources().openRawResourceFd(R.raw.background_sound);
        mSound = new Music(afd);
        mSound.setVolume(10.0f);
        mSound.setLopping(true);

        return true;
    }


}
