/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.listener;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import com.github.ucchyocean.sa.ChatMode;
import com.github.ucchyocean.sa.SAConfig;
import com.github.ucchyocean.sa.ShooterArena;
import com.github.ucchyocean.sa.Utility;
import com.github.ucchyocean.sa.arena.ArenaManager;
import com.github.ucchyocean.sa.game.GamePhase;
import com.github.ucchyocean.sa.game.GameScore;
import com.github.ucchyocean.sa.game.GameSession;

/**
 * @author ucchy
 * プレイヤーの行動に関するリスナー
 */
public class PlayerListener implements Listener {

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

        GameSession session = ArenaManager.getSessionByPlayer(player);

        // ゲーム中のプレイヤーでなければ、何もせずに抜ける
        if ( session == null ) {
            return;
        }

        // チームメンバに送信する
        session.sendChat(player, event.getMessage());

        // 元のイベントをキャンセル
        event.setCancelled(true);
    }

    /**
     * ダメージが発生したときのイベント
     * @param event
     */
    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        // プレイヤーのイベントでなければ無視
        if ( !(event.getEntity() instanceof Player) ) {
            return;
        }

        // 落下ダメージは全てキャンセルする
        if ( event.getCause() != null && event.getCause() == DamageCause.FALL ) {
            event.setCancelled(true);
        }
    }

    /**
     * Playerが死亡したときに発生するイベント
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        String name = player.getName();
        GameSession session = ArenaManager.getSessionByPlayer(player);

        // 死亡メッセージを非表示にする
        event.setDeathMessage("");

        // ゲーム中プレイヤーじゃなければ、関係ないので抜ける
        if ( session == null ) {
            return;
        }

        // ドロップアイテムを消去する
        event.getDrops().clear();

        // 復帰後の経験値をここで設定しておく
        event.setNewExp(SAConfig.kitsExp);

        // スコア取得、デス数加算
        GameScore score = session.getScore();
        score.addPlayerScore(name, 0, 1);

        // 倒したプレイヤーを取得
        // 直接攻撃で倒された場合は、killerをそのまま使う
        // 間接攻撃で倒された場合は、shooterを取得して使う
        Player killer = player.getKiller();

        EntityDamageEvent cause = event.getEntity().getLastDamageCause();
        if ( cause != null && cause instanceof EntityDamageByEntityEvent ) {
            Entity damager = ((EntityDamageByEntityEvent)cause).getDamager();
            if ( damager instanceof Projectile ) {
                LivingEntity shooter = ((Projectile) damager).getShooter();
                if ( shooter instanceof Player ) {
                    killer = (Player)shooter;
                }
            }
        }

        if ( killer != null ) {

            score.addPlayerScore(killer.getName(), 1, 0);

            // チーム得点の加算
            String team = session.getPlayerTeam(killer.getName());
            if ( team.equals("red") )
                score.redTeamScore++;
            else if ( team.equals("blue") )
                score.blueTeamScore++;

            // 死亡メッセージを流す
            session.annouceToAll(String.format(Utility.replaceColorCode(
                    "&b%s&eは&b%s&eに倒されました。RED:&b%d&e, BLUE:&b%d&e"),
                    player.getName(), killer.getName(),
                    score.redTeamScore, score.blueTeamScore));

        } else {

            // 自爆の場合は、チーム得点を減点
            String team = session.getPlayerTeam(name);
            if ( team.equals("red") )
                score.redTeamScore--;
            else if ( team.equals("blue") )
                score.blueTeamScore--;

            // 自爆メッセージを流す
            session.annouceToAll(String.format(Utility.replaceColorCode(
                    "&b%s&eは自爆しました。RED:&b%d&e, BLUE:&b%d&e"),
                    player.getName(),
                    score.redTeamScore, score.blueTeamScore));
        }

        // ライフが無くなったかどうかを確認する
        if ( score.getPlayerLife(name) <= 0 ) {
            session.deadPlayer(name);
        }
    }

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

    /**
     * プレイヤーの移動を検知して、フリーズが指定されているプレイヤーなら移動をキャンセルするメソッド
     * @param event
     */
    @EventHandler
    public void onPlayerMode(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if ( ShooterArena.freezePlayers.contains(player.getName()) ) {

            if ( event.getFrom().getX() != event.getTo().getX() ||
                    event.getFrom().getY() != event.getTo().getY() ||
                    event.getFrom().getZ() != event.getTo().getZ() ) {
                event.setCancelled(true);
                player.setVelocity(new Vector(0, 0, 0));
            }
        }
    }

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
