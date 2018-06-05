package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.graphics.Bitmap;


import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.world.GameScreen;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by andyj on 16/02/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class DeckTest {


    @Mock
    Game game = mock(Game.class);
    GameScreen gameScreen= mock(GameScreen.class);
    Context context = mock(Context.class);
    private Deck deck;
    @Mock
    private AssetStore assetManager;
    @Mock
    private Bitmap bitmap;


    @Before
    public void setup() throws JSONException {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        deck =new Deck(null);
        deck.loadJson("characters.json");
        when(gameScreen.getGame()).thenReturn(game);
        InteractableCard mockedInteractableCard = mock(InteractableCard.class);
        InteractableCard mockedInteractableCard2 = mock(InteractableCard.class);
        InteractableCard mockedInteractableCard3 = mock(InteractableCard.class);
        deck.addCard(mockedInteractableCard);
        deck.addCard(mockedInteractableCard2);
        deck.addCard(mockedInteractableCard3);
       // JSONObject test=mock(JSONObject.class);
        //when(test.getInt(any(String.class))).thenReturn(0);
        //when(test.getString(any(String.class))).thenReturn("test");
        //deck.getCardFromJson(test);
//        when(deck.getCardFromJson(any(JSONObject.class))).thenReturn(mockedInteractableCard);

    }

    @Test
    public void loadJson_ValidData_TestSuccessful(){
        deck =new Deck(gameScreen);
        boolean success = deck.loadJson("characters.json");
        assertTrue(success);
    }


    @Test
    public void loadJson_InvalidData_TestError(){

        boolean success = deck.loadJson("characters");
        assertFalse(success);


    }

    @Test
    public void getCard_ContinueStory() throws Exception {
        InteractableCard card= deck.getCard(0);
        assertNotNull(card);
    }
    @Test
    public void getCard_SubStory() throws Exception {
        InteractableCard card= deck.getCard(1);
        assertNotNull(card);
    }
    @Test
    public void getCard_StopStory() throws Exception {
        InteractableCard mockedInteractableCard = mock(InteractableCard.class);
//        when(deck.getCardFromJson(any(JSONObject.class))).thenReturn(mockedInteractableCard);
        InteractableCard card= deck.getCard(-1);
        assertNotNull(card);
    }

    @Test
    public void addCard() throws Exception {
        InteractableCard mockedInteractableCard = mock(InteractableCard.class);
        int currentDeckSize=deck.getDeck().size();
        deck.addCard(mockedInteractableCard);
        int newDeckSize=deck.getDeck().size();
        assertEquals(currentDeckSize+1,newDeckSize);
    }

    @Test
    public void emptyDeck() throws Exception {
        deck.loadJson("characters.json");
        deck.emptyDeck();
        assertEquals(0,deck.getDeck().size());
    }

    @Test
    public void removeCard() throws Exception {
        int currentDeckSize=deck.getDeck().size();
        deck.removeCard(deck.getCard(0));
        int newDeckSize=deck.getDeck().size();
        assertEquals(currentDeckSize-1,newDeckSize);
    }



    @Test
    public void getDeck() throws Exception {

        ArrayList<InteractableCard> deckList=deck.getDeck();
        assertNotNull(deckList);
    }
    @Test
    public void loadStory() throws Exception {
        boolean success= deck.loadStory();
        assertTrue(success);
    }

    @Test
    public void loadSubStory_validData() throws Exception {
        boolean success= deck.loadSubStory(1);
        assertTrue(success);
    }
    @Test
    public void loadSubStory_invalidData() throws Exception {
        boolean success= deck.loadSubStory(0);
        assertFalse(success);
    }

    @Test
    public void loadJSONFromAsset() throws Exception {
        String jsonContent=deck.loadJSONFromAsset("characters.json");
        assertNotNull(jsonContent);
    }

}
