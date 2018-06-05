package uk.ac.qub.eeecs.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.collectMiniGame.CollectMiniGame;
import uk.ac.qub.eeecs.game.gameScreens.CardScreen;
import uk.ac.qub.eeecs.game.miniGame.BananaGameScreen;

/**
 * Class is used to manage an ArrayList of InteractableCards.
 * JSON file is loaded and parsed into InteractableCard Objects
 * Handles card potential pathways
 *
 * @author Andrew Bell
 *         Adding Authors Chris White, Tom Purdon, Matthew Downey
 */

public class Deck {

    /**
     * Should the deck load the intro story?
     */
    private boolean isIntro = false;

    /**
     * Arraylist of Playercards
     */
    private ArrayList<InteractableCard> deck = new ArrayList<>();

    /**
     * GameScreen object
     */
    private GameScreen gameScreen;

    /**
     * List of stories already played
     */
    private ArrayList<Integer> storiesPlayed = new ArrayList<>();

    /**
     * JsonObject
     */
    private JSONObject jsonObject;

    /**
     * Json Array
     */
    private JSONArray jsonArray;

    public static final String DECK_FILE_NAME="characters.json";
    /**
     * Deck Constructor
     * Sets gamescreen and loads JSON
     *
     * @param gameScreen
     */
    public Deck(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

    }

