/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.game;

import com.github.ucchyocean.sa.ShooterArena;

/**
 * @author ucchy
 * ゲームタイマー
 */
public class GameTimer {

    private GameSession parent;
    private int secondsReadyLeast;
    private int secondsGameLeast;

    /**
     * コンストラクタ
     * @param session 親となるゲームセッション
     * @param secondsReady
     * @param secondsGame
     */
    public GameTimer(GameSession session, int secondsReady, int secondsGame) {
        parent = session;
        secondsReadyLeast = secondsReady;
        secondsGameLeast = secondsGame;
    }

    /**
     * 1秒ごとに呼び出しされるメソッド
     */
    public void onTick() {

        if ( secondsReadyLeast > 0 ) {
            secondsReadyLeast--;

            if ( secondsReadyLeast > 0 && secondsReadyLeast <= 5 ) {
                parent.annouceToAll("ゲーム開始まで " + secondsReadyLeast + " 秒");
            } else if ( secondsReadyLeast == 0 ) {
                parent.annouceToAll("ゲーム開始！");
                // フリーズの解除
                parent.setFreeze(false);
                // カタパルトの実行
                parent.launchAllPlayer();
            }

        } else if ( secondsReadyLeast <= 0 && secondsGameLeast > 0 ) {
            secondsGameLeast--;

            if ( secondsGameLeast == 300 ) {
                parent.annouceToAll("残り 5分です");
            } else if ( secondsGameLeast == 180 ) {
                parent.annouceToAll("残り 3分です");
            } else if ( secondsGameLeast == 60 ) {
                parent.annouceToAll("残り 1分です");
            } else if ( secondsGameLeast > 0 && secondsGameLeast <= 10 ) {
                parent.annouceToAll("残り " + secondsGameLeast + "秒です");
            } else if ( secondsGameLeast == 0 ) {
                parent.annouceToAll("ゲーム終了！");
                parent.endGame();
            }

        } else if ( secondsReadyLeast <= 0 && secondsGameLeast <= 0 ) {

            ShooterArena.cancelGameTimer(this);
        }
    }

}
