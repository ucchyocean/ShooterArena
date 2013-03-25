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
import com.github.ucchyocean.sa.arena.Arena;
import com.github.ucchyocean.sa.arena.ArenaManager;

/**
 * @author ucchy
 *
 */
public class SetRespawnCommand extends CommandAbst {

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#getCommand()
     */
    @Override
    public String getCommand() {
        return "setrespawn";
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
        Location location = player.getLocation();
        Arena arena = ArenaManager.getArenaByLocation(location);

        if ( arena == null ) {
            sender.sendMessage(PREERR + "この場所にアリーナが登録されていません。");
            return true;
        }

        if ( !ShooterArena.wghandler.insideRegion(location, arena.getName()) ) {
            sender.sendMessage(PREERR + "領域loungeの外なので、設定できません。");
            return true;
        }

        // ラウンジリスポーンを登録
        // TODO: グループごとに登録した方がいいのでは？このコマンドは再考する
        ArenaManager.setArenaRespawn(arena.getName(), location);
        sender.sendMessage(PREINFO + "アリーナ" + arena.getName() + "のリスポーンポイントを、" +
                getBlockPointDesc(location) + "に設定しました。");

        return true;
    }

}
