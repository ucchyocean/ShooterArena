/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.arena;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.github.ucchyocean.sa.Utility;
import com.github.ucchyocean.sa.game.MatchMode;

/**
 * @author ucchy
 * アリーナクラス
 */
public class Arena {

    private ArenaSign sign;
    private String name;
    private MatchMode mode;

    private Location redTeamRespawn;
    private Vector redTeamCatapultVector;
    private Location blueTeamRespawn;
    private Vector blueTeamCatapultVector;

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
    protected void setSign(ArenaSign sign) {
        this.sign = sign;
    }

    /**
     * アリーナサインを取得する
     * @return アリーナサイン
     */
    protected ArenaSign getSign() {
        return sign;
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
        refreshSign();
    }

    /**
     * アリーナサインを更新します。
     */
    public void refreshSign() {
        if ( sign != null ) {
            sign.refresh();
        }
    }

    /**
     * 赤チームのリスポーンポイントを設定します。
     * @param location
     */
    protected void setRedRespawn(Location location) {
        this.redTeamRespawn = Utility.toCenterOfBlock(location);
    }

    /**
     * 赤チームのカタパルト打ち出し用ベクトルを設定します。
     * @param vector
     */
    protected void setRedVector(Vector vector) {
        this.redTeamCatapultVector = vector;
    }

    /**
     * 赤チームのリスポーンポイントを取得します。
     * @return
     */
    public Location getRedRespawn() {
        return redTeamRespawn;
    }

    /**
     * 赤チームのカタパルト打ち出し用ベクトルを取得します。
     * @return
     */
    public Vector getRedVector() {
        return redTeamCatapultVector;
    }

    /**
     * 青チームのリスポーンポイントを設定します。
     * @param location
     */
    protected void setBlueRespawn(Location location) {
        this.blueTeamRespawn = Utility.toCenterOfBlock(location);
    }

    /**
     * 青チームのカタパルト打ち出し用ベクトルを設定します。
     * @param vector
     */
    protected void setBlueVector(Vector vector) {
        this.blueTeamCatapultVector = vector;
    }

    /**
     * 青チームのリスポーンポイントを取得します。
     * @return
     */
    public Location getBlueRespawn() {
        return blueTeamRespawn;
    }

    /**
     * 青チームのカタパルト打ち出し用ベクトルを取得します。
     * @return
     */
    public Vector getBlueVector() {
        return blueTeamCatapultVector;
    }
}
