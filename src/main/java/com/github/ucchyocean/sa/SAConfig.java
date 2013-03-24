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

    /** リスポーンしたときにプレイヤーを発射するかどうか */
    public static boolean catapultOnRespawn;

    /** 発射する場合、その威力 */
    public static int catapultPower;

    /**
     * config.ymlの読み出し処理。
     * @throws IOException
     * @return 成功したかどうか
     */
    public static void reloadConfig() {

        File configFile = new File(ShooterArena.instance.getDataFolder(), "config.yml");
        if ( !configFile.exists() ) {
            Utility.copyFileFromJar(ShooterArena.getPluginJarFile(), configFile, "config.yml", false);
        }

        ShooterArena.instance.reloadConfig();
        FileConfiguration config = ShooterArena.instance.getConfig();

        defaultWorldName = config.getString("defaultWorldName", "world");
        catapultOnRespawn = config.getBoolean("catapultOnRespawn", true);
        catapultPower = config.getInt("catapultPower", 5);
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
