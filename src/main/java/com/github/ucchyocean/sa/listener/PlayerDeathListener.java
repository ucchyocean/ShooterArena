/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.github.ucchyocean.sa.arena.ArenaManager;
import com.github.ucchyocean.sa.game.GameScore;
import com.github.ucchyocean.sa.game.GameSession;

/**
 * @author ucchy
 * プレイヤーが死亡したときに、通知を受け取って処理するクラス
 */
public class PlayerDeathListener implements Listener {

    /**
     * Playerが死亡したときに発生するイベント
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        String name = player.getName();
        GameSession session = ArenaManager.getSessionByPlayer(player);

        // ゲーム中プレイヤーじゃなければ、関係ないので抜ける
        if ( session == null ) {
            return;
        }

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
            String team = session.getPlayerTeam(name);
            if ( team.equals("red") )
                score.redTeamScore++;
            else if ( team.equals("blue") )
                score.blueTeamScore++;

        } else {

            // 自殺の場合は、チーム得点を減点
            String team = session.getPlayerTeam(name);
            if ( team.equals("red") )
                score.redTeamScore--;
            else if ( team.equals("blue") )
                score.blueTeamScore--;
        }

        // ライフが無くなったかどうかを確認する
        if ( score.getPlayerLife(name) <= 0 ) {
            session.deadPlayer(name);
        }
    }
}
