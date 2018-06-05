package uk.ac.qub.eeecs.game;

import android.graphics.Bitmap;
import android.graphics.Paint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.gameScreens.CardScreen;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
/**
 * Created by Matthew Downey on 14/02/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class CardScreenTest {

    @Mock
    Game game = mock(Game.class);
    GameScreen gameScreen= mock(GameScreen.class);
    @Mock
    private AssetStore assetManager;

    @Mock
    private Bitmap bitmap;
    CardScreen cardScreen=mock(CardScreen.class);


    @Before
    public void setUp(){
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        cardScreen=spy(new CardScreen(game));

    }

    @Test
    public void testScoreTotalsCorrectly_TestSuccess() {
        int n = 20;
        for (int i = 0 ; i < n; i++) {
            cardScreen.prepareNextCard('l');
        }
        assertEquals(cardScreen.getWeeksSurvived(), n);

    }
}