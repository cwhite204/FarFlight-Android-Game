package uk.ac.qub.eeecs.game.gameScreens;

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
public class MainMenuScreenTest {
    @Mock
    MainMenuScreen aScreen = mock(MainMenuScreen.class);

    @Mock
    Game game = new FarflightGame();

    @Mock
    AssetStore assetStore;

    @Test
    public void drawScreenTest() {
        ScreenManager screenManager = new ScreenManager();
        screenManager.addScreen(aScreen);

        assertEquals(aScreen, screenManager.getCurrentScreen());
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

