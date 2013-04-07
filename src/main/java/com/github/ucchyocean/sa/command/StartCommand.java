/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.ucchyocean.sa.arena.ArenaManager;
import com.github.ucchyocean.sa.game.GamePhase;
import com.github.ucchyocean.sa.game.SAGameSession;

/**
 * @author ucchy
 *
 */
public class StartCommand extends CommandAbst {

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#getCommand()
     */
    @Override
    public String getCommand() {
        return "start";
    }

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#doCommand(org.bukkit.command.CommandSender, java.lang.String[])
     */
    @Override
    public boolean doCommand(CommandSender sender, String[] args) {

        if ( !(sender instanceof Player) ) {
            sender.sendMessage(PREERR + "このコマンドはプレイヤーのみ実行可能です。");
            return true;
        }

        Player player = (Player)sender;
        SAGameSession session = ArenaManager.getSessionByPlayer(player);

        if ( session == null ) {
            sender.sendMessage(PREERR + "あなたはゲーム参加中のプレイヤーではありません。");
            return true;
        }

        if ( session.phase == GamePhase.IN_GAME ) {
            sender.sendMessage(PREERR + "既にゲームがスタートしています。");
            return true;
        }

        if ( session.phase == GamePhase.END || session.phase == GamePhase.CANCELED  ) {
            sender.sendMessage(PREERR + "既にゲームが終了しています。");
            return true;
        }

        if ( !session.isFull() ) {
            sender.sendMessage(PREERR + "メンバーが足りないので開始できません。");
            return true;
        }

        session.startGame();

        return true;
    }

}
