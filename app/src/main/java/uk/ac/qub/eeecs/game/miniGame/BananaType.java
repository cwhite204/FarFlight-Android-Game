package uk.ac.qub.eeecs.game.miniGame;

/**
 * Enum to handle different types of banana.
 *
 * @author Andrew Bell
 */

public enum BananaType {
    GOOD, SUPER, BAD;

    BananaType() {
    }

    public BananaType pickBananaType(int randomInt) {
        if (randomInt == 0) {
            return SUPER;
        } else if (randomInt > 0 && randomInt < 4) {
            return BAD;
        } else return GOOD;
    }
}
