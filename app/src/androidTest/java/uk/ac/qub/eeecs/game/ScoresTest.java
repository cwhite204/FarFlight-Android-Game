package uk.ac.qub.eeecs.game;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.GameTest;
import uk.ac.qub.eeecs.game.gameScreens.GameOverScreen;

import static junit.framework.Assert.assertNotSame;

@RunWith(AndroidJUnit4.class)
public class ScoresTest {
    private Context context;
    Scores scores;
    GameOverScreen gameOverScreen;
    FarflightGame farflightGame;
    Activity activity;
    int scorePass = 11;
    int scoreFail = 10;
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

    @Before
    public void setUp(){
        context = InstrumentationRegistry.getTargetContext();
        GameTest gameTest=new GameTest();
        farflightGame=gameTest.setUp();
        gameOverScreen = new GameOverScreen(farflightGame, scorePass, "");
    }

    @Test
    public void getLastScore_isFailure() {
        assertNotSame(gameOverScreen.getScore(), scoreFail);
    }

}

