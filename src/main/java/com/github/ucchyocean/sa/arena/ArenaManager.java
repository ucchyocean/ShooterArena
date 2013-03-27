/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.arena;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.github.ucchyocean.sa.SAConfig;
import com.github.ucchyocean.sa.ShooterArena;
import com.github.ucchyocean.sa.game.SAGameSession;

/**
 * @author ucchy
 * アリーナと、そのゲームセッションを管理するクラス
 */
public class ArenaManager {

    private static Hashtable<String, Arena> arenas;
    private static Hashtable<String, SAGameSession> sessions;
    private static Location loungeRespawn;
    private static Hashtable<String, Location> arenaRespawn;
    static {
        arenas = new Hashtable<String, Arena>();
        sessions = new Hashtable<String, SAGameSession>();
        arenaRespawn = new Hashtable<String, Location>();
    }


    /**
     * 既に存在するアリーナ名かどうかを確認する
     * @param name アリーナ名
     * @return 存在するかどうか
     */
    public static boolean existArenaName(String name) {
        return arenas.containsKey(name);
    }

    /**
     * 既に登録済みの領域かどうかを確認する
     * @param name 領域名
     * @return 登録済みかどうか
     */
    public static boolean existArenaRegion(String name) {
        World world = ShooterArena.getWorld(SAConfig.defaultWorldName);
        if ( world == null ) {
            return false;
        }
        return ShooterArena.wghandler.existRegion(world, name);
    }

    /**
     * 新しいアリーナを登録する
     * @param name アリーナ名
     * @return アリーナ
     */
    public static Arena registerNewArena(String name) {
        Arena arena = new Arena(name);
        arenas.put(name, arena);
        return arena;
    }

    /**
     * アリーナを取得する
     * @param name アリーナ名
     * @return アリーナ
     */
    public static Arena getArena(String name) {
        return arenas.get(name);
    }

    /**
     * アリーナ名のリストを取得する
     * @return アリーナ名のリスト
     */
    public static ArrayList<String> getArenaNames() {
        ArrayList<String> result = new ArrayList<String>();
        Enumeration<String> keys = arenas.keys();
        while ( keys.hasMoreElements() ) {
            String key = keys.nextElement();
            result.add(key);
        }
        return result;
    }

    public static Arena getArenaByLocation(Location location) {
        String regionName =
                ShooterArena.wghandler.getRegionNameFromLocation(
                        location, ArenaManager.getArenaNames() );
        if ( regionName == null ) {
            return null;
        } else {
            return arenas.get(regionName);
        }
    }

    /**
     * アリーナを削除する
     * @param name アリーナ名
     */
    public static void removeArena(String name) {
        if ( arenas.containsKey(name) ) {
            arenas.remove(name);
        }
    }

    /**
     * アリーナのリストを取得する
     * @return アリーナのリスト
     */
    public static ArrayList<String> getArenaList() {

        ArrayList<String> result = new ArrayList<String>();

        Enumeration<String> keys = arenas.keys();
        while ( keys.hasMoreElements() ) {
            String key = keys.nextElement();
            SAGameSession session = sessions.get(key);
            if ( session == null ) {
                result.add(key + " - 空き");
            } else {
                result.add(key + " - " + session.phase.toJapanese());
            }
        }

        return result;
    }

    /**
     * 新しいゲームセッションを作成して返す
     * @param arena ゲームを行うアリーナ
     * @return 新しいゲームセッション
     */
    public static SAGameSession createNewGameSession(Arena arena, Player leader) {
        SAGameSession session = new SAGameSession(arena, leader.getName());
        session.addPlayer(leader.getName());
        sessions.put(arena.getName(), session);
        return session;
    }

    /**
     * アリーナ名からゲームセッションを取得する
     * @param name アリーナ名
     * @return ゲームセッション
     */
    public static SAGameSession getSession(String name) {
        return sessions.get(name);
    }

    /**
     * ゲームセッションを削除する
     * @param session 削除するゲームセッション
     */
    public static void removeSession(SAGameSession session) {
        if ( sessions.contains(session) ) {
            sessions.remove(session.arena.getName());
        }
    }

    /**
     * ゲームセッションを削除する
     * @param name 削除対象のアリーナ名
     */
    public static void removeSession(String name) {
        if ( sessions.containsKey(name) ) {
            sessions.remove(name);
        }
    }

    /**
     * 指定したプレイヤーが参加しているゲームセッションを取得する
     * @param player プレイヤー
     * @return ゲームセッション
     */
    public static SAGameSession getSessionByPlayer(Player player) {
        Enumeration<String> keys = sessions.keys();
        while ( keys.hasMoreElements() ) {
            String key = keys.nextElement();
            SAGameSession session = sessions.get(key);
            if ( session.getPlayers().contains(player.getName()) ) {
                return session;
            }
        }
        return null;
    }

    /**
     * 全てのArenaSignを返す
     * @return 全てのArenaSign
     */
    public static ArrayList<ArenaSign> getAllArenaSign() {
        ArrayList<ArenaSign> signs = new ArrayList<ArenaSign>();
        Enumeration<String> keys = arenas.keys();
        while ( keys.hasMoreElements() ) {
            String key = keys.nextElement();
            Arena arena = arenas.get(key);
            if ( arena.getSign() != null ) {
                signs.add(arena.getSign());
            }
        }
        return signs;
    }

    /**
     * ラウンジリスポーンポイントを設定します。
     * @param location
     */
    public static void setLoungeRespawn(Location location) {
        loungeRespawn = location;
    }

    /**
     * ラウンジリスポーンポイントを取得します。
     * @return ラウンジリスポーンポイント
     */
    public static Location getLoungeRespawn() {
        return loungeRespawn;
    }

    /**
     * アリーナリスポーンポイントを設定します。
     * @param name リスポーンポイント名
     * @param location リスポーンポイント
     */
    public static void setArenaRespawn(String name, Location location) {
        arenaRespawn.put(name, location);
    }

    /**
     * アリーナリスポーンポイントを取得します。
     * @param name リスポーンポイント名
     * @return リスポーンポイント
     */
    public static Location getArenaRespawn(String name) {
        return arenaRespawn.get(name);
    }
}
