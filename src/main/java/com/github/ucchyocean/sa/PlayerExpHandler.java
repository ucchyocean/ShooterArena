/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa;

import org.bukkit.entity.Player;

/**
 * @author ucchy
 * プレイヤーの経験値を取得したり設定したりするクラス
 */
public class PlayerExpHandler {

    /**
     * プレイヤーの持っている、現在のレベルでの経験値量を取得する。
     * レベルアップ直後では 0 であり、次のレベルまでに必要な経験値-1 が最大値である。
     * @param player プレイヤー
     * @return 経験値
     */
    public static int getExpAtLevel(final Player player) {
        return getExpAtLevel(player.getLevel());
    }

    /**
     * 指定したレベルでの必要経験値を返す。
     * @param level レベル
     * @return 経験値
     */
    public static int getExpAtLevel(final int level) {
        if (level > 29) {
            return 62 + (level - 30) * 7;
        }
        if (level > 15) {
            return 17 + (level - 15) * 3;
        }
        return 17;
    }

    /**
     * プレイヤーの保持している経験値総量を取得する。
     * @param player プレイヤー
     * @return 経験値
     */
    public static int getTotalExperience(final Player player) {
        int exp = (int)Math.round(getExpAtLevel(player) * player.getExp());
        int currentLevel = player.getLevel();

        while (currentLevel > 0) {
            currentLevel--;
            exp += getExpAtLevel(currentLevel);
        }
        return exp;
    }

    /**
     * プレイヤーから、指定した経験値量を減らす。
     * @param player プレイヤー
     * @param amount 減らす量
     */
    public static void takeExperience(final Player player, int amount) {
        int remain = amount;
        int exp = (int)Math.round(getExpAtLevel(player) * player.getExp());
        if ( exp >= remain ) {
            player.giveExp(-remain);
            return;
        } else {
            player.giveExp(-exp);
            remain -= exp;
        }
        while ( remain > 0 && player.getLevel() > 0 ) {
            player.setLevel(player.getLevel()-1);
            player.setExp(1.0f);
            exp = getExpAtLevel(player);
            if ( exp >= remain ) {
                player.giveExp(-remain);
                return;
            } else {
                player.giveExp(-exp);
                remain -= exp;
            }
        }
        return;
    }

    /**
     * プレイヤーが指定した量の経験値を持っているかどうか判定する。
     * @param player プレイヤー
     * @param amount 判定する量
     * @return もっているかどうか
     */
    public static boolean hasExperience(final Player player, int amount) {
        int exp = getTotalExperience(player);
        return (exp >= amount);
    }

    /**
     * プレイヤーの経験値量を、指定値に設定する。
     * @param player プレイヤー
     * @param amount 経験値の量
     */
    public static void setExperience(final Player player, int amount) {
        player.setLevel(0);
        player.setExp(0);
        int remain = amount;
        while ( remain > 0 ) {
            int exp = getExpAtLevel(player);
            if ( exp <= remain ) {
                player.setLevel(player.getLevel()+1);
            } else {
                player.giveExp(exp);
            }
            remain -= exp;
        }
    }
}
