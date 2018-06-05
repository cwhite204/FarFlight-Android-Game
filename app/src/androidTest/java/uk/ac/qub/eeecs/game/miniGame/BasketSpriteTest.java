package uk.ac.qub.eeecs.game.miniGame;

import org.junit.Before;
import org.junit.Test;

import uk.ac.qub.eeecs.gage.GameTest;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.game.FarflightGame;

import static org.junit.Assert.assertEquals;

/**
 * @author Andrew Bell
 */

public class BasketSpriteTest {
    BananaGameScreen bananaGameScreen;
    FarflightGame farflightGame;
    BasketSprite basketSprite;

    @Before
    public void setUp() {
        GameTest gameTest = new GameTest();
        farflightGame = gameTest.setUp();
        bananaGameScreen = new BananaGameScreen(farflightGame);
        basketSprite = new BasketSprite(bananaGameScreen);
    }

    @Test
    public void superSizeMode_success() {
        basketSprite.setSuperSizeMode();
        assertEquals(basketSprite.superSizeMode, true);
    }

    @Test
    public void notTouchedSetBounds_supersizeMode_success() {
        basketSprite.setSuperSizeMode();
        assertEquals(basketSprite.superBound, basketSprite.notTouchedSetBounds());
    }

    @Test
    public void notTouchedSetBounds_normal_success() {
        assertEquals(basketSprite.originalBound, basketSprite.notTouchedSetBounds());
    }

    @Test
    public void touchedSetBounds_supersizeMode_success() {
        basketSprite.setSuperSizeMode();
        assertEquals(basketSprite.superBound, basketSprite.touchedSetBounds());
    }

    @Test
    public void touchedSetBounds_normal_success() {
        assertEquals(basketSprite.bigBound, basketSprite.touchedSetBounds());
    }

    @Test
    public void setPositionOnTouch() {
        Vector2 touchLocation = new Vector2(100, 100);
        basketSprite.setPositionOnTouch(bananaGameScreen.mLayerViewport, touchLocation);
        assertEquals(touchLocation.x, basketSprite.position.x, 1.0);
    }


}
