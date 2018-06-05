package uk.ac.qub.eeecs.game.gameScreens;

import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Paint;

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
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.Deck;
import uk.ac.qub.eeecs.game.InteractableCard;
import uk.ac.qub.eeecs.game.SurvivalStatistics;

/**
 * This class extends the GameScreen class
 * This is where the card sprite will be displayed and is where the main game is played.
 * One InteractableCard is managed on this screen and when swiped causes effects to the survivalStatistic objects
 *
 * @authors Andrew Bell, Christopher Patrick McLearnon, Matthew Downey
 */
public class CardScreen extends GameScreen {
    /**
     * Interactable card sprite that is on top of the card deck
     */
    private InteractableCard interactableCard;

    /**
     * Game objects that display cardPile (card pile background) and leftGrad/rightGrad ( The gradients that appear on either side of the screen)
     */
    protected GameObject cardPile, leftGrad, rightGrad;

    /**
     * Instances of SurvialStatics for each of the 4 score measures
     */
    private SurvivalStatistics health, shelter, foodAndWater, morale;

    /**
     * Weeks survived will be the overall score
     */
    private int weeksSurvived = 0;

    public void setWeeksSurvived(int weeksSurvived) {
        this.weeksSurvived = weeksSurvived;
    }

    /**
     * GameObject object which holds background bitmap and is first thing drawn on screen
     */
    private GameObject background;


    /**
     * Constructor for CardScreen
     * Initialises card screen objects
     *
     * @param game Game object to which this screen belongs
     * @author Andrew Bell
     */
    public CardScreen(Game game) {
        super("CardScreen", game);
        AssetStore assetManager = game.getAssetManager();
        loadAssets(assetManager);
        initScreen();
        initSprites(assetManager);
        initBackgroundSound();
        initDeck();
        paint = initPaint();
    }

    /**
     * Ensures fragment is attached to an activity before attempting to load set and toggle sound
     *
     * @author Andrew Bell
     */
    protected void initBackgroundSound() {
        if (!getGame().isAdded()) return;
        setSound(loadSound(R.raw.background_sound));
        toggleSound();

    }

    /**
     * Converts a raw ID into an AssetFileDescriptor
     *
     * @param rawID The ID for the sound file
     * @return AssestFileDescriptor to pass into setSound method
     * @authors Christopher Patrick McLearnon, Andrew Bell
     */
    protected AssetFileDescriptor loadSound(int rawID) {
        AssetFileDescriptor afd;
        afd = getGame().getResources().openRawResourceFd(rawID);
        return afd;
    }

    /**
     * Holds the background sound object
     */
    protected Music mSound;

    /**
     * Given a AssetFileDescriptor, initialises Music object mSound
     *
     * @param afd AssetFileDescriptor to read the sound file from
     *            authors Christopher Patrick McLearnon, Andrew Bell
     */
    protected void setSound(AssetFileDescriptor afd) {
        mSound = new Music(afd);
        mSound.setVolume(10.0f);
        mSound.setLopping(true);
    }


    /**
     * Checks if card is in a hotspot and then sets the boolean variable indicating so on the card
     *
     * @author Andrew Bell
     */
    protected void setIfCardInHotspot() {
        interactableCard.setCardTouchingLeftHotspot(interactableCard.getBound().intersects(leftGrad.getBound()));
        interactableCard.setCardTouchingRightHotspot(interactableCard.getBound().intersects(rightGrad.getBound()));
    }

    /**
     * @return boolean True if card was dropped. ( Card was touching on the last update but is no longer touched)
     * @author Andrew Bell
     */
    protected boolean wasCardDropped() {
        //If card was touching and is no longer touching then it must have been dropped
        return (interactableCard.getWasTouching() && !interactableCard.getTouching());
    }

