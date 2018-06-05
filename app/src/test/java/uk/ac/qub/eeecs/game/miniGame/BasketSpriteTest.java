package uk.ac.qub.eeecs.game.miniGame;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by andyj on 11/04/2018.
 */

@RunWith(MockitoJUnitRunner.class)


public class BasketSpriteTest {
    @Mock
    Game game = mock(Game.class);

    BananaGameScreen gameScreen= mock(BananaGameScreen.class);
    @Mock
    private AssetStore assetManager;

    @Mock
    private Bitmap bitmap;

    @Mock
    private Input input;

    BasketSprite basketSprite;
    @Before
    public void setUp() {
//        when(gameScreen.getGame()).thenReturn(game);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        gameScreen=new BananaGameScreen(game);

        basketSprite =new BasketSprite(gameScreen);}

    @Test
    public void superSizeMode(){
        basketSprite.setSuperSizeMode();
        assertEquals(basketSprite.superSizeMode,true);
    }

    @Test
    public void touchHandler(){

        when(game.getInput()).thenReturn(input);
        when(input.existsTouch(any(int.class))).thenReturn(true);
        when(input.getTouchX(any(int.class))).thenReturn(20f);
        LayerViewport mockedLayerViewport=mock(LayerViewport.class);
        ScreenViewport mockedScreenViewport=mock(ScreenViewport.class);
        basketSprite.spriteTouchHandler(mockedLayerViewport,mockedScreenViewport);
        assertEquals(20f, basketSprite.position.x,1.0);
    }


}
