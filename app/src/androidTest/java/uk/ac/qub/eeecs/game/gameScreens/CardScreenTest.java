package uk.ac.qub.eeecs.game.gameScreens;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.GameTest;
import uk.ac.qub.eeecs.gage.R;
import uk.ac.qub.eeecs.game.FarflightGame;
import uk.ac.qub.eeecs.game.InteractableCard;
import uk.ac.qub.eeecs.game.SurvivalStatistics;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Andrew Bell on 10/04/2018.
 * Survival Stat tests by Christopher Patrick McLeanron on 14/04/2018
 * Refactored from CardScreenTest Unit Tests
 */

@RunWith(AndroidJUnit4.class)
public class CardScreenTest {
    private Context context;
    CardScreen cardscreen;
    FarflightGame farflightGame;
    InteractableCard interactableCard;
    SurvivalStatistics stat;
    Bitmap mBitmap;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        GameTest gameTest = new GameTest();
        farflightGame = gameTest.setUp();
        cardscreen = new CardScreen(farflightGame);
        interactableCard = cardscreen.getPlayerCard();
    }

    /**
     * @Author: Andrew Bell
     */
    @Test
    public void onCardDropped_left_success() {

        float velocityBefore = interactableCard.velocity.x;
        cardscreen.onCardDropped(true, false);
        float velocityAfter = interactableCard.velocity.x;
        assertNotEquals(velocityBefore, velocityAfter);
    }

    /**
     * @Author: Andrew Bell
     */
    @Test
    public void onCardDropped_right_success() {
        float velocityBefore = interactableCard.velocity.x;
        cardscreen.onCardDropped(false, true);
        float velocityAfter = interactableCard.velocity.x;
        assertNotEquals(velocityBefore, velocityAfter);
    }


    /**
     * @Author: Andrew Bell
     */
    @Test
    public void onCardDropped_middle_success() {
        float velocityBefore = interactableCard.velocity.x;
        cardscreen.onCardDropped(false, false);
        float velocityAfter = interactableCard.velocity.x;
        assertEquals(velocityBefore, velocityAfter, 1.0);
    }


    /**
     * @Author: Andrew Bell
     */
    @Test
    public void setWeeksSurvived_3_Success() {
        cardscreen.setWeeksSurvived(3);
        assertEquals(3, cardscreen.getWeeksSurvived());

    }

    /**
     * @Author: Andrew Bell
     */
    @Test
    public void toggleSound_toggleOn_success() {
        cardscreen.setSound(context.getResources().openRawResourceFd(R.raw.background_sound));
        assertEquals(true, cardscreen.toggleSound());
    }


    /**
     * @Author: Andrew Bell
     */
    @Test
    public void toggleSound_toggleOnThenOff_success() {
        cardscreen.setSound(context.getResources().openRawResourceFd(R.raw.background_sound));
        cardscreen.toggleSound();
        assertEquals(false, cardscreen.toggleSound());
    }


    /**
     * @Author: Andrew Bell
     */
    @Test
    public void setIfCardInHotspot_left_success() {
        interactableCard.setPosition(0, 50);
        cardscreen.setIfCardInHotspot();
        assertTrue(interactableCard.getCardTouchingLeftHotspot());

    }


    /**
     * @Author: Andrew Bell
     */
    @Test
    public void setIfCardInHotspot_right_success() {
        interactableCard.setPosition(cardscreen.getmLayerViewport().getRight() - 10, 50);
        cardscreen.setIfCardInHotspot();
        assertTrue(interactableCard.getCardTouchingRightHotspot());

    }


    /**
     * @Author: Andrew Bell
     */
    @Test
    public void wasCardDropped_dropped_success() {
        interactableCard.setWasTouching(true);
        assertTrue(cardscreen.wasCardDropped());
    }


    /**
     * @Author: Andrew Bell
     */
    @Test
    public void wasCardDropped_notDropped_success() {
        interactableCard.setWasTouching(false);
        assertFalse(cardscreen.wasCardDropped());
    }


    /**
     * @Author: Andrew Bell
     */
    @Test
    public void decideIfCardOffScreenLeft_isAnimatingAndOffLeft_success() {
        interactableCard.setCardAnimatingAway(true);
        interactableCard.setPosition(-500, 100);
        assertTrue(cardscreen.hasCardAnimatedPastScreenLeft());
    }


    /**
     * @Author: Andrew Bell
     */
    @Test
    public void hasCardAnimatedPastScreenLeft_isAnimatingAndNotOffLeft_fail() {
        interactableCard.setCardAnimatingAway(true);
        interactableCard.setPosition(0, 100);
        assertFalse(cardscreen.hasCardAnimatedPastScreenLeft());
    }


    /**
     * @Author: Andrew Bell
     */
    @Test
    public void hasCardAnimatedPastScreenLeft_isNotAnimatingAndIsOffLeft_fail() {
        interactableCard.setCardAnimatingAway(false);
        interactableCard.setPosition(-500, 100);
        assertFalse(cardscreen.hasCardAnimatedPastScreenLeft());
    }


    /**
     * @Author: Andrew Bell
     */
    @Test
    public void hasCardAnimatedPastScreenRight_isAnimatingAndOffRight_success() {
        interactableCard.setCardAnimatingAway(true);
        interactableCard.setPosition(cardscreen.getmLayerViewport().getRight() + 500, 100);
        assertTrue(cardscreen.hasCardAnimatedPastScreenRight());
    }


    /**
     * @Author: Andrew Bell
     */
    @Test
    public void hasCardAnimatedPastScreenRight_isAnimatingAndNotOffRight_fail() {
        interactableCard.setCardAnimatingAway(true);
        interactableCard.setPosition(0, 100);
        assertFalse(cardscreen.hasCardAnimatedPastScreenRight());
    }


    /**
     * @Author: Andrew Bell
     */
    @Test
    public void hasCardAnimatedPastScreenRight_isNotAnimatingAndIsOffRight_fail() {
        interactableCard.setCardAnimatingAway(false);
        interactableCard.setPosition(cardscreen.getmLayerViewport().getRight() + 500, 100);
        assertFalse(cardscreen.hasCardAnimatedPastScreenRight());
    }

    /**
     * @Author: Andrew Bell
     */
    @Test
    public void loadAssets_validAssets_success() {
        cardscreen.loadAssets(farflightGame.getAssetManager());
        assertNotNull(farflightGame.getAssetManager().getBitmap("background"));
        assertNotNull(farflightGame.getAssetManager().getBitmap("card_template1"));
        assertNotNull(farflightGame.getAssetManager().getBitmap("cardback"));
        assertNotNull(farflightGame.getAssetManager().getBitmap("leftGrad"));
        assertNotNull(farflightGame.getAssetManager().getBitmap("rightGrad"));
        assertNotNull(farflightGame.getAssetManager().getBitmap("card_template1"));
        assertNotNull(farflightGame.getAssetManager().getBitmap("health-1"));
        for (int i = 1; i <= 10; i++) {
            assertNotNull(farflightGame.getAssetManager().getBitmap("health-" + String.valueOf(i)));
            assertNotNull(farflightGame.getAssetManager().getBitmap("shelter-" + String.valueOf(i)));
            assertNotNull(farflightGame.getAssetManager().getBitmap("foodAndWater-" + String.valueOf(i)));
            assertNotNull(farflightGame.getAssetManager().getBitmap("morale-" + String.valueOf(i)));

        }
    }

    /**
     * @Author: Andrew Bell
     */
    @Test
    public void loadAssets_invalidAssets_fail() {
        cardscreen.loadAssets(farflightGame.getAssetManager());
        assertNull(farflightGame.getAssetManager().getBitmap("invalid"));

    }

    /**
     * @author Matthew Downey
     */
    @Test
    public void testScoreTotalsCorrectly_TestSuccess() {
        int n = 20;
        for (int i = 0; i < n; i++) {
            cardscreen.prepareNextCard('l');
        }
        assertEquals(cardscreen.getWeeksSurvived(), n);

    }

    /**
     * Author: Christopher Patrick McLearnon
     */
    @Test
    public void initialiseSurvivalStatSprite_TestSuccess() {
        cardscreen.loadAssets(farflightGame.getAssetManager());
        stat = new SurvivalStatistics("health", 120, 120, 200, 200, 50,
                farflightGame.getAssetManager().getBitmap("health-5"), cardscreen);
        assertNotNull(stat);
    }

    /**
     * Author: Christopher Patrick McLearnon
     */
    @Test
    public void survivalStatScore_TestSuccess() {
        cardscreen.loadAssets(farflightGame.getAssetManager());
        int assumedScore = 50;
        stat = new SurvivalStatistics("health", 120, 120, 200, 200, 50,
                farflightGame.getAssetManager().getBitmap("health-5"), cardscreen);
        assertEquals(assumedScore, stat.getCurrentScore());

    }

    /**
     * Author: Christopher Patrick McLearnon
     */
    @Test
    public void alterSurvivalStatScore_Test_Success() {
        cardscreen.loadAssets(farflightGame.getAssetManager());
        int initailScore;
        stat = new SurvivalStatistics("health", 120, 120, 200, 200, 50,
                farflightGame.getAssetManager().getBitmap("health-5"), cardscreen);
        initailScore = stat.getCurrentScore();
        stat.addToScore(40, 2);
        assertNotEquals(stat.getCurrentScore(), initailScore);
    }

    /**
     * Author: Christopher Patrick McLearnon
     */
    @Test
    public void survivalStatScoreBoundaries_TestSucess() {
        cardscreen.loadAssets(farflightGame.getAssetManager());
        stat = new SurvivalStatistics("health", 120, 120, 200, 200, 50,
                farflightGame.getAssetManager().getBitmap("health-5"), cardscreen);
        int initialScore = stat.getCurrentScore();
        stat.addToScore(70, 2);
        assertNotEquals(120, stat.getCurrentScore());
        stat.currentScore = 50;
        stat.addToScore(-100, 3);
        assertNotEquals(-150, stat.getCurrentScore());
    }

    /**
     * Author: Christopher Patrick McLearnon
     */
    @Test
    public void alterSurvivalStatBitmap_TestSuccess() {
        cardscreen.loadAssets(farflightGame.getAssetManager());
        stat = new SurvivalStatistics("health", 120, 120, 200, 200, 50,
                farflightGame.getAssetManager().getBitmap("health-5"), cardscreen);
        mBitmap = stat.getBitmap();
        stat.addToScore(20, 3);
        assertNotEquals(mBitmap, stat.getBitmap());
    }

    /**
     * Author: Christopher Patrick McLearnon
     */
    @Test
    public void checkSurvivalStatBitmapIndex_TestSuccess() {
        cardscreen.loadAssets(farflightGame.getAssetManager());
        stat = new SurvivalStatistics("health", 120, 120, 200, 200, 50,
                farflightGame.getAssetManager().getBitmap("health-5"), cardscreen);
        mBitmap = stat.getBitmap();
        stat.addToScore(25, 2);
        assertEquals(farflightGame.getAssetManager().getBitmap("health-7"), stat.getBitmap());
        stat.addToScore(-40, 3);
        assertEquals(farflightGame.getAssetManager().getBitmap("health-3"), stat.getBitmap());
        stat.addToScore(150, 4);
        assertEquals(farflightGame.getAssetManager().getBitmap("health-10"), stat.getBitmap());
    }

    /**
     * Author: Christopher Patrick McLearnon
     */
    @Test
    public void checkSurvivalStatSpritePosition_TestSuccess() {
        cardscreen.loadAssets(farflightGame.getAssetManager());
        stat = new SurvivalStatistics("health", 120, 120, 200, 200, 50,
                farflightGame.getAssetManager().getBitmap("health-5"), cardscreen);
        float xPosition = 120;
        assertEquals(xPosition, stat.position.x, 0.0);
        float yPosition = 120;
        assertEquals(yPosition, stat.position.y, 0.0);
    }

}
