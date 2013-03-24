/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.arena;

/**
 * @author ucchy
 * アリーナクラス
 */
public class Arena {

    private ArenaSign sign;

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
}
