package uk.ac.qub.eeecs.game;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import uk.ac.qub.eeecs.gage.R;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchHandler;
import uk.ac.qub.eeecs.gage.util.InputHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * This class is an extension of the Card class.
 * It contains additional methods that are only needed in using the card specifically on this screen
 *
 * @author Andrew Bell
 *         Adding Authors: Christopher Patrick McLearnon and Matthew Downey
 */

public class InteractableCard extends Card {

    private Boolean cardTouchingLeftHotspot = false, cardTouchingRightHotspot = false;
    /**
     * Boolean value that should be set true on ly if card has been flicked away and is still on screen
     */
    private boolean cardAnimatingAway = false;

    /**
     * To be used to determine the card flip animation matrix
     */
    protected float flipScale = -1f;

    /**
     * Boolean If card should be flipped
     */
    public boolean flipCard = true;

    /**
     * Manages audio resource files
     */
    private AssetFileDescriptor afd;

    /**
     * Music object that contains sound for card swipe effect
     */
    private Music mSwipe;

    /**
     * If touch is enabled. Eg, set false when card is imitating swipe
     */
    private boolean interactionEnabled = true;


    /**
     * @param gameScreen       CardScreen
     * @param charID           Character ID int
     * @param name             Character Name string
     * @param description      Card description string
     * @param leftSwipeAction  Card left swipe action text string
     * @param rightSwipeAction Card right swipe action text string
     * @param lStoryPathway    Card left swipe story pathway int
     * @param rStoryPathway    Card right swipe story pathway int
     * @param lStatEffect      Card left swipe stat effects int array
     * @param rStatEffect      Card right swipe stat effects int array
     */
    public InteractableCard(GameScreen gameScreen, int charID, String name, String description, String leftSwipeAction,
                            String rightSwipeAction, int lStoryPathway, int rStoryPathway, int[] lStatEffect, int[] rStatEffect) {
        super(gameScreen, charID, name, description, leftSwipeAction, rightSwipeAction, lStoryPathway, rStoryPathway, lStatEffect, rStatEffect);

    }

    private final float SWIPE_ANIMATION_VELOCITY = 1000f;
    private final float SWIPE_ANGULAR_VELOCITY = 70f;

    /**
     * When card has been dropped on either hotspot this method is called.
     * It sets the card a velocity so it moves depending on side dropped
     * Activates playSwipeSound method
     *
     * @param choice char 'l' for left hotspot or 'r' for right hotspot
     * @author Andrew Bell
     */
    public void animateCardOffScreen(char choice) {
        if (choice == 'l') {
            velocity.x = -SWIPE_ANIMATION_VELOCITY;
            angularVelocity = -SWIPE_ANGULAR_VELOCITY;
        } else if (choice == 'r') {
            velocity.x = SWIPE_ANIMATION_VELOCITY;
            angularVelocity = SWIPE_ANGULAR_VELOCITY;
        }
        //Toggle card animating away so this velocity is only set once
        cardAnimatingAway = !cardAnimatingAway;
    }

    /**
     * Called from animateCardAway
     * This methods function is to play a swipe sound effect
     *
     * @author: Christopher Patrick McLearnon
     */
    public void playSwipeSound(AssetFileDescriptor afd) {
        mSwipe = new Music(afd);
        mSwipe.setVolume(100, 100);
        mSwipe.play();
    }

