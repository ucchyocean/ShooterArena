/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.command;

import org.bukkit.command.CommandSender;

import com.github.ucchyocean.sa.arena.Arena;
import com.github.ucchyocean.sa.arena.ArenaManager;
import com.github.ucchyocean.sa.game.GameType;
import com.github.ucchyocean.sa.game.MatchMode;

/**
 * @author ucchy
 *
 */
public class SetGameCommand extends CommandAbst {

    public static final String USAGE = "/sa setGame (arena) (type) (amount)";

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#getCommand()
     */
    @Override
    public String getCommand() {
        return "setgame";
    }

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#doCommand(org.bukkit.command.CommandSender, java.lang.String[])
     */
    @Override
    public boolean doCommand(CommandSender sender, String[] args) {

        if ( args.length <= 1 ) {
            sender.sendMessage(PREERR + "アリーナ名を指定してください。");
            sender.sendMessage(PREINFO + "USAGE : " + USAGE);
            return true;
        }

        String name = args[1];
        Arena arena = ArenaManager.getArena(name);

        if ( arena == null ) {
            sender.sendMessage(PREERR + "アリーナ" + name + "が存在しません。");
            return true;
        }

        if ( args.length <= 2 ) {
            sender.sendMessage(PREERR + "ゲームタイプを指定してください。");
            sender.sendMessage(PREINFO + "USAGE : " + USAGE);
            return true;
        }

        GameType type = GameType.fromString(args[2]);

        if ( type == null ) {
            sender.sendMessage(PREERR + "指定されたゲームタイプが正しくありません。");
            sender.sendMessage(PREINFO + "指定可能なゲームタイプ : ");
            sender.sendMessage(PREINFO + "practice, duel_death_match, duel_time_match, team_death_match, team_time_match");
            return true;
        }

        if ( args.length <= 3 ) {
            sender.sendMessage(PREERR + "amount(数値)を指定してください。");
            sender.sendMessage(PREINFO + "USAGE : " + USAGE);
            return true;
        }

        int amount = 5;

        if ( !args[3].matches("^[1-9]$|^10$") ) {
            sender.sendMessage(PREERR + "指定された" + args[3] + "が指定可能な数値ではありません。");
            return true;
        }

        amount = Integer.parseInt(args[3]);
        MatchMode mode = new MatchMode(type, amount);
        arena.setMode(mode);
        arena.refreshSign();
        ArenaManager.save();

        sender.sendMessage(PREINFO + "アリーナ" + name + "に、ゲームモード\"" +
                mode.toJapanese() + "\"を設定しました。");
        return true;
    }

}
