package com.github.ucchyocean.sa.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.github.ucchyocean.sa.SAConfig;
import com.github.ucchyocean.sa.arena.ArenaManager;
import com.github.ucchyocean.sa.game.GamePhase;
import com.github.ucchyocean.sa.game.GameSession;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        // ゲーム中のプレイヤーなら、装備と経験値を与え、
        // チームリスポーンポイントへ送り、カタパルトする
        Player player = event.getPlayer();

        GameSession session = ArenaManager.getSessionByPlayer(player);
        if ( session == null || session.phase != GamePhase.IN_GAME) {
            Location lounge = ArenaManager.getLoungeRespawn();
            event.setRespawnLocation(lounge);
            return;
        }

        Location location = session.getRespawnPointByPlayer(player);
        if ( location == null ) {
            Location lounge = ArenaManager.getLoungeRespawn();
            event.setRespawnLocation(lounge);
            return;
        }

        session.setPlayerKits(player);
        event.setRespawnLocation(location);
        player.setVelocity(location.getDirection().multiply(SAConfig.catapultPower));
    }
}
