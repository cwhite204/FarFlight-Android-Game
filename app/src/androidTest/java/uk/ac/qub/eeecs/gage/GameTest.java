package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.game.FarflightGame;

/**
 * @author Andrew Bell
 */

public class GameTest {
    private Context context;


    @Before
    public FarflightGame setUp() {
        FarflightGame farflightGame;
        context = InstrumentationRegistry.getTargetContext();
        farflightGame = new FarflightGame();
        FileIO fileIO = new FileIO(context);
        farflightGame.mFileIO = fileIO;
        farflightGame.mAssetManager = new AssetStore(fileIO);
        farflightGame.mScreenManager = new ScreenManager();
        return farflightGame;
    }
}
