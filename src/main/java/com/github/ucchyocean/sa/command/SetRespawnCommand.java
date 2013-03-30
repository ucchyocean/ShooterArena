/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.command;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.ucchyocean.sa.arena.Arena;
import com.github.ucchyocean.sa.arena.ArenaManager;

/**
 * @author ucchy
 *
 */
public class SetRespawnCommand extends CommandAbst {

    private static final String USAGE = "/sa setRespawn (red|blue)";

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

        if ( args.length <= 1 ) {
            sender.sendMessage(PREERR + "グループ red または blue を指定してください。");
            sender.sendMessage(PREINFO + "USAGE : " + USAGE);
            return true;
        }

        if ( !args[1].equalsIgnoreCase("red") || !args[1].equalsIgnoreCase("blue") ) {
            sender.sendMessage(PREERR + "グループ red または blue を指定してください。");
            sender.sendMessage(PREINFO + "USAGE : " + USAGE);
            return true;
        }

        Player player = (Player)sender;
        Location location = player.getLocation();
        Arena arena = ArenaManager.getArenaByLocation(location);
        String group = args[1];

        if ( arena == null ) {
            sender.sendMessage(PREERR + "この場所にアリーナが登録されていません。");
            return true;
        }

        // アリーナリスポーンを登録
        if ( group.equalsIgnoreCase("red") ) {
            ArenaManager.setRedRespawn(arena, location);
        } else {
            ArenaManager.setBlueRespawn(arena, location);
        }

        String message = String.format(
                "アリーナ%sの%sチームリスポーンポイントを、%sに設定しました。",
                arena.getName(), group, getBlockPointDesc(location) );
        sender.sendMessage(PREINFO + message);

        return true;
    }
}
