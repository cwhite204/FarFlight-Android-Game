package uk.ac.qub.eeecs.game.miniGame;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchHandler;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.InputHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;


/**
 * BasketSprite extends the Sprite class from GAGE
 * It is a simple sprite that can be moved along the screens x-axis
 * by touch input.
 * The size of the sprite changes based on bananas collected or if being touched
 * @author Andrew Bell
 */

public class BasketSprite extends Sprite {
    /**
     * Instance of Game needed to track touch events
     */
    protected Game mGame = mGameScreen.getGame();

    /**
     * Instance of input used to manage touch events
     */
    protected Input input;

    /**
     * Stores the touch location as a vector
     */
    protected Vector2 touchLocation = new Vector2();

    /**
     * Asset Store instance needed to load manage the games assets
     */
    protected AssetStore assetManager = mGame.getAssetManager();

    /**
     * Bounding boxes for the sprites different sizes
     */
    protected BoundingBox originalBound, bigBound, superBound;

    /**
     * Setter for superSizeMode variable so that it can be enable from gamescreen when super banana has been caught
     */
    public void setSuperSizeMode() {
        superSizeMode = true;
        superBound = new BoundingBox(superBound.x, superBound.y, superBound.halfWidth * 1.2f, superBound.halfHeight * 1.2f);
    }

    /**
     * This variable indicates whether the superBound should be used as the sprites bounding box
     */
    protected boolean superSizeMode = false;

    /**
     * Constructor for the sprite
     * Sets the position and the bounding boxes
     *
     * @param gameScreen
     */
    public BasketSprite(GameScreen gameScreen) {
        super(50, 50, 100, 50, null, gameScreen);
        setPosition(50, 50);
        mBitmap = assetManager.getBitmap("basket");
        originalBound = mBound;
        bigBound = mBound = new BoundingBox(mBound.x, mBound.y, mBound.halfWidth * 1.05f, mBound.halfHeight * 1.05f);
        superBound = mBound = new BoundingBox(mBound.x, mBound.y, mBound.halfWidth * 1.2f, mBound.halfHeight * 1.2f);
    }

    /**
     * Update method called using games loop
     *
     * @param elapsedTime Elapsed time information for the frame
     * @param layerViewport Gamescreens LayerViewport
     * @param screenViewport GameScreens ScreenViewport
     */
    public void update(ElapsedTime elapsedTime, LayerViewport layerViewport, ScreenViewport screenViewport) {
        super.update(elapsedTime);
        spriteTouchHandler(layerViewport, screenViewport);


    }

    /**
     * Called by sprites update method
     * Checks for touch on sprite then sets sprites position and bounds
     * @param layerViewport Needed to convert touch location
     * @param screenViewport Needed to convert touch location
     */
    protected void spriteTouchHandler(LayerViewport layerViewport, ScreenViewport screenViewport) {
        input = mGame.getInput();
        // Check for any touch events on the card sprite
        for (int idx = 0; idx < TouchHandler.MAX_TOUCHPOINTS; idx++) {
            if (input.existsTouch(idx)) {
                InputHelper.convertScreenPosIntoLayer(screenViewport, input.getTouchX(idx), input.getTouchY(idx), layerViewport, touchLocation);
                setPositionOnTouch(layerViewport, touchLocation);
                touchedSetBounds();
                return;
            } else notTouchedSetBounds();
        }
    }


    /**
     * Set sprites position
     * @param mLayerViewport GameScreens layerViewport to get the bottom of the screen
     * @param touchLocation Vector2 of where the touch input was
     */
    protected void setPositionOnTouch(LayerViewport mLayerViewport, Vector2 touchLocation) {
        //Set position of sprite to x touch location
        position.x = touchLocation.x;
        //Update Y position as bounding box may have increased if superMode activated
        position.y = mLayerViewport.getBottom() + mBound.halfHeight;

    }

    //Set super bound if super size mode
    //Else set big bound to indicate touch feedback
    protected BoundingBox touchedSetBounds() {
        if (superSizeMode) {
            mBound = superBound;
        } else mBound = bigBound;
        return mBound;
    }

    protected BoundingBox notTouchedSetBounds() {
        //Set super bound if super size mode
        //Else set original bound as not being touched
        if (superSizeMode) {
            mBound = superBound;

        } else mBound = originalBound;
        return mBound;
    }
}
