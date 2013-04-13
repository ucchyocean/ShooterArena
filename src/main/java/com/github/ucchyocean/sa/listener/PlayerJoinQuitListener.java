/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author ucchy
 * プレイヤーのサーバー参加、離脱を監視するクラス
 */
public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        // TODO: 復帰先のゲームセッションが存在するのかどうかを確認する

        // TODO: ポイントランキングを表示する

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        // TODO: ゲームセッションから離脱すべきなのかどうかを検討する

        // TODO: 観客なら、ログアウト時に離脱する。

    }
}
