/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.game;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.ucchyocean.sa.ShooterArena;

/**
 * @author ucchy
 *
 */
public class GameTimer extends BukkitRunnable {

    public static final String PRENOTICE = ShooterArena.PRENOTICE;

    private GameSession parent;
    private int secondsReadyLeast;
    private int secondsGameLeast;

    public GameTimer(GameSession session, int secondsReady, int secondsGame) {
        parent = session;
        secondsReadyLeast = secondsReady;
        secondsGameLeast = secondsGame;
    }

    /**
     * 1秒ごとに呼び出しされるメソッド
     * @see java.lang.Runnable#run()
     */
    public void run() {

        if ( secondsReadyLeast > 0 ) {
            secondsReadyLeast--;

            if ( secondsReadyLeast > 0 && secondsReadyLeast <= 5 ) {
                parent.annouceToAll(PRENOTICE + "ゲーム開始まで " + secondsReadyLeast + " 秒");
            } else if ( secondsReadyLeast == 0 ) {
                parent.annouceToAll(PRENOTICE + "ゲーム開始！");
                // フリーズの解除
                parent.setFreeze(false);
                // TODO カタパルトの実行
            }

        } else if ( secondsReadyLeast <= 0 && secondsGameLeast > 0 ) {
            secondsGameLeast--;

            if ( secondsGameLeast == 300 ) {
                parent.annouceToAll(PRENOTICE + "残り 5分です");
            } else if ( secondsGameLeast == 180 ) {
                parent.annouceToAll(PRENOTICE + "残り 3分です");
            } else if ( secondsGameLeast == 60 ) {
                parent.annouceToAll(PRENOTICE + "残り 1分です");
            } else if ( secondsGameLeast > 0 && secondsGameLeast <= 10 ) {
                parent.annouceToAll(PRENOTICE + "残り " + secondsGameLeast + "秒です");
            } else if ( secondsGameLeast == 0 ) {
                parent.annouceToAll(PRENOTICE + "ゲーム終了！");
                parent.endGame();
            }

        } else if ( secondsReadyLeast <= 0 && secondsGameLeast <= 0 ) {
            this.cancel();
        }
    }

}
