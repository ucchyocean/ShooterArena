/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * @author ucchy
 * チーム関連をハンドリングするクラス
 */
public class TeamHandler {

    private Scoreboard sb;
    private Objective obj;

    /**
     * コンストラクタ
     * @param scoreboard
     */
    public TeamHandler(Scoreboard scoreboard) {
        sb = scoreboard;

        obj = sb.getObjective("sacolors");
        if ( obj == null ) {
            obj = sb.registerNewObjective("sacolors", "");
        }
    }

    /**
     * 指定したプレイヤーを、指定した色のチームに所属させる
     * @param player 参加させるプレイヤー
     * @param color 参加先のチーム名
     */
    public void setPlayerColor(Player player, String color) {

        Team team = sb.getTeam(color);
        if ( team == null ) {
            team = sb.registerNewTeam(color);
            team.setDisplayName(Utility.replaceColors(color) + color + ChatColor.RESET);
            team.setPrefix(Utility.replaceColors(color).toString());
            team.setSuffix(ChatColor.RESET.toString());
            team.setAllowFriendlyFire(false);
        }
        team.addPlayer(player);

        player.setDisplayName(
                Utility.replaceColors(color) + player.getName() + ChatColor.RESET);
    }

    /**
     * プレイヤーをチームから離脱させる
     * @param player 離脱させるプレイヤー
     */
    public void leavePlayerTeam(Player player) {

        Team team = getTeamByPlayer(player);
        if ( team != null ) {
            team.removePlayer(player);
        }

        player.setDisplayName(player.getName());
    }

    /**
     * 指定したプレイヤーが所属するチームを取得する
     * @param player プレイヤー
     * @return 所属するチーム
     */
    public Team getTeamByPlayer(Player player) {

        for ( Team team : sb.getTeams() ) {
            for ( OfflinePlayer p : team.getPlayers() ) {
                if ( player.getName().equals(p.getName()) ) {
                    return team;
                }
            }
        }
        return null;
    }

    /**
     * 指定したプレイヤー2人が、同じチームのメンバーかどうかを返す
     * @param p1 プレイヤー1
     * @param p2 プレイヤー2
     * @return 同じチームかどうか
     */
    public boolean isSameTeamMember(Player p1, Player p2) {

        Team t1 = getTeamByPlayer(p1);
        Team t2 = getTeamByPlayer(p2);
        if ( t1 == null || t2 == null ) {
            return false;
        }
        return t1.equals(t2);
    }
}
