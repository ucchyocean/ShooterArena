/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.game;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.ucchyocean.sa.ShooterArena;
import com.github.ucchyocean.sa.arena.Arena;

/**
 * @author ucchy
 * ゲームセッション
 */
public class SAGameSession {

    /** アナウンスメッセージフォーマット */
    private static final String FORMAT_ANNOUNCE = ChatColor.GREEN + "[%s] %s";

    /** 参加プレイヤー */
    private ArrayList<String> players;

    /** 観客プレイヤー */
    private ArrayList<String> listeners;

    /** リーダー */
    private String leader;

    /** ゲームモード */
    public MatchMode mode;

    /** ゲームフェイズ */
    public GamePhase phase;

    /** ゲームロガー */
    private SAGameLogger logger;

    /** アリーナ */
    public Arena arena;

    /**
     * コンストラクタ
     */
    public SAGameSession(Arena arena, String leader) {

        this.players = new ArrayList<String>();
        this.listeners = new ArrayList<String>();
        this.mode = arena.getMode();
        this.logger = new SAGameLogger(arena.getName());
        this.arena = arena;
        this.leader = leader;

        arena.setSession(this);

        runPreparePhase();
    }

    /**
     * セッションを準備中にする
     */
    public void runPreparePhase() {

        logger.write("アリーナ " + arena.getName() + " で、新規ゲームセッションが作成されました。");
        this.phase = GamePhase.PREPARE;

        // そのまま募集中フェーズへ移行する
        runMatchMakingPhase();
   }

    /**
     * セッションを対戦メンバー募集中にする
     */
    public void runMatchMakingPhase() {

        logger.write("アリーナ " + arena.getName() + " が、対戦メンバー募集中になりました。");
        logger.write("ゲームモードは、" + arena.getMode().toJapanese() + " です。");
        this.phase = GamePhase.MATCH_MAKING;
        refreshArenaSign();
    }

    /**
     * セッションを対戦中にする
     */
    public void startGame() {
        // TODO:
    }

    /**
     * セッションをキャンセルして終了する
     */
    public void cancelGame() {
        // TODO:
    }

    /**
     * セッションを終了する
     */
    public void endGame() {
        // TODO:
    }

    /**
     * セッションに参加しているプレイヤー名一覧を取得する
     * @return プレイヤー名一覧
     */
    public ArrayList<String> getPlayers() {
        return players;
    }

    /**
     * セッションに参加メンバーを追加する
     * @param playerName 追加するプレイヤー名
     */
    public void addPlayer(String playerName) {
        logger.write(playerName + " さんが、ゲームセッションに参加しました。");
        players.add(playerName);
        refreshArenaSign();

        // TODO: 満員になったら、開始コマンドを実行するよう、リーダーにメッセージを送信する
    }

    /**
     * セッションから参加メンバーを離脱する
     * @param playerName 離脱するプレイヤー名
     */
    public void removePlayer(String playerName) {
        logger.write(playerName + " さんが、ゲームセッションから離脱しました。");
        players.remove(playerName);
        refreshArenaSign();
    }

    public ArrayList<String> getListeners() {
        return listeners;
    }

    public void addListener(String listenerName) {
        listeners.add(listenerName);
    }

    public void removeListener(String listenerName) {
        listeners.remove(listenerName);
    }

    public void refreshArenaSign() {
        ChatColor color = ChatColor.BLACK;
        if ( phase == GamePhase.PREPARE ||
                phase == GamePhase.MATCH_MAKING ) {
            if ( players.size() < mode.type.getMaxPlayerNum() ) {
                color = ChatColor.DARK_PURPLE;
            } else {
                color = ChatColor.DARK_RED;
            }
        } else if ( phase == GamePhase.IN_GAME ) {
            color = ChatColor.DARK_BLUE;
        } else {
            arena.getSign().clearSub();
        }
        arena.getSign().setSub(color, players);
    }

    public void annouceToPlayer(String message) {
        for ( String name : players ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                String msg = String.format(FORMAT_ANNOUNCE, arena.getName(), message);
                player.sendMessage(msg);
            }
        }
    }

    public void annouceToAll(String message) {
        annouceToPlayer(message);
        for ( String name : listeners ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                String msg = String.format(FORMAT_ANNOUNCE, arena.getName(), message);
                player.sendMessage(msg);
            }
        }
    }
}
