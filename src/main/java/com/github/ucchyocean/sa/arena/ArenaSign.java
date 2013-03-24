/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.arena;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

import com.github.ucchyocean.sa.game.SAGameSession;
/**
 * @author ucchy
 * ラウンジの、対戦募集用サイン
 */
public class ArenaSign {

    private static final String ARENA_TITLE = "[%s - %s]";

    private static final String[] MESSAGE_PREPARE = {
        ChatColor.GRAY + "このアリーナは" + ChatColor.RESET,
        ChatColor.GRAY + "準備中です。" + ChatColor.RESET,
        ""
    };

    private static final String[] MESSAGE_MATCHING = {
        ChatColor.DARK_PURPLE + "%s" + ChatColor.RESET, // %s = ゲームタイプ
        ChatColor.DARK_PURPLE + "<左クリックで参加>" + ChatColor.RESET,
        "[メンバー]"
    };

    private static final String[] MESSAGE_MATCHING_FULL = {
        ChatColor.DARK_PURPLE + "%s" + ChatColor.RESET, // %s = ゲームタイプ
        ChatColor.RED + "<満員>" + ChatColor.RESET,
        "[メンバー]"
    };

    private static final String[] MESSAGE_IN_GAME = {
        ChatColor.AQUA + "%s" + ChatColor.RESET, // %s = ゲームタイプ
        ChatColor.AQUA + "対戦中" + ChatColor.RESET,
        "[メンバー]"
    };

    private SAGameSession controller;
    private Sign main;
    private Sign sub;

    public ArenaSign(Sign main, Sign sub) {
        this.main = main;
        this.sub = sub;
    }

    public boolean equalsSign(Sign sign) {
        return (main.equals(sign) || sub.equals(sign));
    }
}
