/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.listener;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.ucchyocean.sa.Utility;
import com.github.ucchyocean.sa.arena.Arena;
import com.github.ucchyocean.sa.arena.ArenaManager;
import com.github.ucchyocean.sa.arena.ArenaSign;
import com.github.ucchyocean.sa.command.ShooterArenaCommand;

/**
 * @author ucchy
 * プレイヤーのラウンジのカンバンのクリックをハンドリングするためのクラス
 */
public class LoungeSignListener implements Listener {

    protected String PREERR = ChatColor.RED + "[SA]";
    protected String PREINFO = ChatColor.AQUA + "[SA]";
    protected String PRENOTICE = ChatColor.LIGHT_PURPLE + "[SA]";

    /**
     * プレイヤーがクリックしたイベント
     * @param event イベント
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        // 左クリックイベントでなければ対象外
        if ( event.getAction() != Action.LEFT_CLICK_AIR &&
                event.getAction() != Action.LEFT_CLICK_BLOCK ) {
            return;
        }

        Block target = player.getTargetBlock(null, 100);

        // クリック対象がカンバンでない場合は対象外
        if ( !Utility.isSign(target) ) {
            return;
        }

        Sign s = (Sign)target.getState();
        String arenaName = ShooterArenaCommand.getPlayerCommandCache(player.getName());

        if ( arenaName != null ) {
            // sa sign の実行者の処理

            // 登録済みでないか確認する
            boolean isAlreadyRegistered = false;
            ArrayList<ArenaSign> signs = ArenaManager.getAllArenaSign();
            for ( ArenaSign sign : signs ) {
                if ( sign.equalsSign(s) ) {
                    isAlreadyRegistered = true;
                    break;
                }
            }
            if ( isAlreadyRegistered ) {
                player.sendMessage(PREERR + "この看板は既に登録済みです。");
                return;
            }

            if ( !isEmptySign(s) ) {
                player.sendMessage(PREERR + "この看板は何か書かれているため、登録できません。");
                return;
            }

            // アリーナ取得
            Arena arena = ArenaManager.getArena(arenaName);

            // キャッシュ削除
            ShooterArenaCommand.removePlayerCommandCache(player.getName());

            // 看板登録
            ArenaManager.registerArenaSign(arena, s);

            // アリーナサインの更新
            arena.refreshSign();

            player.sendMessage(PREINFO + "アリーナ看板を作成しました。");

            event.setCancelled(true);
            return;
        }

        // クリックされた ArenaSign を探して、見つかったら onHit を実行する
        ArrayList<ArenaSign> signs = ArenaManager.getAllArenaSign();
        for ( ArenaSign sign : signs ) {
            if ( sign.equalsSign(s) ) {
                sign.onHit(player);
                event.setCancelled(true);
                return;
            }
        }
    }

    /**
     * 何も書かれていない看板かどうかを返す
     * @param sign 看板
     * @return 何も書かれていないかどうか
     */
    private boolean isEmptySign(Sign sign) {
        for ( int i=0; i<4; i++ ) {
            if ( ChatColor.stripColor(sign.getLine(i)).length() >= 1 ) {
                return false;
            }
        }
        return true;
    }
}
