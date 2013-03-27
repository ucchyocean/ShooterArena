/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.command;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.ucchyocean.sa.ShooterArena;
import com.github.ucchyocean.sa.arena.ArenaManager;

/**
 * @author ucchy
 *
 */
public class SetLoungeCommand extends CommandAbst {

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#getCommand()
     */
    @Override
    public String getCommand() {
        return "setlounge";
    }

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#doCommand(org.bukkit.command.CommandSender, java.lang.String[])
     */
    @Override
    public boolean doCommand(CommandSender sender, String[] args) {

        if ( !(sender instanceof Player) ) {
            sender.sendMessage(PREERR + "このコマンドはゲーム内でのみ実行可能です。");
            return true;
        }

        Player player = (Player)sender;

        if ( !ShooterArena.wghandler.existRegion(player.getWorld(), "lounge") ) {
            sender.sendMessage(PREERR + "領域loungeが登録されていません。");
            return true;
        }

        Location location = player.getLocation();

        if ( !ShooterArena.wghandler.insideRegion(location, "lounge") ) {
            sender.sendMessage(PREERR + "領域loungeの外なので、設定できません。");
            return true;
        }

        // ラウンジリスポーンを登録
        ArenaManager.setLoungeRespawn(location);
        // ワールドリスポーンを変更
        player.getWorld().setSpawnLocation(
                location.getBlockX(), location.getBlockY(), location.getBlockZ());

        sender.sendMessage(PREINFO + "ラウンジリスポーンポイントを、" +
                getBlockPointDesc(location) + "に設定しました。");

        return true;
    }

}
