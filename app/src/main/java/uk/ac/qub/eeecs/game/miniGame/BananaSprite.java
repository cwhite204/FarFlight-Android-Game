package uk.ac.qub.eeecs.game.miniGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.Animation;

/**
 * BananaSprite is an extension of the sprite class. Upon initialisation the bananas
 * variables are randomly allocated. The sprite also uses sprite animation to give a rotation effect
 *
 * @author Andrew Bell
 */

public class BananaSprite extends Sprite {
    /**
     * Instance of Game
     */
    private Game mGame = mGameScreen.getGame();

    /**
     * Instance of AssetStore to manage games assets
     */
    private AssetStore assetManager = mGame.getAssetManager();

    /**
     * Animation object to manage banana spin animation
     */
    protected Animation spinAnimation;

    /**
     * LayerViewport instance to work out screen dimensions
     */
    private LayerViewport mLayerViewport;

    /**
     * Random object for generating random numbers for varaiables
     */
    private Random random = new Random();

    /**
     * Rects needed for drawing bitmap on canvas
     */
    private Rect sourceRect = new Rect(), screenRect = new Rect();


    /**
     * Instance of BananaType object which is an enum of varios types of banana
     */
    private BananaType bananaType;

    /**
     * Getter for assigned banana type
     *
     * @return BananaType enum
     */
    public BananaType getBananaType() {
        return bananaType;
    }


    /**
     * Constructor for banana sprite
     *
     * @param gameScreen
     * @param mLayerViewport
     */
    public BananaSprite(GameScreen gameScreen, LayerViewport mLayerViewport) {
        super(50, 500, 96, 48, null, gameScreen);
        this.mLayerViewport = mLayerViewport;
        mBitmap = assetManager.getBitmap("bananaBack");
        randomiseBanana();
        setSpriteAnimation();
    }

    public BananaSprite(GameScreen gameScreen, LayerViewport mLayerViewport, BananaType bananaType) {
        super(50, 500, 96, 48, null, gameScreen);
        this.mLayerViewport = mLayerViewport;
        mBitmap = assetManager.getBitmap("bananaBack");
        randomiseBanana();
        this.bananaType = bananaType;
        setSpriteAnimation();
    }

    /**
     * Randomise banana type, x position, velocity and angular velocity
     */
    private void randomiseBanana() {
        bananaType = BananaType.GOOD.pickBananaType(random.nextInt(20));
        setPosition(random.nextFloat() * mLayerViewport.getWidth(), mLayerViewport.getHeight() - 20f);
        velocity.y = (random.nextFloat() + 1) * -100f;
        velocity.x = (random.nextFloat() + 1) * 100;
        orientation = 90f;
        angularVelocity = random.nextFloat() * 200;
    }

    /**
     * Setup animation object with animation bitmap and start animation loop
     */
    protected void setSpriteAnimation() {
        Bitmap animationBitmap;
        switch (bananaType) {
            case GOOD:
                animationBitmap = assetManager.getBitmap("banana");
                break;
            case BAD:
                animationBitmap = assetManager.getBitmap("badBanana");
                break;
            case SUPER:
                animationBitmap = assetManager.getBitmap("superBanana");
                break;
            default:
                animationBitmap = assetManager.getBitmap("banana");
                break;

        }
        spinAnimation = new Animation(animationBitmap, 10);
        spinAnimation.play(1.5, true);
    }

    /**
     * Check for banana being in left or right bounds and if not reverse its x velocity
     */
    private void keepInBounds() {
        if (position.x < mLayerViewport.getRight() - getBound().halfWidth) {
            velocity.x *= -1;
        } else if (position.x > mLayerViewport.getRight() + getBound().halfWidth) {
            velocity.x *= -1;
        }
    }

    /**
     * Get bitmap frame required and set it to the sprites mBitmap variable
     */
    private void setBitmapOfAnimation() {
        spinAnimation.getSourceRect(sourceRect);
        mBitmap = assetManager.getBitmap("bananaBack");
        mBitmap = mBitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mBitmap);
        screenRect.set(0, 0, 48, 24);
        canvas.drawBitmap(spinAnimation.getBitmap(), sourceRect, screenRect, null);
    }

    /**
     * Update methods that need updated every frame
     *
     * @param elapsedTime
     */
    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);
        spinAnimation.update(elapsedTime.stepTime);
        keepInBounds();
        setBitmapOfAnimation();
    }

}
