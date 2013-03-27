/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.ucchyocean.sa.arena.ArenaSign;

/**
 * @author ucchy
 *
 */
public class SignCommand extends CommandAbst {

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#getCommand()
     */
    @Override
    public String getCommand() {
        return "sign";
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

        if ( args.length <= 1 ) {
            sender.sendMessage(PREERR + "アリーナ名を指定してください。");
            return true;
        }

        String name = args[1];

        if ( ShooterArenaCommand.getPlayerCommandCache(player.getName()) != null ) {
            if ( name.equalsIgnoreCase("cancel") ) {
                ShooterArenaCommand.removePlayerCommandCache(player.getName());
                sender.sendMessage(PREINFO + "コマンドの実行待機をキャンセルしました。");
                return true;
            }
            sender.sendMessage(PREERR + "あなたは既に、コマンドの実行待機中です。");
            return true;
        }

        ShooterArenaCommand.setPlayerCommandCache(player.getName(), name);
        sender.sendMessage(PREINFO + "アリーナ" + name + "の看板を作成します。");
        sender.sendMessage(PREINFO + "設定したい看板を左クリックしてください。");
        return true;
    }

    public static void registerSign(ArenaSign sign, String arenaName) {


    }
}
