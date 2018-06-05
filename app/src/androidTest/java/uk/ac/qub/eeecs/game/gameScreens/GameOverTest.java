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
public class GameOverTest {
    private Context context;


    @Before
    public void setUp(){ context = InstrumentationRegistry.getTargetContext(); }

    @Test
    public void loadAndAddBitmap_ValidData_TestIsSuccessful() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "button_main-menu", "img/GameOver/button_main-menu.png");
        assertTrue(success);
    }

    @Test
    public void loadAndAddBitmap_InvalidData_TestError() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "button_main-menu", "img/button_main.png");
        assertFalse(success);
    }

    @Test
    public void loadAndAddBitmapBackground_ValidData_Success() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "FFBackgroundRed", "img/Backgrounds/FFBackgroundRed.png");
        assertTrue(success);
    }

    @Test
    public void loadAndAddBitmapBackground_ValidData_TestIsInvalid() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "FFBackground", "img/FFBaackgroundRed.png");
        assertFalse(success);
    }

    @Test
    public void loadAndAddBitmapCard_ValidData_Success() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "Card", "img/GameOver/game-over-card.png");
        assertTrue(success);
    }

    @Test
    public void loadAndAddBitmapCard_ValidData_TestIsInvalid() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "Card", "img/gameCard.png");
        assertFalse(success);
    }
}
