/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.listener;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.ucchyocean.sa.arena.ArenaManager;
import com.github.ucchyocean.sa.arena.ArenaSign;

/**
 * @author ucchy
 * プレイヤーのラウンジのカンバンのクリックをハンドリングするためのクラス
 */
public class LoungeSignListener implements Listener {

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
        if ( target == null || target instanceof Sign ) {
            return;
        }

        Sign s = (Sign)target;

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
}