    /**
     * Update the cardscreen
     * The cardscreen needs to run methods that continuously check for card touching its hotspots,
     * listen and take action on card drop actions, show the card swipe hint on the first card
     * and prepare the next card i the current card has been swiped away
     *
     * @param elapsedTime Elapsed time information
     * @author Andrew Bell
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        interactableCard.update(elapsedTime);
        setIfCardInHotspot();
        if (wasCardDropped())
            onCardDropped(interactableCard.getCardTouchingLeftHotspot(), interactableCard.getCardTouchingRightHotspot());
        //If first story then show user that card can be swiped left/right
        if (deck.getIsIntro()) interactableCard.cardSwipeHint(mLayerViewport.halfWidth);
        //If card has been animating away and is now off the screen, load the next card and disable animation trigger
        if (hasCardAnimatedPastScreenLeft()) prepareNextCard('l');
        else if (hasCardAnimatedPastScreenRight()) prepareNextCard('r');


    }

    /**
     * @return boolean true if card is animating away currently and is now off the right side off the screen
     * @author Andrew Bell
     */
    protected boolean hasCardAnimatedPastScreenLeft() {
        return (interactableCard.isCardAnimatingAway() && (interactableCard.position.x < mLayerViewport.getLeft() - interactableCard.getBound().halfWidth));
    }

    /**
     * @return boolean true if card is animating away currently and is now off the left side off the screen
     * @author Andrew Bell
     */
    protected boolean hasCardAnimatedPastScreenRight() {
        return (interactableCard.isCardAnimatingAway() && (interactableCard.position.x > mLayerViewport.getRight() + interactableCard.getBound().halfWidth));
    }

    public InteractableCard getPlayerCard() {
        return interactableCard;
    }

    /**
     * Triggerd when card has been dropped. If card is in a hotspot it will animate away according the the side dropped.
     * If card is not in a hotspot it will position back to the middle of the viewport
     *
     * @param cardTouchingLeftHotspot
     * @param cardTouchingRightHotspot
     * @author Andrew Bell
     */
    protected void onCardDropped(Boolean cardTouchingLeftHotspot, Boolean cardTouchingRightHotspot) {
        //If card dropped on left/right zone then activate card animating away
        if (cardTouchingLeftHotspot) {
            interactableCard.animateCardOffScreen('l');
        } else if (cardTouchingRightHotspot) {
            interactableCard.animateCardOffScreen('r');
            // If card dropped somewhere other that left/right zone set it back to the middle of the screen
        } else interactableCard.setCardToMiddle();
        if (getGame().isAdded()) interactableCard.playSwipeSound(interactableCard.loadSwipeSound());
    }


    /**
     * Toggles the sound between playing and not playing
     *
     * @return boolean true if sound is playing
     * @author Andrew Bell
     */
    public boolean toggleSound() {
        if (mSound.isPlaying()) {
            mSound.stop();
        } else mSound.play();
        return mSound.isPlaying();
    }


    /**
     * When card dropped on either hotspot and the card has animated away
     * Increment weeks survived, Apply stat effects and get the next card ready
     *
     * @param choice Char l for left side dropped or r for right side dropped
     * @author: Andrew Bell
     */
    public void prepareNextCard(char choice) {
        weeksSurvived++;
        if (choice == 'l') {
            applyStatsEffect(interactableCard.getlStatEffect());
            deck.removeCard(interactableCard);
            interactableCard = deck.getCard(interactableCard.getlStoryPathway());
        } else {
            applyStatsEffect(interactableCard.getrStatEffect());
            deck.removeCard(interactableCard);
            interactableCard = deck.getCard(interactableCard.getrStoryPathway());
        }
        interactableCard.setCardToMiddle();
        interactableCard.resetCardFlip();
        interactableCard.setCardAnimatingAway(false);
    }


    private void applyStatsEffect(int[] effect) {
        morale.addToScore(effect[0], weeksSurvived);
        foodAndWater.addToScore(effect[1], weeksSurvived);
        health.addToScore(effect[2], weeksSurvived);
        shelter.addToScore(effect[3], weeksSurvived);
    }

