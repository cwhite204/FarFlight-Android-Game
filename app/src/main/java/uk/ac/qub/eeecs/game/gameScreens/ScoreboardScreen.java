package uk.ac.qub.eeecs.game.gameScreens;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.Scores;

/***
 * Class created and edited by Tom Purdon
 */
public class ScoreboardScreen extends GameScreen {

    /***
     * Scoreboard Background Object
     */
    private GameObject scoresBackground;

    /***
     * Level Width
     */
    private final float LEVEL_WIDTH = 1000.0f;

    /***
     * Level Height
     */
    private final float LEVEL_HEIGHT = 1000.0f;

    /***
     * ScreenViewport
     */
    private ScreenViewport mScreenViewport;

    /***
     * LayerViewport
     */
    private LayerViewport mLayerViewport;

    /***
     * Title Image
     */
    private Bitmap titleBitmap;

    /***
     * Title Rect
     */
    private Rect titleBitmapBound;

    /***
     * Title Top Coordinate
     */
    private int titleTop = 75;

    /***
     * Title Width
     */
    private static final int BUTTON_WIDTH = 800;

    /***
     * Title Height
     */
    private static final int BUTTON_HEIGHT = 300;

    /***
     * Array of highscores
     */
    private int[] highScores;

    /***
     * Score from last game
     */
    private int prevScore;

    public ScoreboardScreen(Game game) {
        super("ScoreboardScreen", game);

        // Create the screen viewport
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

        // Create the layer viewport, taking into account the orientation
        // and aspect ratio of the screen.
        if (mScreenViewport.width > mScreenViewport.height)
            mLayerViewport = new LayerViewport(240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240,
                    240.0f * mScreenViewport.height / mScreenViewport.width);
        else
            mLayerViewport = new LayerViewport(240.0f * mScreenViewport.height
                    / mScreenViewport.width, 240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240);

        //  Loading in assets needed for the scoreboard screen
        AssetStore assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("scoresBackground", "img/Backgrounds/FFBackground.png");
        assetManager.loadAndAddBitmap("titleImage", "img/Scoreboard/highscore_sprite.png");

        //  Declaring loaded bitmaps as variables
        this.titleBitmap = assetManager.getBitmap("titleImage");
        this.scoresBackground = new GameObject(LEVEL_WIDTH / 2.0f,
                LEVEL_HEIGHT / 2.0f, (LEVEL_HEIGHT * 2.7f), LEVEL_HEIGHT, getGame()
                .getAssetManager().getBitmap("scoresBackground"), this);

        //  Obtain the previous score from static varible in GameOverScreen
        prevScore = GameOverScreen.getScore();

        //  Using methods from Scores Class on highscores
        Scores scoreClass = new Scores(game.getActivity());
        scoreClass.newHighScore(prevScore);
        highScores = scoreClass.getHighScores();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        // Process any touch events occurring since the update
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            TouchEvent touchEvent = touchEvents.get(0);
        }
    }

    /**
     * Draw the score demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        // Clear the screen and draw the buttons
        graphics2D.clear(Color.WHITE);
        scoresBackground.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        // Setting the paint values and drawing the highscore text
        Paint highScoresFormat = new Paint();
        highScoresFormat.setTextAlign(Paint.Align.CENTER);
        highScoresFormat.setTextSize(150);
        highScoresFormat.setColor(Color.BLACK);

        // Setting format for Previous Score text
        Paint prevScoreFormat = new Paint();
        prevScoreFormat.setTextSize(90);
        prevScoreFormat.setColor(Color.RED);

        // drawing the title centered
        int alignment = ((graphics2D.getSurfaceWidth() - titleBitmap.getWidth()) / 2) - 200;
        if (titleBitmapBound == null) {
            int titleLeft = alignment;
            titleBitmapBound = new Rect(titleLeft, titleTop, titleLeft
                    + BUTTON_WIDTH, titleTop + BUTTON_HEIGHT);
        }

        //Display title and previous score
        graphics2D.drawBitmap(titleBitmap, null, titleBitmapBound, null);
        graphics2D.drawText("PREVIOUS SCORE:",
                mScreenViewport.width / 20,
                mLayerViewport.getHeight(), prevScoreFormat);
        prevScoreFormat.setTextSize(150);
        graphics2D.drawText(String.valueOf(prevScore),
                mScreenViewport.width / 5 * 4,
                mLayerViewport.getHeight() * 20 / 19, prevScoreFormat);

        //Displaying top score list
        for (int i = 0; i < highScores.length; i++) {
            //Display the position of the scores
            graphics2D.drawText("       " + (i + 1) + "     :    ",
                    mScreenViewport.width / 3,
                    mLayerViewport.getHeight() * (10 + ((i + 1) * 4)) / 9, highScoresFormat);

            //calling the highscores array
            graphics2D.drawText(String.valueOf(highScores[i]),
                    mScreenViewport.width / 4 * 3,
                    mLayerViewport.getHeight() * (10 + ((i + 1) * 4)) / 9, highScoresFormat);

        }
    }
}
