/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.arena;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.github.ucchyocean.sa.command.ShooterArenaCommand;
/**
 * @author ucchy
 * ラウンジの、対戦募集用サイン
 */
public class ArenaSign {

    private static final String[] MESSAGE_PREPARE = {
        ChatColor.GRAY + "" + ChatColor.ITALIC +
            "%s" + ChatColor.RESET, // %s = ゲームタイプ
        ChatColor.GRAY + "このアリーナは" + ChatColor.RESET,
        ChatColor.GRAY + "準備中です。" + ChatColor.RESET,
    };

    private static final String[] MESSAGE_MATCHING = {
        ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC +
            "%s" + ChatColor.RESET, // %s = ゲームタイプ
        ChatColor.DARK_PURPLE + "対戦メンバー募集中" + ChatColor.RESET,
        ChatColor.DARK_PURPLE + "<左クリックで参加>" + ChatColor.RESET,
    };

    private static final String[] MESSAGE_MATCHING_FULL = {
        ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC +
            "%s" + ChatColor.RESET, // %s = ゲームタイプ
        ChatColor.DARK_PURPLE + "対戦準備中" + ChatColor.RESET,
        ChatColor.RED + "<満員>" + ChatColor.RESET,
    };

    private static final String[] MESSAGE_IN_GAME = {
        ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC +
            "%s" + ChatColor.RESET, // %s = ゲームタイプ
        ChatColor.DARK_BLUE + "対戦中" + ChatColor.RESET,
        ChatColor.DARK_BLUE + "" + ChatColor.RESET,
    };

    private Arena parent;
    private Sign main;
    private Sign sub;

    /**
     * コンストラクタ
     * @param parent アリーナ
     * @param main メインのカンバン
     * @param sub サブのカンバン
     */
    public ArenaSign(Arena parent, Sign main, Sign sub) {
        this.parent = parent;
        this.main = main;
        this.sub = sub;
    }

    /**
     * 指定されたカンバンが、このオブジェクトのものかどうかを判断する
     * @param sign カンバン
     * @return このオブジェクトのカンバンかどうか
     */
    public boolean equalsSign(Sign sign) {
        return (main.equals(sign) || sub.equals(sign));
    }

    /**
     * ゲームマッチ準備中の内容に変更する
     */
    public void setPrepare() {

        String title = String.format("[%s]", parent.getName());
        String type = String.format(
                MESSAGE_PREPARE[0],
                parent.getMode().type.toJapanese());
        main.setLine(0, title);
        main.setLine(1, type);
        main.setLine(2, MESSAGE_PREPARE[1]);
        main.setLine(3, MESSAGE_PREPARE[2]);
        main.update();
    }

    /**
     * ゲームマッチ募集中の内容に変更する
     */
    public void setMatching() {

        String title = String.format("[%s]", parent.getName());
        String type = String.format(
                MESSAGE_MATCHING[0],
                parent.getMode().type.toJapanese());
        main.setLine(0, title);
        main.setLine(1, type);
        main.setLine(2, MESSAGE_MATCHING[1]);
        main.setLine(3, MESSAGE_MATCHING[2]);
        main.update();
    }

    /**
     * ゲームマッチ満員の内容に変更する
     */
    public void setMatchingFull() {

        String title = String.format("[%s]", parent.getName());
        String type = String.format(
                MESSAGE_MATCHING_FULL[0],
                parent.getMode().type.toJapanese());
        main.setLine(0, title);
        main.setLine(1, type);
        main.setLine(2, MESSAGE_MATCHING_FULL[1]);
        main.setLine(3, MESSAGE_MATCHING_FULL[2]);
        main.update();
    }

    /**
     * ゲームマッチゲーム中の内容に変更する
     */
    public void setInGame() {

        String title = String.format("[%s]", parent.getName());
        String type = String.format(
                MESSAGE_IN_GAME[0],
                parent.getMode().type.toJapanese());
        main.setLine(0, title);
        main.setLine(1, type);
        main.setLine(2, MESSAGE_IN_GAME[1]);
        main.setLine(3, MESSAGE_IN_GAME[2]);
        main.update();
    }

    /**
     * サブのカンバンの内容をクリアする
     */
    public void clearSub() {

        for ( int i=0; i<4; i++ )
            sub.setLine(i, "");
        sub.update();
    }

    /**
     * サブのカンバンの内容を設定する
     * @param names
     */
    public void setSub(ChatColor color, ArrayList<String> names) {

        for ( int i=0; i<4; i++ )
            sub.setLine(i, "");

        int index = 0;
        while ( index < 4 && index < names.size() ) {
            sub.setLine(index, color + names.get(index) + ChatColor.RESET);
            index++;
        }

        sub.update();
    }

    /**
     * カンバンをクリックされたときに呼び出されるイベント
     * @param player クリックしたプレイヤー
     */
    public void onHit(Player player) {
        // TODO:
    }

    public void remove() {

        for ( int i=0; i<4; i++ )
            main.setLine(i, "");
        main.update();

        for ( int i=0; i<4; i++ )
            sub.setLine(i, "");
        sub.update();
    }
}
