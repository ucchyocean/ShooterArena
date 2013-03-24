/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.command;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author ucchy
 * ShooterArenaのコマンドクラス
 */
public class ShooterArenaCommand implements CommandExecutor {

    private ArrayList<CommandAbst> commands;

    /**
     * コンストラクタ
     */
    public ShooterArenaCommand() {
        commands = new ArrayList<CommandAbst>();
        commands.add(new ReloadCommand());
        commands.add(new CreateCommand());
        commands.add(new SetLoungeCommand());
        commands.add(new SetRespawnCommand());
        commands.add(new SignCommand());
        commands.add(new RemoveCommand());
        commands.add(new DisableCommand());
        commands.add(new EnableCommand());
        commands.add(new ListCommand());
        commands.add(new SetGameCommand());
        commands.add(new GotoCommand());
    }

    /**
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    public boolean onCommand(
            CommandSender sender, Command command, String label, String[] args) {

        // 引数なしは実行しない
        if ( args.length == 0 ) {
            return false;
        }

        String type = args[0];

        for ( CommandAbst c : commands ) {
            if ( type.equalsIgnoreCase(c.getCommand()) ) {
                return c.doCommand(sender, args);
            }
        }

        return false;
    }

}
