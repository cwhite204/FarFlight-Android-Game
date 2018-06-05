package uk.ac.qub.eeecs.game.gameScreens;

import android.text.TextPaint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.game.FarflightGame;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Chris White on 09/04/2018.
 */


@RunWith(MockitoJUnitRunner.class)
public class SplashScreenTest {
    @Mock
    SplashScreen aScreen = mock(SplashScreen.class);

    @Mock
    Game game = new FarflightGame();

    @Mock
    AssetStore assetStore;

    @Mock
    TextPaint splashText;

    @Test
    public void drawScreenTest() {
        ScreenManager screenManager = new ScreenManager();
        screenManager.addScreen(aScreen);

        assertEquals(aScreen, screenManager.getCurrentScreen());
    }

    @Test
    public void initPaintTest() {
        when(aScreen.initPaint()).thenReturn(splashText);

        aScreen.initPaint();
        assertNotNull(splashText);
    }

    @Test
    public void loadAssetsTest() {
        when(aScreen.loadAssets(assetStore)).thenReturn(true);
        boolean response = aScreen.loadAssets(assetStore);

        assertNotNull(assetStore);
        assert(response);
    }

    @Test
    public void initSprites(){
        when(aScreen.initSprites(game, assetStore)).thenReturn(true);
        boolean response = aScreen.initSprites(game, assetStore);

        assertNotNull(assetStore);
        assert(response);
    }

    @Test
    public void initScreenTest(){
        when(aScreen.initScreen(game)).thenReturn(true);
        boolean response = aScreen.initScreen(game);

        assert(response);
    }

}