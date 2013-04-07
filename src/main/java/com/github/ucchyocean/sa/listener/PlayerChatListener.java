/*
 * Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Team;

import com.github.ucchyocean.sa.ChatMode;
import com.github.ucchyocean.sa.SAConfig;
import com.github.ucchyocean.sa.ShooterArena;
import com.github.ucchyocean.sa.arena.ArenaManager;
import com.github.ucchyocean.sa.game.SAGameSession;

/**
 * @author ucchy
 * チャットが発生したときに、チームチャットへ転送するためのリスナークラス
 */
public class PlayerChatListener implements Listener {

    private static final String GLOBAL_CHAT_MARKER = "#GLOBAL#";

    /**
     * Playerがチャットを送信したときに発生するイベント
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        // GLOBALマーカーが付いていたら、/g コマンドを経由してきたので、
        // GLOBALマーカーを取り除いてから抜ける。
        if ( event.getMessage().startsWith(GLOBAL_CHAT_MARKER) ) {
            String newMessage = event.getMessage().substring(GLOBAL_CHAT_MARKER.length());
            event.setMessage(newMessage);
            return;
        }

        // チームチャット無効なら、何もせずに抜ける
        if ( SAConfig.chatmode == ChatMode.GLOBAL ) {
            return;
        }

        Player player = event.getPlayer();
        Team team = ShooterArena.thandler.getTeamByPlayer(player);

        // 所属チームが無いなら、何もせずに抜ける
        if ( team == null ) {
            return;
        }

        SAGameSession session = ArenaManager.getSessionByPlayer(player);

        // ゲーム中のプレイヤーでなければ、何もせずに抜ける
        if ( session == null ) {
            return;
        }

        // チームメンバに送信する
        session.sendChat(player, event.getMessage());

        // 元のイベントをキャンセル
        event.setCancelled(true);
    }
}
