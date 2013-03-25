/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.command;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import com.github.ucchyocean.sa.arena.ArenaManager;

/**
 * @author ucchy
 *
 */
public class ListCommand extends CommandAbst {

    private static final String INFORMATION_HEADER =
            "========== Arena List ==========";

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#getCommand()
     */
    @Override
    public String getCommand() {
        return "list";
    }

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#doCommand(org.bukkit.command.CommandSender, java.lang.String[])
     */
    @Override
    public boolean doCommand(CommandSender sender, String[] args) {

        ArrayList<String> list = ArenaManager.getArenaList();

        sender.sendMessage(PREINFO + INFORMATION_HEADER);
        for ( String l : list ) {
            sender.sendMessage(PREINFO + l);
        }

        return true;
    }

}
