package uk.ac.qub.eeecs.game.miniGame;


import org.junit.Before;
import org.junit.Test;

import uk.ac.qub.eeecs.gage.GameTest;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.game.FarflightGame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Andrew Bell
 */

public class BananaSpriteTest {
    BananaGameScreen bananaGameScreen;
    FarflightGame farflightGame;
    BananaSprite bananaSprite;

    @Before
    public void setUp(){
        GameTest gameTest=new GameTest();
        farflightGame=gameTest.setUp();
        bananaGameScreen = new BananaGameScreen(farflightGame);
        bananaSprite=new BananaSprite(bananaGameScreen, bananaGameScreen.mLayerViewport);
    }

    @Test
    public void getBananaType_good_success(){
        bananaSprite=new BananaSprite(bananaGameScreen, bananaGameScreen.mLayerViewport,BananaType.GOOD);
        assertEquals(BananaType.GOOD,bananaSprite.getBananaType());
    }
    @Test
    public void getBananaType_super_success(){
        bananaSprite=new BananaSprite(bananaGameScreen, bananaGameScreen.mLayerViewport,BananaType.SUPER);
        assertEquals(BananaType.SUPER,bananaSprite.getBananaType());
    }
    @Test
    public void getBananaType_bad_success(){
        bananaSprite=new BananaSprite(bananaGameScreen, bananaGameScreen.mLayerViewport,BananaType.BAD);
        assertEquals(BananaType.BAD,bananaSprite.getBananaType());
    }

    @Test
    public void randomiseBanana_callConstructor_success(){
       for(int i=0;i<50;i++){
        bananaSprite=new BananaSprite(bananaGameScreen, bananaGameScreen.mLayerViewport);
        float bananaAfeatures= bananaSprite.position.x+bananaSprite.velocity.x+bananaSprite.velocity.y+bananaSprite.angularVelocity;
        bananaSprite=new BananaSprite(bananaGameScreen, bananaGameScreen.mLayerViewport);
        float bananaBfeatures= bananaSprite.position.x+bananaSprite.velocity.x+bananaSprite.velocity.y+bananaSprite.angularVelocity;
        assertNotEquals(bananaAfeatures,bananaBfeatures);}
    }

    @Test
    public void setBananaAnimation_good_success(){
        bananaSprite=new BananaSprite(bananaGameScreen, bananaGameScreen.mLayerViewport,BananaType.GOOD);
        bananaSprite.setSpriteAnimation();
        assertEquals(farflightGame.getAssetManager().getBitmap("banana"),bananaSprite.spinAnimation.getBitmap());
    }
    @Test
    public void setBananaAnimation_super_success(){
        bananaSprite=new BananaSprite(bananaGameScreen, bananaGameScreen.mLayerViewport,BananaType.SUPER);
        bananaSprite.setSpriteAnimation();
        assertEquals(farflightGame.getAssetManager().getBitmap("superBanana"),bananaSprite.spinAnimation.getBitmap());
    }
    @Test
    public void setBananaAnimation_bad_success(){
        bananaSprite=new BananaSprite(bananaGameScreen, bananaGameScreen.mLayerViewport,BananaType.BAD);
        bananaSprite.setSpriteAnimation();
        assertEquals(farflightGame.getAssetManager().getBitmap("badBanana"),bananaSprite.spinAnimation.getBitmap());
    }

    @Test
    public void keepInBounds_left_success(){
        bananaSprite.position.x=-500;
        bananaSprite.velocity.x=-10;
        ElapsedTime elapsedTime=new ElapsedTime();
        bananaSprite.update(elapsedTime);
        assertEquals(10,bananaSprite.velocity.x,1.0);
    }
    @Test
    public void keepInBounds_right_success(){
        bananaSprite.position.x= bananaGameScreen.mLayerViewport.getRight()+50;
        bananaSprite.velocity.x=10;
        ElapsedTime elapsedTime=new ElapsedTime();
        bananaSprite.update(elapsedTime);
        assertEquals(-10,bananaSprite.velocity.x,1.0);
    }
    @Test
    public void keepInBounds_inBounds_success(){
        bananaSprite.position.x=500;
        bananaSprite.velocity.x=10;
        ElapsedTime elapsedTime=new ElapsedTime();
        bananaSprite.update(elapsedTime);
        assertEquals(10,bananaSprite.velocity.x,1.0);
    }

}
