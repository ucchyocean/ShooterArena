/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.game;

/**
 * @author ucchy
 * 対戦ゲームのモードを示すクラス
 */
public class MatchMode {

    /** 制限時間（分）、無制限の場合は -1 であることに注意 */
    public int minute;

    /** 規定のライフ数。無制限の場合は -1 であることに注意 */
    public int life;

    /** ゲームタイプ */
    public GameType type;

    /**
     * コンストラクタ
     * @param type ゲームタイプ
     * @param amount 数値　デスマッチの場合はライフ数、タイムマッチの場合は制限時間（分）になる
     */
    public MatchMode(GameType type, int amount) {

        if ( type == GameType.DUEL_DEATH_MATCH || type == GameType.TEAM_DEATH_MATCH ) {
            life = amount;
            minute = -1;
        } else {
            life = -1;
            minute = amount;
        }
        this.type = type;
    }

    /**
     * このオフジェクトの日本語表現
     * @return 日本語表現
     */
    public String toJapanese() {
        if ( type == GameType.DUEL_DEATH_MATCH ||
                type == GameType.TEAM_DEATH_MATCH ) {
            return type.toJapanese() + " " + life + "ライフ制";
        } else {
            return type.toJapanese() + " " + minute + "分制限";
        }
    }
}
