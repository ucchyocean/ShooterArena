/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.game;

/**
 * @author ucchy
 * ゲームタイプ
 */
public enum GameType {

    /** 練習モード 1人用 */
    PRACTICE("practice"),

    /** n回死んだら負けのルール、個人戦 */
    DUEL_DEATH_MATCH("duel_death_match"),

    /** リスポ無制限、時間制限ありの、個人戦 */
    DUEL_TIME_MATCH("duel_time_match"),

    /** n回死んだら負けのルール、チーム戦 */
    TEAM_DEATH_MATCH("team_death_match"),

    /** リスポ無制限、時間制限ありの、個人戦 */
    TEAM_TIME_MATCH("team_time_match");

    private String id;

    GameType(String id) {
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
     * 文字列表記から、GameTypeを返す
     * @param id 文字列表記
     * @return GameType
     */
    public static GameType fromString(String id) {
        GameType[] values = values();
        for ( GameType value : values ) {
            if ( value.toString().equalsIgnoreCase(id) ) {
                return value;
            }
        }
        return null;
    }

    /**
     * 日本語表記を返す
     * @return 日本語表記
     */
    public String toJapanese() {
        switch (this) {
        case DUEL_DEATH_MATCH:
            return "デュエルデスマッチ";
        case DUEL_TIME_MATCH:
            return "デュエルタイムマッチ";
        case TEAM_DEATH_MATCH:
            return "チームデスマッチ";
        case TEAM_TIME_MATCH:
            return "チームタイムマッチ";
        case PRACTICE:
            return "プラクティス";
        default:
            return "不明";
        }
    }

    /**
     * ゲームタイプごとの参加可能人数
     * @return 参加可能人数
     */
    public int getMaxPlayerNum() {
        switch (this) {
        case DUEL_DEATH_MATCH:
        case DUEL_TIME_MATCH:
            return 2;
        case TEAM_DEATH_MATCH:
        case TEAM_TIME_MATCH:
            return 4;
        case PRACTICE:
            return 1;
        default:
            return 2;
        }
    }
}
