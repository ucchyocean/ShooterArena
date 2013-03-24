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
    public GameType type;

}
