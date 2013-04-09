package com.github.ucchyocean.sa.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.github.ucchyocean.sa.arena.ArenaManager;
import com.github.ucchyocean.sa.game.GamePhase;
import com.github.ucchyocean.sa.game.GameSession;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        // ゲーム中のプレイヤーなら、チームリスポーンポイントへ送り、カタパルトする
        Player player = event.getPlayer();

        GameSession session = ArenaManager.getSessionByPlayer(player);
        if ( session == null || session.phase != GamePhase.IN_GAME) {
            return;
        }

        Location location = session.getRespawnPointByPlayer(player);
        if ( location == null ) {
            return;
        }

        event.setRespawnLocation(location);
        player.setVelocity(location.getDirection()); // TODO:
    }
}
