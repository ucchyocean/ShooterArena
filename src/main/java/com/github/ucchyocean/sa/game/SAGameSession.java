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

    /** 参加プレイヤー */
    private ArrayList<String> players;

    /** 観客プレイヤー */
    private ArrayList<String> listeners;

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
    public SAGameSession(String gameName, Arena arena) {
        this.players = new ArrayList<String>();
        this.listeners = new ArrayList<String>();
        this.mode = arena.getMode();
        this.phase = GamePhase.PREPARE;
        this.logger = new SAGameLogger(gameName);
        this.arena = arena;

        runPreparePhase();
    }

    public void runPreparePhase() {
        // TODO:
    }

    public void runMatchMakingPhase() {
        // TODO:
    }

    public void startGame() {
        // TODO:
    }

    public void cancelGame() {
        // TODO:
    }

    public void endGame() {
        // TODO:
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void addPlayer(String playerName) {
        players.add(playerName);
    }

    public void removePlayer(String playerName) {
        players.remove(playerName);
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
                player.sendMessage(message); // TODO: メッセージに装飾が欲しい
            }
        }
    }

    public void annouceToAll(String message) {
        annouceToPlayer(message);
        for ( String name : listeners ) {
            Player player = ShooterArena.getPlayerExact(name);
            if ( player != null ) {
                player.sendMessage(message); // TODO: メッセージに装飾が欲しい
            }
        }
    }
}
