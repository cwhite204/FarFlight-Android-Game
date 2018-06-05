package uk.ac.qub.eeecs.game.collectMiniGame;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;

// Created by Matthew Downey
// Modified from Gage

public class Player extends Sprite {

    private Vector2 screenCentre = new Vector2();
    private Vector2 playerTouchAcceleration = new Vector2();

    public Player(float startX, float startY, GameScreen gameScreen) {
        super(startX, startY, 100f, 100f, gameScreen.getGame().getAssetManager().getBitmap("Player"), gameScreen);

        screenCentre.x = gameScreen.getGame().getScreenWidth() / 2;
        screenCentre.y = gameScreen.getGame().getScreenHeight() / 2;

        maxAcceleration = 1500.0f;
        maxVelocity = 1500.0f;
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        // Consider any touch events occurring since the update
        Input input = mGameScreen.getGame().getInput();

        if (input.existsTouch(0)) {
            // Get the primary touch event
            playerTouchAcceleration.x = (input.getTouchX(0) - screenCentre.x)
                    / screenCentre.x;
            playerTouchAcceleration.y = (screenCentre.y - input.getTouchY(0))
                    / screenCentre.y; // Invert the for y axis

            // Convert into an input acceleration
            acceleration.x = playerTouchAcceleration.x * maxAcceleration;
            acceleration.y = playerTouchAcceleration.y * maxAcceleration;
        }

        // Dampen the linear and angular acceleration and velocity
        angularAcceleration *= 0.95f;       // Before 0.95f
        angularVelocity *= 0.75f;           // Before 0.75f
        acceleration.multiply(0.75f);   // Before: 0.75f
        velocity.multiply(0.95f);       // Before: 0.95f

        // Apply the determined accelerations
        super.update(elapsedTime);
    }

}