    /**
     * Take a string and loads the jsonObject with the JSON from the file
     *
     * @param fileName JSON file name
     * @return
     * @author Andrew Bell
     */
    public Boolean loadJson(String fileName) {
        Boolean success = true;

        String jsonString = loadJSONFromAsset(fileName);
        try {
            //loads in JSON file as an array
            jsonArray = (new JSONArray(jsonString));
            jsonObject = jsonArray.getJSONObject(0);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    /**
     * Load a normal story within the game (not a sub story)
     *
     * @return true if completed
     * @authors Andrew Bell, Tom Purdon
     */
    public Boolean loadStory() {
        JSONArray jsonSubArray = null;
        try {
            jsonObject = jsonArray.getJSONObject(0);
            jsonSubArray = jsonObject.getJSONArray("Stories");

            Random random = new Random();
            //gets a random (story) element
            Integer randomStory = random.nextInt(jsonSubArray.length());
            if (storiesPlayed.contains(randomStory) && storiesPlayed.size() < jsonSubArray.length()) {
                //Story has been played and stories left so play a different one
                loadStory();
                return false;
            } else if (!storiesPlayed.contains(randomStory)) {
                storiesPlayed.add(randomStory);
            } else {
                storiesPlayed.clear();
                loadStory();
                return false;
            }
            JSONArray jsonA = jsonSubArray.getJSONArray(random.nextInt(jsonSubArray.length()));
            for (int i = 0; i < jsonA.length(); i++) {
                JSONObject jsonObj = jsonA.getJSONObject(i);
                addCard(getCardFromJson(jsonObj));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Load a substory from JSON
     *
     * @param subStoryPos The sub story that should be loaded
     * @return True if completed successfully
     * @author Andrew Bell
     */
    public Boolean loadSubStory(int subStoryPos) {
        JSONArray jsonSubArray = null;
        try {
            jsonObject = jsonArray.getJSONObject(1);
            jsonSubArray = jsonObject.getJSONArray("SubStories");
            JSONArray jsonA = jsonSubArray.getJSONArray(subStoryPos - 1);
            for (int i = 0; i < jsonA.length(); i++) {
                JSONObject jsonObj = jsonA.getJSONObject(i);
                deck.add(getCardFromJson(jsonObj));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Loads intro story  from JSON
     *
     * @return Boolean, true if method was successful
     * @author Chris White. Adapted from Andrew's loadSubStory()
     */
    public Boolean loadIntro() {
        isIntro = true;
        JSONArray jsonSubArray = null;
        try {
            jsonObject = jsonArray.getJSONObject(2);
            jsonSubArray = jsonObject.getJSONArray("Intro");
            JSONArray jsonA = jsonSubArray.getJSONArray(0);
            for (int i = 0; i < jsonA.length(); i++) {
                JSONObject jsonObj = jsonA.getJSONObject(i);
                deck.add(getCardFromJson(jsonObj));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Scrapes data from JSON object and creates a new player card with these values
     *
     * @param jsonObj The Json object of Card data
     * @return InteractableCard object with values form data
     * @author Andrew Bell
     */
    public InteractableCard getCardFromJson(JSONObject jsonObj) {

        if (gameScreen == null) return null;
        int CharID = 0;
        try {
            CharID = jsonObj.getInt("CharID");

            String name = jsonObj.getString("Name");
            String description = jsonObj.getString("Description");
            String leftSwipeAction = jsonObj.getString("LeftSwipeAction");
            String rightSwipeAction = jsonObj.getString("RightSwipeAction");
            int lStoryPathway = jsonObj.getInt("lStoryPathway");
            int rStoryPathway = jsonObj.getInt("rStoryPathway");
            int lMorale = jsonObj.getInt("lMorale");
            int lFoodWater = jsonObj.getInt("lFoodWater");
            int lHealth = jsonObj.getInt("lHealth");
            int lShelter = jsonObj.getInt("lShelter");
            int rMorale = jsonObj.getInt("rMorale");
            int rFoodWater = jsonObj.getInt("rFoodWater");
            int rHealth = jsonObj.getInt("rHealth");
            int rShelter = jsonObj.getInt("rShelter");
            int[] lStatEffect = {lMorale, lFoodWater, lHealth, lShelter};
            int[] rStatEffect = {rMorale, rFoodWater, rHealth, rShelter};

            InteractableCard card = new InteractableCard(gameScreen, CharID, name, description, leftSwipeAction,
                    rightSwipeAction, lStoryPathway, rStoryPathway, lStatEffect, rStatEffect);

            return card;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a card based on pathway
     * if pathway is 0 load next card, if deck empty, load new card
     * if pathway is -1 Do not continue current story, empty deck, load new story
     * if card is - 2 THen load monkey miniGame
     * if card is -3, then load collectMiniGame
     * Can continue adding negative numbers that perform special actions
     * <p>
     * A positive int means this int in array of substory should now be loaded
     *
     * @param storyPathway The pathway the story should take
     * @return InteractableCard Object that has been loaded
     * @author Andrew Bell
     * Adding Author: Matthew Downey: Added CollectMiniGame
     */
    public InteractableCard getCard(int storyPathway) {
        if (storyPathway == 0) {
            if (deck.size() == 0)
                loadStory();
            return deck.get(0);
        } else if (storyPathway == -1) {
            deck.clear();
            isIntro = false;
            loadStory();
            return deck.get(0);

        } else if (storyPathway == -2) {
            runMiniGame(new BananaGameScreen(gameScreen.getGame(), (CardScreen) gameScreen));
            return deck.get(0);
        } else if (storyPathway == -3) {
            runMiniGame(new CollectMiniGame(gameScreen.getGame(), (CardScreen) gameScreen));
            return deck.get(0);
        } else {
            loadSubStory(storyPathway);
            return deck.get(0);
        }

    }

    /**
     * Creates instance of mini game and sets it as the current game screen
     */
    protected void runMiniGame(GameScreen miniGameScreen) {
        gameScreen.getGame().getScreenManager().removeScreen(gameScreen.getName());
        gameScreen.getGame().getScreenManager().addScreen(miniGameScreen);
    }

    /**
     * Adds card to deck arrayList
     *
     * @param card Interactable card object to add to list
     */
    public boolean addCard(InteractableCard card) {
        if (deck.contains(card)) {
            return false;
        }
        this.deck.add(card);
        return true;
    }


    /**
     * Empty deck arraylist
     *
     * @author Andrew Bell
     */
    public void emptyDeck() {
        this.deck.clear();
    }

    /**
     * Removes a card object from deck arraylist
     *
     * @param card Interactable card object to remove
     * @author Andrew Bell
     */
    public void removeCard(InteractableCard card) {
        deck.remove(card);
    }

    /**
     * Getter
     *
     * @return ArrayList of Player cards
     * @author Andrew Bell
     */
    public ArrayList<InteractableCard> getDeck() {
        return deck;
    }

    /**
     * Opens Json file resource and creates a string of the JSON data
     *
     * @param jsonFileName
     * @return Adapted from Faizan at https://stackoverflow.com/questions/13814503/reading-a-json-file-in-android/13814551#13814551
     */
    public String loadJSONFromAsset(String jsonFileName) {
        String jsonString = null;
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonFileName);

            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;

    }

    /**
     * Getter
     *
     * @return Boolean isIntro
     */
    public boolean getIsIntro() {
        return isIntro;
    }

    public void setIntro(boolean intro) {
        isIntro = intro;
    }
}
