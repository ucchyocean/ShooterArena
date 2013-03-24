/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.game;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.github.ucchyocean.sa.arena.Arena;

/**
 * @author ucchy
 * ゲームセッション
 */
public class SAGameSession {

    /** 参加プレイヤー */
    public ArrayList<Player> players;

    /** 観客プレイヤー */
    public ArrayList<Player> listeners;

    /** ゲームモード */
    public MatchMode mode;

    /** ゲームフェイズ */
    public GamePhase phase;

    /** ゲームロガー */
    public SAGameLogger logger;

    /** アリーナ */
    public Arena arena;

    /**
     * コンストラクタ
     */
    public SAGameSession(String gameName, Arena arena, MatchMode mode) {
        this.players = new ArrayList<Player>();
        this.listeners = new ArrayList<Player>();
        this.mode = mode;
        this.phase = GamePhase.PREPARE;
        this.logger = new SAGameLogger(gameName);
        this.arena = arena;
    }
}
