/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.arena;

import java.util.Hashtable;

import com.github.ucchyocean.sa.ShooterArena;

/**
 * @author ucchy
 * アリーナ管理クラス
 */
public class ArenaManager {

    private static Hashtable<String, Arena> arenas;
    static {
        arenas = new Hashtable<String, Arena>();
    }

    public void registerNewArena(String name) {

    }

    public boolean isValidNewArenaName(String name) {
        return !arenas.containsKey(name) && ShooterArena.wghandler.
    }

}
