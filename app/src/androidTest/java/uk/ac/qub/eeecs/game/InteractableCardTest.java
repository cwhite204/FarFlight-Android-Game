package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.graphics.Matrix;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import uk.ac.qub.eeecs.gage.GameTest;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.game.gameScreens.CardScreen;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by andyj on 14/04/2018.
 */

public class InteractableCardTest {
    private Context context;
    CardScreen cardscreen;
    FarflightGame farflightGame;
    InteractableCard card;

    @Before
    public void setUp(){
        context = InstrumentationRegistry.getTargetContext();
        GameTest gameTest=new GameTest();
        farflightGame=gameTest.setUp();
        cardscreen = new CardScreen(farflightGame);
        int[] intArray={0,0,0,0};
        card=new InteractableCard(cardscreen,1, "test", "test", "test",
                "test", 0, 0, intArray, intArray);
    }

    @Test
    public void animateCardOffScreen_left_success(){
        card.animateCardOffScreen('l');
        assertEquals(-1000f,card.velocity.x,1.0);

    }
    @Test
    public void animateCardOffScreen_right_success(){
        card.animateCardOffScreen('r');
        assertEquals(1000f,card.velocity.x,1.0);

    }

    @Test
    public void cardFlip_currently0_added(){
        assertEquals(-0.9,card.getFlipScaleAndSetBitmap(),1.0);
    }
    @Test
    public void cardFlip_currently0loop_added(){
        for(int i=0;i<20;i++){
            card.getFlipScaleAndSetBitmap();
        }
        assertEquals(1,card.getFlipScaleAndSetBitmap(),1.0);
    }

    @Test
    public void getHorizontalFLipBitmap(){
        Matrix a=card.getHorizontalFlipMatrix(-0.5f);
        Matrix b=card.getHorizontalFlipMatrix(-0.4f);
        assertNotEquals(a,b);
    }

    @Test
    public void resetCardFlip(){
        card.resetCardFlip();
        assertTrue(card.flipCard);
        assertEquals(-1,card.flipScale,1.0);

    }

    @Test
    public void cardAnimatingAway_true_success(){
        Boolean expected=true;
        card.setCardAnimatingAway(expected);
        assertEquals(expected,card.isCardAnimatingAway());

    }
    @Test
    public void cardAnimatingAway_false_success(){
        Boolean expected=true;
        card.setCardAnimatingAway(expected);
        assertEquals(expected,card.isCardAnimatingAway());

    }

    @Test
    public void cardTouchingLeftHotspot_true_success(){
        Boolean expected=true;
        card.setCardTouchingLeftHotspot(expected);
        assertEquals(expected,card.getCardTouchingLeftHotspot());
    }
    @Test
    public void cardTouchingLeftHotspot_false_success(){
        Boolean expected=false;
        card.setCardTouchingLeftHotspot(expected);
        assertEquals(expected,card.getCardTouchingLeftHotspot());
    }

    @Test
    public void cardTouchingRightHotspot_true_success(){
        Boolean expected=true;
        card.setCardTouchingLeftHotspot(expected);
        assertEquals(expected,card.getCardTouchingLeftHotspot());
    }
    @Test
    public void cardTouchingRightHotspot_false_success(){
        Boolean expected=false;
        card.setCardTouchingRightHotspot(expected);
        assertEquals(expected,card.getCardTouchingRightHotspot());
    }

    @Test
    public void wasTouching_true_success(){
        Boolean expected=false;
        card.setWasTouching(expected);
        assertEquals(expected,card.getWasTouching());
    }

    @Test
    public void cardSwipeHint(){
        for(int i=0;i<10000;i++){
        card.cardSwipeHint(cardscreen.getmLayerViewport().halfWidth);}
        assertTrue(card.cardCenter);
    }
}
