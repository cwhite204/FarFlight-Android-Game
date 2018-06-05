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
/**
 * Created by tompurdon on 15/04/2018.
 */
@RunWith(AndroidJUnit4.class)
public class ScoreboardScreenTest {

    private Context context;

    @Before
    public void setUp(){ context = InstrumentationRegistry.getTargetContext(); }

    @Test
    public void loadAndAddBitmap_ValidData_TestIsSuccessful() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean successBackground, successTitle;
        successBackground = assetStore.loadAndAddBitmap(
                        "scoresBackground", "img/Backgrounds/FFBackground.png");
        successTitle = assetStore.loadAndAddBitmap(
                "titleImage", "img/Scoreboard/highscore_sprite.png");
        assertTrue(successBackground);
        assertTrue(successTitle);
    }

    @Test
    public void loadAndAddBitmap_InvalidData_TestError() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean failureBackground, failureTitle;
        failureBackground = assetStore.loadAndAddBitmap(
                "scoresBackground", "img/Characters/FFBackground.png");
        failureTitle = assetStore.loadAndAddBitmap(
                "titleImage", "img/Settings/highscore_sprite.png");
        assertFalse(failureBackground);
        assertFalse(failureTitle);
    }



}


