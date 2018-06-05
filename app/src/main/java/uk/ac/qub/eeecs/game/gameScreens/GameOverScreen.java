package uk.ac.qub.eeecs.game.gameScreens;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.text.TextPaint;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.Scores;

/**
 * This class extends the GameScreen class
 * This is where the game over screen is displayed
 *
 * @author Christopher White
 */
public class GameOverScreen extends GameScreen {

    /**
     * Sprite for gravestone that appears in center or screen
     */
    private Sprite gameOverCard;

    /**
     * GameObject object which holds the background bitmap and is first thing drawn on screen
     */
    private GameObject mFFBackground;

    /**
     * PushButton when pressed takes user to main menu
     */
    private PushButton mMainMenu;

    /**
     * Activity used when creating a Scores object
     */
    private Activity activity;

    /**
     * Layer and Screen viewports used for positioning
     */
    private LayerViewport mLayerViewport;
    private ScreenViewport mScreenViewport;

    /**
     * HashMap to store key value pairs of stat that went to 0
     * and an associated string describing death
     */
    private HashMap<String, String> deathCause = new HashMap<>();

    /**
     * Static int that stores the score of the user
     */
    private static int mScore;

    /**
     * If user gets a highscore this is set to true and is used to
     * display highscore message
     */
    private boolean isHigh;

    /**
     * Stores the statName of the stat that went to 0 and ended the game
     */
    private String statName;

    /**
     * Number of weeks the user survived
     */
    private int weeks;

    /**
     * Random object to create random numbers
     */
    private Random rand = new Random();

    /**
     * Arrays containing causes of death for each stat
     */
    String healthDeath[] = {
            "You were murdered by the spy",
            "You fell out of a tree and died",
            "You ate a poisonous lemon and died"
    };
    String moraleDeath[] = {
            "You ran out of morale and gave up",
            "Everyone lost hope in you and left you for dead",
            "You lost hope and gave up"
    };
    String foodAndWaterDeath[] = {
            "You ran out of food and died",
            "You ran out of water and died",
            "You starved to death"
    };
    String shelterDeath[] = {
            "You were killed by a fox while sleeping outside",
            "You had no shelter and got killed by acid rain",
            "You had no shelter and became a gorillas dinner"
    };

    /**
     * Constructor for GameOverScreen
     *
     * @param game
     * @param score
     * @param name
     */
    public GameOverScreen(Game game, int score, String name) {
        super("GameOverScreen", game);

        statName = name;
        mScore = score;
        //isHigh = compareHighScore();

        initDeathCause();
        initScreen(game);
        AssetStore assetManager = mGame.getAssetManager();
        loadAssets(assetManager);
        initSprites(game, assetManager);
    }

    /**
     * Update the game over screen
     *
     * @param elapsedTime Elapsed time information for the frame
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        // Counts score from 0 to score to display animation on screen
        if (weeks < mScore) {
            weeks += 1;
            SystemClock.sleep(100);
        }

        // Allows for user touch input
        Input input = mGame.getInput();

        // Create list of touch events
        List<TouchEvent> touchEvents = input.getTouchEvents();

        if (touchEvents.size() > 0) {
            // Create list of touch events
            TouchEvent touchEvent = touchEvents.get(0);

            mMainMenu.update(elapsedTime);

            // If main menu button is pressed user is taken to main menu
            if (mMainMenu.isPushTriggered())
                changeToScreen(new MainMenuScreen(mGame));
        }
    }

    /**
     * Draw the game over screen
     *
     * @param elapsedTime Elapsed time information for the frame
     * @param graphics2D  Graphics instance used to draw the screen
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Sets initial background color to blue
        graphics2D.clear(Color.BLUE);

        // Creates a new TextPaint object to display text
        TextPaint gameOver = initPaint();

        // Draw background
        mFFBackground.draw(elapsedTime, graphics2D, mLayerViewport,
                mScreenViewport);

        //Draw menu button and card sprite
        mMainMenu.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        gameOverCard.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        // Displays highscore text if the users score is a new highest score
        if (isHigh) {
            graphics2D.drawText("HIGHSCORE!", mScreenViewport.width / 2, mLayerViewport.getHeight() / 3 + 100, gameOver);
        }

        // Change TextPaint object properties for next piece of text
        gameOver.setTextSize(150);
        gameOver.setColor(Color.RED);
        // Display game over text
        graphics2D.drawText("GAME OVER", mScreenViewport.width / 2, mLayerViewport.getHeight() / 3, gameOver);

        // Change TextPaint object properties for the next piece of text
        gameOver.setTextSize(80);
        gameOver.setColor(Color.BLACK);
        // Display number of weeks survived text (animated)
        graphics2D.drawText(String.valueOf(weeks) + " weeks survived.", mScreenViewport.width / 2, mLayerViewport.getHeight() / 3 + 180, gameOver);

        // Change TextPaint object properties for the next piece of text
        gameOver.setTextSize(60);
        // Display cause of death
        graphics2D.drawText(causeOfDeath(), mScreenViewport.width / 2, mScreenViewport.height / 1.3f, gameOver);
    }

    /**
     * Checks if current score is greater than highscore
     *
     * @return true if its greater than high score and false otherwise
     */
    protected boolean compareHighScore() {
        Scores scores = new Scores(activity);

        int highscore = scores.getTopScore();
        if (mScore > highscore) {
            return true;
        }
        return false;
    }

