/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.game;

/**
 * @author ucchy
 * ゲームフェーズ
 */
public enum GamePhase {

    /** 準備中 */
    PREPARE("prepare"),

    /** 対戦メンバー募集中 */
    MATCH_MAKING("match_making"),

    /** 対戦中 */
    IN_GAME("in_game"),

    /** キャンセル */
    CANCELED("canceled"),

    /** 終了 */
    END("end");

    private String id;

    /**
     * コンストラクタ
     * @param id
     */
    GamePhase(String id) {
        this.id = id;
    }

    /**
     * 英語文字列表記を返す
     * @see java.lang.Enum#toString()
     */
    public String toString() {
        return id;
    }

    /**
     * 日本語表記を返す
     * @return 日本語表記
     */
    public String toJapanese() {
        switch (this) {
        case PREPARE:
            return "準備中";
        case MATCH_MAKING:
            return "参加者募集中";
        case IN_GAME:
            return "対戦中";
        case CANCELED:
            return "キャンセル";
        case END:
            return "終了";
        default:
            return "不明";
        }
    }
}
