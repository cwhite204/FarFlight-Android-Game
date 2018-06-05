package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Christopher Patrick McLearnon on 11/03/2018.
 */

@RunWith(AndroidJUnit4.class)
public class StatIconLoadTests {

    private Context context;
    private AssetStore assetManager;


    @Before
    public void setUp() throws Exception    {
        context = InstrumentationRegistry.getTargetContext();
        assetManager = new AssetStore(new FileIO(context));
    }

    @Test
    public void loadShelterIcon1_ValidData_TestSuccess()  {
        boolean shelter1ValidSuccess = assetManager.loadAndAddBitmap("shelter-1", "img/StatIcons/shelter-1.png");
        assertTrue(shelter1ValidSuccess);
        assertNotNull(assetManager.getBitmap("shelter-1"));
    }

    @Test
    public void loadShelterIcon1_InvalidData_TestSuccess()  {
        boolean shelter1InvalidSuccess = assetManager.loadAndAddBitmap("shelter-1", "shlter-1.png");
        assertFalse(shelter1InvalidSuccess);
        assertNull(assetManager.getBitmap("shelter-1"));
    }

    @Test
    public void loadAllShelterIcons_ValidData_Success() {
        for (int i = 1; i < 11; i++)    {
            boolean shelterBitmapSuccess = assetManager.loadAndAddBitmap("shelter-" + String.valueOf(i),
                                            "img/StatIcons/shelter-" + String.valueOf(i) + ".png");
            assertTrue(shelterBitmapSuccess);
            assertNotNull(assetManager.getBitmap("shelter-" + String.valueOf(i)));
        }
    }

    @Test
    public void loadHealth1Icon1_ValidData_TestSuccess()   {
        boolean health1ValidSuccess = assetManager.loadAndAddBitmap("health-1", "img/StatIcons/health-1.png");
        assertTrue(health1ValidSuccess);
        assertNotNull(assetManager.getBitmap("health-1"));
    }

    @Test
    public void loadHealthIcon1_InvalidData_TestSuccess()   {
        boolean health1InvalidDataSuccess = assetManager.loadAndAddBitmap("health-1", "img/StatIco/heal10.png");
        assertFalse(health1InvalidDataSuccess);
        assertNull(assetManager.getBitmap("health-1"));
    }

    @Test
    public void loadAllHealthIcons_ValidData_TestSuccess()  {
        for (int i = 1; i < 11; i++)    {
            boolean healthBitmapSuccess = assetManager.loadAndAddBitmap("health-" + String.valueOf(i),
                                            "img/StatIcons/health-" + String.valueOf(i) + ".png");
            assertTrue(healthBitmapSuccess);
            assertNotNull(assetManager.getBitmap("health-" + String.valueOf(i)));
        }
    }

    @Test
    public void loadMoraleIcon1_ValidData_TestSuccess() {
        boolean morale1ValidDataSuccess = assetManager.loadAndAddBitmap("morale-1", "img/StatIcons/morale-1.png");
        assertTrue(morale1ValidDataSuccess);
        assertNotNull(assetManager.getBitmap("morale-1"));
    }

    @Test
    public void loadMoraleIcon1_InvalidData_TestSuccess()   {
        boolean  morale1InvalidDataSuccess = assetManager.loadAndAddBitmap("morale-1", "img/StatIcons/morale__1.png");
        assertFalse(morale1InvalidDataSuccess);
        assertNull(assetManager.getBitmap("morale-1"));
    }

    @Test
    public void loadAllMoraleIcons_ValidData_TestSuccess()  {
        for (int i = 1; i < 11; i++)    {
            boolean moraleBitmapSuccess = assetManager.loadAndAddBitmap("morale-" + String.valueOf(i),
                                            "img/StatIcons/morale-" + String.valueOf(i) + ".png");
            assertTrue(moraleBitmapSuccess);
            assertNotNull(assetManager.getBitmap("morale-" + String.valueOf(i)));
        }
    }

    @Test
    public void loadFoodAndWaterIcon1_ValidData_TestSuccess()   {
        boolean foodAndWater1ValidDataSuccess = assetManager.loadAndAddBitmap("foodAndWater-1", "img/StatIcons/foodAndWater-1.png");
        assertTrue(foodAndWater1ValidDataSuccess);
        assertNotNull(assetManager.getBitmap("foodAndWater-1"));
    }

    @Test
    public void loadFoodAndWaterIcon1_InvalidData_TestSuccess()  {
        boolean foodAndWater1InvalidDataSuccess = assetManager.loadAndAddBitmap("foodAndWater-1", "img/StatIcons/food-1.png");
        assertFalse(foodAndWater1InvalidDataSuccess);
        assertNull(assetManager.getBitmap("foodAndWater-1"));
    }

    @Test
    public void loadAllFoodAndWaterIcons_ValidData_TestSuccess()    {
        for (int i = 1; i < 11; i++)    {
            boolean foodAndWaterBitmapSuccess = assetManager.loadAndAddBitmap("foodAndWater-" + String.valueOf(i),
                                            "img/StatIcons/foodAndWater-" + String.valueOf(i) + ".png");
            assertTrue(foodAndWaterBitmapSuccess);
            assertNotNull(assetManager.getBitmap("foodAndWater-" + String.valueOf(i)));
        }
    }
}