    /**
     * Chooses a cause of death based on stat that went to 0
     *
     * @return String containing cause of death
     */
    protected String causeOfDeath() {
        String cOD = statName;

        // Returns deathCause based on the stat that went to 0 first
        switch (cOD) {
            case "health":
                return deathCause.get("health");
            case "morale":
                return deathCause.get("morale");
            case "foodAndWater":
                return deathCause.get("foodAndWater");
            case "shelter":
                return deathCause.get("shelter");

            // Default return statement
            default:
                return "You died";
        }
    }

    /**
     * Used to change from this screen to another screen
     *
     * @param screen
     */
    protected void changeToScreen(GameScreen screen) {
        mGame.getScreenManager().removeScreen(this.getName());
        mGame.getScreenManager().addScreen(screen);
    }

    /**
     * Puts random cause of death from array into hash map for each stat
     *
     * @return true upon completion
     */
    protected boolean initDeathCause() {
        deathCause.put("health", healthDeath[rand.nextInt(3)]);
        deathCause.put("morale", moraleDeath[rand.nextInt(3)]);
        deathCause.put("foodAndWater", foodAndWaterDeath[rand.nextInt(3)]);
        deathCause.put("shelter", shelterDeath[rand.nextInt(3)]);

        return true;
    }

    /**
     * Load assets into the asset manager
     *
     * @param assetManager
     * @return true upon completion
     */
    protected boolean loadAssets(AssetStore assetManager) {
        // Load in assets that will be used on screen
        assetManager.loadAndAddBitmap("button_main-menu", "img/GameOver/button_main-menu.png");
        assetManager.loadAndAddBitmap("FFBackgroundRed", "img/Backgrounds/FFBackgroundRed.png");
        assetManager.loadAndAddBitmap("game-over-card", "img/GameOver/game-over-card.png");
        assetManager.loadAndAddBitmap("highscore_sprite", "img/Scoreboard/highscore_sprite.png");

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
        // Variables used to position sprites on the screen
        int screenX = game.getScreenWidth() / 5;
        int screenY = game.getScreenHeight() / 3;
        float LEVEL_VALUE = 1000.0f;

        // Define size and position of background
        mFFBackground = new GameObject(LEVEL_VALUE / 2.0f,
                LEVEL_VALUE / 2.0f, (LEVEL_VALUE * 2.7f), LEVEL_VALUE, getGame()
                .getAssetManager().getBitmap("FFBackgroundRed"), this);

        // Define size and position of button and sprite
        gameOverCard = new Sprite(mLayerViewport.halfWidth, (mLayerViewport.halfHeight * 13) / 14, 500f, 184f, assetManager.getBitmap("game-over-card"), this);
        mMainMenu = new PushButton(
                screenX * 2.5f, screenY * 2.6f, 700, 200, "button_main-menu", this);

        return true;
    }

    /**
     * Initialize the viewports which are used to size different sprites
     *
     * @param game
     * @return
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
     * Initialize TextPaint object
     *
     * @return initialized TextPaint object
     */
    protected TextPaint initPaint() {
        TextPaint gameOver = new TextPaint();
        gameOver.setTextSize(100);
        gameOver.setTextAlign(Paint.Align.CENTER);
        gameOver.setColor(Color.GREEN);
        gameOver.setTypeface(Typeface.create("Arial", Typeface.BOLD));

        return gameOver;
    }

    /**
     * Accessor - get score of user
     *
     * @return mScore integer
     */
    public static int getScore() {
        return mScore;
    }

    /**
     * Accessor - get name of stat that caused death
     *
     * @return stateName string
     */
    public String getStatName() {
        return statName;
    }

    /**
     * Accessor - get main menu button
     *
     * @return main menu pushbutton
     */
    public PushButton getMenuButton() {
        return mMainMenu;
    }

    /**
     * Mutator - set the score of the user
     * @param mScore
     */
}