package uk.ac.qub.eeecs.game;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.gameScreens.GameOverScreen;

/**
 * Created by Christopher Patrick McLearnon on 12/02/2018.
 */

public class SurvivalStatistics extends Sprite {

    /**
     * Instance of GameScreen
     */
    private Game mGame = mGameScreen.getGame();

    /**
     * Instance of AssetStore class needed to manage game assets
     */
    private AssetStore assetManager = mGame.getAssetManager();

    /**
     * Survival Stat score value
     */
    public int currentScore;

    /**
     * Dimensions of Survival Stat sprite
     */
    public float width, height;

    /**
     * Maximum possible Survival Stat score
     */
    private int MAX_SCORE = 100;

    /**
     * Minimum possible Survival Stat score
     */
    private int MIN_SCORE = 0;

    /**
     * Name of Survival Stat sprite instance
     */
    private String statID;
    private String name;


    /**
     *
     * @param statID
     * @param x
     * @param y
     * @param width
     * @param height
     * @param currentScore
     * @param bitmap
     * @param gameScreen
     */
    public SurvivalStatistics(String statID, float x, float y, float width, float height, int currentScore, Bitmap bitmap, GameScreen gameScreen) {
        super(x, y, width, height, bitmap, gameScreen);

        this.statID = statID;
        this.currentScore = currentScore;
        mBitmap = bitmap;
    }

    /**
     * Calculates the score of each Survival Stat
     * using stat effect values from JSON file
      */

    public void addToScore(int addition, int score) {
        if ((currentScore + addition) >= MAX_SCORE) {
            currentScore = MAX_SCORE;
        }
        else if ((currentScore+addition)<=MIN_SCORE){
            currentScore = MIN_SCORE;

         GameOverScreen gameOverScreen = new GameOverScreen(mGame, score, statID);
            mGame.getScreenManager().addScreen(gameOverScreen);
            mGame.getScreenManager().setAsCurrentScreen("GameOverScreen");
            mGame.getScreenManager().removeScreen("CardScreen");
        }
        else  {
            this.currentScore += addition;
        }

        /**
         * Calls the setBitmap() method to change each
         * sprite's bitmap according to their current score
         */
        setBitmap();
    }

    /**
     * GETTER
     * Returns the current score of the stat
     */

    public int getCurrentScore()    {
        return this.currentScore;
    }


    /**
     * Changes bitmap of each stat level according to the current score
     * @return
     */
    public Bitmap setBitmap() {
        int roundedScore = 10 * (Math.round(getCurrentScore() / 10));
        mBitmap = assetManager.getBitmap(statID + "-" + String.valueOf(roundedScore / 10));
        return mBitmap;
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        super.update(elapsedTime);


    }

    /**
     * Draw method displays the score of each Survival Stat
     * sprite underneath each icon
     * @param elapsedTime
     * @param graphics2D
     * @param layerViewport
     * @param screenViewport
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);

        String score = Integer.toString(currentScore);

        Paint paintScore = new Paint();
        paintScore.setColor(Color.RED);
        paintScore.setTextSize(50);
        paintScore.setShadowLayer(0.5f,1,1,Color.WHITE);
        graphics2D.drawText(score, drawScreenRect.centerX(), drawScreenRect.centerY() + (drawScreenRect.height() / 2), paintScore);
    }
}