    public int getWeeksSurvived() {
        return weeksSurvived;
    }

    // /////////////////////////////////////////////////////////////////////////
    // INITIALISE AND LOAD
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Load assets into the asset manager
     *
     * @param assetManager use this object to load the assets
     * @return true upon completion
     * @authors Andrew Bell, Christopher Patrick McLearnon
     */
    protected boolean loadAssets(AssetStore assetManager) {

        //Image for front of each character card - Text is just to demonstrate where text will be displayed
        assetManager.loadAndAddBitmap("background", "img/Backgrounds/FFBackground.png");
        assetManager.loadAndAddBitmap("card_template1", "img/Game/card_template1.png");
        //assetManager.loadAndAddBitmap("card_template2", "img/card_template2.png");
        assetManager.loadAndAddBitmap("cardback", "img/Game/Deck.png");
        assetManager.loadAndAddBitmap("leftGrad", "img/Game/leftGrad.png");
        assetManager.loadAndAddBitmap("rightGrad", "img/Game/rightGrad.png");
        //Images for each survival stat icon
        // For loop to cycle through each stat level image of each stat
        for (int i = 1; i <= 10; i++) {
            assetManager.loadAndAddBitmap("health-" + String.valueOf(i), "img/StatIcons/health-" + String.valueOf(i) + ".png");
            assetManager.loadAndAddBitmap("shelter-" + String.valueOf(i), "img/StatIcons/shelter-" + String.valueOf(i) + ".png");
            assetManager.loadAndAddBitmap("foodAndWater-" + String.valueOf(i), "img/StatIcons/foodAndWater-" + String.valueOf(i) + ".png");
            assetManager.loadAndAddBitmap("morale-" + String.valueOf(i), "img/StatIcons/morale-" + String.valueOf(i) + ".png");

        }
        return true;
    }


    /**
     * Game Level dimensions
     */
    private final float LEVEL_WIDTH = 1000.0f;
    private final float LEVEL_HEIGHT = 1000.0f;

    /**
     * Initialise all sprites here
     *
     * @param assetManager Instance of games assetManager
     * @authors Andrew Bell, Christopher Patrick McLearnon
     */
    private void initSprites(AssetStore assetManager) {
        // Create the background
        background = new GameObject(LEVEL_WIDTH / 2.0f,
                LEVEL_HEIGHT / 2.0f, (LEVEL_HEIGHT * 2.7f), LEVEL_HEIGHT, getGame()
                .getAssetManager().getBitmap("background"), this);
        leftGrad = new Sprite(0, 0, 300, 1000, assetManager.getBitmap("leftGrad"), this);
        rightGrad = new Sprite(mLayerViewport.getWidth(), 0, 300, 1000, assetManager.getBitmap("rightGrad"), this);

        health = new SurvivalStatistics("health", mLayerViewport.halfWidth - 300, mLayerViewport.halfHeight + 210, (float) (150 * 0.8),
                (float) (50 * 0.8), 50, mGame.getAssetManager().getBitmap("health-5"), this);

        shelter = new SurvivalStatistics("shelter", mLayerViewport.halfWidth - 100, mLayerViewport.halfHeight + 210, (float) (150 * 0.8),
                (float) (50 * 0.8), 50, mGame.getAssetManager().getBitmap("shelter-5"), this);

        foodAndWater = new SurvivalStatistics("foodAndWater", mLayerViewport.halfWidth + 120, mLayerViewport.halfHeight + 210, (float) (150 * 0.8),
                (float) (50 * 0.8), 50, mGame.getAssetManager().getBitmap("foodAndWater-5"), this);

        morale = new SurvivalStatistics("morale", mLayerViewport.halfWidth + 300, mLayerViewport.halfHeight + 210, (float) (150 * 0.8),
                (float) (50 * 0.8), 50, mGame.getAssetManager().getBitmap("morale-5"), this);


        cardPile = new Sprite(mLayerViewport.halfWidth, (mLayerViewport.halfHeight * 13) / 14, 500f, 230f, assetManager.getBitmap("cardback"), this);
    }

