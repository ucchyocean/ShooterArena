/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.arena;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.github.ucchyocean.sa.ShooterArena;
import com.github.ucchyocean.sa.Utility;
import com.github.ucchyocean.sa.command.SetGameCommand;
import com.github.ucchyocean.sa.game.GamePhase;
import com.github.ucchyocean.sa.game.MatchMode;
import com.github.ucchyocean.sa.game.SAGameSession;
/**
 * @author ucchy
 * ラウンジの、対戦募集用サイン
 */
public class ArenaSign {

    private static final String[] MESSAGE_PREPARE = {
        ChatColor.DARK_AQUA + "" + ChatColor.ITALIC +
            "%s" + ChatColor.RESET, // %s = ゲームタイプ
        ChatColor.DARK_AQUA + "このアリーナは" + ChatColor.RESET,
        ChatColor.DARK_AQUA + "準備中です。" + ChatColor.RESET,
    };

    private static final String[] MESSAGE_MATCHING = {
        ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC +
            "%s" + ChatColor.RESET, // %s = ゲームタイプ
        ChatColor.DARK_PURPLE + "対戦メンバー募集中" + ChatColor.RESET,
        ChatColor.DARK_PURPLE + "<左クリックで参加>" + ChatColor.RESET,
    };

    private static final String[] MESSAGE_MATCHING_FULL = {
        ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC +
            "%s" + ChatColor.RESET, // %s = ゲームタイプ
        ChatColor.DARK_PURPLE + "対戦準備中" + ChatColor.RESET,
        ChatColor.DARK_RED + "<満員>" + ChatColor.RESET,
    };

    private static final String[] MESSAGE_IN_GAME = {
        ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC +
            "%s" + ChatColor.RESET, // %s = ゲームタイプ
        ChatColor.DARK_BLUE + "対戦中" + ChatColor.RESET,
        ChatColor.DARK_BLUE + "" + ChatColor.RESET,
    };

    private Arena parent;
    private Sign sign;

    /**
     * コンストラクタ
     * @param parent アリーナ
     * @param sign メインのカンバン
     * @param sub サブのカンバン
     */
    protected ArenaSign(Sign sign) {
        this.sign = sign;
    }

    /**
     * 対応するアリーナを設定する
     * @param arena アリーナ
     */
    protected void setParent(Arena arena) {
        this.parent = arena;
        arena.setSign(this);
    }

    /**
     * 指定されたカンバンが、このオブジェクトのものかどうかを判断する
     * @param sign カンバン
     * @return このオブジェクトのカンバンかどうか
     */
    public boolean equalsSign(Sign sign) {
        return sign.equals(sign);
    }

    /**
     * ゲームマッチ準備中の内容に変更する
     */
    private void setPrepare() {

        String mode = "";
        if ( parent.getMode() != null ) {
            mode = parent.getMode().type.toJapanese();
        }
        String title = String.format("[%s]", parent.getName());
        String type = String.format(MESSAGE_PREPARE[0], mode);
        sign.setLine(0, title);
        sign.setLine(1, type);
        sign.setLine(2, MESSAGE_PREPARE[1]);
        sign.setLine(3, MESSAGE_PREPARE[2]);
        sign.update();
    }

    /**
     * ゲームマッチ募集中の内容に変更する
     */
    private void setMatching() {

        String title = String.format("[%s]", parent.getName());
        String type = String.format(
                MESSAGE_MATCHING[0],
                parent.getMode().type.toJapanese());
        sign.setLine(0, title);
        sign.setLine(1, type);
        sign.setLine(2, MESSAGE_MATCHING[1]);
        sign.setLine(3, MESSAGE_MATCHING[2]);
        sign.update();
    }

    /**
     * ゲームマッチ満員の内容に変更する
     */
    private void setMatchingFull() {

        String title = String.format("[%s]", parent.getName());
        String type = String.format(
                MESSAGE_MATCHING_FULL[0],
                parent.getMode().type.toJapanese());
        sign.setLine(0, title);
        sign.setLine(1, type);
        sign.setLine(2, MESSAGE_MATCHING_FULL[1]);
        sign.setLine(3, MESSAGE_MATCHING_FULL[2]);
        sign.update();
    }

    /**
     * ゲームマッチゲーム中の内容に変更する
     */
    private void setInGame() {

        String title = String.format("[%s]", parent.getName());
        String type = String.format(
                MESSAGE_IN_GAME[0],
                parent.getMode().type.toJapanese());
        sign.setLine(0, title);
        sign.setLine(1, type);
        sign.setLine(2, MESSAGE_IN_GAME[1]);
        sign.setLine(3, MESSAGE_IN_GAME[2]);
        sign.update();
    }

    /**
     * カンバンをクリックされたときに呼び出されるイベント
     * @param player クリックしたプレイヤー
     */
    public void onHit(Player player) {

        if ( ArenaManager.getSessionByPlayer(player) != null ) {
            player.sendMessage(ShooterArena.PREERR +
                    "あなたは既にゲームに参加中です。");
            return;
        }

        if ( ArenaManager.getSession(parent.getName()) == null ) {
            // アリーナのゲームセッションが無い場合

            if ( parent.getMode() == null ) {
                player.sendMessage(ShooterArena.PREERR +
                        "このアリーナには、ゲームモードが設定されていません。");
                player.sendMessage(ShooterArena.PREERR +
                        "先にsetGameコマンドで、ゲームモードを設定してください。");
                player.sendMessage(ShooterArena.PREINFO + SetGameCommand.USAGE);
                return;

            } else {
                // 新規ゲームセッションの作成
                ArenaManager.createNewGameSession(parent, player);
                return;
            }

        } else {
            // アリーナのゲームセッションに参加する場合


        }
    }

    /**
     * 看板をArenaの登録から解除して、普通の看板にする。
     */
    protected void remove() {

        for ( int i=0; i<4; i++ )
            sign.setLine(i, "");
        sign.update();

        parent.setSign(null);
    }

    /**
     * ArenaSignの設置されている場所を表現する文字列を返す
     * @return
     */
    protected String toLocationDesc() {

        Location location = sign.getLocation();
        return Utility.convLocationToDesc(location);
    }

    /**
     * 文字列表現から、ArenaSignを生成して返す。
     * @param arena 紐付けるArena
     * @param desc 文字列表現
     * @return 生成されたArenaSign、指定内容が無効である場合は nullになる。
     */
    protected static ArenaSign fromLocationDesc(Arena arena, String desc) {

        Location location = Utility.convDescToLocation(desc);
        if ( location == null ) {
            return null;
        }

        Block block = location.getWorld().getBlockAt(location);
        if ( !Utility.isSign(block) ) {
            return null;
        }

        Sign sign = (Sign)block.getState();
        ArenaSign as = new ArenaSign(sign);
        as.setParent(arena);
        return as;
    }

    /**
     * 親のArenaの状態を取得して、ArenaSignを更新します。
     */
    protected void refresh() {

        MatchMode mode = parent.getMode();
        if ( mode == null ) {
            setPrepare();
            return;
        }

        SAGameSession session = ArenaManager.getSession(parent.getName());
        if ( session == null ) {
            setMatching();
            return;
        }

        if ( session.phase == GamePhase.IN_GAME ) {
            setInGame();
            return;
        }

        if ( session.phase == GamePhase.MATCH_MAKING ) {
            if ( session.isFull() ) {
                setMatchingFull();
                return;
            } else {
                setMatching();
                return;
            }
        }

        setPrepare();
    }
}
