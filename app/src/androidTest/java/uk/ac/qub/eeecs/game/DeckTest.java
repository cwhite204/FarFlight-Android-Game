package uk.ac.qub.eeecs.game;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.GameTest;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.game.gameScreens.CardScreen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Andrew Bell
 */

@RunWith(AndroidJUnit4.class)
public class DeckTest {
    CardScreen cardScreen;
    FarflightGame farflightGame;
    ScreenManager screenManager;
    Deck deck;

    @Before
    public void setUp() {
        GameTest gameTest = new GameTest();
        farflightGame = gameTest.setUp();
        cardScreen = new CardScreen(farflightGame);
        screenManager = farflightGame.getScreenManager();

        deck = new Deck(cardScreen);
        deck.loadJson("characters.json");
    }

    @Test
    public void loadJson_ValidData_TestSuccessful() {
        boolean success = deck.loadJson("characters.json");
        assertTrue(success);
    }

    @Test
    public void loadJson_InvalidData_TestError() {
        boolean success = deck.loadJson("characters");
        assertFalse(success);
    }

    @Test
    public void getCard_ContinueStory_Success() {
        InteractableCard card = deck.getCard(0);
        assertNotNull(card);
    }

    @Test
    public void getCard_SubStory_Success() {
        InteractableCard card = deck.getCard(1);
        assertNotNull(card);
    }

    @Test
    public void getCard_StopStory_Success() {
        InteractableCard card = deck.getCard(-1);
        assertNotNull(card);
    }

    @Test
    public void addCard_Success() {
        int[] intArray = {0, 0, 0, 0};
        InteractableCard interactableCard = new InteractableCard(cardScreen, 1, "test", "test", "test",
                "test", 0, 0, intArray, intArray);
        int currentDeckSize = deck.getDeck().size();
        deck.addCard(interactableCard);
        int newDeckSize = deck.getDeck().size();
        assertEquals(currentDeckSize + 1, newDeckSize);
    }

    @Test
    public void emptyDeck_success() {
        deck.loadJson("characters.json");
        deck.emptyDeck();
        assertEquals(0, deck.getDeck().size());
    }

    @Test
    public void removeCard_success() {
        deck.loadStory();
        int currentDeckSize = deck.getDeck().size();
        deck.removeCard(deck.getCard(0));
        int newDeckSize = deck.getDeck().size();
        assertEquals(currentDeckSize - 1, newDeckSize);
    }

    @Test
    public void getDeck_success() {
        ArrayList<InteractableCard> deckList = deck.getDeck();
        assertNotNull(deckList);
    }

    @Test
    public void loadStory() {
        boolean success = deck.loadStory();
        assertTrue(success);
    }

    @Test
    public void loadSubStory_validData_success() {
        boolean success = deck.loadSubStory(1);
        assertTrue(success);
    }

    @Test
    public void loadSubStory_invalidData_fail() {
        boolean success = deck.loadSubStory(0);
        assertFalse(success);
    }

    @Test
    public void loadJSONFromAsset_validData_success() {
        String jsonContent = deck.loadJSONFromAsset("characters.json");
        assertNotNull(jsonContent);
    }

    @Test
    public void runMiniGame_success() {
        deck.runMiniGame(cardScreen);
        assertEquals("BananaGameScreen", screenManager.getCurrentScreen().getName());
    }

}
