package uk.ac.qub.eeecs.game;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//Author Tom Purdon
//Class adapted from example found on QOL for Blasto and Tribal Hunter
public class Scores{

    //Application activity
    private Activity activity;
    //Size of the list of Top Scores
    private final int highScoreList = 5;
    //Array of the highest scores
    private int highScores[] = new int[highScoreList];

    /***
     * Return the top High Scores from SharedPreferences
     */
    public int[] getHighScores() {
        loadScores();
        return highScores;
    }

    /***
     * Create a new HighScoreManager
     *
     * @param activity
     *            - application activity
     */
    public Scores(Activity activity) {
        this.activity = activity;
        loadScores();
    }

    //to be used for a clear scores button in the future
    public void clearScores(){
        // Get the shared preferences for the app
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(activity.getApplicationContext());

        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public void loadScores (){
        // Get the shared preferences for the app
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(activity.getApplicationContext());

        // Retrieve the top scores
        for (int i = 0; i < highScoreList; i++) {
            highScores[i] = preferences.getInt("Top" + (i + 1), 0);
        }
    }

    /***
     * Save the top scores to the shared preferences
     */
    private void saveTopScores() {
        // Get the shared preferences for the app
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(activity.getApplicationContext());

        // Get an editor for updating the preferences
        SharedPreferences.Editor preferenceEditor = preferences.edit();

        // Store the top scores
        for (int i = 0; i < highScoreList; i++) {
            preferenceEditor.putInt("Top" + (i + 1), highScores[i]);
        }

        // Commit the preference changes
        preferenceEditor.commit();
    }

    /***
     * Update the top scores with the new score
     *
     * @param newScore
     *            New score to consider against the top scores
     *
     * @return Boolean true if the top scores were changed, otherwise false
     */
    private boolean updateTopScores(int newScore) {

        boolean isChanged = false;

        int score = newScore, temp;
        for (int i = 0; i < highScoreList; i++) {
            if (score > highScores[i]) {
                temp = highScores[i];
                highScores[i] = score;
                score = temp;

                isChanged = true;
            }
        }

        return isChanged;
    }

    /***
     * Public method to check if player has scored a new high score and if so,
     * save.
     */
    public boolean newHighScore(int playerScore) {
        if (updateTopScores(playerScore)) {
            saveTopScores();
            return true;
        } else {
            return false;
        }
    }

    public int getTopScore() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(activity.getApplicationContext());

        SharedPreferences.Editor preferenceEditor = preferences.edit();

        int topScore = preferences.getInt("Top" + (0 + 1), 0);

        return topScore;
    }
}