/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.game;

import java.util.ArrayList;

import com.github.ucchyocean.sa.arena.Arena;

/**
 * @author ucchy
 * SAGameSessionを管理するクラス
 */
public class SAGameSessionManager {

    private static ArrayList<SAGameSession> sessions;
    static {
        sessions = new ArrayList<SAGameSession>();
    }

    /**
     * 新しいゲームセッションを作成して返す
     * @return 新しいゲームセッション
     */
    public static SAGameSession createNewGameSession(Arena arena) {
        SAGameSession session = new SAGameSession();
        sessions.add(session);
        return session;
    }

    /**
     * ゲームセッションを削除する
     * @param session 削除するゲームセッション
     */
    public static void removeSession(SAGameSession session) {
        if ( sessions.contains(session) ) {
            sessions.remove(session);
        }
    }


}
