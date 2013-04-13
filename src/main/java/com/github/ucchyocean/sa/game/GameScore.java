/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.game;

import java.util.Hashtable;

public class GameScore {

    public Hashtable<String, int[]> playerKillDeath;
    public int redTeamScore;
    public int blueTeamScore;
    private int life;

    public GameScore(int life) {
        this.life = life;
        playerKillDeath = new Hashtable<String, int[]>();
    }

    public void addPlayerScore(String name, int kill, int death) {
        if ( playerKillDeath.contains(name) ) {
            int[] data = playerKillDeath.get(name);
            data[0] += kill;
            data[1] += death;
            playerKillDeath.put(name, data);
        } else {
            int[] data = new int[2];
            data[0] = kill;
            data[1] = death;
            playerKillDeath.put(name, data);
        }
    }

    public int getPlayerLife(String name) {
        if ( playerKillDeath.contains(name) ) {
            return life - playerKillDeath.get(name)[1];
        } else {
            return life;
        }
    }
}
