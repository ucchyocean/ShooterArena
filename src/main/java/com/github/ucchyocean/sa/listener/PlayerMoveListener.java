/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.ucchyocean.sa.ShooterArena;

/**
 * @author ucchy
 * プレイヤーの移動を検知するクラス
 */
public class PlayerMoveListener implements Listener {

    /**
     * プレイヤーの移動を検知して、フリーズが指定されているプレイヤーなら移動をキャンセルするメソッド
     * @param event
     */
    @EventHandler
    public void onPlayerMode(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if ( ShooterArena.freezePlayers.contains(player.getName()) ) {
            event.setCancelled(true);
        }
    }
}
