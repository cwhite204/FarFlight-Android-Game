package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import uk.ac.qub.eeecs.gage.GameTest;
import uk.ac.qub.eeecs.game.gameScreens.CardScreen;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * author: Andrew Bell
 */

public class CardTest {
    private Context context;
    CardScreen cardscreen;
    FarflightGame farflightGame;
    Card card;

    @Before
    public void setUp(){
        context = InstrumentationRegistry.getTargetContext();
        GameTest gameTest=new GameTest();
        farflightGame=gameTest.setUp();
        cardscreen = new CardScreen(farflightGame);
        int[] intArray={0,0,0,0};
        card=new Card(cardscreen,1, "test", "test", "test",
                "test", 0, 0, intArray, intArray);
    }
    @Test
    public void drawIconOnCard_Success(){
        Bitmap success = card.drawIconOnCard(farflightGame.getAssetManager().getBitmap("card_template1"),
                farflightGame.getAssetManager().getBitmap("cardback"));
        assertNotNull(success);
    }

    @Test
    public void getlStoryPathway(){
        int success= card.getlStoryPathway();
        assertNotNull(success);
    }

    @Test
    public void getrStoryPathway(){
        int success= card.getrStoryPathway();
        assertNotNull(success);
    }

    @Test
    public void getlStatEffect(){
        int[] success=card.getlStatEffect();
        assertNotNull(success);
    }

    @Test
    public void getrStatEffect(){
        int[] success=card.getrStatEffect();
        assertNotNull(success);
    }

    @Test
    public void setBitmap(){
        card.setBitmap(farflightGame.getAssetManager().getBitmap("card_template1"));
        assertNotNull(card.getBitmap());
    }

    @Test
    public void toggleCardBitmap_true(){
        Boolean success= card.toggleCardBitmap(true);
        assertTrue(success);
    }
    @Test
    public void toggleCardBitmap_false(){
        Boolean success= card.toggleCardBitmap(false);
        assertFalse(success);
    }

    @Test
    public void setCardToMiddle(){
        card.position.add(340,0);
        card.setCardToMiddle();
        assertEquals(240.0,card.position.x,1.0);
    }
}