    public AssetFileDescriptor loadSwipeSound() {
        AssetFileDescriptor afd;
        afd = mGameScreen.getGame().getResources().openRawResourceFd(R.raw.card_swipe_effect);
        return afd;
    }

    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);
        wasTouching = isTouching;
        if (interactionEnabled) cardTouchHandler(mLayerViewport, mScreenViewport);
    }

    /**
     * True if card is currently being touched, false if not being touched
     */
    private Boolean isTouching = false;

    public Boolean getTouching() {
        return isTouching;
    }

    /**
     * Instance of Input from game engine, manages games input events
     */
    private Input input;
    /**
     * Input touch location as a vector2 object
     */
    private Vector2 touchLocation = new Vector2();


    /**
     * Set card position and rotation on touch within bounds
     *
     * @param layerViewport To measure bounds
     * @author Andrew Bell
     */
    protected void setCardLocationOnTouch(LayerViewport layerViewport) {
        Boolean boundLeft, boundRight;
        Float padding = 12f;
        boundLeft = (mBound.getLeft() >= padding || touchLocation.x > position.x);
        boundRight = (mBound.getRight() <= layerViewport.getWidth() - padding || touchLocation.x < position.x);
        if (boundLeft && boundRight) {
            position.x = touchLocation.x;
            orientation = (layerViewport.halfWidth - touchLocation.x) / -20f;

        }
    }

    /**
     * Handles input on card. Checks if card has been touched and sets position and bounds.
     *
     * @param layerViewport  Game screens LayerViewport to work out touch location
     * @param screenViewport GameScreens ScreenViewport to work out touch location
     * @author Andrew Bell
     */
    public void cardTouchHandler(LayerViewport layerViewport, ScreenViewport screenViewport) {
        input = mGame.getInput();
        setCardSide(layerViewport);

        // Check for any touch events on the card sprite
        for (int idx = 0; idx < TouchHandler.MAX_TOUCHPOINTS; idx++) {
            if (input.existsTouch(idx)) {
                InputHelper.convertScreenPosIntoLayer(screenViewport, input.getTouchX(idx), input.getTouchY(idx), layerViewport, touchLocation);
                if (mBound.contains(touchLocation.x, touchLocation.y)) {
                    setBigBound();
                    isTouching = true;
                    setCardLocationOnTouch(layerViewport);
                }
                return;
            } else {
                isTouching = false;
                //Set Position and Orientation to center when touch is let go
                setOriginalBound();

            }
        }
    }


    private final float FLIP_SCALE_ADDITION = 0.10f;

    /**
     * Decides the level the matrix should be scaled and sets bitmap to either front or back
     * Resulting in the 'Card flip' effect
     *
     * @return Float flipscale. The Level that the matrix should be scaled
     * @author Andrew Bell
     */
    public float getFlipScaleAndSetBitmap() {
        if (flipScale < 0) {
            toggleCardBitmap(false);
            flipScale += FLIP_SCALE_ADDITION;
        } else if (flipScale >= 0 && flipScale < 1) {
            toggleCardBitmap(true);
            flipScale += FLIP_SCALE_ADDITION;
        } else {
            flipCard = false;
        }
        return flipScale;
    }


    /**
     * Called in update method when the needs to be flipped
     * Modifies the card matrix gradually based on flip scale resulting in a flip animation on the bitmap
     * on the cards x axis.
     *
     * @param flipScale level that the matrix should be scaled
     * @return Matrix Matrix of the bitmap flipped scaled horizontally
     * author: Andrew Bell
     * Adapted from https://stackoverflow.com/questions/7774618/flipping-a-bitmap-in-android-help
     **/
    public Matrix getHorizontalFlipMatrix(float flipScale) {
        Matrix flipHorizontalMatrix = new Matrix();
        Bitmap cardBitmap = getBitmap();
        flipHorizontalMatrix.setScale(flipScale, 1, cardBitmap.getWidth() / 2, cardBitmap.getHeight() / 2);
        int centreX = (mScreenViewport.width - (cardBitmap.getWidth()) * 18 / 10) / 2;
        int centreY = (mScreenViewport.height - (cardBitmap.getHeight()) * 18 / 10) / 2;
        flipHorizontalMatrix.postScale(1.8f, 1.8f);
        flipHorizontalMatrix.postTranslate(centreX, centreY);
        return flipHorizontalMatrix;
    }

    /**
     * Resets card flip variables so its ready to flip again
     */
    public void resetCardFlip() {

        flipCard = true;
        flipScale = -1;
    }

    /**
     * @param layerViewport
     * @author Matthew Downey
     */
    protected void setCardSide(LayerViewport layerViewport) {
        if (position.x < (layerViewport.getWidth() / 2)) {
            left = true;
            right = false;
        } else if (position.x > (layerViewport.getWidth() / 2)) {
            right = true;
            left = false;
        }
    }


    /**
     * To be used in cardHint which is updated in update so cannot be a local variable
     */
    private boolean cardMoveRight = true;

    /**
     * If card should be set to center
     */
    protected boolean cardCenter;
    /**
     * Final int, is te distance the card swipes during hint
     */
    private final int CARD_SWIPE_SIZE = 260;

    /**
     * Final hint, is the speed the card moves at during card hint
     */
    private final float SWIPE_HINT_VELOCITY = 800;

    /**
     * Final int, is the angluar velocity the card moves with during card hint
     */
    private final int SWIPE_HINT_ANGULAR_VELOCITY = 12;

    /**
     * Uses basic boolean algebra AI to immitate card movement. Called through update method.
     * @author Andrew Bell
     */
    public void cardSwipeHint(float cardMid) {
        if (cardCenter) {
            return;
        }
        if (cardMoveRight && (position.x < cardMid) && (position.x > cardMid - 20)) {
            cardCenter = true;
            setCardToMiddle();
            interactionEnabled = true;
        } else if ((position.x > cardMid - CARD_SWIPE_SIZE) && !cardMoveRight) {
            velocity.x = -SWIPE_HINT_VELOCITY;
            angularVelocity = -SWIPE_HINT_ANGULAR_VELOCITY;
            interactionEnabled = false;
        } else if ((position.x < cardMid + CARD_SWIPE_SIZE) && cardMoveRight) {
            velocity.x = SWIPE_HINT_VELOCITY;
            angularVelocity = SWIPE_HINT_ANGULAR_VELOCITY;
        } else if (position.x >= cardMid + CARD_SWIPE_SIZE) {
            cardMoveRight = false;
        } else if (position.x <= cardMid + CARD_SWIPE_SIZE) {
            cardMoveRight = true;
        }
    }

    //GETTERS AND SETTERS @author Andrew Bell

    public boolean isCardAnimatingAway() {
        return cardAnimatingAway;
    }

    public void setCardAnimatingAway(boolean cardAnimatingAway) {
        this.cardAnimatingAway = cardAnimatingAway;
    }

    public Boolean getCardTouchingLeftHotspot() {
        return cardTouchingLeftHotspot;
    }

    public void setCardTouchingLeftHotspot(Boolean cardTouchingLeftHotspot) {
        this.cardTouchingLeftHotspot = cardTouchingLeftHotspot;
    }

    public Boolean getCardTouchingRightHotspot() {
        return cardTouchingRightHotspot;
    }

    public void setCardTouchingRightHotspot(Boolean cardTouchingRightHotspot) {
        this.cardTouchingRightHotspot = cardTouchingRightHotspot;
    }

    /**
     * Booleans to determine touch events
     */
    private Boolean wasTouching = false;

    public Boolean getWasTouching() {
        return wasTouching;
    }

    public void setWasTouching(Boolean wasTouching) {
        this.wasTouching = wasTouching;
    }
}
