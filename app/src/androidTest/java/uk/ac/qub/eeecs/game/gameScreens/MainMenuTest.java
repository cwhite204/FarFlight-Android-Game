package uk.ac.qub.eeecs.game.gameScreens;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainMenuTest {
    private Context context;

    @Before
    public void setUp(){
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void loadAndAddBitmapPlay_Successful() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "button_play", "img/MainMenu/button_play.png");
        assertTrue(success);
    }

    @Test
    public void loadAndAddBitmapSettings_Successful() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "button_settings", "img/MainMenu/button_settings.png");
        assertTrue(success);
    }

    @Test
    public void loadAndAddBitmapScoreboard_ValidData_TestIsSuccessful() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "button_scoreboard", "img/MainMenu/button_scoreboard.png");
        assertTrue(success);
    }

    @Test
    public void loadAndAddBitmapBackground_ValidData_TestIsSuccessful() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "FFBackground", "img/Backgrounds/FFBackgroundRed.png");
        assertTrue(success);
    }


    @Test
    public void loadAndAddBitmapPlay_TestIsInvalid() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "button_play", "img/buttoon_play.png");
        assertFalse(success);
    }

    @Test
    public void loadAndAddBitmapSettings_TestIsInvalid() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "button_settings", "img/button_sett_ings.png");
        assertFalse(success);
    }

    @Test
    public void loadAndAddBitmapScoreboard_ValidData_TestIsInvalid() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "button_scoreboard", "img/butpton_scoreboard.png");
        assertFalse(success);
    }

    @Test
    public void loadAndAddBitmapBackground_ValidData_TestIsInvalid() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "FFBackground", "img/FFBaackgroundewd.png");
        assertFalse(success);
    }
}
