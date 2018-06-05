package uk.ac.qub.eeecs.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.game.gameScreens.CardScreen;
import uk.ac.qub.eeecs.game.gameScreens.MainMenuScreen;
import uk.ac.qub.eeecs.game.gameScreens.SplashScreen;
import uk.ac.qub.eeecs.game.miniGame.BananaGameScreen;

/**
 * Farflight Game adapted from the demo Game
 *
 * @version 1.0
 */
public class FarflightGame extends Game {

    /**
     * Create a FarFlight Game
     */
    public FarflightGame() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.ac.qub.eeecs.gage.Game#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Increased FPS to 60
        setTargetFramesPerSecond(60);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Call the Game's onCreateView to get the view to be returned.
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Create and add a stub game screen to the screen manager. We don't
        // want to do this within the onCreate method as the menu screen
        // will layout the buttons based on the size of the view.

        SplashScreen splash = new SplashScreen(this);

        mScreenManager.addScreen(splash);

        return view;
    }

    /**
     * Overrides onBackPressed to help game navigation and toggles game sound
     *
     * @return
     */
    @Override
    public boolean onBackPressed() {
        // If we are already at the menu screen then exit
        if (mScreenManager.getCurrentScreen().getName().equals("MainMenuScreen")
                || mScreenManager.getCurrentScreen().getName().equals("SplashScreen"))
            return false;

        if (mScreenManager.getCurrentScreen().getName().equals("CardScreen")) {
            CardScreen cardScreen = (CardScreen) mScreenManager.getCurrentScreen();
            cardScreen.toggleSound();
        } else if (mScreenManager.getCurrentScreen().getName().equals("MainMenuScreen")) {
            MainMenuScreen mainMenuScreen = (MainMenuScreen) mScreenManager.getCurrentScreen();
            mainMenuScreen.toggleSound();
        } else if (mScreenManager.getCurrentScreen().getName().equals("BananaGameScreen")) {
            BananaGameScreen bananaGameScreen = (BananaGameScreen) mScreenManager.getCurrentScreen();
            bananaGameScreen.toggleSound();
        }
        // Go back to the menu screen
        getScreenManager().removeScreen(mScreenManager.getCurrentScreen().getName());
        MainMenuScreen mainMenuScreen = new MainMenuScreen(this);
        mainMenuScreen.toggleSound();
        getScreenManager().addScreen(mainMenuScreen);

        return true;
    }

    /**
     * onStart
     * Toggles the sound to stop
     * author: Andrew Bell
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mScreenManager.getCurrentScreen().getName().equals("CardScreen")) {
            CardScreen cardScreen = (CardScreen) mScreenManager.getCurrentScreen();
            cardScreen.toggleSound();
        } else if (mScreenManager.getCurrentScreen().getName().equals("MainMenuScreen")) {
            MainMenuScreen mainMenuScreen = (MainMenuScreen) mScreenManager.getCurrentScreen();
            mainMenuScreen.toggleSound();
        } else if (mScreenManager.getCurrentScreen().getName().equals("BananaGameScreen")) {
            BananaGameScreen bananaGameScreen = (BananaGameScreen) mScreenManager.getCurrentScreen();
            bananaGameScreen.toggleSound();
        }
    }

    /**
     * onStart
     * Toggles the sound to play
     * author: Andrew Bell
     */
    @Override
    public void onStart() {
        super.onStart();
        if (mScreenManager.getCurrentScreen().getName().equals("CardScreen")) {
            CardScreen cardScreen = (CardScreen) mScreenManager.getCurrentScreen();
            cardScreen.toggleSound();
        } else if (mScreenManager.getCurrentScreen().getName().equals("MainMenuScreen")) {
            MainMenuScreen mainMenuScreen = (MainMenuScreen) mScreenManager.getCurrentScreen();
            mainMenuScreen.toggleSound();
        } else if (mScreenManager.getCurrentScreen().getName().equals("BananaGameScreen")) {
            BananaGameScreen bananaGameScreena = (BananaGameScreen) mScreenManager.getCurrentScreen();
            bananaGameScreena.toggleSound();
        }
    }
}