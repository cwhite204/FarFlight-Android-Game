package uk.ac.qub.eeecs.game.gameScreens;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextPaint;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;


/**
 * This class extends the GameScreen class
 * This is where the splash screen is displayed
 *
 * @auther Christopher White
 */
public class SplashScreen extends GameScreen{

    /**
     * Sprites
     */
    private Sprite logo;

    /**
     * Layer and Screen viewports used for positioning
     */
    private LayerViewport mLayerViewport;
    private ScreenViewport mScreenViewport;

    /**
     * Handler used for timing
     */
    final Handler handler = new Handler();

    /**
     * Constructor for SplashScreen
     *
     * @param game
     */
    public SplashScreen(Game game) {
        super("SplashScreen", game);

        initScreen(game);
        AssetStore assetManager = mGame.getAssetManager();
        loadAssets(assetManager);
        initSprites(game, assetManager);
    }

    /**
     * Delays going to main menu so user can see splash screen
     *
     * Method contains modified code from StackOverflow:
     * https://stackoverflow.com/questions/3072173/how-to-call-a-method-after-a-delay-in-android
     */
    private void delayScreenChange() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeToScreen(new MainMenuScreen(mGame));
            }
        }, 500);
    }

    /**
     * Update the splash screen
     *
     * @param elapsedTime Elapsed time information for the frame
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
    }

    /**
     * Draw the splash screen
     *
     * @param elapsedTime Elapsed time information for the frame
     * @param graphics2D  Graphics instance used to draw the screen
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);

        TextPaint studioName = initPaint();

        logo.draw(elapsedTime, graphics2D);

        graphics2D.drawText("Headless Viking Studios", mScreenViewport.width/2, mLayerViewport.halfHeight -100, studioName);

        delayScreenChange();
    }

    /**
     * Used the change from this screen to another screen
     *
     * @param screen
     */
    private void changeToScreen(GameScreen screen) {
        mGame.getScreenManager().removeScreen(this.getName());
        mGame.getScreenManager().addScreen(screen);
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
     * @return true on completion
     */
    protected boolean loadAssets(AssetStore assetManager) {
        // Load in logo image
        assetManager.loadAndAddBitmap("logo", "img/logo.jpg");

        return true;
    }

    /**
     * Initialize sprites
     *
     * @param game
     * @param assetManager
     * @return true on completion
     */
    protected boolean initSprites(Game game, AssetStore assetManager) {
        int spacingX = game.getScreenWidth() / 5;

        logo = new Sprite(spacingX * 2.5f, mLayerViewport.halfHeight+600f,
                1000f, 1200f, assetManager.getBitmap("logo"), this);

        return true;
    }

    /**
     * Initialize TextPaint object
     *
     * @return initialized TextPaint object
     */
    protected TextPaint initPaint() {
        TextPaint studioName = new TextPaint();
        studioName.setTextSize(50);
        studioName.setTextAlign(Paint.Align.CENTER);
        studioName.setColor(Color.BLACK);
        studioName.setTypeface(Typeface.create("Arial", Typeface.BOLD));

        return studioName;
    }
}