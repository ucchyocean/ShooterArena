/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.game;

import java.util.ArrayList;
import java.util.Hashtable;

public class GameScore {

    Hashtable<String, Integer> playerScores;
    int redTeamScore;
    int blueTeamScore;

    public GameScore(ArrayList<String> playerNames) {
        playerScores = new Hashtable<String, Integer>();
        for ( String name : playerNames ) {
            playerScores.put(name, 0);
        }
    }

    public void incrementPlayerScore(String name) {
        if ( playerScores.contains(name) ) {
            playerScores.put(name, playerScores.get(name) + 1);
        } else {
            playerScores.put(name, 1);
        }
    }

    public void incrementRedScore() {
        redTeamScore++;
    }

    public void incrementBlueScore() {
        blueTeamScore++;
    }
}