    /**
     * Paint object to draw text
     */
    private Paint paint;

    /**
     * Initialise paint object
     *
     * @return initialised paint object
     */
    private Paint initPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    /**
     * Instance of deck class used to fetch cards
     */
    private Deck deck;

    /**
     * Initialise deck object and load intro card
     *
     * @return initialised deck object
     * @author Andrew Bell
     */
    private Deck initDeck() {
        deck = new Deck(this);
        deck.loadJson(Deck.DECK_FILE_NAME);
        deck.loadIntro();
        interactableCard = deck.getCard(0);
        return deck;
    }


    /**
     * ScreenViewport
     */
    private ScreenViewport mScreenViewport;

    /**
     * @return ScreenViewport CardScreens ScreenViewport
     */
    public ScreenViewport getmScreenViewport() {
        return mScreenViewport;
    }

    public LayerViewport getmLayerViewport() {
        return mLayerViewport;
    }

    private LayerViewport mLayerViewport;


    /**
     * Initialise the screen viewport
     * Adapted from the SpaceshipDemoScreen in GAGE
     *
     * @return
     */
    private boolean initScreen() {
        mScreenViewport = new ScreenViewport(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());
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

    // /////////////////////////////////////////////////////////////////////////
    // DRAW
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Implements the GameScreens abstract onDraw method to draw the game screen.
     *
     * @param elapsedTime Elapsed time information for the frame
     * @param graphics2D  Graphics instance used to draw the screen
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        background.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        drawWeeksSurvived(graphics2D);
        drawStatisticsSprites(elapsedTime, graphics2D);
        drawCardHotspotIndicators(elapsedTime, graphics2D);
        drawCardPileAndCard(elapsedTime, graphics2D);
    }

    /**
     * Draws a CardPile followed by a Interactable Card
     *
     * @param elapsedTime Elapsed time information for the frame
     * @param graphics2D  Graphics instance used to draw the screen
     * @author Andrew Bell
     */
    private void drawCardPileAndCard(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        cardPile.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        if (!interactableCard.flipCard)
            interactableCard.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        else {
            graphics2D.drawBitmap(interactableCard.getBitmap(),
                    interactableCard.getHorizontalFlipMatrix(interactableCard.getFlipScaleAndSetBitmap())
                    , null);

        }
    }

    /**
     * Draws a CardHotSpot Indicators if card is in hotspot
     *
     * @param elapsedTime Elapsed time information for the frame
     * @param graphics2D  Graphics instance used to draw the screen
     * @author Andrew Bell
     */
    private void drawCardHotspotIndicators(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (interactableCard.getCardTouchingLeftHotspot())
            leftGrad.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        else if (interactableCard.getCardTouchingRightHotspot())
            rightGrad.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
    }

    /**
     * Draws Statistics Sprites
     *
     * @param elapsedTime Elapsed time information for the frame
     * @param graphics2D  Graphics instance used to draw the screen
     * @author Christopher Patrick McLearnon
     */
    private void drawStatisticsSprites(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        health.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        shelter.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        foodAndWater.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        morale.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
    }

    /**
     * Draws Weeks Survived Text
     *
     * @param graphics2D Graphics instance used to draw the screen
     * @author Matthew Downey
     */
    private void drawWeeksSurvived(IGraphics2D graphics2D) {
        if (deck.getIsIntro()) return;
        if (weeksSurvived == 1) {
            graphics2D.drawText(String.valueOf(weeksSurvived) + " week in power", mScreenViewport.centerX(), (mScreenViewport.height * 0.90f + 70f), paint);
        } else {
            graphics2D.drawText(String.valueOf(weeksSurvived) + " weeks in power", mScreenViewport.centerX(), (mScreenViewport.height * 0.90f + 70f), paint);
        }
    }
}