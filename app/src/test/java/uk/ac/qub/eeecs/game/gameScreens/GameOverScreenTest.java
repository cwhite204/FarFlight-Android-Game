package uk.ac.qub.eeecs.game.gameScreens;

import android.text.TextPaint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.game.FarflightGame;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameOverScreenTest {

    @Mock
    GameOverScreen aScreen = mock(GameOverScreen.class);

    @Mock
    Game game = new FarflightGame();

    @Mock
    AssetStore assetStore;

    @Mock
    PushButton mainMenu;

    @Mock
    TextPaint gameOver;

    @Test
    public void drawScreenTest() {
        ScreenManager screenManager = new ScreenManager();
        screenManager.addScreen(aScreen);

        assertEquals(aScreen, screenManager.getCurrentScreen());
    }

    @Test
    public void initDeathCauseTest(){

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

    @Test
    public void causeOfDeathTest() {

        when(aScreen.causeOfDeath()).thenReturn("health");

        String statName = aScreen.causeOfDeath();
        assertEquals("health", statName);
    }

    @Test
    public void initPaintTest() {
        when(aScreen.initPaint()).thenReturn(gameOver);

        aScreen.initPaint();
        assertNotNull(gameOver);
    }

    @Test
    public void getStatNameTest() {
        when(aScreen.getStatName()).thenReturn("health");
        String statName = aScreen.getStatName();

        assertEquals("health", statName);
    }
}
