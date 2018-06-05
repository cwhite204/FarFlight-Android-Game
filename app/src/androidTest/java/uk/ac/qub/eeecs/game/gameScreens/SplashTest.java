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
 * Created by Chris on 09/04/2018.
 */

@RunWith(AndroidJUnit4.class)
public class SplashTest {
    private Context context;

    @Before
    public void setUp(){
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void loadAndAddBitmapLogo_TestIsValid() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "logo", "img/logo.jpg");
        assertTrue(success);
    }

    @Test
    public void loadAndAddBitmapLogo_TestIsInvalid() {
        AssetStore assetStore = new AssetStore(new FileIO(context));
        boolean success =
                assetStore.loadAndAddBitmap(
                        "logo", "img/logos.png");
        assertFalse(success);
    }
}
