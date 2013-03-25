/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.arena;

import com.github.ucchyocean.sa.game.MatchMode;
import com.github.ucchyocean.sa.game.SAGameSession;

/**
 * @author ucchy
 * アリーナクラス
 */
public class Arena {

    private ArenaSign sign;
    private String name;
    private MatchMode mode;
    private SAGameSession session;

    /**
     * コンストラクタ
     * @param name アリーナの名前
     */
    public Arena(String name) {
        this.name = name;
    }

    /**
     * アリーナ名を返す
     * @return アリーナ名
     */
    public String getName() {
        return name;
    }

    /**
     * アリーナサインを設定する
     * @param sign アリーナサイン
     */
    public void setSign(ArenaSign sign) {
        this.sign = sign;
    }

    /**
     * アリーナサインを取得する
     * @return アリーナサイン
     */
    public ArenaSign getSign() {
        return sign;
    }

    /**
     * ゲームセッションを設定します。null指定で消去します。
     * @param session ゲームセッション
     */
    public void setSession(SAGameSession session) {
        this.session = session;
    }

    /**
     * 現在設定されているゲームセッションを返します。
     * @return 現在設定されているゲームセッション
     */
    public SAGameSession getSession() {
        return session;
    }

    /**
     * マッチモードをアリーナに設定します。
     * @param mode ゲームモード
     */
    public void setMode(MatchMode mode) {
        this.mode = mode;
    }

    /**
     * 現在設定されているマッチモードを返します。
     */
    public MatchMode getMode() {
        return mode;
    }

    /**
     * アリーナを準備中に変更します。
     */
    public void setDisable() {
        setMode(null);
        if ( sign != null ) {
            sign.setPrepare();
        }
    }
}
