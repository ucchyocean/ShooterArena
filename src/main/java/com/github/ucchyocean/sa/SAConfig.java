/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author ucchy
 * Shooter Arena の設定ハンドラー
 */
public class SAConfig {

    /** デフォルトで使用するワールド */
    public static String defaultWorldName;

    /** Shooterのパワーレベル */
    public static int shooterPower;

    /** Shooterを一回打つのに必要なexpコスト */
    public static int shooterCost;

    /** 燃料切れになったときに、一定時間経過後に燃料が再提供されるかどうか */
    public static boolean reviveExp;

    /** 燃料切れになってから何秒で再燃料が提供されるか */
    public static int reviveExpSeconds;

    /** 燃料切れになってから再提供される量 */
    public static int reviveExpAmount;

    /** リスポーンしたときにプレイヤーを発射するかどうか */
    public static boolean catapultOnRespawn;

    /** 発射する場合、その威力 */
    public static int catapultPower;

    /** ゲーム開始時に配布するexp */
    public static int kitsExp;

    /** ゲーム開始時に配布するキット */
    public static String kitsItems;

    /** ゲーム開始時に配布する防具 */
    public static String kitsArmors;

    /**
     * config.ymlの読み出し処理。
     * @throws IOException
     * @return 成功したかどうか
     */
    public static void reloadConfig() {

        File configFile = new File(ShooterArena.instance.getDataFolder(), "config.yml");
        if ( !configFile.exists() ) {
            Utility.copyFileFromJar(ShooterArena.getPluginJarFile(), configFile, "config_ja.yml", false);
        }

        ShooterArena.instance.reloadConfig();
        FileConfiguration config = ShooterArena.instance.getConfig();

        defaultWorldName = config.getString("defaultWorldName", "world");
        shooterPower = config.getInt("shooterPower", 5);
        shooterCost = config.getInt("shooterCost", 10);
        reviveExp = config.getBoolean("reviveExp", true);
        reviveExpSeconds = config.getInt("reviveExpSeconds", 15);
        reviveExpAmount = config.getInt("reviveExpAmount", 100);
        catapultOnRespawn = config.getBoolean("catapultOnRespawn", true);
        catapultPower = config.getInt("catapultPower", 5);
        kitsExp = config.getInt("kits.exp", 1500);
        kitsItems = config.getString("kits.items", "");
        kitsArmors = config.getString("kits.armors", "");
    }

    /**
     * config.yml に、設定値を保存する
     * @param key 設定値のキー
     * @param value 設定値の値
     */
    public static void setConfigValue(String key, Object value) {

        FileConfiguration config = ShooterArena.instance.getConfig();
        config.set(key, value);
        ShooterArena.instance.saveConfig();
    }
}
