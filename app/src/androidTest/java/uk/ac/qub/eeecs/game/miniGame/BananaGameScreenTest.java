package uk.ac.qub.eeecs.game.miniGame;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.GameTest;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.game.FarflightGame;
import uk.ac.qub.eeecs.game.gameScreens.CardScreen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Andrew Bell
 */

@RunWith(AndroidJUnit4.class)
public class BananaGameScreenTest {
    BananaGameScreen bananaGameScreen;
    FarflightGame farflightGame;
    ScreenManager screenManager;


    @Before
    public void setUp() {
        GameTest gameTest = new GameTest();
        farflightGame = gameTest.setUp();
        bananaGameScreen = new BananaGameScreen(farflightGame);
        screenManager=farflightGame.getScreenManager();

    }

    @Test
    public void releaseBananas_alreadyTenBananas_noneAdded(){
        bananaGameScreen.releaseBananas();
        assertEquals(10, bananaGameScreen.bananaSprites.size());
    }

    @Test
    public void releaseBananas_clearArray_4BananasAdded(){
        bananaGameScreen.bananaSprites.clear();
        bananaGameScreen.releaseBananas();
        assertEquals(4, bananaGameScreen.bananaSprites.size());
    }

    @Test
    public void decideNextScreen_nullCardScreen_launchMainMenuScreen() {
        bananaGameScreen.cardScreen = null;
        bananaGameScreen.decideNextScreen();
        assertEquals("MainMenuScreen",screenManager.getCurrentScreen().getName());
    }
    @Test
    public void decideNextScreen_cardScreen_launchMainMenuScreen() {
        bananaGameScreen.cardScreen = new CardScreen(farflightGame);
        bananaGameScreen.decideNextScreen();
        assertEquals("CardScreen",screenManager.getCurrentScreen().getName());
    }

    @Test
    public void removeBananaOffScreenTest_offScreen_bananaRemoved(){
        BananaSprite bananaSprite= bananaGameScreen.bananaSprite;
        bananaSprite.position.y=-500;
        bananaGameScreen.removeBananaIfOffScreen(bananaSprite);
        assertEquals(9, bananaGameScreen.bananaSprites.size());
    }

    @Test
    public void removeBananaOffScreenTest_onScreen_bananaNotRemoved(){
        BananaSprite bananaSprite= bananaGameScreen.bananaSprite;
        bananaSprite.position.y=500;
        bananaGameScreen.removeBananaIfOffScreen(bananaSprite);
        assertEquals(10, bananaGameScreen.bananaSprites.size());
    }

    @Test
    public void addToScore(){
        bananaGameScreen.addToScore(1);
        assertEquals(1, bananaGameScreen.score);
    }
    @Test
    public void addToScore_Invalid_Successful(){
        bananaGameScreen.addToScore(-10);
        assertEquals(0, bananaGameScreen.score);
    }

    @Test
    public void addBananaSprites(){
        bananaGameScreen.bananaSprites.clear();
        bananaGameScreen.addBananaSprites(10);
        assertEquals(10, bananaGameScreen.bananaSprites.size());
    }
    @Test
    public void addBananaSprites_invalidData_testSuccessful(){
        bananaGameScreen.bananaSprites.clear();
        bananaGameScreen.addBananaSprites(-1);
        assertEquals(0, bananaGameScreen.bananaSprites.size());
    }
    @Test
    public void testInitScreen(){
        bananaGameScreen.initScreen();
        assertNotNull(bananaGameScreen.mLayerViewport);
    }

    @Test
    public void handleBananaCollision_superBananaCollided_add3(){
        BananaSprite bananaSprite=new BananaSprite(bananaGameScreen, bananaGameScreen.mLayerViewport,BananaType.SUPER);
        BasketSprite basketSprite =new BasketSprite(bananaGameScreen);
        bananaSprite.setPosition(0,0);
        basketSprite.setPosition(0,0);
        bananaGameScreen.handleBananaCollision(bananaSprite, basketSprite);
        assertEquals(3, bananaGameScreen.score);}
    @Test
    public void handleBananaCollision_normalBananaCollided_add1(){
        BananaSprite bananaSprite=new BananaSprite(bananaGameScreen, bananaGameScreen.mLayerViewport,BananaType.GOOD);
        BasketSprite basketSprite =new BasketSprite(bananaGameScreen);
        bananaSprite.setPosition(0,0);
        basketSprite.setPosition(0,0);
        bananaGameScreen.handleBananaCollision(bananaSprite, basketSprite);
        assertEquals(1, bananaGameScreen.score);}
    @Test
    public void handleBananaCollision_badBananaCollided_minus5(){
        BananaSprite bananaSprite=new BananaSprite(bananaGameScreen, bananaGameScreen.mLayerViewport,BananaType.BAD);
        BasketSprite basketSprite =new BasketSprite(bananaGameScreen);
        bananaSprite.setPosition(0,0);
        basketSprite.setPosition(0,0);
        bananaGameScreen.handleBananaCollision(bananaSprite, basketSprite);
        assertEquals(0, bananaGameScreen.score);}

    public void handleBananaCollision_notCollided_nothing(){
        BananaSprite bananaSprite=new BananaSprite(bananaGameScreen, bananaGameScreen.mLayerViewport,BananaType.BAD);
        BasketSprite basketSprite =new BasketSprite(bananaGameScreen);
        bananaSprite.setPosition(0,0);
        basketSprite.setPosition(0,0);
        bananaGameScreen.handleBananaCollision(bananaSprite, basketSprite);
        assertEquals(0, bananaGameScreen.score);}

    public void loadAssets_validAssets_success() {
        bananaGameScreen.loadAssets(farflightGame.getAssetManager());
        assertNotNull(farflightGame.getAssetManager().getBitmap("backgroundM"));
        assertNotNull(farflightGame.getAssetManager().getBitmap("1"));
        assertNotNull(farflightGame.getAssetManager().getBitmap("banana"));
        assertNotNull(farflightGame.getAssetManager().getBitmap("superBanana"));
        assertNotNull(farflightGame.getAssetManager().getBitmap("badBanana"));}

    public void loadAssets_invalidAssets_fail() {
        bananaGameScreen.loadAssets(farflightGame.getAssetManager());
        assertNotNull(farflightGame.getAssetManager().getBitmap("fail"));
    }
}
