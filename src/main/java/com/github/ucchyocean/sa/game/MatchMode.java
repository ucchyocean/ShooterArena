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

    /**
     * @author ucchy
     * ゲームタイプ
     */
    public enum Type {
        /** n回死んだら負けのルール、個人戦 */
        DEATH_MATCH,
        /** n回死んだら負けのルール、チーム戦 */
        TEAM_DEATH_MATCH,
        /** リスポ無制限、時間制限ありの、個人戦 */
        TIME_MATCH,
        /** リスポ無制限、時間制限ありの、個人戦 */
        TEAM_TIME_MATCH;
    }

    /**
     * 制限時間（分）、無制限の場合は -1 であることに注意
     */
    public int minute;

    /**
     * 規定のライフ数。無制限の場合は -1 であることに注意
     */
    public int life;

    /**
     * ゲームタイプ
     */
    public Type type;

}
